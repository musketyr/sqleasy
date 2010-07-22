package eu.ebdit.sqleasy;

import java.sql.ResultSet;
import java.sql.SQLException;

import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;
import eu.ebdit.sqleasy.processors.ResultProcessor;
import eu.ebdit.sqleasy.processors.ResultProcessors;

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

		private String jmeno;
		private String prijmeni;
		private String bla;
		
		public String getJmeno() {
			return jmeno;
		}
		public void setJmeno(String jmeno) {
			this.jmeno = jmeno;
		}
		public String getBla() {
			return bla;
		}
		public void setBla(String bla) {
			this.bla = bla;
		}
		public String getPrijmeni() {
			return prijmeni;
		}
		public void setPrijmeni(String prijmeni) {
			this.prijmeni = prijmeni;
		}
		@Override
		public String toString() {
			return "MyFancyObject [jmeno=" + jmeno + ", prijmeni=" + prijmeni
					+ ", bla=" + bla + "]";
		}
		
		
		
		 

	}

	public static void main(String[] args) throws Exception {
		DbUnitDataLoader.populateTestData();
		ConnectionProvider provider = DbUnitDataLoader.getConnectionProvider();
		
		SqlHelper helper = SqlEasy.getHelper(provider);
		helper.setHandler(ExceptionHandlers.stackTraceHandler());
		
		
		String content = helper.executeQuery("SELECT * FROM TABULKA").processWith(ResultProcessors.print());
		System.out.println(content);
		
		String bla = helper.executeQuery("SELECT BLA FROM TABULKA WHERE ID=?", 1)
			.processWith(ResultProcessors.singleResult(String.class));
		System.out.println(bla);
		
		MyFancyObject obj = helper.executeQuery("SELECT JMENO, PRIJMENI, BLA FROM TABULKA WHERE ID = ?", 3)
			.processWith(MyFancyObjectProcessor.INSTANCE);
		System.out.println(obj);
		
		// not supported by HSQLDB
//		int id = helper.executeInsert("INSERT INTO TABULKA(JMENO, PRIJMENI, BLA) VALUES (?,?,?)", "JMENO", "PRIJMENI", "BLA")
//			.processWith(ResultProcessors.singleResult(Integer.class));
//		System.out.println(id);
	}
	
}
