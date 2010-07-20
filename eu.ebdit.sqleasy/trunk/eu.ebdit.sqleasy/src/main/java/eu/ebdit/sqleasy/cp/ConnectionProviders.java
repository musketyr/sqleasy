package eu.ebdit.sqleasy.cp;

import java.sql.Connection;

import javax.sql.DataSource;

public final class ConnectionProviders {

	public static ConnectionProvider forConnection(Connection conn){
		return new DirectConnectionProvider(conn);
	}
	
	public static ConnectionProvider forDataSource(DataSource ds){
		return new DirectDataSourceConnectionProvider(ds);
	}
	
	public static ConnectionProvider forDataSource(String dataSourceName){
		return new DataSourceConnectionProvider(dataSourceName);
	}
	
}
