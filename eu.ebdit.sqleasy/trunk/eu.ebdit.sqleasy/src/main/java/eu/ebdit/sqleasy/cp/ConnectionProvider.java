package eu.ebdit.sqleasy.cp;

import java.sql.Connection;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

public interface ConnectionProvider {
	
	Connection getConnection(ExceptionHandler handler);
	
	public void closeConnection(Connection connection, ExceptionHandler handler);

}
