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

import io.cloudslang.content.database.utils.Format;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLQueryTabularService {

    /**
     * Run a SQL query with given configuration
     *
     * @return the formatted result set by colDelimiter and rowDelimiter
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static String execSqlQueryTabular(@NotNull final SQLInputs sqlInputs) throws Exception {
        ConnectionService connectionService = new ConnectionService();
        try (final Connection connection = connectionService.setUpConnection(sqlInputs)){
            connection.setReadOnly(true);

            final Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency());
            statement.setQueryTimeout(sqlInputs.getTimeout());

            final ResultSet resultSet = statement.executeQuery(sqlInputs.getSqlCommand());

            final String resultSetToTable = Format.resultSetToTable(resultSet, sqlInputs.isNetcool());
            resultSet.close();
            return resultSetToTable;
        }
    }
}
