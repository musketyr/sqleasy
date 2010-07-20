package eu.ebdit.sqleasy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


/**
 * Knihovni trida pro zjednoduseni prace s rozhranim JDBC.
 * @author Vladimir Orany
 *
 */
public class SqlEasy {
	
	private static final Logger log = Logger.getLogger(SqlEasy.class);
	private static final ConcurrentMap<String, DataSource> dsCache = new ConcurrentHashMap<String, DataSource>(6);
	
	private static final InitialContext ic;
	static {
		try {
			ic = new InitialContext();
		} catch (NamingException e) {
			log.error("Chyba pri ziskavani naming kontextu", e);
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Soukromy konstruktor zamezuje vzniku instanci.
	 */
	private SqlEasy() {}
	
	/**
	 * Zabali result set do rozhrani iterable, takze 
	 * je mozne jej pouzit v rozsirenem foreach.
	 * Jednotlive elementy result setu jsou upraveny tak,
	 * aby nebylo mozne menit pri prochazeni cyklu polohu kurzoru. 
	 * @param rs	result set, ktery ma byt zabalen do rozhrani {@link Iterable}
	 * @return		result set zabaleny do rozhrani {@link Iterable}
	 * * @throws		SqlExceptionWrapper pokud dojde k chybe pri praci s databazi
	 */
	static Iterable<ResultSet> asIterable(final ResultSet rs){
		return asIterable(rs, SqlHelper.ZakladniSpravceSqlVyjimek.INSTANCE);
	}
	
	/**
	 * Zabali result set do rozhrani iterable, takze 
	 * je mozne jej pouzit v rozsirenem foreach.
	 * Prislusny {@link SqlExceptionHandler} zpracuje vyjimky, ktere se obejvi pri praci
	 * s databazi.  
	 * Jednotlive elementy result setu jsou upraveny tak,
	 * aby nebylo mozne menit pri prochazeni cyklu polohu kurzoru. 
	 * @param rs	result set, ktery ma byt zabalen do rozhrani {@link Iterable}
	 * @param osv	exception handler pro zpracovani chyb, ke kterym dojde pri praci s databazi
	 * @return		result set zabaleny do rozhrani {@link Iterable}
	 * @throws		SqlExceptionWrapper pri prochazeni cyklu, pokud dojde k chybe pri praci s databazi
	 */
	static Iterable<ResultSet> asIterable(final ResultSet rs, final SqlExceptionHandler osv){
		
		final ResultSet wrapped = new CursorlessResultSet(rs);
		
		return new Iterable<ResultSet>(){

			public Iterator<ResultSet> iterator() {
				return new Iterator<ResultSet>(){

					public boolean hasNext() {
						try {
							return rs.next();
						} catch (SQLException e) {
							osv.handleException(e);
							// handler by to mel zaridit sam, ale pokud ne, tak ji vyhodime sami
							// je to porad lepsi, nez vracet null a dojit k null pointer exception
							throw new SqlExceptionWrapper(e);
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
	
	/**
	 * Bezpecne uzavre {@link Statement} s
	 * @param s	{@link Statement} k uzavreni
	 */
	static void uzavri(Statement s){
		try {
			if (s != null) {
				s.close();
			}
		} catch (SQLException e) {
			log.error("Chyba pri uzvirani Statement", e);
		}
	}
	
	/**
	 * Bezpecne uzavre {@link Connection} s
	 * @param c	{@link Connection} k uzavreni
	 */
	static void uzavri(Connection c){
		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			log.error("Chyba pri uzvirani Statement", e);
		}
	}
	
	/**
	 * Ziska datasource daneho jmena.
	 * @param dataSourceName	jmeno datasource
	 * @return	datasource daneho jmena
	 * @throws IllegalArgumentException pokud se nepodari datasource daneho jmena najit
	 */
	public static DataSource getDataSource(String dataSourceName) {
		DataSource ds = dsCache.get(dataSourceName);
		if (ds == null) {
			ds = lookupDataSource(dataSourceName);
			dsCache.putIfAbsent(dataSourceName, ds);
		}
		return ds;
	}

	/**
	 * Ziska helper pro dany datasource
	 * @param dsName	jmeno datasource
	 * @return			helper pro dany datasource
	 */
	public static SqlHelper getPomocnik(String dsName){
		return SqlHelperImpl.getHelper(dsName);
	}
	
	/**
	 * Pokusi se najit datasouce podle jeho nazvu.
	 * @param dsName	nazev datasource
	 * @return	datasouce daneho jmena
	 * @throws IllegalArgumentException pokud datasource daneho nazvu neexistuje
	 */
	private static DataSource lookupDataSource(String dsName) {
		try {
			return (DataSource) ic.lookup(dsName);
		} catch (NamingException e) {
			log.error("Chyba pri ziskavani datasource", e);
			throw new IllegalArgumentException("Datasource daneho nazvu neexistuje: " + dsName);
		}
	}
	
	static List<String> getJmenaSloupcu(Connection c, String tabulka) throws SQLException{
		Statement s = null;
		try {
			List<String> skutecneSloupce = new ArrayList<String>();
			s = c.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM " + tabulka + " LIMIT 1");
			int pocetSloupcu = rs.getMetaData().getColumnCount();
			for (int i = 1; i <=  pocetSloupcu; i++) {
				skutecneSloupce.add(rs.getMetaData().getColumnName(i));
			}
			return skutecneSloupce;
		} finally {
			uzavri(s);
		}

	}
	
	
}
