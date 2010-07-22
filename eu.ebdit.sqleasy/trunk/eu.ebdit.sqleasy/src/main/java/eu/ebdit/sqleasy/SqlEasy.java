package eu.ebdit.sqleasy;

import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.handlers.ExceptionHandler;


/**
 * Fasade class which makes working with JDBC more easy.
 * @author Vladimir Orany
 *
 */
public final class SqlEasy {
	
	private SqlEasy() { /* library class */}
	
	
	/**
	 * Obtains {@link SqlHelper} for given connection provider.
	 * @param provider connection provider for the helper
	 * @return instance of {@link SqlHelper} using given connection provider
	 */
	public static SqlHelper getHelper(ConnectionProvider provider){
		return SqlHelperImpl.getHelper(provider);
	}
	
	/**
	 * Obtains {@link SqlHelper} for given connection provider and exception handler.
	 * @param provider connection provider for the helper
	 * @param handler exception handler for the helper
	 * @return instance of {@link SqlHelper} using given connection provider
	 */
	public static SqlHelper getHelper(ConnectionProvider provider, ExceptionHandler handler){
		SqlHelper ret = SqlHelperImpl.getHelper(provider);
		ret.setHandler(handler);
		return ret;
	}

}
