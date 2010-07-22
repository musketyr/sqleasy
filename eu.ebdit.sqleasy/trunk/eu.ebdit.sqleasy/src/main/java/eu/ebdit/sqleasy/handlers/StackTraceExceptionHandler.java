package eu.ebdit.sqleasy.handlers;

import java.sql.SQLException;

enum StackTraceExceptionHandler implements ExceptionHandler{
	INSTANCE;
	
	public void handleException(SQLException ex) {
		if (ex != null) {
			ex.printStackTrace();
		}
	}
}
