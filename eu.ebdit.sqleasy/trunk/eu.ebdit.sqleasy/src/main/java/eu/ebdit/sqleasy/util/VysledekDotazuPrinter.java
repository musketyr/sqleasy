package eu.ebdit.sqleasy.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import eu.ebdit.sqleasy.ResultProcessor;

public class VysledekDotazuPrinter {

	private VysledekDotazuPrinter() {
		// prevents instance creation and subtyping
	}
	
	public static <T> ResultProcessor<T> vypis(Class<T> otisk){
		return new ResultProcessor<T>(){
			public T processResultSet(Iterable<ResultSet> irs) throws SQLException {
				for (ResultSet rs : irs) {
					int columnCount = rs.getMetaData().getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						System.out.print(rs.getMetaData().getColumnName(i + 1));
						if (i < columnCount) {
							System.out.print(" ~ ");
						}
					}
					System.out.println();
					break;
				}
				for (ResultSet rs : irs) {
					int columnCount = rs.getMetaData().getColumnCount();
					for (int i = 0; i < columnCount; i++) {
						System.out.print("" + rs.getObject(i + 1));
						if (i < columnCount) {
							System.out.print(" ~ ");
						}
					}
					System.out.println();
				}
				return null;
			}
		};
	}
	
	
}
