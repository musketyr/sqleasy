package eu.ebdit.sqleasy.cp;

import java.sql.Connection;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

final class DirectConnectionProvider implements ConnectionProvider{

	private final Connection connection;
	private final boolean closeAutomatically;

	public DirectConnectionProvider(Connection connection, boolean cloaseAutomatically) {
		this.connection = connection;
		this.closeAutomatically = cloaseAutomatically;
	}
	
	public Connection getConnection(ExceptionHandler handler) {
		return this.connection;
	}
	
	public void closeConnection(Connection conn, ExceptionHandler handler) {
		if (this.closeAutomatically) {
			ConnectionProviders.close(conn, handler);
		}
	}
	
}
