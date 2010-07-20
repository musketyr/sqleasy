package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

class DirectDataSourceConnectionProvider implements ConnectionProvider {

	private final DataSource dataSource;
	
	
	
	public DirectDataSourceConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getConnection(ExceptionHandler handler) {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			handler.handleException(e);
		}
		throw new IllegalStateException("Cannot obtain connection from datasource!");
	}

}
