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

import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.StringUtils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.cloudslang.content.constants.InputNames.DELIMITER;
import static io.cloudslang.content.database.constants.DBDefaultValues.AUTH_SQL;

import static io.cloudslang.content.database.constants.DBInputNames.*;
import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class InputsProcessor {

    public static SQLInputs handleInputParameters(Map<String, String> parameters, String resultSetType, String resultSetConcurrency) throws Exception {
        SQLInputs sqlInputs = new SQLInputs();
        init(sqlInputs);//clean up the variables before use them

        String resultSetTypeParameter = parameters.get(RESULT_SET_TYPE);
        String resultSetConcurrencyParameter = parameters.get(RESULT_SET_CONCURRENCY);

        if (StringUtils.isNoneEmpty(resultSetTypeParameter)) {
            if (TYPE_VALUES.containsKey(resultSetTypeParameter)) {
                resultSetType = resultSetTypeParameter;
            }
        }

        if (StringUtils.isNoneEmpty(resultSetConcurrencyParameter)) {
            if (CONCUR_VALUES.containsKey(resultSetConcurrencyParameter)) {
                resultSetConcurrency = resultSetConcurrencyParameter;
            }
        }

        sqlInputs.setResultSetType(TYPE_VALUES.get(resultSetType));
        sqlInputs.setResultSetConcurrency(CONCUR_VALUES.get(resultSetConcurrency));

        //ignore case
        String ignoreCase = parameters.get(IGNORE_CASE);
        if (isEmpty(ignoreCase)) {
            ignoreCase = Boolean.TRUE.toString();
        } else {
            if (!(ignoreCase.equalsIgnoreCase("true") || ignoreCase.equalsIgnoreCase("false"))) {
                throw new Exception("The value \"" + ignoreCase + "\" is an invalid value for ignoreCase input.");
            }
        }
        sqlInputs.setIgnoreCase(ignoreCase);

        //host
        String dbServer = parameters.get(DB_SERVER_NAME);
        sqlInputs.setDbServer(dbServer);


        //dbType
        String dbType = parameters.get(DB_TYPE);
        sqlInputs.setDbType(dbType);
        //default to be oracle if it is empty or null
        if (isEmpty(sqlInputs.getDbType())) {
            sqlInputs.setDbType(ORACLE_DB_TYPE);
        }

        //username
        String username = parameters.get(USERNAME);
        sqlInputs.setUsername(username);
        if (isEmpty(sqlInputs.getUsername())) {
            throw new Exception("username input is empty.");
        }

        //password
        String password = parameters.get(PASSWORD);
        sqlInputs.setPassword(password);
        if (isEmpty(sqlInputs.getPassword())) {
            throw new Exception("password input is empty.");
        }


        //if using tns to make the connection, no need to process dbPort, database and autyType inputs

        //database
        String dbName = parameters.get(DATABASE_NAME);
        sqlInputs.setDbName(dbName);
        //check "Database" input is empty only if dbtype is not Sybase or MSSQL
        //cause for Sybase and MSSQL, it is ok to use empty database, they will pick up the default db
        if (!SYBASE_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())
                && !MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())
                && !NETCOOL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
            if (isEmpty(sqlInputs.getDbName())) {
                throw new Exception("database input is empty.");
            }
        }


        //authType
        String authenticationType = parameters.get(AUTHENTICATION_TYPE);
        sqlInputs.setAuthenticationType(authenticationType);

        //windows authentication is only used with MSSQL.
        if (isEmpty(sqlInputs.getAuthenticationType())) {
            sqlInputs.setAuthenticationType(AUTH_SQL);
        }
        if (!AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !AUTH_SQL.equalsIgnoreCase(sqlInputs.getAuthenticationType())) {
            throw new Exception("authentication type input is not valid.");
        }
        if (AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
            throw new Exception("Windows authentication can only be used with MSSQL!");
        }

        //dbPort
        String dbPort = parameters.get(DB_PORT);
        sqlInputs.setDbPort(Integer.valueOf(dbPort));

        //command
        String sqlCommand = parameters.get(COMMAND);
        sqlInputs.setSqlCommand(sqlCommand);

        //key
        //"key" is a required input. The original code does not check if it's empty
        //keep the way it is.
        //when user enters empty value for "key", default it to empty string
        //otherwise key will have the value "null" (the same as user enters "null")
        String key = parameters.get(KEY);
        if (isEmpty(key)) {
            key = "";
        }
        sqlInputs.setKey(key);

        //delimiter
        String strDelim = parameters.get(DELIMITER);
        if (isEmpty(strDelim)) {
            strDelim = "";
        }
        sqlInputs.setStrDelim(strDelim);

        //get a fresh list for each run
        String inputDbUrl = parameters.get(DB_URL);
        if (!isEmpty(inputDbUrl)) {
            final List<String> dbUrls = sqlInputs.getDbUrls();
            dbUrls.add(inputDbUrl);
        }

        String dbClass = parameters.get(DB_CLASS);
        String trimRowstat = parameters.get(TRIM_ROWSTAT);
        sqlInputs.setDbClass(dbClass);
        sqlInputs.setTrimRowstat(trimRowstat);

        //instance
        String instance = parameters.get(INSTANCE);
        sqlInputs.setInstance(instance);
        if (!isEmpty(sqlInputs.getInstance()) && !sqlInputs.getDbType().equalsIgnoreCase(MSSQL_DB_TYPE)) {
            throw new Exception("The instance input can only be used with MSSQL.");
        }

        //timeout
        String timeoutString = parameters.get(TIMEOUT);
        try {
            if (isEmpty(timeoutString)) {
                sqlInputs.setTimeout(DEFAULTTIMEOUT);
            } else {
                sqlInputs.setTimeout(Integer.parseInt(timeoutString));
            }
            if (sqlInputs.getTimeout() < 0) {
                throw new Exception("Timeout must be greater than zero!");
            }
        } catch (Exception e) {
            throw new Exception("timeout input is not in valid format.");
        }
        String databasePoolingPropertiesStr = parameters.get(DATABASE_POOLING_PROPERTIES);
        Properties databasePoolingProperties = new Properties();
        databasePoolingProperties.load(new StringReader(databasePoolingPropertiesStr));
        sqlInputs.setDatabasePoolingProperties(databasePoolingProperties);

        boolean trustAllRoots = BooleanUtilities.toBoolean(parameters.get(TRUST_ALL_ROOTS), false);

        String trustStore = parameters.get(TRUST_STORE);
        String trustStorePassword = parameters.get(TRUST_STORE_PASSWORD);


        if (!trustAllRoots && (isEmpty(trustStore) || isEmpty(trustStorePassword))) {
            throw new Exception("A trustStore and a trustStorePassword should be provided.");
        }

        sqlInputs.setTrustAllRoots(trustAllRoots);
        sqlInputs.setTrustStore(trustStore);
        sqlInputs.setTrustStorePassword(trustStorePassword);

        return sqlInputs;
    }

    /**
     * init all the non-static variables.
     *
     * @param sqlInputs
     */
    public static void init(SQLInputs sqlInputs) throws Exception {
        if (sqlInputs != null) {
            sqlInputs.setStrDelim(",");
            sqlInputs.setStrColumns("");
            sqlInputs.setlRows(new ArrayList<String>());
            sqlInputs.setiUpdateCount(0);
            sqlInputs.setSqlCommand(null);
            sqlInputs.setDbServer(null);
            sqlInputs.setDbName(null);
            sqlInputs.setDbType(null);
            sqlInputs.setStrKeyCol(null);
            sqlInputs.setStrKeyFiles(null);
            sqlInputs.setStrKeyNames(null);
            sqlInputs.setStrKeySkip(null);
            sqlInputs.setUsername(null);
            sqlInputs.setPassword(null);
            sqlInputs.setAuthenticationType(null);
            sqlInputs.setDbUrl(null);
            sqlInputs.setDbUrls(new ArrayList<String>(3));
            sqlInputs.setDbClass(null);
            sqlInputs.setTrimRowstat("true");
            sqlInputs.setNetcool(false);
            sqlInputs.setlRowsFiles(new ArrayList<List<String>>());
            sqlInputs.setlRowsNames(new ArrayList<List<String>>());
            sqlInputs.setSkip(0L);
            sqlInputs.setInstance(null);
            sqlInputs.setTimeout(0);
            sqlInputs.setKey(null);
            sqlInputs.setIgnoreCase(null);
            sqlInputs.setResultSetConcurrency(-1000000);
            sqlInputs.setResultSetType(-1000000);
            sqlInputs.setTrustAllRoots(false);
            sqlInputs.setTrustStore("");
        } else throw new Exception("Cannot init null Sql inputs!");
    }
}
