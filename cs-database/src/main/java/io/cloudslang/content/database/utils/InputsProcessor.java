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

import io.cloudslang.content.database.services.entities.SQLInputs;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by vranau on 12/4/2014.
 */
public class InputsProcessor {
  /*
    public static SQLInputs processInputParameters(ActionRequest actionRequest, OOResultSet resultSetType, OOResultSet resultSetConcurrency) throws Exception {
        SQLInputs sqlInputs = new SQLInputs();
        init(sqlInputs);//clean up the variables before use them

        Map parameters = actionRequest.getParameters();

        String resultSetTypeParameter = (String) parameters.map(Constants.RESULT_SET_TYPE);
        String resultSetConcurrencyParameter = (String) parameters.map(Constants.RESULT_SET_CONCURRENCY);

        if(StringUtils.isNoneEmpty(resultSetTypeParameter)) {
            resultSetType = transformResultSetType(resultSetTypeParameter);
        }

        if(StringUtils.isNoneEmpty(resultSetConcurrencyParameter)) {
            resultSetConcurrency = transformResultSetConcurrency(resultSetConcurrencyParameter);
        }

        sqlInputs.setResultSetType(resultSetType);
        sqlInputs.setResultSetConcurrency(resultSetConcurrency);

        //ignore case
        String ignoreCase = ((String) parameters.map(Constants.IGNORE_CASE));
        if (StringUtils.isEmpty(ignoreCase)) {
            ignoreCase = Boolean.TRUE.toString();
        } else {
            if (!(ignoreCase.equalsIgnoreCase("true") || ignoreCase.equalsIgnoreCase("false"))) {
                throw new Exception("The value \"" + ignoreCase + "\" is an invalid value for ignoreCase input.");
            }
        }
        sqlInputs.setIgnoreCase(ignoreCase);

        //host
        String dbServer = (String) parameters.map(Constants.DBSERVERNAME);
        sqlInputs.setDbServer(dbServer);

        //tns
        String tnsPath = (String) parameters.map(Constants.TNS_PATH);
        sqlInputs.setTnsPath(tnsPath);
        String tnsEntry = (String) parameters.map(Constants.TNS_ENTRY);
        sqlInputs.setTnsEntry(tnsEntry);

        //dbType
        String dbType = (String) parameters.map(Constants.DBTYPE);
        sqlInputs.setDbType(dbType);
        //default to be oracle if it is empty or null
        if (StringUtils.isEmpty(sqlInputs.getDbType())) {
            sqlInputs.setDbType(Constants.ORACLE_DB_TYPE);
        }

        SQLUtils.processHostorTNS(sqlInputs.getDbType(), sqlInputs.getDbServer(), sqlInputs.getTnsEntry());
        //username
        String username = ActionRequestUtils.resolveStringParam(actionRequest, Constants.USERNAME);
        sqlInputs.setUsername(username);
        if (StringUtils.isEmpty(sqlInputs.getUsername())) {
            throw new Exception("username input is empty.");
        }

        //password
        String password = ActionRequestUtils.resolveStringParam(actionRequest, Constants.PASSWORD);
        sqlInputs.setPassword(password);
        if (StringUtils.isEmpty(sqlInputs.getPassword())) {
            throw new Exception("password input is empty.");
        }

        //if using tns to make the connection, no need to process dbPort, database and autyType inputs
        if (StringUtils.isEmpty(sqlInputs.getTnsEntry())) {

            //database
            String dbName = (String) parameters.map(Constants.DATABASENAME);
            sqlInputs.setDbName(dbName);
            //check "Database" input is empty only if dbtype is not Sybase or MSSQL
            //cause for Sybase and MSSQL, it is ok to use empty database, they will pick up the default db
            if (!Constants.SYBASE_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())
                    && !Constants.MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())
                    && !Constants.NETCOOL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                if (StringUtils.isEmpty(sqlInputs.getDbName())) {
                    throw new Exception("database input is empty.");
                }
            }


            //authType
            String authenticationType = (String) parameters.map(Constants.AUTH_TYPE);
            sqlInputs.setAuthenticationType(authenticationType);
            //windows authentication is only used with MSSQL.
            if (StringUtils.isEmpty(sqlInputs.getAuthenticationType())) {
                sqlInputs.setAuthenticationType(Constants.AUTH_SQL);
            }
            if (!Constants.AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !Constants.AUTH_SQL.equalsIgnoreCase(sqlInputs.getAuthenticationType())) {
                throw new Exception("authentication type input is not valid.");
            }
            if (Constants.AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType()) && !Constants.MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                throw new Exception("Windows authentication can only be used with MSSQL!");
            }

            //dbPort
            String dbPort = (String) parameters.map(Constants.DBPORT);
            sqlInputs.setDbPort(dbPort);
            processDefaultValues(sqlInputs, sqlInputs.getDbType(), sqlInputs.getAuthenticationType(), username);
        }

        //command
        String sqlCommand = (String) parameters.map(Constants.COMMAND);
        sqlInputs.setSqlCommand(sqlCommand);

        //key
        //"key" is a required input. The original code does not check if it's empty
        //keep the way it is.
        //when user enters empty value for "key", default it to empty string
        //otherwise key will have the value "null" (the same as user enters "null")
        String key = (String) parameters.map(Constants.KEY);
        if (StringUtils.isEmpty(key)) {
            key = "";
        }
        sqlInputs.setKey(key);

        //delimiter
        String strDelim = (String) parameters.map(Constants.DELIM);
        if (StringUtils.isEmpty(strDelim)) {
            strDelim = "";
        }
        sqlInputs.setStrDelim(strDelim);

        //get a fresh list for each run
        String inputDbUrl = (String) parameters.map(Constants.DBURL);
        if (StringUtils.isNoneEmpty(inputDbUrl)) {
            final List<String> dbUrls = sqlInputs.getDbUrls();
            dbUrls.add(inputDbUrl);
        }

        String dbClass = (String) parameters.map(Constants.CUSTOM_DB_CLASS);
        String trimRowstat = (String) parameters.map(Constants.TRIM_ROWSTAT);
        sqlInputs.setDbClass(dbClass);
        sqlInputs.setTrimRowstat(trimRowstat);

        //instance
        String instance = (String) parameters.map(Constants.INSTANCE);
        sqlInputs.setInstance(instance);
        if (StringUtils.isNoneEmpty(sqlInputs.getInstance()) && !sqlInputs.getDbType().equalsIgnoreCase(Constants.MSSQL_DB_TYPE)) {
            throw new Exception("The instance input can only be used with MSSQL.");
        }

        //timeout
        String timeoutString = StringUtils.resolveString(actionRequest, Constants.TIMEOUT);
        try {
            if (StringUtils.isEmpty(timeoutString)) {
                sqlInputs.setTimeout(Constants.DEFAULTTIMEOUT);
            } else {
                sqlInputs.setTimeout(Integer.parseInt(timeoutString));
            }
            if (sqlInputs.getTimeout() < 0) {
                throw new Exception("Timeout must be greater than zero!");
            }
        } catch (Exception e) {
            throw new Exception("timeout input is not in valid format.");
        }

        String databasePoolingPropertiesStr = StringUtils.resolveString(actionRequest, Constants.DATABASE_POOLING_PROPRTIES);
        Properties databasePoolingProperties = new Properties();
        databasePoolingProperties.load(new StringReader(databasePoolingPropertiesStr));
        sqlInputs.setDatabasePoolingProperties(databasePoolingProperties);

        return sqlInputs;
    }
*/

