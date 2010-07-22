package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

class DirectDataSourceConnectionProvider implements ConnectionProvider {

	private final DataSource dataSource;
	private final boolean closeAutomatically;
	
	
	public DirectDataSourceConnectionProvider(DataSource dataSource, boolean closeAutomatically) {
		this.dataSource = dataSource;
		this.closeAutomatically = closeAutomatically;
	}

	public Connection getConnection(ExceptionHandler handler) {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			handler.handleException(e);
		}
		throw new IllegalStateException("Cannot obtain connection from datasource!");
	}
	
	public void closeConnection(Connection connection, ExceptionHandler handler) {
		if (this.closeAutomatically) {
			ConnectionProviders.close(connection, handler);
		}
	}

}
