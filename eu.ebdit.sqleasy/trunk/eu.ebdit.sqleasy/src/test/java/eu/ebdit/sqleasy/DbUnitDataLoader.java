package eu.ebdit.sqleasy;

import java.io.InputStream;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.cp.ConnectionProviders;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;

/**
 * Loads test data in the form of DbUnit XML into a database.
 * 
 * @author rcoffin
 */
public class DbUnitDataLoader
{
	private static ConnectionProvider connectionProvider = ConnectionProviders.usingDriverManager("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:my-project-test", "sa", "");
    
    public static void populateTestData() throws Exception
    {

        connectionProvider = ConnectionProviders.usingDriverManager("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:my-project-test", "sa", "");

        SqlHelper helper = SqlEasy.getHelper(connectionProvider);
		helper.setHandler(ExceptionHandlers.stackTraceHandler());
        helper.execute("CREATE TABLE TABULKA (ID int NOT NULL, JMENO varchar(255), PRIJMENI varchar(255), BLA varchar(255))");
        

        InputStream testData = DbUnitDataLoader.class.getResourceAsStream("/fancy.db.xml");
        FlatXmlDataSet dataSet = new FlatXmlDataSet(testData);
        IDatabaseConnection con = new DatabaseConnection(connectionProvider.getConnection(ExceptionHandlers.stackTraceHandler()));

        DatabaseOperation.INSERT.execute(con, dataSet);

        con.close();
    }
    
    public static ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}
}
