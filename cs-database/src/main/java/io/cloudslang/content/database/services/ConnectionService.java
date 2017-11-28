/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.database.services;

import io.cloudslang.content.database.services.databases.SqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;
import io.cloudslang.content.database.services.dbconnection.TotalMaxPoolSizeExceedException;
import io.cloudslang.content.database.utils.SQLInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.MSSQL_DB_TYPE;
import static io.cloudslang.content.database.utils.Constants.AUTH_WINDOWS;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbClassForType;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbEnumForType;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Created by victor on 13.01.2017.
 */
public class ConnectionService {

    private DBConnectionManager dbConnectionManager = null;

    public Connection setUpConnection(@NotNull final SQLInputs sqlInputs) throws SQLException {
        dbConnectionManager = DBConnectionManager.getInstance();
        final List<String> connectionUrls = getConnectionUrls(sqlInputs);
        return obtainConnection(connectionUrls, sqlInputs);
    }

    public List<String> getConnectionUrls(@NotNull final SQLInputs sqlInputs) {
        final SqlDatabase currentDatabase = getDbClassForType(sqlInputs.getDbType());
        return currentDatabase.setUp(sqlInputs);
    }

    private Connection obtainConnection(@NotNull final List<String> dbUrls, @NotNull final SQLInputs sqlInputs) {
        final DBType enumDbType = getDbEnumForType(sqlInputs.getDbType());
        final Properties properties = sqlInputs.getDatabasePoolingProperties();


        for (final String currentUrl : dbUrls) {
            try {
                final Connection dbCon;
                if (AUTH_WINDOWS.equalsIgnoreCase(sqlInputs.getAuthenticationType()) && MSSQL_DB_TYPE.equalsIgnoreCase(sqlInputs.getDbType())) {
                    dbCon = dbConnectionManager.getConnection(enumDbType, currentUrl, SPACE, SPACE, properties);
                } else {
                    dbCon = dbConnectionManager.getConnection(enumDbType, currentUrl, sqlInputs.getUsername(),
                            sqlInputs.getPassword(), properties); //closing of the connection should not be handled here( no try with resources)
                }
                sqlInputs.setDbUrl(currentUrl);
                return dbCon;
            } catch (TotalMaxPoolSizeExceedException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (SQLException ignored) {
            }
        }
        throw new RuntimeException("Couldn't find a valid url to connect to");


    }
}
