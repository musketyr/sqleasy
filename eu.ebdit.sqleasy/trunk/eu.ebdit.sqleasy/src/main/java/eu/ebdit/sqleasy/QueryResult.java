package eu.ebdit.sqleasy;


/**
 * Vysledek dotazu do databaze. K ziskani potrebnych dat
 * z vysledku pouzijte metodu {@link #processWith(ResultProcessor)}
 * s prislusnym objektem implementujici rozhrani {@link ResultProcessor}.
 * @author Vladimir Orany
 * @see SqlHelper#provedDotaz(String, Object...)
 * @see ResultProcessor#processResultSet(Iterable)
 */
public interface QueryResult {
	/**
	 * Zpracuje vysledek a vrati objekt pozdovaneho typu. Metoda mùže být na jednom
	 * objektu volána pouze jednou, jinak dojde k vyhození vyjímky {@link IllegalStateException}.
	 * @param <T>	trida pozadovaneho objektu
	 * @param zpracovani objekt, ktery je zodpovedny za zpracovani vysledku sql dotazu
	 * @return	objekt pozadovaneho typu <code>T</code> odpovidajici datum ziskanych z databaze
	 * @throws ApplicationException pokud dojde k chybe pri praci s databazi
	 * @throws IllegalStateException pokud je metoda volana vicekrat nez jednou
	 */
	<T> T processWith(ResultProcessor<T> zpracovani);
}
