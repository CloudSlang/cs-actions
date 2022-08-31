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



package io.cloudslang.content.database.service;


import io.cloudslang.content.database.entities.OracleCloudInputs;
import io.cloudslang.content.database.utils.SQLUtils;
import oracle.jdbc.driver.OracleConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.SYBASE_DB_TYPE;
import static io.cloudslang.content.database.utils.Constants.JKS;
import static io.cloudslang.content.database.utils.Constants.ORACLE_URL;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by victor on 13.01.2017.
 */
public class OracleCloudScriptService {

    public static String executeSqlScript(List<String> lines, OracleCloudInputs sqlInputs)
            throws Exception {
        if (lines == null || lines.isEmpty()) {
            throw new Exception("No SQL command to be executed.");
        }
        Properties props = getProperties(sqlInputs);
        try (Connection connection = DriverManager.getConnection(ORACLE_URL + sqlInputs.getConnectionString(), props)) {
            connection.setReadOnly(true);

            try (final Statement statement = connection.createStatement(sqlInputs.getResultSetType(), sqlInputs.getResultSetConcurrency())) {
                statement.setQueryTimeout(sqlInputs.getExecutionTimeout());
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