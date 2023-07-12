/*
 * Copyright 2022-2023 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.database.utils.Format;
import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static io.cloudslang.content.database.utils.Constants.JKS;
import static io.cloudslang.content.database.utils.Constants.ORACLE_URL;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class OracleCloudQueryAllRowsService {

    public static String execQueryAllRows(final OracleCloudInputs sqlInputs) throws Exception {

        Properties props = getProperties(sqlInputs);
        try (Connection connection = DriverManager.getConnection(ORACLE_URL + sqlInputs.getConnectionString(), props)) {
            connection.setReadOnly(true);
            Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency());
            statement.setQueryTimeout(sqlInputs.getExecutionTimeout());

            final ResultSet results = statement.executeQuery(sqlInputs.getSqlCommand());

            final String resultSetToDelimitedColsAndRows = Format.resultSetToDelimitedColsAndRows(results, sqlInputs.getColDelimiter(), sqlInputs.getRowDelimiter());
            if (results != null) {
                results.close();
            }
            return resultSetToDelimitedColsAndRows;
        }
    }

    private static Properties getProperties(OracleCloudInputs sqlInputs) {
        Properties props = new Properties();
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_USER_NAME, sqlInputs.getUsername());
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_PASSWORD, sqlInputs.getPassword());
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORETYPE, JKS);
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORETYPE, JKS);

        if (!isEmpty(sqlInputs.getWalletPath()))
            props.setProperty(OracleConnection.CONNECTION_PROPERTY_TNS_ADMIN, sqlInputs.getWalletPath());
        else {
            if (!isEmpty(sqlInputs.getTrustStore()))
                props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE, sqlInputs.getTrustStore());

            if (!isEmpty(sqlInputs.getTrustStorePassword()))
                props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTOREPASSWORD, sqlInputs.getTrustStorePassword());

            if (!isEmpty(sqlInputs.getKeyStore()))
                props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE, sqlInputs.getKeyStore());

            if (!isEmpty(sqlInputs.getKeyStorePassword()))
                props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTOREPASSWORD, sqlInputs.getKeyStorePassword());
        }
        return props;
    }
}

