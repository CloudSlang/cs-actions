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


import io.cloudslang.content.database.services.entities.SQLInputs;
import io.cloudslang.content.database.utils.Format;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by vranau on 12/9/2014.
 */
public class SQLQueryAllRowsService {
    /**
     * Run a SQL query with given configuration
     *
     * @return the formatted result set by colDelimiter and rowDelimiter
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public String execQueryAllRows(SQLInputs sqlInputs, String colDelimiter, String rowDelimiter) throws Exception {

        if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
            throw new Exception("command input is empty.");
        }
        Connection connection = null;
        ConnectionService connectionService = new ConnectionService();
        try {
            connection = connectionService.setUpConnection(sqlInputs);
            connection.setReadOnly(true);

            Statement statement = connection.createStatement(sqlInputs.getResultSetType().getValue(), sqlInputs.getResultSetConcurrency().getValue());

            statement.setQueryTimeout(sqlInputs.getTimeout());
            final ResultSet resultSet = statement.executeQuery(sqlInputs.getSqlCommand());

            final String resultSetToDelimitedColsAndRows = Format.resultSetToDelimitedColsAndRows(resultSet, sqlInputs.isNetcool(), colDelimiter, rowDelimiter);
            if(resultSet != null) {
                resultSet.close();
            }
            return resultSetToDelimitedColsAndRows;
        } finally {
            connectionService.closeConnection(connection);
        }
    }
}
