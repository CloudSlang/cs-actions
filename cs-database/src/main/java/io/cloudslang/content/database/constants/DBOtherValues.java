/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.constants;

import io.cloudslang.content.database.utils.SQLInputsUtils;

import java.util.Map;

/**
 * Created by pinteae on 1/17/2017.
 */
public class DBOtherValues {
    public static final String ZERO = "0";
    public static final String DATABASE_NAME_CAP = "DatabaseName";
    public static final String SET_NOCOUNT_ON = "SET NOCOUNT ON";
    public static final String FORWARD_SLASH = "/";
    public static final String BACK_SLASH = "\\";

    public static final String CONCUR_READ_ONLY = "CONCUR_READ_ONLY";
    public static final String CONCUR_UPDATABLE = "CONCUR_UPDATABLE";

    public static final String TYPE_FORWARD_ONLY = "TYPE_FORWARD_ONLY";
    public static final String TYPE_SCROLL_INSENSITIVE = "TYPE_SCROLL_INSENSITIVE";
    public static final String TYPE_SCROLL_SENSITIVE = "TYPE_SCROLL_SENSITIVE";

    public static final String ORACLE_DB_TYPE = "Oracle";
    public static final String MSSQL_DB_TYPE = "MSSQL";
    public static final String SYBASE_DB_TYPE = "Sybase";
    public static final String NETCOOL_DB_TYPE = "Netcool";
    public static final String DB2_DB_TYPE = "DB2";
    public static final String MYSQL_DB_TYPE = "MySQL";
    public static final String POSTGRES_DB_TYPE = "PostgreSQL";
    public static final String CUSTOM_DB_TYPE = "Custom";

    public static final Integer DEFAULT_PORT_ORACLE = 1521;
    public static final Integer DEFAULT_PORT_MSSQL = 1433;
    public static final Integer DEFAULT_PORT_SYBASE = 5000;
    public static final Integer DEFAULT_PORT_NETCOOL = 4100;
    public static final Integer DEFAULT_PORT_DB2 = 50000;
    public static final Integer DEFAULT_PORT_MYSQL = 3306;
    public static final Integer DEFAULT_PORT_PSQL = 5432;
    public static final Integer DEFAULT_PORT_CUSTOM = DEFAULT_PORT_ORACLE;

    //    NO_RESULT_SET(-1000000, "NO_RESULT_SET");
}
