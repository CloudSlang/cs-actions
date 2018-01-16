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

import io.cloudslang.content.database.services.databases.SqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;
import io.cloudslang.content.database.services.dbconnection.TotalMaxPoolSizeExceedException;
import io.cloudslang.content.database.utils.SQLInputs;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbClassForType;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getDbEnumForType;

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
                final Connection dbCon = dbConnectionManager.getConnection(enumDbType, sqlInputs.getAuthenticationType(), currentUrl, sqlInputs.getUsername(), sqlInputs.getPassword(), properties);
                sqlInputs.setDbUrl(currentUrl);
                return dbCon;
            } catch (TotalMaxPoolSizeExceedException e) {
                throw new RuntimeException(e.getMessage(), e.getCause());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Couldn't find a valid url to connect to");


    }
}
