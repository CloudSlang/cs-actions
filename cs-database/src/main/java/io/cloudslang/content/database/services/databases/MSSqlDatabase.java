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
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.database.utils.Constants.JTDS_JDBC_DRIVER;
import static io.cloudslang.content.database.utils.Constants.SQLSERVER_JDBC_DRIVER;

/**
 * Created by victor on 13.01.2017.
 */
public class MSSqlDatabase {
    private List<String> supportedJdbcDrivers;

    public void setUp(String dbName, String dbServer, String dbPort, List<String> dbUrls, final String authenticationType, final String instance, String windowsDomain, String dbClass) throws ClassNotFoundException, SQLException {
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
        String host = "";

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

        if (serverInstanceComponents != null) {
            //removed username and password form the url, since
            //driver manager will use url , username and password later
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + serverInstanceComponents[1] + ";";
        }
        //has instance input
        else if (StringUtils.isNoneEmpty(instance)) {
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + instance + ";";
        }
        //no instance
        else {
            dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName;
        }
        if (Constants.AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
            String domain = "CORP";
            // If present and the user name and password are provided, jTDS uses Windows (NTLM) authentication instead of the usual SQL Server authentication
            if (windowsDomain != null) {
                domain = windowsDomain;
            }
            //instance is included in the host name
            if (serverInstanceComponents != null) {
                dbUrlMSSQL =
                        Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + serverInstanceComponents[1] + ";domain=" + domain;
            }
            //has instance input
            else if (StringUtils.isNoneEmpty(instance)) {
                dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";instance=" + instance + ";domain=" + domain;
            }
            //no instance
            else {
                dbUrlMSSQL = Constants.MSSQL_URL + host + ":" + dbPort + dbName + ";domain=" + domain;
            }
            // Set to true to send LMv2/NTLMv2 responses when using Windows authentication
            dbUrlMSSQL += ";useNTLMv2=true";
        } else if (!Constants.AUTH_SQL.equalsIgnoreCase(authenticationType)) //check invalid authentication type
        {
            //if it is something other than sql or empty,
            //we supply the empty string with sql
            throw new SQLException("Invalid authentication type for MS SQL : " + authenticationType);
        }

        dbUrls.add(dbUrlMSSQL);
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
}
