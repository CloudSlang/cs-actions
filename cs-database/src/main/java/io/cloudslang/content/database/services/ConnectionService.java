/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services;

import io.cloudslang.content.database.services.databases.*;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.services.dbconnection.TotalMaxPoolSizeExceedException;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class ConnectionService {

    private DBConnectionManager dbConnectionManager = null;

    private final Map<String, Class<? extends SqlDatabase>> dbTypes = getTypesOfDatabase();

    /**
     * get a pooled connection or a plain connection
     *
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public Connection setUpConnection(@NotNull final SQLInputs sqlInputs) throws ClassNotFoundException, SQLException {

        Properties databasePoolingProperties = sqlInputs.getDatabasePoolingProperties();

        dbConnectionManager = DBConnectionManager.getInstance();

        final String instance = sqlInputs.getInstance();
        final String dbClass = sqlInputs.getDbClass();
        final List<String> dbUrls = sqlInputs.getDbUrls();
        final String dbType = sqlInputs.getDbType();
        final String dbServer = sqlInputs.getDbServer();
        final String dbName = sqlInputs.getDbName();
        final int dbPort = sqlInputs.getDbPort();
        final String authenticationType = sqlInputs.getAuthenticationType();
        final String trustStore = sqlInputs.getTrustStore();
        final String trustStorePassword = sqlInputs.getTrustStorePassword();
        final boolean trustAllRoots = sqlInputs.getTrustAllRoots();

        if (dbUrls == null) {
            throw new SQLException("No database URL was provided");
        }
        //db type if we use connection pooling
        DBConnectionManager.DBType enumDbType;
        String triedUrls = " ";
//        SqlDatabase currentDatabase = dbTypes.get()
        //Oracle
        if (ORACLE_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.ORACLE;
            OracleDatabase oracleDatabase = new OracleDatabase();
            oracleDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        }
        //MySql
        else if (MYSQL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.MYSQL;
            MySqlDatabase mySqlDatabase = new MySqlDatabase();
            mySqlDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        }
        //MSSQL
        else if (MSSQL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.MSSQL;
            MSSqlDatabase msSqlDatabase = new MSSqlDatabase();
            msSqlDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls, authenticationType, instance, dbClass, trustAllRoots, trustStore, trustStorePassword);
        }
        //Sybase
        else if (SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.SYBASE;
            SybaseDatabase sybaseDatabase = new SybaseDatabase();
            sybaseDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        }
        //NetCool
        else if (NETCOOL_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.NETCOOL;
            NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
            netcoolDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        }
        //Postgresql
        else if (POSTGRES_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.POSTGRESQL;
            PostgreSqlDatabase psDatabase = new PostgreSqlDatabase();
            psDatabase.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        }
        //DB2papple
        else if (DB2_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.DB2;
            DB2Database db2Database = new DB2Database();
            db2Database.setUp(dbName, dbServer, Integer.toString(dbPort), dbUrls);
        } else if (CUSTOM_DB_TYPE.equalsIgnoreCase(dbType)) {
            enumDbType = DBConnectionManager.DBType.CUSTOM;
            CustomDatabase customDatabase = new CustomDatabase();
            customDatabase.setUp(dbClass);
            // dbUrl should already be defined!
        } else {
            //if something other than allowed type or empty
            //we supply dbType to be default to Oracle if it is empty
            throw new SQLException("Invalid database type : " + dbType);
        }
        return obtainConnection(enumDbType, triedUrls, dbType, dbUrls, sqlInputs, databasePoolingProperties);
    }

// todo set the types to custom
//   private Class<? extends SqlDatabase> getDbClassForType(@NotNull final String dbType) {
//
//    }
////    private SqlDatabase getDbForType(final String dbType) {
//
//    }

    private static Map<String, Class<? extends SqlDatabase>> getTypesOfDatabase() {
        final Map<String, Class<? extends SqlDatabase>> dbForType = new HashMap<>();
        dbForType.put(ORACLE_DB_TYPE, OracleDatabase.class);
        dbForType.put(MYSQL_DB_TYPE, MySqlDatabase.class);
        dbForType.put(MSSQL_DB_TYPE, MSSqlDatabase.class);
        dbForType.put(SYBASE_DB_TYPE, SybaseDatabase.class);
        dbForType.put(NETCOOL_DB_TYPE, NetcoolDatabase.class);
        dbForType.put(POSTGRES_DB_TYPE, PostgreSqlDatabase.class);
        dbForType.put(DB2_DB_TYPE, DB2Database.class);
        dbForType.put(CUSTOM_DB_TYPE, CustomDatabase.class);
        return dbForType;
    }

    private Connection obtainConnection(DBConnectionManager.DBType enumDbType, String triedUrls, String dbType, List<String> dbUrls, SQLInputs sqlInputs, Properties properties) throws SQLException {
        //iterate through the dbUrls until we find one that we can connect
        //to the DB with and throw an error if no URL works
        Iterator<String> iter = dbUrls.iterator();

        Connection dbCon = null;
        boolean hasException = true;
        SQLException ex = new SQLException("No database URL was provided");
        while (hasException && iter.hasNext()) {
            String dbUrlTry = iter.next();

            //for logging, in case something goes wrong
            if (dbUrlTry != null && dbUrlTry.length() != 0) {
                triedUrls = triedUrls + dbUrlTry + " | ";

//          todo      if (logger.isDebugEnabled()) {
//                    logger.debug("dbUrl so far: " + triedUrls);
//                }
            } else {
                continue;//somehow the dbUrls might have empty string there
            }

            try {
                //Get the Connection, depends on what user set in
                //databasePooling.properties, this connection could be
                //pooled or not pooled
                dbCon = dbConnectionManager.getConnection(enumDbType,
                                dbUrlTry,
                                sqlInputs.getUsername(),
                                sqlInputs.getPassword(),
                                properties);
                sqlInputs.setDbUrl(dbUrlTry);
                hasException = false;
            } catch (SQLException e) {
//           todo     if (logger.isDebugEnabled()) {
//                    logger.debug(e);
//                }

                hasException = true;
                ex = e;

                if (e instanceof TotalMaxPoolSizeExceedException) {
//             todo       if (logger.isDebugEnabled()) {
//                        logger.debug("Get TotalMaxPoolSizeExceedException, will stop trying");
//                    }
                    break;//don't try any more
                }
            }
        }
        if (hasException) {
            //have some logging
            String msg = "Failed to check out connection for dbType = "
                    + dbType + " username = " + sqlInputs.getUsername() + " tried dbUrls = " + triedUrls;
//      todo      logger.error(msg, ex);
            throw ex;
        }

        //for some reason it is till null, should not happen
        if (dbCon == null) {
            String msg = "Failed to check out connection. Connection is null. dbType = "
                    + dbType + " username = " + sqlInputs.getUsername() + " tried dbUrls = " + triedUrls;

            throw new SQLException(msg);
        }
        return dbCon;
    }
}
