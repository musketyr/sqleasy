package eu.ebdit.sqleasy.checkers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class SqlCheckers {

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
