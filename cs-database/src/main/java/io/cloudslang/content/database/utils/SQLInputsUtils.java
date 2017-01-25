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

import io.cloudslang.content.utils.CollectionUtilities;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.SQLInputsValidator.isValidDbType;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Created by victor on 18.01.2017.
 */
public class SQLInputsUtils {

    public static int getOrDefaultDBPort(final String dbPort, final String dbType) {
        if(isNoneEmpty(dbPort)) {
            return Integer.valueOf(dbPort);
        }
        if (isValidDbType(dbType)) {
            return getPortForDbType(dbType);
        }
        return -1;
    }

    private static int getPortForDbType(final String dbType) {
        for (final String toCheck : DB_PORTS.keySet()) {
            if (toCheck.equalsIgnoreCase(dbType)) {
                return DB_PORTS.get(toCheck);
            }
        }
        return -1;
    }

    public static List<String> getSqlCommands(final String sqlCommandsStr, final String scriptFileName, final String commandsDelimiter) {
        if (isNoneEmpty(sqlCommandsStr)) {
            return CollectionUtilities.toList(sqlCommandsStr, commandsDelimiter);
        }
        if (isNoneEmpty(scriptFileName)) {
            return SQLUtils.readFromFile(scriptFileName);
        }
        return Collections.emptyList();
    }

    public static List<String> getDbUrls(final String dbUrl) {
        final List<String> dbUrls = new ArrayList<>();
        if (isNoneEmpty(dbUrl)) {
            dbUrls.add(dbUrl);
        }
        return dbUrls;
    }

    public static int getOrDefaultTimeout(final String timeout, final String defaultVal) {
        try {
            return Integer.parseInt(defaultIfEmpty(timeout, defaultVal));
        } catch (Exception e) {
            e.printStackTrace(); //todo
            return -1;
        }
    }

    public static Properties getOrDefaultDBPoolingProperties(final String dbPoolingProperties, final String defaultVal) {
        final Properties databasePoolingProperties = new Properties();
        try (final Reader reader = new StringReader(defaultIfBlank(dbPoolingProperties, defaultVal))) {
            databasePoolingProperties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return databasePoolingProperties;
    }

    public static int getResultSetConcurrency(final String resultSetConcurrency) {
        if (SQLInputsValidator.isValidResultSetConcurrency(resultSetConcurrency)) {
            return CONCUR_VALUES.get(resultSetConcurrency);
        }
        return -1000000;
    }

    public static int getResultSetType(final String resultSetType) {
        if (SQLInputsValidator.isValidResultSetType(resultSetType)) {
            return TYPE_VALUES.get(resultSetType);
        }
        return -1000000;
    }

    public static void processDefaultValues(SQLInputs sqlInputs, String dbType, String authenticationType, String username) throws Exception {
        int dbPort = 0;
        if (dbType.equalsIgnoreCase(ORACLE_DB_TYPE)) {
            dbPort = DEFAULT_PORT_ORACLE;
        } else if (dbType.equalsIgnoreCase(MSSQL_DB_TYPE)) {
            dbPort = DEFAULT_PORT_MSSQL;
            if (AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
                if (username.contains(ESCAPED_BACKSLASH)) {
                    String domain = username.substring(0, username.indexOf(ESCAPED_BACKSLASH));
                    final String newUsername = username.substring(username.indexOf(ESCAPED_BACKSLASH) + 1, username.length());
                    sqlInputs.setUsername(newUsername);
                    sqlInputs.setWindowsDomain(domain);
                }

            }
        } else if (dbType.equalsIgnoreCase(NETCOOL_DB_TYPE)) {
            dbPort = DEFAULT_PORT_NETCOOL;
            sqlInputs.setNetcool(true);
        } else if (dbType.equalsIgnoreCase(DB2_DB_TYPE)) {
            dbPort = DEFAULT_PORT_DB2;
        } else if (dbType.equalsIgnoreCase(SYBASE_DB_TYPE)) {
            dbPort = DEFAULT_PORT_SYBASE;
        } else if (dbType.equalsIgnoreCase(MYSQL_DB_TYPE)) {
            dbPort = DEFAULT_PORT_MYSQL;
        } else if (dbType.equalsIgnoreCase(POSTGRES_DB_TYPE)) {
            dbPort = DEFAULT_PORT_PSQL;
        }
        if (isEmpty(Integer.toString(sqlInputs.getDbPort()))) { //todo temporary fix
            sqlInputs.setDbPort(dbPort);
        }
        try {
            //in case that user entered dbType is not oracle, netcool, mssql or db2
            if (isEmpty(Integer.toString(sqlInputs.getDbPort()))) { //todo temporary fix
                throw new Exception("There's no default DBPort for " + sqlInputs.getDbType() + " db server, please enter a valid dbPort.");
            } else {
//         todo       Integer.parseInt(sqlInputs.getDbPort());
            }
        } catch (NumberFormatException e) {
            throw new Exception("dbPort input is not in valid format.");
        }
    }

    public static boolean notInCollectionIgnoreCase(final String toCheck, final Iterable<String> inList) {
        return !inCollectionIgnoreCase(toCheck, inList);
    }

    public static boolean inCollectionIgnoreCase(final String toCheck, final Iterable<String> inList) {
        boolean isPresent = false;
        for (final String element : inList) {
            if (!element.equalsIgnoreCase(toCheck)) {
                isPresent = true;
                break;
            }
        }
        return isPresent;
    }
}
