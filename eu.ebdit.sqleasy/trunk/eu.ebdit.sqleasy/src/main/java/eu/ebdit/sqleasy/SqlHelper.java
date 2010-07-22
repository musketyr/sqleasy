package eu.ebdit.sqleasy;



import org.omg.CORBA.portable.ApplicationException;

import eu.ebdit.sqleasy.checkers.SqlChecker;
import eu.ebdit.sqleasy.handlers.ExceptionHandler;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;

/**
 * Rozhrani pro jednoduche provadeni prikazu do databaze. Tridy implementujici
 * toto rozhrani se zarucuji, ze obslouzi vsechny dulezite cinnosti nutne
 * pri praci s databazi, zejmena ziskavani zdroju a jejich bezpecne uvolnovani
 * a zpracovani vyjimek, ktere nastanou pri komunikaci s databazi. Dale
 * se zavazuji implemetnovat zakladni transakcni rozhrani, ktere musi byt
 * implementovano bezpecne pro praci s vlakny.
 * @author Vladimir Orany
 *
 */
public interface SqlHelper {
	
	
	/**
	 * Zahaji novou transakci. Opetovne zavolani vyusti ve vytovreni vnorene transakce. 
	 * Transakce je automaticky zrusena, pokud dojde k chybe behem provadeni sql prikazu.
	 * @param kontrolori kontrolori, ktere budou posuzovat spravnost spoustenych sql prikazu
	 * @throws ApplicationException pokud dojde k chybe pri tvoreni transakce
	 */
	void beginTransaction(SqlChecker... interceptory);
	
	/**
	 * Zjisti, zda prave probiha nejaka transakce. 
	 * @return <code>true</code> pokud nejaka transakce probiha
	 * @throws ApplicationException	pokud dojde k chybe pri zjistovani stavu transakce
	 */
	boolean isTransactionActive();
	
	/**
	 * Potvrdi zmeny provedene behem transakce a ukonci aktualni transakci. 
	 * @throws ApplicationException pokud se nepodari transakci odeslat
	 * @throws NullPointerException pokud jiz zadna transakce neprobiha
	 */
	void commitTransaction();
	
	/**
	 * Vrati spravce vyjimek. Hodnota musi byt vzdy nenullova, misto vraceni <code>null</code>
	 * musi implementatori vracet {@link  ExceptionHandlers#defaultHandler()}.
	 * @return spravce vyjimek
	 */
	ExceptionHandler getHandler();
	
	/**
	 * Nastavi noveho spravce vyjimek.
	 * @param handler novy spravce vyjimek
	 */
	void setHandler(ExceptionHandler handler);
	
	/**
	 * Provede SQL dotaz a vrati vysledek jako instanci rozhrani {@link QueryResult}.
	 * Textova hodnota dotazu muze obsahovat otazniky, ktere jsou nahrazeny
	 * parametry volani metody. Pocet otazniku i parametru musi byt vzdy shodny.
	 * @param sql			sql dotaz (napr. SELECT * FROM TABULKA WHERE SLOUPEC = HODNOTA)
	 * @param parametry		parametry, ktere maji byt dosazeni do volani dotazu
	 * @return	vysledek dotazu jako instanci implementujici rozhrani {@link QueryResult}
	 * @throws ApplicationException pokud dojde k chybe pri praci s databazi
	 */
	QueryResult executeQuery(String sql, Object... parametry);
	
	/**
	 * Provede SQL aktualizaci a vrati pocet ovlivnenych radku.
	 * Textova hodnota dotazu muze obsahovat otazniky, ktere jsou nahrazeny
	 * parametry volani metody. Pocet otazniku i parametru musi byt vzdy shodny.
	 * @param sql			sql aktualizace (napr. DELETE FROM..., INSERT INTO..., ALTER TABLE..., UPDATE...)
	 * @param parametry		parametry, ktere maji byt dosazeni do volani dotazu
	 * @return	pocet radku ovlivnenych aktualizaci
	 * @throws ApplicationException pokud dojde k chybe pri praci s databazi
	 */
	int execute(String sql, Object... parametry);
	
	/**
	 * Provede insert a vrati automaticky generovane klice. Tato funkce nemusi byt podprorovana
	 * databazi. V pripade problemu pouzijte {@link #execute(String, Object...)}.
	 * @param sql
	 * @param parametry
	 * @return 
	 */
	QueryResult executeInsert(String sql, Object... parametry);
	
}
