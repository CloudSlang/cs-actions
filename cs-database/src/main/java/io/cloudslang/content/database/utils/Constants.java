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


import java.util.List;

import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE;
import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE_PASSWORD;

/**
 * Created by victor on 13.01.2017.
 */
public class Constants {

    //default values
    public static final int DEFAULTTIMEOUT = 0;
//    public static final String DBSERVERNAME = "DBServerName";
//    public static final String DBPORT = "DBPort";
//    public static final String USERNAME = "Username";
//    public static final String PASSWORD = "Password";
//    public static final String COMMAND = "Command";
//    public static final String DATABASENAME = "Database";
//    public static final String AUTH_TYPE = "authenticationType";
//    public static final String TNS_PATH = "TNSPath";
//    public static final String TNS_ENTRY = "TNSEntry";
    public static final String AUTH_WINDOWS = "Windows";
//    public static final String AUTH_SQL = "Sql";
//    public static final String IGNORE_CASE = "ignoreCase";
//    public static final String CUSTOM_DB_CLASS = "dbClass";
//    public static final String DELIM = "delimiter";
//    public static final String KEY = "Key";
    public static final String TRIM_ROWSTAT = "trimRowstat";
    public static final String MSSQL_URL = "jdbc:sqlserver://";
//    public static final String LINE = "Line0";
    public static final String RETURNRESULT = "returnResult";
//    public static final String RESULT_SET_TYPE = "resultSetType";
//    public static final String RESULT_SET_CONCURRENCY = "resultSetConcurrency";
    //QCCR 131222 add an optional input "instance"
//    public static final String INSTANCE = "instance";
//    public static final String TRUST_STORE = "trustStore";
//    public static final String TRUST_STORE_PASSWORD = TRUST_STORE + "Password";
//    public static final String TRUST_ALL_ROOTS = "trustAllRoots";
    public static final String TRUST_SERVER_CERTIFICATE = "trustServerCertificate";
    //QCCR 131219 add an optional input "timeout"
//    public static final String TIMEOUT = "timeout";
//    public static final String DATABASE_POOLING_PROPRTIES = "databasePoolingProperties";
    //    public static final String RESPONSESUCCESS = PropsLoader.RESPONSES.getProperty("Passed");
//    public static final String RESPONSEPASSED = PropsLoader.RESPONSES.getProperty("More");
//    public static final String RESPONSEFAILED = PropsLoader.RESPONSES.getProperty("Failed");
//    public static final String RESPONSENOMORE = PropsLoader.RESPONSES.getProperty("NoMore");
//    public static final String NOMOREROWS = PropsLoader.RETURNRESULTS.getProperty("SQLNoMore");
//    public static final int PASSED = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("Passed"));
//    public static final int FAILED = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("Failed"));
//    public static final int NOMORE = Integer.parseInt(PropsLoader.RETURNCODES.getProperty("NoMore"));
    public static final String ESCAPED_BACKSLASH = "\\";
    public static final String JTDS_JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    public static final String SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String TRUE = Boolean.TRUE.toString();
    public static final String ENCRYPT = "encrypt";
    public static final String FALSE = Boolean.FALSE.toString();
    public static final String SEMI_COLON = ";";
    public static final String EQUALS = "=";
    public static final String STRING_PARAMETER = "%s";
    public static final String TRUSTORE_PARAMS = SEMI_COLON + TRUST_STORE + EQUALS + STRING_PARAMETER + SEMI_COLON + TRUST_STORE_PASSWORD + EQUALS + STRING_PARAMETER;
    public static final String COLON = ":";
    public static final String INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL = "Invalid authentication type for MS SQL : ";
//    public static final String DATABASE_NAME = "DatabaseName";
    public static final String INTEGRATED_SECURITY = "integratedSecurity";
}
