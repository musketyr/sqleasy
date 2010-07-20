package eu.ebdit.sqleasy.checkers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.ebdit.sqleasy.SqlEasy;


/**
 * Kontrolor, ktery hlida praci se sloupci.
 * @author Vladimir Orany
 *
 */
public enum SloupceKontrolor implements SqlChecker {

	
	/**
	 * Kontrolor, ktery hlida, aby nebyl pridan sloupec, ktery jiz pro danou tabulku existuje.
	 */
	ADD {
		
		@Override public String toString() { return "Kontrolor pridavani sloupcu"; }
		
	} ;

	Logger log = Logger.getLogger(SloupceKontrolor.class);
	
	public boolean moznoSpustit(Connection c, String sql,
			Object... parametry) {
		if (parametry != null && parametry.length > 0) {
			// tohle jeste zpracovat neumime
			return true;
		}
		if (!sql.endsWith(";")) {
			sql = sql + ";";
		}
		String regexJeAddColumn = "alter\\s+table\\s+`?(\\w+)`?((\\s*(add\\s+column\\s+(`?\\w+`?).+?)[,;])+)";
		Matcher m = Pattern.compile(regexJeAddColumn, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(sql);
		if (m.matches()) {
			String tabulka = m.group(1);
			String addColumnStrings[] = m.group(2).split("[,;]");
			List<String> sloupce = new ArrayList<String>(); 
			
			String columnRegexp = "\\s*add\\s+column\\s+`?(\\w+)`?.+\\s*";
			Pattern pc = Pattern.compile(columnRegexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			for (String cs : addColumnStrings) {
				Matcher mc = pc.matcher(cs);
				if (mc.matches()) {
					sloupce.add(mc.group(1));
				} else {
					log.error("Not matches!! regex: \"" + columnRegexp + "\", text: \"" + cs + "\"");
				}
			}
			try {
				List<String> skutecneSloupce = SqlEasy.getJmenaSloupcu(c, tabulka);
				int pocetSloupcu = skutecneSloupce.size();
				skutecneSloupce.removeAll(sloupce);
				return skutecneSloupce.size() == pocetSloupcu;
			} catch (SQLException e) {
				log.error("Chyba pri spousteni kontroloru", e);
			} 
			
		}
		return true;
	}

}
