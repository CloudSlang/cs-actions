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


import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static io.cloudslang.content.database.utils.SQLUtils.getResultSetValue;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLScriptService {

    public String executeSqlScript(ArrayList<String> lines, SQLInputs sqlInputs)
            throws Exception {
        if (lines == null || lines.isEmpty()) {
            throw new Exception("No SQL command to be executed.");
        }
        Connection connection = null;
        ConnectionService connectionService = new ConnectionService();
        try {
            connection = connectionService.setUpConnection(sqlInputs);

            try {
                connection.setReadOnly(false);
            } catch (Exception e) {
            } // not all drivers support this

            final Statement statement = connection.createStatement(getResultSetValue(sqlInputs.getResultSetType()), getResultSetValue(sqlInputs.getResultSetConcurrency()));
            statement.setQueryTimeout(sqlInputs.getTimeout());
            try {
                boolean autoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);
                int updateCount = 0;
                if (lines.size() > 1) {
                    for (String line : lines) {
                        statement.addBatch(line);
                    }
                    int[] updateCounts = statement.executeBatch();
                    for (int i : updateCounts) {
                        if (i > 0) {
                            updateCount += i;
                        }
                    }
                } else {
                    statement.execute(lines.get(0));
                    updateCount = statement.getUpdateCount();
                }
                sqlInputs.setiUpdateCount(updateCount);
                connection.commit();
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                //during a dump sybase sends back status as exceptions.
                final String dbType = sqlInputs.getDbType();
                if (Constants.SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
                    if (lines.get(0).trim().toLowerCase().startsWith("dump")) {
                        return SQLUtils.processDumpException(e);
                    } else if (lines.get(0).trim().toLowerCase().startsWith("load")) {
                        return SQLUtils.processLoadException(e);
                    }
                } else
                    throw e;
            }

        } finally {
            connectionService.closeConnection(connection);
        }
        return "Command completed successfully";
    }
}
