package eu.ebdit.sqleasy.cp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import eu.ebdit.sqleasy.handlers.ExceptionHandler;

class DriverManagerConnectionProvider implements ConnectionProvider {

	private final Properties properties;
	private final String url;

	public DriverManagerConnectionProvider(String driverName, String url,
			Map<String, Object> properties) {
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
		
		if (properties == null) {
			this.properties = new Properties();
		} else {
			this.properties = new Properties();
			this.properties.putAll(properties);
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

}
