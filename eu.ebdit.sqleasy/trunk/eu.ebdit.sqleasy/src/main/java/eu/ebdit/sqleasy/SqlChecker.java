package eu.ebdit.sqleasy;

import java.sql.Connection;



/**
 * Kontrolor spusteni SQL prikazu.
 * Metoda {@link #moznoSpustit(Connection, String, Object...)} muze zakazat spusteni skriptu.
 * Pokud kontrolor nedokaze rozhodnout o spusteni prikazu, musi jej vzdy pustit!
 * @author Vladimir Orany
 * @see SqlHelper#zacniTransakci(ISqlPrikazKontrolor...)
 *
 */
public interface SqlChecker {
	/**
	 * Otestuje, zda je mozne metodu pustit. Pokud je vraceno <code>false</code>,
	 * prikaz by nemel byt spusten. Pokud je vraceno <code>true</code> nelze o spusteni
	 * rozhodnout.
	 * @param c			pripojeni k databazi
	 * @param sql		testovany prikaz
	 * @param parametry	parametry prikazu
	 * @return	<code>true</code> pokud nelze rozhodnout o spravnosti prikazu, <code>false</code> pokud je
	 * spousteni prikazu zakazano
	 */
	boolean moznoSpustit(Connection c, String sql, Object... parametry);
}
