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
	
	@Override
	public Connection getConnection(ExceptionHandler handler) {
		return connection;
	}
	
	@Override
	public void closeConnection(Connection connection, ExceptionHandler handler) {
		if (closeAutomatically) {
			ConnectionProviders.close(connection, handler);
		}
	}
	
}