    /**
     * @param resultSetTypeParameter This is a required input. If this is empty an exception is thrown.
     * @return
     */
    private static OOResultSet transformResultSetType(String resultSetTypeParameter) throws SQLException {
        if (resultSetTypeParameter.equalsIgnoreCase(OOResultSet.TYPE_FORWARD_ONLY.toString())) {
            return OOResultSet.TYPE_FORWARD_ONLY;
        } else if (resultSetTypeParameter.equalsIgnoreCase(OOResultSet.TYPE_SCROLL_INSENSITIVE.toString())) {
            return OOResultSet.TYPE_SCROLL_INSENSITIVE;
        } else if (resultSetTypeParameter.equalsIgnoreCase(OOResultSet.TYPE_SCROLL_SENSITIVE.toString())) {
            return OOResultSet.TYPE_SCROLL_SENSITIVE;
        }
        throw new SQLException("Invalid resultSetConcurrency provided. The allowed values for resultSetTypeParameter are: "
                + OOResultSet.TYPE_FORWARD_ONLY.toString() + ", " + OOResultSet.TYPE_SCROLL_INSENSITIVE.toString() +
                " and " + OOResultSet.TYPE_SCROLL_SENSITIVE.toString());

    }

    /**
     * @param resultSetConcurrency This is a required input. If this is empty an exception is thrown.
     * @return
     */
    private static OOResultSet transformResultSetConcurrency(String resultSetConcurrency) throws SQLException {
        if (resultSetConcurrency.equalsIgnoreCase(OOResultSet.CONCUR_READ_ONLY.toString())) {
            return OOResultSet.CONCUR_READ_ONLY;
        } else if (resultSetConcurrency.equalsIgnoreCase(OOResultSet.CONCUR_UPDATABLE.toString())) {
            return OOResultSet.CONCUR_UPDATABLE;
        }
        throw new SQLException("Invalid resultSetConcurrency provided. The allowed values for resultSetConcurrency are: "
                + OOResultSet.CONCUR_READ_ONLY.toString() + " and " + OOResultSet.CONCUR_UPDATABLE.toString());
    }

