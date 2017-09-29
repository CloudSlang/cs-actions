/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.services.dbconnection;

import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by victor on 13.01.2017.
 */
public abstract class PooledDataSourceProvider {
    //Seconds, effectively a time to live. A Connection older than lifetime
    //will be destroyed and purged from the pool.
    public static String CONNECTION_LIFETIME_DEFAULT_VALUE = "7200"; //in seconds

    //general default values for pool configuration
    //connection lifetime are db specific,
    //but the defaults are global
    //general configuration for url+user+password of each pooled datasource
    //max number of connections a pooled datasource will maintain at a time
    public static String MAX_POOL_SIZE_NAME = "connection.maxpoolsize";
    public static String MAX_POOL_SIZE_DEFAULT_VALUE = "20";
    //min number of connections a pooled datasource will maintain at a time
    public static String MIN_POOL_SIZE_NAME = "connection.minpoolsize";
    public static String MIN_POOL_SIZE_DEFAULT_VALUE = "0";
    //init number of connections when a pooled datasource starts
    public static String INIT_POOL_SIZE_NAME = "connection.initpoolsize";
    public static String INIT_POOL_SIZE_DEFAULT_VALUE = "1";
    //Seconds a Connection can remain pooled but unused before being discarded.
    //Zero means idle connections never expire.
    public static String CONNECTION_MAX_IDLETIME_NAME = "connection.maxidletime";
    public static String CONNECTION_MAX_IDLETIME_DEFAULT_VALUE = "300"; //in seconds
    //The number of milliseconds a client to call checkout connection will wait for a
    //Connection to be checked-in or acquired when the pool is exhausted
    public static String CONNECTION_CHECKOUT_TIMEOUT_NAME =
            "connection.checkout.timeout";
    public static String CONNECTION_CHECKOUT_TIMEOUT_DEFAULT_VALUE = "20000"; //in mili seconds
    //Determines how many connections at a time connection will try to acquire when
    //the pool is exhausted.
    public static String CONNECTION_ACQUIREINCREMENT_SIZE_NAME =
            "connection.acquireincrementsize";
    public static String CONNECTION_ACQUIREINCREMENT_SIZE_DEFAULT_VALUE = "1";
    //Defines how many times connection pool will try to acquire a new Connection from the
    //database before giving up. If this value is less than or equal to zero,
    //c3p0 will keep trying to fetch a Connection indefinitely.
    public static String CONNECTION_RETRY_COUNT_NAME =
            "connection.retrycount";
    public static String CONNECTION_RETRY_COUNT_DEFAULT_VALUE = "1";
    //Seconds connection pool will wait between acquire attempts
    public static String CONNECTION_RETRY_DELAY_NAME =
            "connection.retrydelay";
    public static String CONNECTION_RETRY_DELAY_DEFAULT_VALUE = "5";//in seconds
    //If true, an operation will be performed asynchronously at every
    //connection checkout to verify that the connection is valid.
    public static String CONNECTION_TEST_ONCHECKOUT_NAME =
            "connection.validate_oncheckout";
    public static String CONNECTION_TEST_ONCHECKOUT_DEFAULT_VALUE = "true";
    //If true, an operation will be performed asynchronously at every connection
    //checkin to verify that the connection is valid.
    public static String CONNECTION_TEST_ONCHECKIN_NAME =
            "connection.validate_oncheckin";
    public static String CONNECTION_TEST_ONCHECKIN_DEFAULT_VALUE = "false";
    //If this is a number greater than 0, c3p0 will test all idle,
    //pooled but unchecked-out connections, every this number of seconds
    public static String CONNECTION_TEST_PERIOD_NAME =
            "connection.idle_validateperiod";
    public static String CONNECTION_TEST_PERIOD_DEFAULT_VALUE = "0";// 30 minutes in seconds
    //If true, a pooled DataSource will declare itself broken and be permanently
    //closed if a Connection cannot be obtained from the database after making
    //acquireRetryAttempts to acquire one. If false, failure to obtain a Connection
    //will cause all Threads waiting for the pool to acquire a Connection to throw
    //an Exception, but the DataSource will remain valid, and will attempt to
    //acquire again following a call to getConnection().
    public static String CONNECTION_BREAKAFTERACQUIREFAILURE_NAME =
            "connection.break_afteracquirefailure";
    public static String CONNECTION_BREAKAFTERACQUIREFAILURE_DEFAULT_VALUE = "true";
    //properties in databasePooling.properties which are specific for
    //dbtype
    //oracle
    public static String ORACLE_CONNECTION_LIFETIME_NAME =
            "oracle.connection.lifetime";
    //sql server
    public static String MSSQL_CONNECTION_LIFETIME_NAME =
            "mssql.connection.lifetime";
    //mysql
    public static String MYSQL_CONNECTION_LIFETIME_NAME =
            "mysql.connection.lifetime";
    //db2
    public static String DB2_CONNECTION_LIFETIME_NAME =
            "db2.connection.lifetime";
    //sybase
    public static String SYBASE_CONNECTION_LIFETIME_NAME =
            "sybase.connection.lifetime";
    //netcool
    public static String NETCOOL_CONNECTION_LIFETIME_NAME =
            "netcool.connection.lifetime";
    //custom
    public static String CUSTOM_CONNECTION_LIFETIME_NAME =
            "custom.connection.lifetime";
    //properties that contain configurable connection pooling params
    protected static Properties dbPoolingProperties = null;

    /**
     * constructor
     *
     * @param aDBPoolingProperties
     */
    public PooledDataSourceProvider(Properties aDBPoolingProperties) {
        //load configurable properties from databasePooling.properties
        if (dbPoolingProperties == null) {
            dbPoolingProperties = aDBPoolingProperties;
        }
    }

    /**
     * return the name of this provider
     */
    public abstract String getProviderName();

    /**
     * close pooled datasource
     *
     * @param aPooledDataSource a Pooled DataSource
     * @throws SQLException
     */
    public abstract void closePooledDataSource(DataSource aPooledDataSource)
            throws SQLException;

    /**
     * open a pooled datasource
     *
     * @param aDbType   a supported database type.
     * @param aDbUrl    a connection url
     * @param aUsername a username for the database
     * @param aPassword a password for the database connection
     * @return a DataSource is a pooled datasource
     * @throws SQLException
     */
    public abstract DataSource openPooledDataSource(DBType aDbType,
                                                    String aDbUrl,
                                                    String aUsername,
                                                    String aPassword)
            throws SQLException;

    /**
     * get string value based on the property name from property file
     * the property file is databasePooling.properties
     *
     * @param aPropName     a property name
     * @param aDefaultValue a default value for that property, if the property is not there.
     * @return string value of that property
     */
    protected String getPropStringValue(String aPropName, String aDefaultValue) {
        return dbPoolingProperties.getProperty(aPropName,
                aDefaultValue);
    }

    //the followings are only for testing purpose
    public abstract int getAllConnectionNumber(DataSource aPooledDataSource)
            throws SQLException;

    public abstract int getCheckedInConnectionNumber(DataSource aPooledDataSource)
            throws SQLException;

    public abstract int getCheckedOutConnectionNumber(DataSource aPooledDataSource)
            throws SQLException;
}//end PooledDataSoruceProvider class
