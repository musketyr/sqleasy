package eu.ebdit.sqleasy.handlers;

import java.sql.SQLException;



/**
 * Simple interface for handling SQL exceptions.
 * @author Vladimir Orany
 *
 */
public interface ExceptionHandler {
	/**
	 * Handles exception.
	 * @param ex thrown exception
	 */
	void handleException(SQLException ex);
}
