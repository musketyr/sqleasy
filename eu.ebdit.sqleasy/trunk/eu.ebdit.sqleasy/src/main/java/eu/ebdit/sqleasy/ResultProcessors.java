package eu.ebdit.sqleasy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Tovarni trida pro tvorbu tech nejcastejsich instanci
 * rozhrani {@link ResultProcessor}. Mela by sem patrit 
 * vetsina implementaci {@link ResultProcessor},
 * ktere v malych obmenach pouzivate velmi casto jako napr. 
 * zjistovani, zda dotaz nevratil zadny vysledek, nebo
 * ziskani hodnoty pouze jednoho jedinneho sloupce. Zasadne sem
 * nepatri specificke implementace, ktere jsou pouzity v kodu pouze jednou
 * ci dvakrat. Tyto implementace patri do trid "data acces object".
 * @author Vladimir Orany
 *
 */
public class ResultProcessors {
	
	/**
	 * Soukromy konstruktor zamezuje vzniku instanci a dedeni.
	 */
	private ResultProcessors() {
		// zabranuje vzniku instanci a dedeni
	}
	
	/**
	 * Ziska datum ulozene databazi, jako objekt kalendare.
	 * @param jmenoSloupce jmeno sloupce s datovym udajem
	 * @param defaultniHodnota hodnota, ktera bude vracena v nouzovem pripade
	 * @return objekt kalendare vyjadrujici datum ulozene v databazi
	 */
	public static ResultProcessor<Calendar> ziskejKalendar(final String jmenoSloupce, final Calendar defaultniHodnota){
		return new ResultProcessor<Calendar>(){
			public Calendar processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					Calendar cal = Calendar.getInstance();
					String s = rs.getString(jmenoSloupce);
					int year = Integer.parseInt(s.substring(0, 4));
					int month = Integer.parseInt(s.substring(5, 7)) - 1;
					int date = Integer.parseInt(s.substring(8, 10));
					cal.set(year, month, date);
					return cal;
				}
				return defaultniHodnota;
			}
		};
	}
	
	/**
	 * Ziska hodnotu z daneho sloupce prvniho radku vysledku, nebo defaultni
	 * hodnotu, pokud je vysledek prazdny.
	 * @param jmenoSloupce	jmeno sloupce, ze ktereho chceme ziskat hodnotu
	 * @param defaultniHodnota	hodnota, ktera se pouzije, pokud je vysledek prazdny
	 * @return objekt implementujici rozhrani {@link ResultProcessor}, ktery vraci 
	 * 		hodnotu z daneho sloupce prvniho radku jako integer, nebo defaultni hodnotu, 
	 * 		pokud je vysledek prazdny 
	 */
	public static ResultProcessor<Integer> ziskejInteger(final String jmenoSloupce, final int defaultniHodnota){
		return new ResultProcessor<Integer>(){
			public Integer processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					Integer i = rs.getInt(jmenoSloupce);
					if (!rs.wasNull()) {
						return i;
					}
				}
				return defaultniHodnota;
			}
		};
	}
	
	/**
	 * Vraci objekt {@link ResultProcessor}, jehoz metoda
	 * {@link ResultProcessor#processResultSet(Iterable)} vraci
	 * logickou hodnotu, zda je vysledek prazdny (neobsahuje zadne radky) ci nikoli.
	 * @return objekt {@link ResultProcessor}, jehoz metoda
	 * {@link ResultProcessor#processResultSet(Iterable)} vraci
	 * logickou hodnotu, zda je vysledek prazdny (neobsahuje zadne radky) ci nikoli
	 */
	public static ResultProcessor<Boolean> jePrazdny(){
		return new ResultProcessor<Boolean>(){
			public Boolean processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
						if (rs.getObject(i) != null) {
							return false;
						}
					}
				}
				return true;
			}
		};
	}
	
	/**
	 * Ziska hodnotu z daneho sloupce prvniho radku vysledku, nebo defaultni
	 * hodnotu, pokud je vysledek prazdny.
	 * @param jmenoSloupce	jmeno sloupce, ze ktereho chceme ziskat hodnotu
	 * @param defaultniHodnota	hodnota, ktera se pouzije, pokud je vysledek prazdny
	 * @param <T> trida objektu, ktery je vracen metodou {@link ResultProcessor#processResultSet(Iterable)} vraceneho objektu
	 * @return objekt implementujici rozhrani {@link ResultProcessor}, ktery vraci 
	 * 		hodnotu z daneho sloupce prvniho radku jako instanci tridy <code>T</code>, nebo defaultni hodnotu, 
	 * 		pokud je vysledek prazdny 
	 */
	public static <T> ResultProcessor<T> ziskejObjekt(final String jmenoSloupce, final T defaultniHodnota){
		return new ResultProcessor<T>(){
			public T processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					@SuppressWarnings("unchecked") T ret = (T)rs.getObject(jmenoSloupce);
					return ret;
				}
				return defaultniHodnota;
			}
		};
	}
	

	
	/**
	 * Ziska hodnotu prvniho sloupce prvniho radku vysledku, nebo <code>null</code>,
	 * pokud je vysledek prazdny.
	 * @param trida trida objektu, ktery je vracen metodou {@link ResultProcessor#processResultSet(Iterable)} vraceneho objektu
	 * @param <T> trida objektu, ktery je vracen metodou {@link ResultProcessor#processResultSet(Iterable)} vraceneho objektu
	 * @return objekt implementujici rozhrani {@link ResultProcessor}, ktery vraci 
	 * 		hodnotu z prvniho sloupce prvniho radku jako instanci tridy <code>T</code>, nebo defaultni hodnotu, 
	 * 		pokud je vysledek prazdny 
	 */
	public static <T> ResultProcessor<T> vysledek(Class<T> trida){
		return new ResultProcessor<T>(){
			public T processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					@SuppressWarnings("unchecked") T ret = (T)rs.getObject(1);
					return ret;
				}
				return null;
			}
		};
	}

	/**
	 * Ziska seznam hodnot z daneho sloupce vysledku, nebo prazdny seznam,
	 * pokud je vysledek prazdny.
	 * @param jmenoSloupce	jmeno sloupce, ze ktereho chceme ziskat hodnotu
	 * @param tridaObjektu trida objektu, jehoz seznam se ma vracet
	 * @param <T> trida objektu, jehoz seznam bude vracen metodou {@link ResultProcessor#processResultSet(Iterable)} vraceneho objektu
	 * @return objekt implementujici rozhrani {@link ResultProcessor}, ktery vraci 
	 * 		seznam hodnot z daneho sloupce jako instanci seznamu s paramterem tridy <code>T</code>, nebo prazdny seznam, 
	 * 		pokud je vysledek prazdny 
	 */
	public static <T> ResultProcessor<List<T>> ziskejSeznamObjektu(final String jmenoSloupce, final Class<T> tridaObjektu){
		return new ResultProcessor<List<T>>(){
			@SuppressWarnings("unchecked")
			public List<T> processResultSet(Iterable<ResultSet> irs) throws SQLException {
				List<T> list = new ArrayList<T>();
				for (ResultSet rs : irs) {
					T ret;
					if (String.class.equals(tridaObjektu)) {
						ret = (T)rs.getString(jmenoSloupce);
					} else {
						ret = (T)rs.getObject(jmenoSloupce);
					}
					
					list.add(ret);
				}
				return Collections.unmodifiableList(list);
			}
		};
	}


	/**
	 * Ziska seznam hodnot z prvniho sloupce vysledku, nebo prazdny seznam,
	 * pokud je vysledek prazdny.
	 * @param jmenoSloupce	jmeno sloupce, ze ktereho chceme ziskat hodnotu
	 * @param <T> trida objektu, jehoz seznam bude vracen metodou {@link ResultProcessor#processResultSet(Iterable)} vraceneho objektu
	 * @return objekt implementujici rozhrani {@link ResultProcessor}, ktery vraci 
	 * 		seznam hodnot z daneho sloupce jako instanci seznamu s paramterem tridy <code>T</code>, nebo prazdny seznam, 
	 * 		pokud je vysledek prazdny 
	 */
	public static <T> ResultProcessor<List<T>> ziskejSeznamObjektu(final Class<T> tridaObjektu){
		return new ResultProcessor<List<T>>(){
			public List<T> processResultSet(Iterable<ResultSet> irs) throws SQLException {
				List<T> list = new ArrayList<T>();
				for (ResultSet rs : irs) {
					@SuppressWarnings("unchecked") T ret = (T)rs.getObject(1);
					list.add(ret);
				}
				return Collections.unmodifiableList(list);
			}
		};
	}
	
	/**
	 * Ziska mapu hodnot z prvnich dvou sloupcu, nebo prazdnou mapu, je-li vysledek prazdny. Prvni sloupec tvori klice,
	 * druhy sloupec hodnoty.
	 * @param <K> trida klicu
	 * @param <V> trida hodnot
	 * @param keyClass trida klicu
	 * @param valueClass trida hodnota
	 * @return mapu tvorenou klici z prvniho sloupce a hodnotami z druheho sloupce, nebo prazdnou mapu
	 * pokud je vysledek prazdny
	 */
	public static <K, V> ResultProcessor<Map<K,V>> ziskejMapu(Class<K> keyClass, Class<V> valueClass){
		return new ResultProcessor<Map<K,V>>(){
			public Map<K, V> processResultSet(Iterable<ResultSet> irs)
					throws SQLException {
				Map<K, V> ret =  new LinkedHashMap<K, V>();
				for (ResultSet rs : irs) {
					@SuppressWarnings("unchecked") K key = (K) rs.getObject(1);
					@SuppressWarnings("unchecked") V value = (V) rs.getObject(2);
					ret.put(key, value);
				}
				return Collections.unmodifiableMap(ret);
			}
		};
	}
	
}
