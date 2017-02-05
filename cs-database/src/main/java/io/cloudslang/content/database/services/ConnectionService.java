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

import io.cloudslang.content.database.services.databases.SqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;
import io.cloudslang.content.database.services.dbconnection.TotalMaxPoolSizeExceedException;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbClassForType;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbEnumForType;

/**
 * Created by victor on 13.01.2017.
 */
public class ConnectionService {

    private DBConnectionManager dbConnectionManager = null;

    /**
     * get a pooled connection or a plain connection
     *
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public Connection setUpConnection(@NotNull final SQLInputs sqlInputs) throws ClassNotFoundException, SQLException {
        dbConnectionManager = DBConnectionManager.getInstance();
        final List<String> connectionUrls = getConnectionUrls(sqlInputs);
        return obtainConnection(connectionUrls, sqlInputs);
    }

    public List<String> getConnectionUrls(@NotNull final SQLInputs sqlInputs) {
        final SqlDatabase currentDatabase = getDbClassForType(sqlInputs.getDbType());
        return currentDatabase.setUp(sqlInputs);
    }

    private Connection obtainConnection(List<String> dbUrls, SQLInputs sqlInputs) throws SQLException {
        //iterate through the dbUrls until we find one that we can connect
        //to the DB with and throw an error if no URL works
        String triedUrls = " ";
        final String dbType = sqlInputs.getDbType();
        final DBType enumDbType = getDbEnumForType(dbType);
        final Properties properties = sqlInputs.getDatabasePoolingProperties();

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
