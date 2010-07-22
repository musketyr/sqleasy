package eu.ebdit.sqleasy;

import eu.ebdit.sqleasy.processors.ResultProcessor;


/**
 * Vysledek dotazu do databaze. K ziskani potrebnych dat
 * z vysledku pouzijte metodu {@link #processWith(ResultProcessor)}
 * s prislusnym objektem implementujici rozhrani {@link ResultProcessor}.
 * @author Vladimir Orany
 * @see SqlHelper#executeQuery(String, Object...)
 * @see ResultProcessor#processResultSet(Iterable)
 */
public interface QueryResult {
	/**
	 * Zpracuje vysledek a vrati objekt pozdovaneho typu. Metoda m��e b�t na jednom
	 * objektu vol�na pouze jednou, jinak dojde k vyhozen� vyj�mky {@link IllegalStateException}.
	 * @param <T>	trida pozadovaneho objektu
	 * @param zpracovani objekt, ktery je zodpovedny za zpracovani vysledku sql dotazu
	 * @return	objekt pozadovaneho typu <code>T</code> odpovidajici datum ziskanych z databaze
	 * @throws IllegalStateException pokud je metoda volana vicekrat nez jednou
	 */
	<T> T processWith(ResultProcessor<T> zpracovani);
}
