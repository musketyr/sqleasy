package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

public final class ConnectionProviders {

	public static ConnectionProvider forConnection(Connection conn){
		return new DirectConnectionProvider(conn, true);
	}
	
	public static ConnectionProvider forConnection(Connection conn, boolean closeAutomatically){
		return new DirectConnectionProvider(conn, closeAutomatically);
	}
	
	public static ConnectionProvider forDataSource(DataSource ds){
		return new DirectDataSourceConnectionProvider(ds, true);
	}

	public static ConnectionProvider forDataSource(DataSource ds, boolean closeAutomatically){
		return new DirectDataSourceConnectionProvider(ds, closeAutomatically);
	}
	
	public static ConnectionProvider forDataSource(String dataSourceName){
		return new DataSourceConnectionProvider(dataSourceName, true);
	}
	
	public static ConnectionProvider forDataSource(String dataSourceName, boolean closeAutomatically){
		return new DataSourceConnectionProvider(dataSourceName, closeAutomatically);
	}
	
	public static ConnectionProvider usingDriverManager(String managerClassName, String url, Map<String, Object> properties){
		return new DriverManagerConnectionProvider(managerClassName, url, properties);
	}
	
	public static ConnectionProvider usingDriverManager(String url, Map<String, Object> properties){
		return new DriverManagerConnectionProvider(null, url, properties);
	}
	
	public static ConnectionProvider usingDriverManager(String managerClassName, String url, String username, String password){
		return new DriverManagerConnectionProvider(managerClassName, url, prepareProps(username, password, true));
	}
	
	public static ConnectionProvider usingDriverManager(String url, String username, String password){
		return new DriverManagerConnectionProvider(null, url, prepareProps(username, password, true));
	}
	
	public static ConnectionProvider usingDriverManager(String managerClassName, String url, String username, String password, boolean closeAutomatically){
		return new DriverManagerConnectionProvider(managerClassName, url, prepareProps(username, password, closeAutomatically));
	}
	
	public static ConnectionProvider usingDriverManager(String url, String username, String password, boolean closeAutomatically){
		return new DriverManagerConnectionProvider(null, url, prepareProps(username, password, closeAutomatically));
	}
	
	private static Map<String, Object> prepareProps(String username, String password, boolean closeAutomatically){
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("user", username);
		props.put("password", password);
		props.put(ConnectionProviders.CLOSE_AUTOMATICALLY, Boolean.valueOf(closeAutomatically));
		return props;
	}
	
	public static void close(Connection c, ExceptionHandler handler){
		try {
			if (c != null) {
				c.close();
			}
		} catch (SQLException e) {
			handler.handleException(e);
		}
	}

	public static final String CLOSE_AUTOMATICALLY = "closeAutomatically";
	
}
