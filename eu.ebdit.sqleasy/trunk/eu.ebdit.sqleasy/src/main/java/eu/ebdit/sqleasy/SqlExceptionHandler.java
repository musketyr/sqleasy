package eu.ebdit.sqleasy;

import java.sql.SQLException;


/**
 * Simple interface for handling SQL exceptions.
 * @author Vladimir Orany
 *
 * @see SqlEasy#asIterable(java.sql.ResultSet) ukazka vhodneho zpusobu vyuziti
 */
public interface SqlExceptionHandler {
	/**
	 * Handles SQL exception.
	 * @param ex thrown exception
	 */
	void handleException(SQLException ex);
}
