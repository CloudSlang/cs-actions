package io.cloudslang.content.database.services.dbconnection;

import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * wrapper class for database pooling implementation using c3p0
 *
 * @author ggu
 */
public class C3P0PooledDataSourceProvider extends PooledDataSourceProvider {
    //properties names for c3p0
    //deal with pool sizes
    //set default 1
    private final static String C3P0_INIT_POOL_SIZE_NAME = "initialPoolSize";
    //set default 0
    private final static String C3P0_MIN_POOL_SIZE_NAME = "minPoolSize";
    //set default 20
    private final static String C3P0_MAX_POOL_SIZE_NAME = "maxPoolSize";

    //deal with cleaning connections
    //set default 300 seconds
    private final static String C3P0_MAX_IDLE_TIME_NAME = "maxIdleTime";
    //set default 3600 seconds = 1 hour
    private final static String C3P0_MAX_CONNECTION_AGE_NAME =
            "maxConnectionAge";

    //deal with validation
    //set default 1800 = 30 minutes
    private final static String C3P0_IDLE_CONNECTION_TEST_PERIOD_NAME =
            "idleConnectionTestPeriod";

    //set default true
    private final static String C3P0_TEST_CONNECTION_ON_CHECKOUT_NAME =
            "testConnectionOnCheckout";

    //set default false
    private final static String C3P0_TEST_CONNECTION_ON_CHECKIN_NAME =
            "testConnectionOnCheckin";

    //deal with check out connections
    //set default 1
    private final static String C3P0_ACQUIRE_INCREMENT_NAME = "acquireIncrement";
    //set default 3
    private final static String C3P0_ACQUIRE_RETRY_ATTEMPTS_NAME = "acquireRetryAttempts";
    //set default 5 seconds
    private final static String C3P0_ACQUIRE_RETRY_DELAY_NAME = "acquireRetryDelay";

    //deal with if max pool size reached, timeout will break the getConnection
    //hanging
    //set default 20 seconds
    private final static String C3P0_CHECKOUT_TIMEOUT_NAME = "checkoutTimeout";

    //deal with failure connection, there is a problem in c3p0, it will keeps
    //trying to getConneciton on its own if the connection fails. set this to
    //be true to clean the pool, so it won't keep trying
    private final static String C3P0_BREAK_AFTERACQUIREFAILURE_NAME =
            "breakAfterAcquireFailure";

    //name of this provider
    public final static String C3P0_DATASOURCE_PROVIDER_NAME = "C3P0PooledDataSourceProvider";

    /**
     * constructor
     *
     * @param aDBPoolingProperties the Properties from databasePooling.properties
     */
    public C3P0PooledDataSourceProvider(Properties aDBPoolingProperties) {
        super(aDBPoolingProperties);
    }

    /**
     * return the name of this provider
     */
    public String getProviderName() {
        return C3P0_DATASOURCE_PROVIDER_NAME;
    }

    /**
     * close the pooled data source
     *
     * @param aPooledDataSource a pooled datasource
     * @throws SQLException
     */
    public void closePooledDataSource(DataSource aPooledDataSource)
            throws SQLException {
        if (aPooledDataSource == null) {
            return;
        }

        DataSources.destroy(aPooledDataSource);
    }

    /**
     * get the pooled datasource from c3p0 pool
     *
     * @param aDbType   a supported database type.
     * @param aDbUrl    a connection url
     * @param aUsername a username for the database
     * @param aPassword a password for the database connection
     * @return a DataSource  a pooled data source
     * @throws SQLException
     */
    public DataSource openPooledDataSource(DBType aDbType,
                                           String aDbUrl,
                                           String aUsername,
                                           String aPassword)
            throws SQLException {
        DataSource retPooledDS = null;

        DataSource unPooledDS =
                DataSources.unpooledDataSource(aDbUrl, aUsername, aPassword);

        //override the default properties with ours
        Map<String, String> props = this.getPoolingProperties(aDbType);

        retPooledDS = DataSources.pooledDataSource(unPooledDS, props);

        return retPooledDS;

    }

