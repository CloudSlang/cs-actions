/*
 * (c) Copyright 2022 Micro Focus
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
/*
 * (c) Copyright 2022 Micro Focus
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

package io.cloudslang.content.database.service;

import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.utils.OracleDbmsOutput;
import oracle.jdbc.driver.OracleConnection;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

import static io.cloudslang.content.database.utils.Constants.*;
import static io.cloudslang.content.database.utils.Outputs.UPDATE_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class OracleCloudQueryService {


    public static Map<String, String> executeSqlCommand(OracleCloudInputs sqlInputs) throws SQLException {

        Properties props = getProperties(sqlInputs);
        try (Connection connection = DriverManager.getConnection(ORACLE_URL + sqlInputs.getConnectionString(), props)) {

            if (sqlInputs.getSqlCommand().contains(DBMS_OUTPUT)) {
                try (final PreparedStatement statement = connection.prepareStatement(sqlInputs.getSqlCommand());
                     final OracleDbmsOutput oracleDbmsOutput = new OracleDbmsOutput(connection)) {

                    statement.setQueryTimeout(sqlInputs.getTimeout());
                    statement.executeQuery();
                    int updateCount = statement.getUpdateCount();
                    Map<String, String> result = getSuccessResultsMap(oracleDbmsOutput.getOutput());
                    result.put(UPDATE_COUNT, String.valueOf(updateCount));
                    return result;
                }
            } else {
                try (final Statement statement = connection.createStatement()) {

                    statement.setQueryTimeout(sqlInputs.getTimeout());
                    statement.executeUpdate(sqlInputs.getSqlCommand());
                    int updateCount = statement.getUpdateCount();
                    Map<String, String> result = getSuccessResultsMap(SUCCESS_MESSAGE);
                    result.put(UPDATE_COUNT, String.valueOf(updateCount));
                    return result;
                }
            }
        }
    }

    public static void executeSqlQuery(final OracleCloudInputs sqlInputs) throws Exception {

        Properties props = getProperties(sqlInputs);

        Connection connection = DriverManager.getConnection(ORACLE_URL + sqlInputs.getConnectionString(), props);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(sqlInputs.getTimeout());

        final ResultSet results = statement.executeQuery(sqlInputs.getSqlCommand());
        final ResultSetMetaData metaData = results.getMetaData();

        final StringBuilder strColumns = new StringBuilder();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {

            if (i > 1)
                strColumns.append(sqlInputs.getDelimiter());

            strColumns.append(metaData.getColumnLabel(i));
        }

        sqlInputs.setColumnNames(strColumns.toString());

        while (results.next()) {

            final StringBuilder strRowHolder = new StringBuilder();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                if (i > 1)
                    strRowHolder.append(sqlInputs.getDelimiter());

                if (results.getString(i) != null)
                    strRowHolder.append(results.getString(i).trim());
            }

            sqlInputs.getRowsLeft().add(strRowHolder.toString());
        }
    }


    private static Properties getProperties(OracleCloudInputs sqlInputs) {
        Properties props = new Properties();
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_USER_NAME, sqlInputs.getUsername());
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_PASSWORD, sqlInputs.getPassword());

        if (!isEmpty(sqlInputs.getTrustStore()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE, sqlInputs.getTrustStore());

        if (!isEmpty(sqlInputs.getTrustStorePassword()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTOREPASSWORD, sqlInputs.getTrustStorePassword());

        if (!isEmpty(sqlInputs.getKeyStore()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE, sqlInputs.getKeyStore());

        if (!isEmpty(sqlInputs.getKeyStorePassword()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTOREPASSWORD, sqlInputs.getKeyStorePassword());

        if (!isEmpty(sqlInputs.getWalletPath()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_TNS_ADMIN, sqlInputs.getWalletPath());

        return props;
    }

    static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}

