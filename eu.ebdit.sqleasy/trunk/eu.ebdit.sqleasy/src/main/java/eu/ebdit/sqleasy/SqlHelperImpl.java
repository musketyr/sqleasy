package eu.ebdit.sqleasy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.ebdit.sqleasy.checkers.SqlChecker;
import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.handlers.ExceptionHandler;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;
import eu.ebdit.sqleasy.processors.ResultProcessor;

/**
 * Zakladni implementace rozhrani {@link SqlHelper}. Provadi sql dotazy aniz by
 * se museli klienti starat o spravu prostredku a take cachuje datasource podle
 * jejich nazvu.
 * 
 * @author Vladimir Orany
 * 
 */
class SqlHelperImpl implements SqlHelper {

	private static enum StatementType {
		NORMAL {
			@Override
			public Statement novyStatement(Connection arg0, String sql)
					throws SQLException {
				return arg0.createStatement();
			}
		},
		PREPARED {
			@Override
			public Statement novyStatement(Connection arg0, String sql)
					throws SQLException {
				return arg0.prepareStatement(sql);
			}
		},
		PREPARED_FOR_INSERT {
			@Override
			public Statement novyStatement(Connection arg0, String sql)
					throws SQLException {
				return arg0.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}
		},
		CALLABLE {
			@Override
			public Statement novyStatement(Connection arg0, String sql)
					throws SQLException {
				return arg0.prepareCall(sql);
			}
		};

		public abstract Statement novyStatement(Connection c, String sql)
				throws SQLException;

		public void pripravStatement(Statement c, Object... params)
				throws SQLException {
			if (c instanceof PreparedStatement) {
				PreparedStatement pstmt = (PreparedStatement) c;
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
			}
		}

		public ResultSet provedDotaz(Statement stmt, String sql)
				throws SQLException {
			if (stmt instanceof PreparedStatement) {
				PreparedStatement ps = (PreparedStatement) stmt;
				return ps.executeQuery();
			}
			return stmt.executeQuery(sql);
			
		}

		public int provedAkutalizaci(Statement s, String sql)
				throws SQLException {
			if (s instanceof PreparedStatement) {
				PreparedStatement ps = (PreparedStatement) s;
				return ps.executeUpdate();
			}
			return s.executeUpdate(sql);
		}

	};

	private static final ConcurrentMap<ConnectionProvider, SqlHelper> cache = new ConcurrentHashMap<ConnectionProvider, SqlHelper>(
			5);
	private static final ThreadLocal<PriorityQueue<Transakce>> lokalniFrontaTransakci = new ThreadLocal<PriorityQueue<Transakce>>() {
		@Override
		protected PriorityQueue<Transakce> initialValue() {
			return new PriorityQueue<Transakce>(5, KomparatorTransakci.INSTANCE);
		}
	};

	final ConnectionProvider connectionProvider;
	private ExceptionHandler handler = null;

	/**
	 * Ziska instanci helperu pro dany datasource
	 * 
	 * @param dataSourceName
	 *            jmeno datasource
	 * @return instance helperu datasource daneho jmena
	 */
	public static final SqlHelper getHelper(ConnectionProvider provider) {
		SqlHelper h = cache.get(provider);
		if (h != null) {
			h = new SqlHelperImpl(provider);
			cache.putIfAbsent(provider, h);
		}
		return h;

	}
	
	Iterable<ResultSet> asIterable(final ResultSet rs){
		
		final ResultSet wrapped = new CursorlessResultSet(rs);
		
		return new Iterable<ResultSet>(){

			public Iterator<ResultSet> iterator() {
				return new Iterator<ResultSet>(){

					public boolean hasNext() {
						try {
							return rs.next();
						} catch (SQLException e) {
							getHandler().handleException(e);
							return false;
						}
					}

					public ResultSet next() {
						return wrapped;
					}

					public void remove() {
						throw new UnsupportedOperationException("Odebirani neni mozne provest!");
					}
					
				};
			}
			
		};
	}
	
	private void close(Statement s){
		try {
			if (s != null) {
				s.close();
			}
		} catch (SQLException e) {
			getHandler().handleException(e);
		}
	}
	

	/**
	 * Vytvori helper pro dany datasource
	 * 
	 * @param provider
	 */
	private SqlHelperImpl(ConnectionProvider provider) {
		this.connectionProvider = provider;
	}

	public void setHandler(ExceptionHandler handler) {
		this.handler = handler;
	}

	public ExceptionHandler getHandler() {
		return ExceptionHandlers.nullSafe(this.handler);
	}

