/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.utils.Address;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.SQLInputs;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;
import static io.cloudslang.content.database.utils.Constants.*;

/**
 * Created by victor on 13.01.2017.
 */
public class MSSqlDatabase implements SqlDatabase {
    private List<String> supportedJdbcDrivers;

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls, final String authenticationType, final String instance, String windowsDomain, String dbClass,
                      boolean trustAllRoots, String trustStore, String trustStorePassword) throws ClassNotFoundException, SQLException {
        if (dbName == null) {
            throw new SQLException("No database provided!");
        }
        if (StringUtils.isEmpty(dbPort)) {
            throw new SQLException("No port provided!");
        }
        if (StringUtils.isEmpty(dbServer)) {
            throw new IllegalArgumentException("host   not valid");
        }

        loadJdbcDriver(dbClass);

        String dbUrlMSSQL = "";
        String host;

        //compute the host value that will be used in the url
        String[] serverInstanceComponents = null;
        if (dbServer.contains(Constants.ESCAPED_BACKSLASH)) { //instance is included in the dbServer value
            serverInstanceComponents = dbServer.split("\\\\");
            host = serverInstanceComponents[0];
        } else {
            host = dbServer;
        }
        Address address = new Address(host);
        host = address.getURIIPV6Literal();

        //instance is included in the host name
        if (AUTH_SQL.equalsIgnoreCase(authenticationType)) {
            if (serverInstanceComponents != null) {
                //removed username and password form the url, since
                //driver manager will use url , username and password later
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName + SEMI_COLON + INSTANCE + EQUALS + serverInstanceComponents[1];
            }
            //has instance input
            else if (StringUtils.isNoneEmpty(instance)) {
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName + SEMI_COLON + INSTANCE + EQUALS + instance;
            }
            //no instance
            else {
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName;
            }
            dbUrlMSSQL = addSslEncryptionToConnection(trustAllRoots, trustStore, trustStorePassword, dbUrlMSSQL);
        }
        if (Constants.AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
            //instance is included in the host name
            if (serverInstanceComponents != null) {
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName + SEMI_COLON + INSTANCE + EQUALS + serverInstanceComponents[1] + SEMI_COLON + INTEGRATED_SECURITY + EQUALS + TRUE;
            }
            //has instance input
            else if (StringUtils.isNoneEmpty(instance)) {
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName + SEMI_COLON + INSTANCE + EQUALS + instance + SEMI_COLON + INTEGRATED_SECURITY + EQUALS + TRUE;
            }
            //no instance
            else {
                dbUrlMSSQL = Constants.MSSQL_URL + host + COLON + dbPort + SEMI_COLON + DATABASE_NAME + EQUALS + dbName + SEMI_COLON + INTEGRATED_SECURITY + EQUALS + TRUE;
            }
            // Set to true to send LMv2/NTLMv2 responses when using Windows authentication
            dbUrlMSSQL = addSslEncryptionToConnection(trustAllRoots, trustStore, trustStorePassword, dbUrlMSSQL);
        } else if (!AUTH_SQL.equalsIgnoreCase(authenticationType)) //check invalid authentication type
        {
            //if it is something other than sql or empty,
            //we supply the empty string with sql
            throw new SQLException(INVALID_AUTHENTICATION_TYPE_FOR_MS_SQL + authenticationType);
        }
        dbUrls.add(dbUrlMSSQL);
    }

    public static String addSslEncryptionToConnection(boolean trustAllRoots, String trustStore, String trustStorePassword, String dbUrlMSSQL) {
        final StringBuilder dbUrlBuilder = new StringBuilder(dbUrlMSSQL);
        dbUrlBuilder.append(SEMI_COLON + ENCRYPT + EQUALS)
                .append(TRUE)
                .append(SEMI_COLON)
                .append(TRUST_SERVER_CERTIFICATE)
                .append(EQUALS);
        if (trustAllRoots) {
            dbUrlBuilder.append(TRUE);
        } else {
            dbUrlBuilder.append(FALSE)
                    .append(String.format(TRUSTORE_PARAMS, trustStore, trustStorePassword));
        }
        return dbUrlBuilder.toString();
    }

    private void initializeJdbcDrivers() {
        supportedJdbcDrivers = Arrays.asList(SQLSERVER_JDBC_DRIVER, JTDS_JDBC_DRIVER);
    }

    private void loadJdbcDriver(String dbClass) throws ClassNotFoundException {
        boolean driverFound = false;
        initializeJdbcDrivers();
        for (String driver : supportedJdbcDrivers) {
            if (driver.equals(dbClass)) {
                driverFound = true;
            }
        }
        if (driverFound) {
            Class.forName(dbClass);
        } else {
            throw new RuntimeException("The driver provided is not supported.");
        }
    }

    @Override
    public void setUp(SQLInputs sqlInputs) {

    }
}
