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

import io.cloudslang.content.database.utils.OracleCloudInputs;

import java.sql.*;
import java.util.Properties;

public class OracleCloudQueryService {

    static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static void executeSqlQuery(final OracleCloudInputs sqlInputs) throws Exception {

        Class.forName("oracle.jdbc.driver.OracleDriver");

        Properties props = new Properties();
        props.setProperty("user", sqlInputs.getUser());
        props.setProperty("password", sqlInputs.getPassword());

        if (!isEmpty(sqlInputs.getTrustStore())) {
            props.setProperty("javax.net.ssl.trustStoreType", "JKS");
            props.setProperty("javax.net.ssl.trustStore", sqlInputs.getTrustStore());
        }

        if (!isEmpty(sqlInputs.getTrustStorePassword()))
            props.setProperty("javax.net.ssl.trustStorePassword", sqlInputs.getTrustStorePassword());

        if (!isEmpty(sqlInputs.getKeyStore()))
            props.setProperty("javax.net.ssl.keyStore", sqlInputs.getKeyStore());

        if (!isEmpty(sqlInputs.getKeyStorePassword()))
            props.setProperty("javax.net.ssl.keyStorePassword", sqlInputs.getKeyStorePassword());

        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@" + sqlInputs.getConnectionString(), props);

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
}

