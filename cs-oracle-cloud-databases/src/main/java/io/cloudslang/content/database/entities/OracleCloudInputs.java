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
package io.cloudslang.content.database.entities;

import io.cloudslang.content.database.utils.SQLUtils;
import io.cloudslang.content.utils.CollectionUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

public class OracleCloudInputs {
    //Inputs
    private String connectionString;
    private String username;
    private String password;

    private String walletPath;
    private String sqlCommand;
    private List<String> sqlCommands = new ArrayList<>();
    private String delimiter;
    private String colDelimiter;
    private String rowDelimiter;
    private String trustStore;
    private String trustStorePassword;
    private String keyStore;
    private String keyStorePassword;
    private int executionTimeout;

    private boolean overwrite;

    private int resultSetType;
    private int resultSetConcurrency;

    private String key;
    private int iUpdateCount;
    private String dbType;

    //Outputs
    private String columnNames;
    private List<String> rowsLeft = new ArrayList<>();

    @java.beans.ConstructorProperties({"connectionString", "username", "password", "walletPath", "sqlCommand", "sqlCommands",
            "delimiter", "colDelimiter", "rowDelimiter", "overwrite", "trustStore", "trustStorePassword", "keyStore", "keyStorePassword",
            "executionTimeout", "resultSetType", "resultSetConcurrency", "key"})
    public OracleCloudInputs(String connectionString,
                             String username,
                             String password,
                             String walletPath,
                             String sqlCommand,
                             List<String> sqlCommands,
                             String delimiter,
                             String colDelimiter,
                             String rowDelimiter,
                             boolean overwrite,
                             String trustStore,
                             String trustStorePassword,
                             String keyStore,
                             String keyStorePassword,
                             int executionTimeout,
                             int resultSetType,
                             int resultSetConcurrency,
                             String key
    ) {

        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
        this.walletPath = walletPath;
        this.sqlCommand = sqlCommand;
        this.sqlCommands = sqlCommands;
        this.delimiter = delimiter;
        this.colDelimiter = colDelimiter;
        this.rowDelimiter = rowDelimiter;
        this.overwrite = overwrite;
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.keyStore = keyStore;
        this.keyStorePassword = keyStorePassword;
        this.executionTimeout = executionTimeout;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.key = key;
    }

    public static OracleCloudInputsBuilder builder() {
        return new OracleCloudInputsBuilder();
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getWalletPath() {
        return walletPath;
    }

    public boolean getOverwrite() {
        return overwrite;
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

    public int getExecutionTimeout() {
        return executionTimeout;
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

    public int getResultSetType() {
        return resultSetType;
    }

    public int getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    public String getKey() {
        return key;
    }

    public String getColDelimiter() {
        return colDelimiter;
    }

    public void setColDelimiter(String colDelimiter) {
        this.colDelimiter = colDelimiter;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    public List<String> getSqlCommands() {
        return sqlCommands;
    }

    public static List<String> getSqlCommands(final String sqlCommandsStr, final String scriptFileName, final String commandsDelimiter) {
        if (isNoneEmpty(sqlCommandsStr)) {
            return CollectionUtilities.toList(sqlCommandsStr, commandsDelimiter);
        }
        if (isNoneEmpty(scriptFileName)) {
            return SQLUtils.readFromFile(scriptFileName);
        }
        return Collections.emptyList();
    }

    public void setSqlCommands(List<String> sqlCommands) {
        this.sqlCommands = sqlCommands;
    }

    public int getIUpdateCount() {
        return iUpdateCount;
    }

    public void setIUpdateCount(int iUpdateCount) {
        this.iUpdateCount = iUpdateCount;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public static class OracleCloudInputsBuilder {

        private String connectionString;
        private String username;
        private String password;
        private String walletPath;
        private String sqlCommand;
        private List<String> sqlCommands;
        private String delimiter;
        private String colDelimiter;
        private String rowDelimiter;
        private String trustStore;
        private String trustStorePassword;
        private String keyStore;
        private String keyStorePassword;
        private int executionTimeout;
        private int resultSetType;
        private int resultSetConcurrency;
        private boolean overwrite;
        private String key;

        public OracleCloudInputsBuilder() {
        }

        public OracleCloudInputsBuilder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }

        public OracleCloudInputsBuilder sqlCommand(String sqlCommand) {
            this.sqlCommand = sqlCommand;
            return this;
        }

        public OracleCloudInputsBuilder sqlCommands(List<String> sqlCommands) {
            this.sqlCommands = sqlCommands;
            return this;
        }

        public OracleCloudInputsBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public OracleCloudInputsBuilder colDelimiter(String colDelimiter) {
            this.colDelimiter = colDelimiter;
            return this;
        }

        public OracleCloudInputsBuilder rowDelimiter(String rowDelimiter) {
            this.rowDelimiter = rowDelimiter;
            return this;
        }

        public OracleCloudInputsBuilder overwrite(boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

        public OracleCloudInputsBuilder walletPath(String walletPath) {
            this.walletPath = walletPath;
            return this;
        }

        public OracleCloudInputsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public OracleCloudInputsBuilder password(String password) {
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

        public OracleCloudInputsBuilder executionTimeout(int executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        public OracleCloudInputsBuilder resultSetType(int resultSetType) {
            this.resultSetType = resultSetType;
            return this;
        }

        public OracleCloudInputsBuilder resultSetConcurrency(int resultSetConcurrency) {
            this.resultSetConcurrency = resultSetConcurrency;
            return this;
        }

        public OracleCloudInputsBuilder key(String key) {
            this.key = key;
            return this;
        }


        public OracleCloudInputs build() {
            return new OracleCloudInputs(connectionString,
                    username,
                    password,
                    walletPath,
                    sqlCommand,
                    sqlCommands,
                    delimiter,
                    colDelimiter,
                    rowDelimiter,
                    overwrite,
                    trustStore,
                    trustStorePassword,
                    keyStore,
                    keyStorePassword,
                    executionTimeout,
                    resultSetType,
                    resultSetConcurrency,
                    key
            );
        }
    }
}
