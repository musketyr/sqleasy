package eu.ebdit.easyjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import eu.ebdit.sqleasy.ResultProcessor;
import eu.ebdit.sqleasy.ResultProcessors;
import eu.ebdit.sqleasy.SqlEasy;
import eu.ebdit.sqleasy.SqlHelper;
import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.cp.ConnectionProviders;

public class ExampleClass {

	public static class MyFancyObject {

		public void setJmeno(String string) {
			// TODO Auto-generated method stub
			
		}

		public void setPrijmeni(String string) {
			// TODO Auto-generated method stub
			
		}

		public void setBla(String string) {
			// TODO Auto-generated method stub
			
		}

	}

	public static void main(String[] args) {
		ConnectionProvider provider = ConnectionProviders.forConnection(null);
		SqlHelper helper = SqlEasy.getHelper(provider);
		String bla = helper.executeQuery("SELECT BLA FROM TABLE WHERE ID=?", 1)
			.processWith(ResultProcessors.first(String.class));
		
		MyFancyObject obj = helper.executeQuery("SELECT JMENO, PRIJMENI, BLA FROM FANCY WHERE ID = ?", 5)
			.processWith(new ResultProcessor<MyFancyObject>() {
				@Override public MyFancyObject processResultSet(Iterable<ResultSet> irs)
						throws SQLException {
					for (ResultSet rs : irs) {
						MyFancyObject myf = new MyFancyObject();
						myf.setJmeno(rs.getString("JMENO"));
						myf.setPrijmeni(rs.getString("PRIJMENI"));
						myf.setBla(rs.getString("BLA"));
						return myf;
					}
					return null;
				}
			});
	}
	
}
