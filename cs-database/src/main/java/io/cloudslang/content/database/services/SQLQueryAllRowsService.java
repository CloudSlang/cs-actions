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


import io.cloudslang.content.database.utils.Format;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLQueryAllRowsService {
    /** todo
     * Run a SQL query with given configuration
     *
     * @return the formatted result set by colDelimiter and rowDelimiter
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static String execQueryAllRows(@NotNull final SQLInputs sqlInputs) throws Exception {
        ConnectionService connectionService = new ConnectionService();
        try (final Connection connection = connectionService.setUpConnection(sqlInputs)) {
            connection.setReadOnly(true);

            Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency());

            statement.setQueryTimeout(sqlInputs.getTimeout());
            final ResultSet resultSet = statement.executeQuery(sqlInputs.getSqlCommand());

            final String resultSetToDelimitedColsAndRows = Format.resultSetToDelimitedColsAndRows(resultSet, sqlInputs.isNetcool(), sqlInputs.getColDelimiter(), sqlInputs.getRowDelimiter());
            if (resultSet != null) {
                resultSet.close();
            }
            return resultSetToDelimitedColsAndRows;
        }
    }
}
