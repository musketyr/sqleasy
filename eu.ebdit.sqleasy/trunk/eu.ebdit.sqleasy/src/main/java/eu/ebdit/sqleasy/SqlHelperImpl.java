package eu.ebdit.sqleasy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;



/**
 * Zakladni implementace rozhrani {@link SqlHelper}. Provadi sql dotazy
 * aniz by se museli klienti starat o spravu prostredku a take cachuje datasource
 * podle jejich nazvu.
 * @author Vladimir Orany
 *
 */
class SqlHelperImpl implements SqlHelper {
	
	private static enum TypVolani{
		NORMALNI{
			@Override
			public Statement novyStatement(Connection arg0, String sql) throws SQLException {
				return arg0.createStatement();
			}
		}, 
		PRIPRAVENY{
			@Override
			public Statement novyStatement(Connection arg0, String sql) throws SQLException {
				return arg0.prepareStatement(sql);
			}
		},
		VOLATELNY{
			@Override
			public Statement novyStatement(Connection arg0, String sql) throws SQLException {
				return arg0.prepareCall(sql);
			}
		}	;
		
		public abstract Statement novyStatement(Connection c, String sql) throws SQLException;
		
		public void pripravStatement(Statement c, Object... params) throws SQLException {
			if (c instanceof PreparedStatement) {
				PreparedStatement pstmt = (PreparedStatement) c;
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
					LOG.info(String.format("Nastavuji parametr %d na hodnotu %s!", i + 1, String.valueOf(params[i]) ));
				}
			}
		}
		
		public ResultSet provedDotaz(Statement stmt, String sql) throws SQLException{
			if (stmt instanceof PreparedStatement) {
				PreparedStatement ps = (PreparedStatement) stmt;
				return ps.executeQuery();
			} else {
				return stmt.executeQuery(sql);
			}
		}
		
