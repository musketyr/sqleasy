package eu.ebdit.sqleasy.cp;

import java.sql.Connection;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

final class DirectConnectionProvider implements ConnectionProvider{

	private final Connection connection;

	public DirectConnectionProvider(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public Connection getConnection(ExceptionHandler handler) {
		return connection;
	}
	
}
