package eu.ebdit.sqleasy.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.ebdit.sqleasy.SqlEasy;
import eu.ebdit.sqleasy.SqlChecker;

/**
 * Kontrolor, ktery testuje, aby se do databaze nevkladali duplicitni hodnoty.
 * Funguje pouze na prikazy INSERT, ktere vkladaji hodnoty vsech sloupcu. 
 * Testuje se, zda jiz v databazi neni radek, ktery mohl vzniknout volanim
 * prave testovaneho prikazu.
 * @author Vladimir Orany
 *
 */
public enum DuplikatyKontrolor implements SqlChecker {
	
	/**
	 * Zakladni instance kontroloru testuje, aby nebyly vkladany duplicitni radky do databaze.
	 */
	INSERT {
		
		public boolean moznoSpustit(Connection c, String sql, Object... parametry) {
			if (parametry != null && parametry.length > 0) {
				// tohle jeste zpracovat neumime
				return true;
			}
			
			String regex = ".*?insert\\s+into\\s+`?(\\w+)`?\\s+values\\s*\\((.*)\\)\\s*;?";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(sql);
			if (m.matches()) {
				String tabulka = m.group(1);
				String[] hodnoty = m.group(2).split(",");
				try {
					String testSelect = getTestSelectInsert(c, tabulka, hodnoty, SqlEasy.getJmenaSloupcu(c, tabulka));
					if (testSelect == null) {
						// z nejakeho duvodu se nepodarilo vytvorit select, musim sql pustit k dalsimu zpracovani
						return true;
					}
					Statement stmt = null;
					try {
						stmt = c.createStatement();
						ResultSet rs = stmt.executeQuery(testSelect);
						while (rs.next()) {
							// radek jiz exituje
							return false;
						}
						return true;
					} finally {
						SqlEasy.uzavri(stmt);
					}
				} catch (SQLException e) {
					Logger.getLogger(DuplikatyKontrolor.class).error("Chyba pri zjistovani duplicty",e);
					return true;
				}
			}
			return true;
		}
		

		private String getTestSelectInsert(Connection c, String tabulka, String[] hodnoty, List<String> skutecneSloupce) {
			if (hodnoty.length != skutecneSloupce.size()) {
				Logger.getLogger(DuplikatyKontrolor.class).warn("rozdilny pocet hodnot a sloupcu, nekde se stala chyba!!!");
				return null;
			}
			
			List<String> paryHodnot = new ArrayList<String>();
			for (int i = 0; i < hodnoty.length; i++) {
				String hodnota = hodnoty[i];
				if (!"default".equalsIgnoreCase(hodnota)) {
					paryHodnot.add(" `" + skutecneSloupce.get(i) + "` = " + hodnota + " ");
				}
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM ").append(tabulka).append(" WHERE ");
			sb.append(StringUtils.toStringBuffer(paryHodnot, " AND ", ""));
			
			return sb.toString();
		}
		
		@Override public String toString() { return "Kontrolor duplicity pri insertech"; }
		
	},
	
	INDEX {
		public boolean moznoSpustit(Connection c, String sql, Object... parametry) {
			if (parametry != null && parametry.length > 0) {
				// tohle jeste zpracovat neumime
				return true;
			}
			
			String regex = "\\s*ALTER\\s+TABLE\\s+`?(\\w+)`?\\s+ADD\\s+INDEX\\s+`?(\\w+)`?\\s*\\(\\w+\\).*+;?";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(sql);
			if (m.matches()) {
				String tabulka = m.group(1);
				String nazevIndexu = m.group(2);
				StringBuilder sb = new StringBuilder();
				sb.append("SHOW INDEX FROM `").append(tabulka).append("` WHERE `Key_name` like '").append(nazevIndexu).append("'");
				try {
					Statement stmt = null;
					try {
						stmt = c.createStatement();
						ResultSet rs = stmt.executeQuery(sb.toString());
						while (rs.next()) {
							// radek jiz exituje
							return false;
						}
						return true;
					} finally {
						SqlEasy.uzavri(stmt);
					}
				} catch (SQLException e) {
					Logger.getLogger(DuplikatyKontrolor.class).error("Chyba pri zjistovani duplicty",e);
					return true;
				}
			}
			return true;
			
		}
		
		@Override public String toString() { return "Kontrolor duplicity indexu"; }
	};
	
	
}
