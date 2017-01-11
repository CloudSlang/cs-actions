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
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.OracleDbmsOutput;
import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;

/**
 * Created by vranau on 12/3/2014.
 */
public class SQLCommandService {

    public String executeSqlCommand(SQLInputs sqlInputs)
            throws Exception {
        if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
            throw new Exception("command input is empty.");
        }
        Connection connection = null;
        ConnectionService connectionService = new ConnectionService();
        try {
            connection = connectionService.setUpConnection(sqlInputs);
            connection.setReadOnly(false);

            final String dbType = sqlInputs.getDbType();
            if (Constants.ORACLE_DB_TYPE.equalsIgnoreCase(dbType) &&
                    sqlInputs.getSqlCommand().toLowerCase().contains("dbms_output")) {
                final PreparedStatement preparedStatement = connection.prepareStatement(sqlInputs.getSqlCommand());
                preparedStatement.setQueryTimeout(sqlInputs.getTimeout());
                OracleDbmsOutput oracleDbmsOutput = new OracleDbmsOutput(connection);
                preparedStatement.executeQuery();
                sqlInputs.setiUpdateCount(preparedStatement.getUpdateCount());
                preparedStatement.close();
                String output = oracleDbmsOutput.getOutput();
                oracleDbmsOutput.close();
                return output;
            } else {
                final Statement statement = connection.createStatement(sqlInputs.getResultSetType().getValue(), sqlInputs.getResultSetConcurrency().getValue());
                statement.setQueryTimeout(sqlInputs.getTimeout());
                try {
                    statement.execute(sqlInputs.getSqlCommand());
                } catch (SQLException e) {
                    if (Constants.SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
                        //during a dump sybase sends back status as exceptions.
                        if (sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("dump")) {
                            return SQLUtils.processDumpException(e);
                        } else if (sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("load")) {
                            return SQLUtils.processLoadException(e);
                        }
                    } else {
                        throw e;
                    }
                }

                ResultSet rs = statement.getResultSet();
                if (rs != null) {
                    ResultSetMetaData rsMtd = rs.getMetaData();
                    if (rsMtd != null) {
                        sqlInputs.getlRows().clear();
                        int colCount = rsMtd.getColumnCount();

                        final String columnLabel = rsMtd.getColumnLabel(colCount);
                        if (columnLabel != null && columnLabel.equalsIgnoreCase("ROWSTAT") && SQLUtils.trimRowstat(sqlInputs.getDbUrl(), sqlInputs.getTrimRowstat())) {
                            colCount -= 1;
                        }

                        if (sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("dbcc")) {
                            while (rs.next()) {
                                if (colCount >= 4) {
                                    sqlInputs.getlRows().add(rs.getString(4));
                                }
                            }
                        } else {
                            String delimiter = (StringUtils.isNoneEmpty(sqlInputs.getStrDelim())) ? sqlInputs.getStrDelim() : ",";
                            String strRowHolder;
                            while (rs.next()) {
                                strRowHolder = "";
                                for (int i = 1; i <= colCount; i++) {
                                    if (i > 1) {
                                        strRowHolder += delimiter;
                                    }
                                    strRowHolder += rs.getString(i);
                                }
                                sqlInputs.getlRows().add(strRowHolder);
                            }
                        }
                        rs.close();
                    }

                }
                //For sybase, when dbcc command is executed, the result is shown in warning message
                else if (dbType.equalsIgnoreCase(Constants.SYBASE_DB_TYPE) && sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("dbcc")) {
                    SQLWarning warning = statement.getWarnings();
                    while (warning != null) {
                        sqlInputs.getlRows().add(warning.getMessage());
                        warning = warning.getNextWarning();
                    }
                }

                sqlInputs.setiUpdateCount(statement.getUpdateCount());
            }
        } finally {
            connectionService.closeConnection(connection);
        }
        return "Command completed successfully";
    }
}
