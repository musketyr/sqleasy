package eu.ebdit.sqleasy;

import java.sql.Connection;

import javax.sql.DataSource;

import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.cp.ConnectionProviders;
import eu.ebdit.sqleasy.handlers.ExceptionHandler;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;


/**
 * Fasade class which makes working with JDBC more easy.
 * @author Vladimir Orany
 *
 */
public final class SqlEasy {
	
	private SqlEasy() {}
	
	
	/**
	 * Obtains {@link SqlHelper} for given connection provider.
	 * @param provider connection provider for the helper
	 * @return instance of {@link SqlHelper} using given connection provider
	 */
	public static SqlHelper getHelper(ConnectionProvider provider){
		return SqlHelperImpl.getHelper(provider);
	}
	
	
	// providers
	
	public static ConnectionProvider forConnection(Connection conn){
		return ConnectionProviders.forConnection(conn);
	}
	
	public static ConnectionProvider forDataSource(DataSource ds){
		return ConnectionProviders.forDataSource(ds);
	}
	
	public static ConnectionProvider forDataSource(String dataSourceName){
		return ConnectionProviders.forDataSource(dataSourceName);
	}
	
	// handlers
	
	public static ExceptionHandler defaultHandler(){
		return ExceptionHandlers.defaultHandler();
	}
	
	public static ExceptionHandler stackTraceHandler(){
		return ExceptionHandlers.stackTraceHandler();
	}
	
	public static ExceptionHandler wrappringExceptionHandler(){
		return ExceptionHandlers.wrappringExceptionHandler();
	}

	public static ExceptionHandler nullSafe(ExceptionHandler handler) {
		return ExceptionHandlers.nullSafe(handler);
	}
	
	// processors
}
