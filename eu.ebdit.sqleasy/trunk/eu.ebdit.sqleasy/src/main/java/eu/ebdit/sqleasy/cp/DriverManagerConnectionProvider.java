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
			this.closeAutomatically = initAutoClose(props);
			props.remove(ConnectionProviders.CLOSE_AUTOMATICALLY);
			this.properties = new Properties();
			this.properties.putAll(props);
		}

	}

	public Connection getConnection(ExceptionHandler handler) {
		try {
			return DriverManager.getConnection(this.url, this.properties);
		} catch (SQLException ex) {
			handler.handleException(ex);
			throw new IllegalStateException("Cannot obtain connection!");
		}
	}
	
	public void closeConnection(Connection connection, ExceptionHandler handler) {
		if (this.closeAutomatically) {
			ConnectionProviders.close(connection, handler);
		}
	}

	private boolean initAutoClose(Map<String, Object> props) {
		boolean ret = false;
		Object autoClose = props.get(ConnectionProviders.CLOSE_AUTOMATICALLY);
		if (autoClose == null) {
			ret = true;
		} else if (autoClose instanceof Boolean) {
			Boolean close = (Boolean) autoClose;
			ret = close.booleanValue();
		} else if (autoClose instanceof String) {
			String close = (String) autoClose;
			ret = Boolean.valueOf(close).booleanValue();
		}
		return ret;
	}

}
