/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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


import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.SQLUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static io.cloudslang.content.database.constants.DBOtherValues.SYBASE_DB_TYPE;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLScriptService {

    public static String executeSqlScript(List<String> lines, SQLInputs sqlInputs)
            throws Exception {
        if (lines == null || lines.isEmpty()) {
            throw new Exception("No SQL command to be executed.");
        }
        ConnectionService connectionService = new ConnectionService();
        try (final Connection connection = connectionService.setUpConnection(sqlInputs)) {

            try {
                connection.setReadOnly(false);
            } catch (Exception e) {
            } // not all drivers support this

            try (final Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency())) {
                statement.setQueryTimeout(sqlInputs.getTimeout());
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
                sqlInputs.setIUpdateCount(updateCount);
                connection.commit();
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                //during a dump sybase sends back status as exceptions.
                final String dbType = sqlInputs.getDbType();
                if (SYBASE_DB_TYPE.equalsIgnoreCase(dbType)) {
                    if (lines.get(0).trim().toLowerCase().startsWith("dump")) {
                        return SQLUtils.processDumpException(e);
                    } else if (lines.get(0).trim().toLowerCase().startsWith("load")) {
                        return SQLUtils.processLoadException(e);
                    }
                } else
                    throw e;
            }
        }
        return "Command completed successfully";
    }
}
