package eu.ebdit.sqleasy.checkers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.ebdit.sqleasy.SqlEasy;
import eu.ebdit.sqleasy.SqlHelper;

/**
 * Kontrolor, ktery hlida, aby nebyly mazany triggery, ktere neexistuji,
 *  ci vytvareny triggery, ktere jiz existuji.
 * @author Vladimir Orany
 *
 */
public enum TriggerChecker implements SqlChecker {
	/**
	 * Kontrolor, ktery hlida, aby se nikdo nepokousel vymazat trigger,
	 * ktery neexistuje.
	 *
	 */
	DROP {
		@Override
		boolean muzeSpustitInternal(Connection c, String arg0) {
			try {
				return existujeTrigger(c, arg0);
			} catch (SQLException e) {
				log.error("Chyba pri zjistovani existence triggeru",e);
				return true;
			}
		}
		
		@Override
		String getJmenoTriggeru(String sql) {
			String regex = "\\s*drop\\s+trigger\\s+`?(\\w+)`?\\s*;?";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(sql);
			if (m.matches()) {
				return m.group(1);
			}
			return null;
		}
		
		@Override public String toString() { return "Kontrolor mazani triggeru"; }
		
	},
	/**
	 * Kontrolor, ktery hlida, aby nikdo nevytvoril trigger, ktery jiz existuje.
	 */
	CREATE{
		@Override
		boolean muzeSpustitInternal(Connection c, String arg0) {
			try {
				return !existujeTrigger(c, arg0);
			} catch (SQLException e) {
				log.error("Chyba pri zjistovani existence triggeru",e);
				return true;
			}
		}
		
		@Override
		String getJmenoTriggeru(String sql) {
			String regex = ".*create\\s+trigger\\s+`(\\w+)`.*";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(sql);
			if (m.matches()) {
				return m.group(1);
			}
			return null;
		}
		
		@Override public String toString() { return "Kontrolor tvorby triggeru"; }
	};
	
	protected static Logger log = Logger.getLogger(TriggerChecker.class);
	
	public boolean moznoSpustit(SqlHelper helper, String sql,
			Object... parametry) {
		if (parametry != null && parametry.length > 0) {
			// tohle jeste zpracovat neumime
			return true;
		}
		
		String jmenoTriggeru = getJmenoTriggeru(sql);
		if (jmenoTriggeru == null) {
			return true;
		}
		return muzeSpustitInternal(helper,jmenoTriggeru);
	}
	
	abstract String getJmenoTriggeru(String sql);

	abstract boolean muzeSpustitInternal(Connection c, String jmenoTriggeru);
	
	protected boolean existujeTrigger(Connection c, String jmeno) throws SQLException{
		
		Statement stmt = null;
		try {
			String catalogName = getCatalogName(c);
			
			stmt = c.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM `information_schema`.`TRIGGERS` WHERE TRIGGER_SCHEMA = '" + catalogName + "'" +
					" AND TRIGGER_NAME = '" + jmeno + "'");
			boolean existuje = false;
			while (rs.next()) {
				existuje = true;
			}
			
			return existuje;
		}  finally {
			SqlEasy.uzavri(stmt);
		}
		
		
		
	}

	protected String getCatalogName(Connection c) throws SQLException {
		Statement stmt = null;
		try {
			String tableName = "TEST_TRIGGER_TEMP_TABLE";
			stmt = c.createStatement();
			stmt.execute("DROP TABLE IF EXISTS `" + tableName + "`");
			stmt.execute("CREATE TABLE  `" + tableName + "` ( "
					+ "`PRVNI` int(10) unsigned NOT NULL)");
			stmt.execute("INSERT INTO `" + tableName + "` VALUES (123)");
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
			String catalogName = "";
			while (rs.next()) {
				catalogName = rs.getMetaData().getCatalogName(1);
			}
			stmt.execute("DROP TABLE IF EXISTS `" + tableName + "`");
			return catalogName;
		} finally {
			SqlEasy.uzavri(stmt);
		}
		
	}
	
}
