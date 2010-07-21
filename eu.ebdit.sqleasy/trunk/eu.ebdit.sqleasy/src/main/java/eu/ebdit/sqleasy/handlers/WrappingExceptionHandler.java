package eu.ebdit.sqleasy.handlers;

import java.sql.SQLException;


enum WrappingExceptionHandler implements ExceptionHandler{
	INSTANCE;
	
	@Override
	public void handleException(SQLException ex) {
		throw new SqlExceptionWrapper(ex);
	}
}
