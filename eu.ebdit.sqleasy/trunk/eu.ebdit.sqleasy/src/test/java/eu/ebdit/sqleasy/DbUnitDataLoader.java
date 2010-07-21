package eu.ebdit.sqleasy;

import java.io.InputStream;
import java.sql.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.ejb.HibernateEntityManager;

import eu.ebdit.sqleasy.cp.ConnectionProvider;
import eu.ebdit.sqleasy.cp.ConnectionProviders;
import eu.ebdit.sqleasy.handlers.ExceptionHandler;
import eu.ebdit.sqleasy.handlers.ExceptionHandlers;

/**
 * Loads test data in the form of DbUnit XML into a database.
 * 
 * @author rcoffin
 */
public class DbUnitDataLoader
{
    private InputStream testData;
    private Connection connection;

	private ConnectionProvider connectionProvider;
    
    public DbUnitDataLoader(InputStream testData, Connection connection)
    {
        this.testData = testData;
        this.connection = connection;
    }

    /**
     * Replace existing data with test data
     * 
     * @throws Exception
     */
    public void populateTestData() throws Exception
    {
        FlatXmlDataSet dataSet = new FlatXmlDataSet(testData);

        IDatabaseConnection con = new DatabaseConnection(connection);

        DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);

        con.close();
    }
    

    public void setUp() throws Exception
    {
        InputStream testData = DbUnitDataLoader.class.getResourceAsStream("/user.db.xml");

        connectionProvider = ConnectionProviders.usingDriverManager("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:my-project-test", "sa", "");

        DbUnitDataLoader loader = new DbUnitDataLoader(testData, connectionProvider.getConnection(ExceptionHandlers.stackTraceHandler()));

        loader.populateTestData();
    }
}
