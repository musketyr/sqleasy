package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

class DriverManagerConnectionProvider implements ConnectionProvider {

	private final Properties properties;
	private final String url;
	private final boolean closeAutomatically;

	public DriverManagerConnectionProvider(String driverName, String url,
			Map<String, Object> propertiesMap) {
		if (url == null) {
			throw new IllegalArgumentException("Url cannot be null!");
		}
		this.url = url;
		if (driverName != null) {
			try {
				Class.forName(driverName);
			} catch (java.lang.ClassNotFoundException e) {
				throw new IllegalArgumentException("Driver class not found!", e);
			}
		}  /* else new API doesn't need to call .forName() */
		
		if (propertiesMap == null) {
			this.properties = new Properties();
			this.closeAutomatically = true;
		} else {
			Map<String, Object> props = new HashMap<String, Object>(propertiesMap);
			closeAutomatically = initAutoClose(props);
			props.remove(ConnectionProviders.CLOSE_AUTOMATICALLY);
			this.properties = new Properties();
			this.properties.putAll(props);
		}

	}

	@Override
	public Connection getConnection(ExceptionHandler handler) {
		try {
			return DriverManager.getConnection(url, properties);
		} catch (SQLException ex) {
			handler.handleException(ex);
			throw new IllegalStateException("Cannot obtain connection!");
		}
	}
	
	@Override
	public void closeConnection(Connection connection, ExceptionHandler handler) {
		if (closeAutomatically) {
			ConnectionProviders.close(connection, handler);
		}
	}

	private boolean initAutoClose(Map<String, Object> properties) {
		boolean ret = false;
		Object closeAutomatically = properties.get(ConnectionProviders.CLOSE_AUTOMATICALLY);
		if (closeAutomatically == null) {
			ret = true;
		} else if (closeAutomatically instanceof Boolean) {
			Boolean close = (Boolean) closeAutomatically;
			ret = close.booleanValue();
		} else if (closeAutomatically instanceof String) {
			String close = (String) closeAutomatically;
			ret = Boolean.valueOf(close).booleanValue();
		}
		return ret;
	}

}
