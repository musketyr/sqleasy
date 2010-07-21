package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

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
	
	public static ConnectionProvider usingDriverManager(String managerClassName, String url, Map<String, Object> properties){
		return new DriverManagerConnectionProvider(managerClassName, url, properties);
	}
	
	public static ConnectionProvider usingDriverManager(String url, Map<String, Object> properties){
		return new DriverManagerConnectionProvider(null, url, properties);
	}
	
	public static ConnectionProvider usingDriverManager(String managerClassName, String url, String username, String password){
		return new DriverManagerConnectionProvider(managerClassName, url, prepareProps(username, password));
	}
	
	public static ConnectionProvider usingDriverManager(String url, String username, String password){
		return new DriverManagerConnectionProvider(null, url, prepareProps(username, password));
	}
	
	private static Map<String, Object> prepareProps(String username, String password){
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", username);
		props.put("password", password);
		return props;
	}
	
}
