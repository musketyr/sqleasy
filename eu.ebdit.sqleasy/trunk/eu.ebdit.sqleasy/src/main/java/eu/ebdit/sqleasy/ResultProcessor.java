package eu.ebdit.sqleasy;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Implementations of this interface processes 
 * Zpracuje vysledek dotazu do databaze. Pro nejbeznejsi zpusoby zpracovani
 * (ziskani jednoho cisla, otestovani prazdnosti vysledku) pouzijte tovarni 
 * metody tridy {@link ResultProcessors}, pro specialni pripady (tzn. takove
 * kdy byste museli napr. volat vicekrat metodu {@link QueryResult#processWith(IZpracovaniVysledku)}
 * pouzijte vlastni implementace rozhrani. Ve vetsine pripadu muze byt vyhodne
 * pouziti anonymni trid.
 * @author Vladimir Orany
 *
 * @param <T> trida objektu, ktery ma vracet metoda {@link #processResultSet(Iterable)}
 * @see SqlHelper#executeQuery(String, Object...)
 * @see QueryResult#processWith(IZpracovaniVysledku)
 */
public interface ResultProcessor<T>  {
	/**
	 * Zpracuje vysledek dotazu do databaze a vrati objekt
	 * tridy <code>T</code>
	 * @param irs	iterovatelny vysledek dotazu do databaze
	 * @return	zpracovany vysledek, ktery je instanci objektu tridy <code>T</code>
	 * @throws SQLException pokud dojde k chybe pri praci s databazi
	 */
	T processResultSet(Iterable<ResultSet> irs) throws SQLException;
}
