/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.database.services;

import io.cloudslang.content.database.utils.OracleDbmsOutput;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;

import static io.cloudslang.content.database.constants.DBOtherValues.DBMS_OUTPUT;
import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static io.cloudslang.content.database.constants.DBOtherValues.SYBASE_DB_TYPE;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLCommandService {

    public static String executeSqlCommand(final SQLInputs sqlInputs) throws Exception {
        final ConnectionService connectionService = new ConnectionService();
        try (final Connection connection = connectionService.setUpConnection(sqlInputs)){

            connection.setReadOnly(false);

            final String dbType = sqlInputs.getDbType();
            if (ORACLE_DB_TYPE.equalsIgnoreCase(dbType) && sqlInputs.getSqlCommand().toLowerCase().contains(DBMS_OUTPUT)) {

                final PreparedStatement preparedStatement = connection.prepareStatement(sqlInputs.getSqlCommand());
                preparedStatement.setQueryTimeout(sqlInputs.getTimeout());
                OracleDbmsOutput oracleDbmsOutput = new OracleDbmsOutput(connection);
                preparedStatement.executeQuery();
                sqlInputs.setIUpdateCount(preparedStatement.getUpdateCount());
                preparedStatement.close();
                final String output = oracleDbmsOutput.getOutput();
                oracleDbmsOutput.close();
                return output;
            } else {
                final Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency());
                statement.setQueryTimeout(sqlInputs.getTimeout());
                try {
                    statement.execute(sqlInputs.getSqlCommand());
                } catch (SQLException e) {
                    if (SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
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
                        sqlInputs.getLRows().clear();
                        int colCount = rsMtd.getColumnCount();

                        if (sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("dbcc")) {
                            while (rs.next()) {
                                if (colCount >= 4) {
                                    sqlInputs.getLRows().add(rs.getString(4));
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
                                sqlInputs.getLRows().add(strRowHolder);
                            }
                        }
                        rs.close();
                    }

                }
                //For sybase, when dbcc command is executed, the result is shown in warning message
                else if (dbType.equalsIgnoreCase(SYBASE_DB_TYPE) && sqlInputs.getSqlCommand().trim().toLowerCase().startsWith("dbcc")) {
                    SQLWarning warning = statement.getWarnings();
                    while (warning != null) {
                        sqlInputs.getLRows().add(warning.getMessage());
                        warning = warning.getNextWarning();
                    }
                }

                sqlInputs.setIUpdateCount(statement.getUpdateCount());
            }
        }
        return "Command completed successfully";
    }
}
