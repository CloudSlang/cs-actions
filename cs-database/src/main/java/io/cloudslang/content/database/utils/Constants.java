/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;


/**
 * Created by vranau on 11/26/2014.
 */
public class Constants {

    //default values
    public static final String DEFAULTPORT_ORACLE   = "1521";
    public static final String DEFAULTPORT_MSSQL    = "1433";
    public static final String DEFAULTPORT_SYBASE   = "5000";
    public static final String DEFAULTPORT_NETCOOL  = "4100";
    public static final String DEFAULTPORT_DB2      = "50000";
    public static final String DEFAULTPORT_MYSQL    = "3306";
    public static final String DEFAULTPORT_PSQL     = "5432";
    public static final int DEFAULTTIMEOUT = 0;
    public static final String DBSERVERNAME = "DBServerName";
    public static final String DBPORT = "DBPort";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String COMMAND = "Command";
    public static final String DATABASENAME = "Database";
    public static final String AUTH_TYPE = "authenticationType";
    public static final String TNS_PATH = "TNSPath";
    public static final String TNS_ENTRY = "TNSEntry";
    public static final String AUTH_WINDOWS = "Windows";
    public static final String AUTH_SQL = "Sql";
    public static final String IGNORE_CASE = "ignoreCase";
    public static final String ORACLE_DB_TYPE = "Oracle";
    public static final String MYSQL_DB_TYPE = "MySQL";
    public static final String MSSQL_DB_TYPE = "MSSQL";
    public static final String SYBASE_DB_TYPE = "Sybase";
    public static final String NETCOOL_DB_TYPE = "Netcool";
    public static final String DB2_DB_TYPE = "DB2";
    public static final String POSTGRES_DB_TYPE = "PostgreSQL";
    public static final String CUSTOM_DB_TYPE = "Custom";
    public static final String CUSTOM_DB_CLASS = "dbClass";
    public static final String DBURL = "dbURL";
    public static final String DBTYPE = "DbType";
    public static final String DELIM = "Delimiter";
    public static final String KEY = "Key";
    public static final String TRIM_ROWSTAT = "trimRowstat";
    public static final String MSSQL_URL = "jdbc:jtds:sqlserver://";
    public static final String LINE = "Line0";
    public static final String RETURNRESULT = "returnResult";
    public static final String OUTPUTTEXT = "outputText";
    public static final String RESULT_SET_TYPE = "resultSetType";
    public static final String RESULT_SET_CONCURRENCY = "resultSetConcurrency";
    //QCCR 131222 add an optional input "instance"
    public static final String INSTANCE = "instance";
    //QCCR 131219 add an optional input "timeout"
    public static final String TIMEOUT = "timeout";
    public static final String DATABASE_POOLING_PROPRTIES = "databasePoolingProperties";
//    public static final String RESPONSESUCCESS = PropsLoader.RESPONSES.getProperty("Passed");
//    public static final String RESPONSEPASSED = PropsLoader.RESPONSES.getProperty("More");
//    public static final String RESPONSEFAILED = PropsLoader.RESPONSES.getProperty("Failed");
//    public static final String RESPONSENOMORE = PropsLoader.RESPONSES.getProperty("NoMore");
//    public static final String NOMOREROWS = PropsLoader.RETURNRESULTS.getProperty("SQLNoMore");
//    public static final int PASSED = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("Passed"));
//    public static final int FAILED = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("Failed"));
//    public static final int NOMORE = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("NoMore"));
    public static final String ESCAPED_BACKSLASH = "\\";
}
