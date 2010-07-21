package eu.ebdit.sqleasy.cp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

class DataSourceConnectionProvider extends DirectDataSourceConnectionProvider {

	private static final InitialContext ic;
	private static final ConcurrentMap<String, DataSource> dsCache = new ConcurrentHashMap<String, DataSource>(6);
	
	static {
		try {
			ic = new InitialContext();
		} catch (NamingException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public DataSourceConnectionProvider(String dsName) {
		super(lookupDataSource(dsName));
	}
	
	
	private static final DataSource lookupDataSource(String dsName) {
		DataSource dataSource = dsCache.get(dsName);
		if (dataSource != null) {
			return dataSource;
		}
		try {
			dataSource = (DataSource) ic.lookup(dsName);
			dsCache.put(dsName, dataSource);
			return dataSource;
		} catch (NamingException e) {
			throw new IllegalArgumentException("Datasource \"" + dsName + "\" does not exist!", e);
		}
	}
	
}
