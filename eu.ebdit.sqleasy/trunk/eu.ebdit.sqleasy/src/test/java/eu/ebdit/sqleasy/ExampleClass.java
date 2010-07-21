package eu.ebdit.sqleasy;

import java.sql.ResultSet;
import java.sql.SQLException;

import eu.ebdit.sqleasy.ResultProcessor;
import eu.ebdit.sqleasy.ResultProcessors;
import eu.ebdit.sqleasy.SqlEasy;
import eu.ebdit.sqleasy.SqlHelper;
import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.cp.ConnectionProviders;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;

public class ExampleClass {

	public static enum MyFancyObjectProcessor implements
			ResultProcessor<MyFancyObject> {
		INSTANCE;
		
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
	}

	public static class MyFancyObject {

		public void setJmeno(String string) {}

		public void setPrijmeni(String string) {}

		public void setBla(String string) {}

	}

	public static void main(String[] args) {
		// tohle by mohl delat supplier
		ConnectionProvider provider = ConnectionProviders.forConnection(null);
		SqlHelper helper = SqlEasy.getHelper(provider);
		helper.setHandler(ExceptionHandlers.stackTraceHandler());
		
		String bla = helper.executeQuery("SELECT BLA FROM TABLE WHERE ID=?", 1)
			.processWith(ResultProcessors.first(String.class));
		System.out.println(bla);
		MyFancyObject obj = helper.executeQuery("SELECT JMENO, PRIJMENI, BLA FROM FANCY WHERE ID = ?", 5)
			.processWith(MyFancyObjectProcessor.INSTANCE);
		System.out.println(obj);
	}
	
}