	public QueryResult executeInsert(String sql, Object... parametry) {
		StatementType typVolani = StatementType.PREPARED_FOR_INSERT;
		Connection connection = ziskejSpojeni();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = (PreparedStatement) prepareStatement(sql, typVolani, connection, parametry);
			typVolani.provedAkutalizaci(stmt, sql);
			rs = stmt.getGeneratedKeys();
			final ResultSet frs = rs;
			final Statement fstmt = stmt;
			final Connection fc = connection;
			return new QueryResult() {
				public <T> T processWith(ResultProcessor<T> zpracovani) {
					try {
						// if (frs.isClosed()) {
						// throw new
						// IllegalStateException("Metoda zpracuj nemuze byt volana vicekrat nez jednou!");
						// }
						try {
							T t = zpracovani.processResultSet(asIterable(frs));
							return t;
						} finally {
							if (frs != null) {
								frs.close();
							}
							zavriJeLiTreba(fstmt, fc);
						}
					} catch (SQLException e) {
						handleException(e);
						return null;
					}
				}
			};
		} catch (SQLException e) {
			handleException(e);
			return null;
		}
	}
	
	public int execute(String sql, Object... parametry) {
		StatementType typVolani = getTyp(sql, parametry);
		Connection connection = ziskejSpojeni();
		Statement stmt = null;

		if (jeAktivniTransakce()) {
			for (SqlChecker i : getAktivniTransakce().interceptory) {
				if (!i.moznoSpustit(this, sql, parametry)) {
					return -1;
				}
			}
		}


		try {
			try {
				stmt = prepareStatement(sql, typVolani, connection, parametry);
				return typVolani.provedAkutalizaci(stmt, sql);
			} finally {
				zavriJeLiTreba(stmt, connection);
			}
		} catch (SQLException e) {
			handleException(e);
			return -1;
		}
	}

	private Statement prepareStatement(String sql, StatementType typVolani,
			Connection connection, Object... parametry) throws SQLException {
		Statement stmt = typVolani.novyStatement(connection, sql);
		typVolani.pripravStatement(stmt, parametry);
		return stmt;
	}

	void handleException(SQLException e) {
		getHandler().handleException(e);
		zrusTransakciPokudExistuje();
	}

	public QueryResult executeQuery(String sql, Object... parametry) {
		StatementType typVolani = getTyp(sql, parametry);
		Connection connection = ziskejSpojeni();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = prepareStatement(sql, typVolani, connection, parametry);
			rs = typVolani.provedDotaz(stmt, sql);
			final ResultSet frs = rs;
			final Statement fstmt = stmt;
			final Connection fc = connection;
			return new QueryResult() {
				public <T> T processWith(ResultProcessor<T> zpracovani) {
					try {
						// if (frs.isClosed()) {
						// throw new
						// IllegalStateException("Metoda zpracuj nemuze byt volana vicekrat nez jednou!");
						// }
						try {
							T t = zpracovani.processResultSet(asIterable(frs));
							return t;
						} finally {
							if (frs != null) {
								frs.close();
							}
							zavriJeLiTreba(fstmt, fc);
						}
					} catch (SQLException e) {
						handleException(e);
						return null;
					}
				}
			};
		} catch (SQLException e) {
			handleException(e);
			return null;
		}
	}

	/**
	 * @param sql  
	 */
	private StatementType getTyp(String sql, Object[] parametry) {
		if (parametry != null && parametry.length > 0) {
			return StatementType.PREPARED;
		}
		return StatementType.NORMAL;

	}

	void zavriJeLiTreba(Statement stmt, Connection connection) {
		close(stmt);
		if (!jeAktivniTransakce()) {
			this.connectionProvider.closeConnection(connection, getHandler());
		}
	}

	private Connection ziskejSpojeni() {
		if (!jeAktivniTransakce()) {
			return this.connectionProvider.getConnection(getHandler());
		}
		return getAktivniTransakce().getConnection();
	}

	private Transakce getAktivniTransakce() {
		return lokalniFrontaTransakci.get().peek();
	}

	private static enum KomparatorTransakci implements Comparator<Transakce> {
		INSTANCE;

		public int compare(Transakce o1, Transakce o2) {
			// vnoreni musi byt vzdy kladne, takze si muzeme dovolit
			// pouzit tento trik (nehrozi preteceni)
			return o2.vnoreni - o1.vnoreni;
		}
	}

	private class Transakce {
		private final Connection c;
		final int vnoreni;
		final SqlChecker[] interceptory;

		public Transakce(SqlChecker... interceptory) {
			this(0, interceptory);
		}

		private Transakce(int vnoreni, SqlChecker... interceptory) {
			Connection conn = null;
			try {
				conn = SqlHelperImpl.this.connectionProvider.getConnection(getHandler());
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				getHandler().handleException(e);
			}
			this.interceptory = interceptory;
			this.vnoreni = vnoreni;
			this.c = conn;
		}

		public Transakce vytvorVnorenou() {
			return new Transakce(this.vnoreni + 1, this.interceptory);
		}

		public Connection getConnection() {
			return this.c;
		}
	}

	public void commitTransaction() {
		try {
			Transakce t = lokalniFrontaTransakci.get().poll();
			if (t != null) {
				Connection c = t.getConnection();
				c.commit();
				this.connectionProvider.closeConnection(c, getHandler());
			}
		} catch (SQLException e) {
			getHandler().handleException(e);
		}
	}

	public boolean isTransactionActive() {
		return !lokalniFrontaTransakci.get().isEmpty();
	}

	public void beginTransaction(SqlChecker... interceptory) {
		Transakce t = getAktivniTransakce();
		if (t == null) {
			t = new Transakce(interceptory);
			lokalniFrontaTransakci.get().add(t);
		} else {
			t = t.vytvorVnorenou();
			lokalniFrontaTransakci.get().add(t);
		}
	}

	private boolean jeAktivniTransakce() {
		return getAktivniTransakce() != null;
	}

	private void zrusTransakciPokudExistuje() {
		try {
			Transakce t = lokalniFrontaTransakci.get().poll();
			if (t != null) {
				Connection c = t.getConnection();
				c.rollback();
				this.connectionProvider.closeConnection(c, getHandler());
			}
		} catch (SQLException e) {
			getHandler().handleException(e);
		}
	}

}
