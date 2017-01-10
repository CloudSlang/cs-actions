package io.cloudslang.content.database.services;

import com.iconclude.content.actions.sql.entities.SQLInputs;
import org.apache.commons.lang3.StringUtils;
import com.opsware.pas.content.commons.util.sql.Format;

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
