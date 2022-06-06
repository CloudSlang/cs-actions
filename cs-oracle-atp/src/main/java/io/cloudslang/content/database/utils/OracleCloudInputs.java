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
package io.cloudslang.content.database.utils;

import java.util.ArrayList;
import java.util.List;

public class OracleCloudInputs {
    //Inputs
    private String connectionString;
    private String user;
    private String password;
    private String sqlCommand;
    private String delimiter;
    private String trustStore;
    private String trustStorePassword;
    private String keyStore;
    private String keyStorePassword;
    private int timeout;

    //Outputs
    private String columnNames;
    private List<String> rowsLeft = new ArrayList<>();

    @java.beans.ConstructorProperties({"connectionString", "user", "password", "sqlCommand", "delimiter", "trustStore", "trustStorePassword", "keyStore", "keyStorePassword", "timeout"})
    public OracleCloudInputs(String connectionString,
                             String user,
                             String password,
                             String sqlCommand,
                             String delimiter,
                             String trustStore,
                             String trustStorePassword,
                             String keyStore,
                             String keyStorePassword,
                             int timeout) {

        this.connectionString = connectionString;
        this.user = user;
        this.password = password;
        this.sqlCommand = sqlCommand;
        this.delimiter = delimiter;
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.keyStore = keyStore;
        this.keyStorePassword = keyStorePassword;
        this.timeout = timeout;
    }

    public static OracleCloudInputsBuilder builder() {
        return new OracleCloudInputsBuilder();
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getRowsLeft() {
        return rowsLeft;
    }

    public void setRowsLeft(List<String> rowsLeft) {
        this.rowsLeft = rowsLeft;
    }

    public static class OracleCloudInputsBuilder {

        private String connectionString;
        private String user;
        private String password;
        private String sqlCommand;
        private String delimiter;
        private String trustStore;
        private String trustStorePassword;
        private String keyStore;
        private String keyStorePassword;
        private int timeout;

        OracleCloudInputsBuilder() {
        }

        public OracleCloudInputsBuilder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public OracleCloudInputsBuilder sqlCommand(String sqlCommand) {
            this.sqlCommand = sqlCommand;
            return this;
        }

        public OracleCloudInputsBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public OracleCloudInputsBuilder user(String sqlCommand) {
            this.user = user;
            return this;
        }

        public OracleCloudInputsBuilder password(String sqlCommand) {
            this.password = password;
            return this;
        }

        public OracleCloudInputsBuilder trustStore(String trustStore) {
            this.trustStore = trustStore;
            return this;
        }

        public OracleCloudInputsBuilder trustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
            return this;
        }

        public OracleCloudInputsBuilder keyStore(String keyStore) {
            this.keyStore = keyStore;
            return this;
        }

        public OracleCloudInputsBuilder keyStorePassword(String keyStorePassword) {
            this.keyStorePassword = keyStorePassword;
            return this;
        }

        public OracleCloudInputsBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public OracleCloudInputs build() {
            return new OracleCloudInputs(
                    connectionString,
                    user,
                    password,
                    sqlCommand,
                    delimiter,
                    trustStore,
                    trustStorePassword,
                    keyStore,
                    keyStorePassword,
                    timeout
            );
        }
    }
}
