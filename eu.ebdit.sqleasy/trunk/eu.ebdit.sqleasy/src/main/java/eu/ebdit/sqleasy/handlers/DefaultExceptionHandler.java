package eu.ebdit.sqleasy.handlers;

import java.sql.SQLException;

/**
 * Zakladni obsluzna trida vyjimek pri komunikaci s databazi. 
 * Nedela nic.
 * @author Vladimir Orany
 *
 */
enum DefaultExceptionHandler implements ExceptionHandler {
	
	/**
	 * Sdilena instance handleru
	 */
	INSTANCE;
	
	public void handleException(SQLException e) { /* do nothing */}

}