    private static void processDefaultValues(SQLInputs sqlInputs, String dbType, String authenticationType, String username) throws Exception {
        String dbPort = "";
        if (dbType.equalsIgnoreCase(Constants.ORACLE_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_ORACLE;
        } else if (dbType.equalsIgnoreCase(Constants.MSSQL_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_MSSQL;
            if (Constants.AUTH_WINDOWS.equalsIgnoreCase(authenticationType)) {
                if (username.contains(Constants.ESCAPED_BACKSLASH)) {
                    String domain = username.substring(0, username.indexOf(Constants.ESCAPED_BACKSLASH));
                    final String newUsername = username.substring(username.indexOf(Constants.ESCAPED_BACKSLASH) + 1, username.length());
                    sqlInputs.setUsername(newUsername);
                    sqlInputs.setWindowsDomain(domain);
                }

            }

        } else if (dbType.equalsIgnoreCase(Constants.NETCOOL_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_NETCOOL;
            sqlInputs.setNetcool(true);
        } else if (dbType.equalsIgnoreCase(Constants.DB2_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_DB2;
        } else if (dbType.equalsIgnoreCase(Constants.SYBASE_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_SYBASE;
        } else if (dbType.equalsIgnoreCase(Constants.MYSQL_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_MYSQL;
        } else if (dbType.equalsIgnoreCase(Constants.POSTGRES_DB_TYPE)) {
            dbPort = Constants.DEFAULTPORT_PSQL;
        }
        if (StringUtils.isEmpty(sqlInputs.getDbPort())) {
            sqlInputs.setDbPort(dbPort);
        }
        try {
            //in case that user entered dbType is not oracle, netcool, mssql or db2
            if (StringUtils.isEmpty(sqlInputs.getDbPort())) {
                throw new Exception("There's no default DBPort for " + sqlInputs.getDbType() + " db server, please enter a valid dbPort.");
            } else {
                Integer.parseInt(sqlInputs.getDbPort());
            }
        } catch (NumberFormatException e) {
            throw new Exception("dbPort input is not in valid format.");
        }
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
            sqlInputs.setiQuerys(0);
            sqlInputs.setiUpdateCount(0);
            sqlInputs.setSqlCommand(null);
            sqlInputs.setDbServer(null);
            sqlInputs.setDbName(null);
            sqlInputs.setDbPort(null);
            sqlInputs.setDbType(null);
            sqlInputs.setTnsPath(null);
            sqlInputs.setTnsEntry(null);
            sqlInputs.setStrKey(null);
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
            sqlInputs.setlFiles(null);
            sqlInputs.setlRowsFiles(new ArrayList<ArrayList<String>>());
            sqlInputs.setlRowsNames(new ArrayList<ArrayList<String>>());
            sqlInputs.setlNames(null);
            sqlInputs.setSkip(0L);
            sqlInputs.setConnection(null);
            sqlInputs.setInstance(null);
            sqlInputs.setTimeout(0);
            sqlInputs.setKey(null);
            sqlInputs.setIgnoreCase(null);
            sqlInputs.setResultSetConcurrency(OOResultSet.NO_RESULT_SET);
            sqlInputs.setResultSetType(OOResultSet.NO_RESULT_SET);
            sqlInputs.setWindowsDomain(null);
        } else throw new Exception("Cannot init null Sql inputs!");
    }
}
