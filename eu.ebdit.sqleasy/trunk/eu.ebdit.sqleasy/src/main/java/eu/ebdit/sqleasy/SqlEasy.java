package eu.ebdit.sqleasy;

import eu.ebdit.sqleasy.cp.ConnectionProvider;


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
	
}