		public int provedAkutalizaci(Statement s, String sql) throws SQLException{
			if (s instanceof PreparedStatement) {
				PreparedStatement ps = (PreparedStatement) s;
				return ps.executeUpdate();
			}
			return s.executeUpdate(sql);
		}
		
	};
	

	
	
	
	private static final ConcurrentMap<String, SqlHelper> cache = new ConcurrentHashMap<String, SqlHelper>(5);
	private static final ThreadLocal<PriorityQueue<Transakce>> lokalniFrontaTransakci = new ThreadLocal<PriorityQueue<Transakce>>(){
		@Override
		protected PriorityQueue<Transakce> initialValue() {
			return new PriorityQueue<Transakce>(5, KomparatorTransakci.INSTANCE);
		}
	};
	
	
	private final DataSource datasource;
	private SqlExceptionHandler handler = null;
	
	
	
	/**
	 * Ziska instanci helperu pro dany datasource
	 * @param dataSourceName	jmeno datasource
	 * @return	instance helperu datasource daneho jmena
	 */
	public static final SqlHelper getHelper(String dataSourceName){
		SqlHelper h = cache.get(dataSourceName);
		if (h == null) {
			h = new SqlHelperImpl(SqlEasy.getDataSource(dataSourceName));
			cache.putIfAbsent(dataSourceName, h);
		}
		return h;
		
	}
	
	/**
	 * Vytvori helper pro dany datasource
	 * @param d
	 */
	private SqlHelperImpl(DataSource d) {
		datasource = d;
	}

	
	
	
	
	
	
	public void setSpravce(SqlExceptionHandler handler) {
		this.handler = handler;
	}
	
	public SqlExceptionHandler getSpravce() {
		return handler == null ? ZakladniSpravceSqlVyjimek.INSTANCE : handler;
	}
	
	public int provedAktualizaci(String sql, Object... parametry) throws ApplicationException{
		TypVolani typVolani = getTyp(parametry);
		Connection connection = ziskejSpojeni();
		
		if (jeAktivniTransakce()) {
			for (SqlChecker i : getAktivniTransakce().interceptory) {
				if (!i.moznoSpustit(connection, sql, parametry)) {
					LOG.info("Kontrolor " + i + " zabranil spusteni aktualizace! sql = " + sql);
					return -1;
				}
			}
		}
		
		Statement stmt = null; 
		LOG.info("Spoustim sql: " + sql);
		
		try {
			try {
				stmt = typVolani.novyStatement(connection, sql);
				typVolani.pripravStatement(stmt, parametry);
				return typVolani.provedAkutalizaci(stmt, sql);
			} finally {
				zavriJeLiTreba(stmt, connection);
			}
		} catch (SQLException e) {
			getSpravce().handleException(e);
			zrusTransakciPokudExistuje();
			throw new ApplicationException("exception.database.failed", e);
		} 
	}
	
	public QueryResult provedDotaz(String sql, Object... parametry) throws ApplicationException{
		TypVolani typVolani = getTyp(parametry);
		Connection connection = ziskejSpojeni();
		Statement stmt = null; 
		ResultSet rs = null;
		LOG.info("Spoustim sql: " + sql == null ? "nezadano" :  sql.trim());
		
		try {
			stmt = typVolani.novyStatement(connection, sql);
			typVolani.pripravStatement(stmt, parametry);
			rs = typVolani.provedDotaz(stmt, sql);
			final ResultSet frs = rs;
			final Statement fstmt = stmt;
			final Connection fc = connection;
			return new QueryResult(){
				public <T> T processWith(ResultProcessor<T> zpracovani) throws ApplicationException {
					try {
//						if (frs.isClosed()) {
//							throw new IllegalStateException("Metoda zpracuj nemuze byt volana vicekrat nez jednou!");
//						}
						try {
							T t = zpracovani.processResultSet(SqlEasy.asIterable(frs, getSpravce()));
							LOG.debug("Vysledek = " + t);
							return t;
						} finally {
							if (frs != null) {
								frs.close();
							}
							zavriJeLiTreba(fstmt, fc);
						}
					} catch (SQLException e) {
						getSpravce().handleException(e);
						zrusTransakciPokudExistuje();
						throw new ApplicationException("exception.database.failed", e);
					} 
				}
			};
		} catch (SQLException e) {
			getSpravce().handleException(e);
			zrusTransakciPokudExistuje();
			throw new ApplicationException("exception.database.failed", e);
		} 
	}

	private TypVolani getTyp(Object[] parametry) throws ApplicationException{
		if (parametry != null && parametry.length > 0) {
			return TypVolani.PRIPRAVENY;
		} 
		return TypVolani.NORMALNI;
		
	}
	
	private void zavriJeLiTreba(Statement stmt, Connection connection) {
		SqlEasy.uzavri(stmt);
		if (!jeAktivniTransakce()) {
			SqlEasy.uzavri(connection);
		}
	}
	

	private Connection ziskejSpojeni() throws ApplicationException{
		try {
			if (!jeAktivniTransakce()) {
				return datasource.getConnection();
			} else {
				return getAktivniTransakce().getConnection();
			}
		} catch (SQLException e) {
			throw new ApplicationException("exception.database.failed",e);
		}
	}

	private Transakce getAktivniTransakce() {
		return lokalniFrontaTransakci.get().peek();
	}
	
	private static enum KomparatorTransakci implements Comparator<Transakce>{
		INSTANCE;

		public int compare(Transakce o1, Transakce o2) {
			// vnoreni musi byt vzdy kladne, takze si muzeme dovolit
			// pouzit tento trik (nehrozi preteceni)
			return o2.vnoreni - o1.vnoreni; 
		}
	}
	
	private class Transakce {
		private final Connection c;
		private final int vnoreni;
		private final SqlChecker[] interceptory;
		
		public Transakce(SqlChecker... interceptory) throws ApplicationException {
			this(0, interceptory);
		}
		
		private Transakce(int vnoreni, SqlChecker... interceptory) throws ApplicationException {
			try {
				this.interceptory = interceptory;
				this.c = SqlHelperImpl.this.datasource.getConnection();
				this.c.setAutoCommit(false);
			} catch (SQLException e) {
				throw new ApplicationException("exception.database.failed",e);
			}
			this.vnoreni = vnoreni;
		}
		
		public Transakce vytvorVnorenou() throws ApplicationException{
			return new Transakce(vnoreni + 1, interceptory);
		}
		
		public Connection getConnection() {
			return c;
		}
	}

	public void ukonciTransakci() throws ApplicationException {
		try {
			Transakce t = lokalniFrontaTransakci.get().poll();
			if (t != null) {
				LOG.info(String.format("Ukoncuji transakci: %s", t.toString()));
				Connection c = t.getConnection();
				c.commit();
				SqlEasy.uzavri(c);
			} else {
				LOG.warn("Snaha o ukonceni neexistujici transakce!");
			}
		} catch (SQLException e) {
			throw new ApplicationException("exception.database.failed",e);
		}
	}

	public boolean probihaTransakce() throws ApplicationException {
		return !lokalniFrontaTransakci.get().isEmpty();
	}

	public void zacniTransakci(SqlChecker... interceptory) throws ApplicationException {
		Transakce t = getAktivniTransakce();
		if (t == null) {
			t = new Transakce(interceptory);
			lokalniFrontaTransakci.get().add(t);
			LOG.info(String.format("Vytvorena nova transakce: %s", t.toString()));
		} else {
			t = t.vytvorVnorenou();
			lokalniFrontaTransakci.get().add(t);
			LOG.info(String.format("Vytvorena vnorena transakce: %s", t.toString()));
		}
	}

	private boolean jeAktivniTransakce(){
		return getAktivniTransakce() != null;
	}
	
	private void zrusTransakciPokudExistuje() throws ApplicationException {
		try {
			Transakce t = lokalniFrontaTransakci.get().poll();
			if (t != null) {
				LOG.info(String.format("Rusim transakci: %s", t.toString()));
				Connection c = t.getConnection();
				c.rollback();
				SqlEasy.uzavri(c);
			} else {
				LOG.warn("Snaha o zruseni neexistujici transakce!");
			}
		} catch (SQLException e) {
			throw new ApplicationException("exception.database.failed",e);
		}
	}
	
}
