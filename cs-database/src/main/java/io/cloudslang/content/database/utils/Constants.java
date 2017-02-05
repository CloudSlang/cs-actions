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


import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE;
import static io.cloudslang.content.database.constants.DBInputNames.TRUST_STORE_PASSWORD;

/**
 * Created by victor on 13.01.2017.
 */
public class Constants {

    //default values
    public static final int DEFAULTTIMEOUT = 0;
    public static final String AUTH_WINDOWS = "Windows";
    public static final String TRIM_ROWSTAT = "trimRowstat";
    public static final String MSSQL_URL = "jdbc:sqlserver://";
    public static final String TRUST_SERVER_CERTIFICATE = "trustServerCertificate";
    public static final String JTDS_JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    public static final String SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String ENCRYPT = "encrypt";
    public static final String SEMI_COLON = ";";
    public static final String EQUALS = "=";
    public static final String STRING_PARAMETER = "%s";
    public static final String TRUSTORE_PARAMS = SEMI_COLON + TRUST_STORE + EQUALS + STRING_PARAMETER + SEMI_COLON + TRUST_STORE_PASSWORD + EQUALS + STRING_PARAMETER;
    public static final String COLON = ":";
    public static final String INTEGRATED_SECURITY = "integratedSecurity";
}