    /**
     * set up the properties for c3p0 based on the properties values in
     * databasePooling.properties.
     *
     * @param aDbType a supported db type.
     * @return a HashMap of c3p0 db pooling properties.
     */
    private Map<String, String> getPoolingProperties(DBType aDbType) {
        Map<String, String> retMap = new HashMap<>();

        //general properties
        //acquire increment size
        String acqIncSize = this.getPropStringValue(CONNECTION_ACQUIREINCREMENT_SIZE_NAME,
                CONNECTION_ACQUIREINCREMENT_SIZE_DEFAULT_VALUE);
        retMap.put(C3P0_ACQUIRE_INCREMENT_NAME, acqIncSize);

        //retry counts
        String retryCount = this.getPropStringValue(CONNECTION_RETRY_COUNT_NAME,
                CONNECTION_RETRY_COUNT_DEFAULT_VALUE);
        retMap.put(C3P0_ACQUIRE_RETRY_ATTEMPTS_NAME, retryCount);

        //retry deplay this will be value of milseconds
        String retryDelay = this.getPropStringValue(CONNECTION_RETRY_DELAY_NAME,
                CONNECTION_RETRY_DELAY_DEFAULT_VALUE);
        retMap.put(C3P0_ACQUIRE_RETRY_DELAY_NAME, retryDelay);

        //test period
        String testPeriod = this.getPropStringValue(CONNECTION_TEST_PERIOD_NAME,
                CONNECTION_TEST_PERIOD_DEFAULT_VALUE);
        retMap.put(C3P0_IDLE_CONNECTION_TEST_PERIOD_NAME, testPeriod);

        //test on check in
        String bTestOnCheckIn = this.getPropStringValue(CONNECTION_TEST_ONCHECKIN_NAME,
                CONNECTION_TEST_ONCHECKIN_DEFAULT_VALUE);
        retMap.put(C3P0_TEST_CONNECTION_ON_CHECKIN_NAME, bTestOnCheckIn);

        //test on check out
        String bTestOnCheckOut = this.getPropStringValue(CONNECTION_TEST_ONCHECKOUT_NAME,
                CONNECTION_TEST_ONCHECKOUT_DEFAULT_VALUE);
        retMap.put(C3P0_TEST_CONNECTION_ON_CHECKOUT_NAME, bTestOnCheckOut);

        //max idle time
        String maxIdleTime = this.getPropStringValue(CONNECTION_MAX_IDLETIME_NAME,
                CONNECTION_MAX_IDLETIME_DEFAULT_VALUE);
        retMap.put(C3P0_MAX_IDLE_TIME_NAME, maxIdleTime);

        //max pool size
        String maxPoolSize = this.getPropStringValue(MAX_POOL_SIZE_NAME,
                MAX_POOL_SIZE_DEFAULT_VALUE);

        retMap.put(C3P0_MAX_POOL_SIZE_NAME, maxPoolSize);

        //min pool size
        String minPoolSize = this.getPropStringValue(MIN_POOL_SIZE_NAME,
                MIN_POOL_SIZE_DEFAULT_VALUE);

        retMap.put(C3P0_MIN_POOL_SIZE_NAME, minPoolSize);

        //init pool size
        String initPoolSize = this.getPropStringValue(INIT_POOL_SIZE_NAME,
                INIT_POOL_SIZE_DEFAULT_VALUE);

        retMap.put(C3P0_INIT_POOL_SIZE_NAME, initPoolSize);

        //connection timeout
        String conTimeout = this.getPropStringValue(CONNECTION_CHECKOUT_TIMEOUT_NAME,
                CONNECTION_CHECKOUT_TIMEOUT_DEFAULT_VALUE);
        retMap.put(C3P0_CHECKOUT_TIMEOUT_NAME, conTimeout);

        //connection break after acquire failure
        String breakAfterFailure = this.getPropStringValue(CONNECTION_BREAKAFTERACQUIREFAILURE_NAME,
                CONNECTION_BREAKAFTERACQUIREFAILURE_DEFAULT_VALUE);
        retMap.put(C3P0_BREAK_AFTERACQUIREFAILURE_NAME, breakAfterFailure);

        //db specific properties
        //connection life time
        String conLifeTimeName = null;

        switch (aDbType) {
            case ORACLE:
                conLifeTimeName = ORACLE_CONNECTION_LIFETIME_NAME;
                break;
            case MSSQL:
                conLifeTimeName = MSSQL_CONNECTION_LIFETIME_NAME;
                break;
            case MYSQL:
                conLifeTimeName = MYSQL_CONNECTION_LIFETIME_NAME;
                break;
            case SYBASE:
                conLifeTimeName = SYBASE_CONNECTION_LIFETIME_NAME;
                break;
            case DB2:
                conLifeTimeName = DB2_CONNECTION_LIFETIME_NAME;
                break;
            case NETCOOL:
                conLifeTimeName = NETCOOL_CONNECTION_LIFETIME_NAME;
                break;
            default:
                conLifeTimeName = CUSTOM_CONNECTION_LIFETIME_NAME;
                break;
        }

        String connectionLifetime =
                this.getPropStringValue(conLifeTimeName, CONNECTION_LIFETIME_DEFAULT_VALUE);

        retMap.put(C3P0_MAX_CONNECTION_AGE_NAME, connectionLifetime);


        return retMap;
    }

    //The followings are only for testing purpose
    public int getAllConnectionNumber(DataSource aPooledDataSource)
            throws SQLException {
        PooledDataSource pDs = (PooledDataSource) aPooledDataSource;
        int retValue = pDs.getNumConnectionsAllUsers();
        return retValue;
    }

    public int getCheckedInConnectionNumber(DataSource aPooledDataSource)
            throws SQLException {
        PooledDataSource pDs = (PooledDataSource) aPooledDataSource;
        int retValue = pDs.getNumIdleConnectionsAllUsers();
        return retValue;
    }

    public int getCheckedOutConnectionNumber(DataSource aPooledDataSource)
            throws SQLException {
        PooledDataSource pDs = (PooledDataSource) aPooledDataSource;
        int retValue = pDs.getNumBusyConnectionsAllUsers();
        return retValue;
    }

}//end of C3P0PooledDataSourceProvider

	
