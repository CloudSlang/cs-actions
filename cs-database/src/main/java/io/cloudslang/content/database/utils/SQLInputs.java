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

package io.cloudslang.content.database.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLInputs {
    private String sqlCommand;
    private String dbServer;
    private String dbName;
    private int dbPort;
    private String dbType;
    private String key;
    private String username;
    private String password;
    private String authenticationType;
    private String instance;
    private boolean ignoreCase;
    private int timeout;
    private String dbUrl;  //the final dbUrl that was used to connect to the database
    private String dbClass;
    private boolean isNetcool = false; // needs to be visible for SQLQueryTabular to do check
    private List<List<String>> lRowsFiles = new ArrayList<>();
    private List<List<String>> lRowsNames = new ArrayList<>();
    private long skip = 0L;
    private String strDelim;
    private String strColumns;
    private List<String> lRows = new ArrayList<>();
    private int iUpdateCount;
    private Properties databasePoolingProperties;
    private String trustStore;
    private String trustStorePassword;
    private boolean trustAllRoots;
    private String authLibraryPath;
    private String colDelimiter;
    private String rowDelimiter;
    private Integer resultSetType;
    private Integer resultSetConcurrency;
    private List<String> sqlCommands = new ArrayList<>();

    @java.beans.ConstructorProperties({"sqlCommand", "dbServer", "dbName", "dbPort", "dbType", "key", "username", "password", "authenticationType", "instance", "ignoreCase", "timeout", "dbUrl", "dbClass", "isNetcool", "lRowsFiles", "lRowsNames", "skip", "strDelim", "strColumns", "lRows", "iUpdateCount", "databasePoolingProperties", "trustStore", "trustStorePassword", "trustAllRoots", "authLibraryPath", "colDelimiter", "rowDelimiter", "resultSetType", "resultSetConcurrency", "sqlCommands"})
    SQLInputs(String sqlCommand, String dbServer, String dbName, int dbPort, String dbType, String key, String username, String password, String authenticationType, String instance, boolean ignoreCase, int timeout, String dbUrl, String dbClass, boolean isNetcool, List<List<String>> lRowsFiles, List<List<String>> lRowsNames, long skip, String strDelim, String strColumns, List<String> lRows, int iUpdateCount, Properties databasePoolingProperties, String trustStore, String trustStorePassword, boolean trustAllRoots, String authLibraryPath, String colDelimiter, String rowDelimiter, Integer resultSetType, Integer resultSetConcurrency, List<String> sqlCommands) {
        this.sqlCommand = sqlCommand;
        this.dbServer = dbServer;
        this.dbName = dbName;
        this.dbPort = dbPort;
        this.dbType = dbType;
        this.key = key;
        this.username = username;
        this.password = password;
        this.authenticationType = authenticationType;
        this.instance = instance;
        this.ignoreCase = ignoreCase;
        this.timeout = timeout;
        this.dbUrl = dbUrl;
        this.dbClass = dbClass;
        this.isNetcool = isNetcool;
        this.lRowsFiles = lRowsFiles == null ? new ArrayList<List<String>>() : lRowsFiles;
        this.lRowsNames = lRowsNames == null ? new ArrayList<List<String>>() : lRowsNames;
        this.skip = skip;
        this.strDelim = strDelim;
        this.strColumns = StringUtils.defaultIfEmpty(strColumns, EMPTY);
        this.lRows = lRows == null ? new ArrayList<String>() : lRows;
        this.iUpdateCount = iUpdateCount;
        this.databasePoolingProperties = databasePoolingProperties;
        this.trustStore = trustStore;
        this.trustStorePassword = trustStorePassword;
        this.trustAllRoots = trustAllRoots;
        this.authLibraryPath = authLibraryPath;
        this.colDelimiter = colDelimiter;
        this.rowDelimiter = rowDelimiter;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.sqlCommands = sqlCommands == null ? new ArrayList<String>() : sqlCommands;
    }

    public static SQLInputsBuilder builder() {
        return new SQLInputsBuilder();
    }

    public String getSqlCommand() {
        return this.sqlCommand;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public String getDbServer() {
        return this.dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getDbPort() {
        return this.dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenticationType() {
        return this.authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getInstance() {
        return this.instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getDbUrl() {
        return this.dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbClass() {
        return this.dbClass;
    }

    public void setDbClass(String dbClass) {
        this.dbClass = dbClass;
    }

    public boolean isNetcool() {
        return this.isNetcool;
    }

    public void setNetcool(boolean isNetcool) {
        this.isNetcool = isNetcool;
    }

    public List<List<String>> getLRowsFiles() {
        return this.lRowsFiles;
    }

    public void setLRowsFiles(List<List<String>> lRowsFiles) {
        this.lRowsFiles = lRowsFiles;
    }

    public List<List<String>> getLRowsNames() {
        return this.lRowsNames;
    }

    public void setLRowsNames(List<List<String>> lRowsNames) {
        this.lRowsNames = lRowsNames;
    }

    public long getSkip() {
        return this.skip;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public String getStrDelim() {
        return this.strDelim;
    }

    public void setStrDelim(String strDelim) {
        this.strDelim = strDelim;
    }

    public String getStrColumns() {
        return this.strColumns;
    }

    public void setStrColumns(String strColumns) {
        this.strColumns = strColumns;
    }

    public List<String> getLRows() {
        return this.lRows;
    }

    public void setLRows(List<String> lRows) {
        this.lRows = lRows;
    }

    public int getIUpdateCount() {
        return this.iUpdateCount;
    }

    public void setIUpdateCount(int iUpdateCount) {
        this.iUpdateCount = iUpdateCount;
    }

    public Properties getDatabasePoolingProperties() {
        return this.databasePoolingProperties;
    }

    public void setDatabasePoolingProperties(Properties databasePoolingProperties) {
        this.databasePoolingProperties = databasePoolingProperties;
    }

    public String getTrustStore() {
        return this.trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return this.trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public boolean isTrustAllRoots() {
        return this.trustAllRoots;
    }

    public void setTrustAllRoots(boolean trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }

    public String getAuthLibraryPath() {
        return this.authLibraryPath;
    }

    public void setAuthLibraryPath(String authLibraryPath) {
        this.authLibraryPath = authLibraryPath;
    }

    public String getColDelimiter() {
        return this.colDelimiter;
    }

    public void setColDelimiter(String colDelimiter) {
        this.colDelimiter = colDelimiter;
    }

    public String getRowDelimiter() {
        return this.rowDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    public Integer getResultSetType() {
        return this.resultSetType;
    }

    public void setResultSetType(Integer resultSetType) {
        this.resultSetType = resultSetType;
    }

    public Integer getResultSetConcurrency() {
        return this.resultSetConcurrency;
    }

    public void setResultSetConcurrency(Integer resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    public List<String> getSqlCommands() {
        return this.sqlCommands;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SQLInputs)) return false;
        final SQLInputs other = (SQLInputs) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$sqlCommand = this.getSqlCommand();
        final Object other$sqlCommand = other.getSqlCommand();
        if (this$sqlCommand == null ? other$sqlCommand != null : !this$sqlCommand.equals(other$sqlCommand))
            return false;
        final Object this$dbServer = this.getDbServer();
        final Object other$dbServer = other.getDbServer();
        if (this$dbServer == null ? other$dbServer != null : !this$dbServer.equals(other$dbServer)) return false;
        final Object this$dbName = this.getDbName();
        final Object other$dbName = other.getDbName();
        if (this$dbName == null ? other$dbName != null : !this$dbName.equals(other$dbName)) return false;
        if (this.getDbPort() != other.getDbPort()) return false;
        final Object this$dbType = this.getDbType();
        final Object other$dbType = other.getDbType();
        if (this$dbType == null ? other$dbType != null : !this$dbType.equals(other$dbType)) return false;
        final Object this$key = this.getKey();
        final Object other$key = other.getKey();
        if (this$key == null ? other$key != null : !this$key.equals(other$key)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$authenticationType = this.getAuthenticationType();
        final Object other$authenticationType = other.getAuthenticationType();
        if (this$authenticationType == null ? other$authenticationType != null : !this$authenticationType.equals(other$authenticationType))
            return false;
        final Object this$instance = this.getInstance();
        final Object other$instance = other.getInstance();
        if (this$instance == null ? other$instance != null : !this$instance.equals(other$instance)) return false;
        if (this.isIgnoreCase() != other.isIgnoreCase()) return false;
        if (this.getTimeout() != other.getTimeout()) return false;
        final Object this$dbUrl = this.getDbUrl();
        final Object other$dbUrl = other.getDbUrl();
        if (this$dbUrl == null ? other$dbUrl != null : !this$dbUrl.equals(other$dbUrl)) return false;
        final Object this$dbClass = this.getDbClass();
        final Object other$dbClass = other.getDbClass();
        if (this$dbClass == null ? other$dbClass != null : !this$dbClass.equals(other$dbClass)) return false;
        if (this.isNetcool() != other.isNetcool()) return false;
        final Object this$lRowsFiles = this.getLRowsFiles();
        final Object other$lRowsFiles = other.getLRowsFiles();
        if (this$lRowsFiles == null ? other$lRowsFiles != null : !this$lRowsFiles.equals(other$lRowsFiles))
            return false;
        final Object this$lRowsNames = this.getLRowsNames();
        final Object other$lRowsNames = other.getLRowsNames();
        if (this$lRowsNames == null ? other$lRowsNames != null : !this$lRowsNames.equals(other$lRowsNames))
            return false;
        if (this.getSkip() != other.getSkip()) return false;
        final Object this$strDelim = this.getStrDelim();
        final Object other$strDelim = other.getStrDelim();
        if (this$strDelim == null ? other$strDelim != null : !this$strDelim.equals(other$strDelim)) return false;
        final Object this$strColumns = this.getStrColumns();
        final Object other$strColumns = other.getStrColumns();
        if (this$strColumns == null ? other$strColumns != null : !this$strColumns.equals(other$strColumns))
            return false;
        final Object this$lRows = this.getLRows();
        final Object other$lRows = other.getLRows();
        if (this$lRows == null ? other$lRows != null : !this$lRows.equals(other$lRows)) return false;
        if (this.getIUpdateCount() != other.getIUpdateCount()) return false;
        final Object this$databasePoolingProperties = this.getDatabasePoolingProperties();
        final Object other$databasePoolingProperties = other.getDatabasePoolingProperties();
        if (this$databasePoolingProperties == null ? other$databasePoolingProperties != null : !this$databasePoolingProperties.equals(other$databasePoolingProperties))
            return false;
        final Object this$trustStore = this.getTrustStore();
        final Object other$trustStore = other.getTrustStore();
        if (this$trustStore == null ? other$trustStore != null : !this$trustStore.equals(other$trustStore))
            return false;
        final Object this$trustStorePassword = this.getTrustStorePassword();
        final Object other$trustStorePassword = other.getTrustStorePassword();
        if (this$trustStorePassword == null ? other$trustStorePassword != null : !this$trustStorePassword.equals(other$trustStorePassword))
            return false;
        final Object this$authLibraryPath = this.getAuthLibraryPath();
        final Object other$authLibraryPath = other.getAuthLibraryPath();
        if (this$authLibraryPath == null ? other$authLibraryPath != null : !this$authLibraryPath.equals(other$authLibraryPath))
            return false;
        if (this.isTrustAllRoots() != other.isTrustAllRoots()) return false;
        final Object this$colDelimiter = this.getColDelimiter();
        final Object other$colDelimiter = other.getColDelimiter();
        if (this$colDelimiter == null ? other$colDelimiter != null : !this$colDelimiter.equals(other$colDelimiter))
            return false;
        final Object this$rowDelimiter = this.getRowDelimiter();
        final Object other$rowDelimiter = other.getRowDelimiter();
        if (this$rowDelimiter == null ? other$rowDelimiter != null : !this$rowDelimiter.equals(other$rowDelimiter))
            return false;
        final Object this$resultSetType = this.getResultSetType();
        final Object other$resultSetType = other.getResultSetType();
        if (this$resultSetType == null ? other$resultSetType != null : !this$resultSetType.equals(other$resultSetType))
            return false;
        final Object this$resultSetConcurrency = this.getResultSetConcurrency();
        final Object other$resultSetConcurrency = other.getResultSetConcurrency();
        if (this$resultSetConcurrency == null ? other$resultSetConcurrency != null : !this$resultSetConcurrency.equals(other$resultSetConcurrency))
            return false;
        final Object this$sqlCommands = this.getSqlCommands();
        final Object other$sqlCommands = other.getSqlCommands();
        if (this$sqlCommands == null ? other$sqlCommands != null : !this$sqlCommands.equals(other$sqlCommands))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sqlCommand = this.getSqlCommand();
        result = result * PRIME + ($sqlCommand == null ? 43 : $sqlCommand.hashCode());
        final Object $dbServer = this.getDbServer();
        result = result * PRIME + ($dbServer == null ? 43 : $dbServer.hashCode());
        final Object $dbName = this.getDbName();
        result = result * PRIME + ($dbName == null ? 43 : $dbName.hashCode());
        result = result * PRIME + this.getDbPort();
        final Object $dbType = this.getDbType();
        result = result * PRIME + ($dbType == null ? 43 : $dbType.hashCode());
        final Object $key = this.getKey();
        result = result * PRIME + ($key == null ? 43 : $key.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $authenticationType = this.getAuthenticationType();
        result = result * PRIME + ($authenticationType == null ? 43 : $authenticationType.hashCode());
        final Object $instance = this.getInstance();
        result = result * PRIME + ($instance == null ? 43 : $instance.hashCode());
        result = result * PRIME + (this.isIgnoreCase() ? 79 : 97);
        result = result * PRIME + this.getTimeout();
        final Object $dbUrl = this.getDbUrl();
        result = result * PRIME + ($dbUrl == null ? 43 : $dbUrl.hashCode());
        final Object $dbClass = this.getDbClass();
        result = result * PRIME + ($dbClass == null ? 43 : $dbClass.hashCode());
        result = result * PRIME + (this.isNetcool() ? 79 : 97);
        final Object $lRowsFiles = this.getLRowsFiles();
        result = result * PRIME + ($lRowsFiles == null ? 43 : $lRowsFiles.hashCode());
        final Object $lRowsNames = this.getLRowsNames();
        result = result * PRIME + ($lRowsNames == null ? 43 : $lRowsNames.hashCode());
        final long $skip = this.getSkip();
        result = result * PRIME + (int) ($skip >>> 32 ^ $skip);
        final Object $strDelim = this.getStrDelim();
        result = result * PRIME + ($strDelim == null ? 43 : $strDelim.hashCode());
        final Object $strColumns = this.getStrColumns();
        result = result * PRIME + ($strColumns == null ? 43 : $strColumns.hashCode());
        final Object $lRows = this.getLRows();
        result = result * PRIME + ($lRows == null ? 43 : $lRows.hashCode());
        result = result * PRIME + this.getIUpdateCount();
        final Object $databasePoolingProperties = this.getDatabasePoolingProperties();
        result = result * PRIME + ($databasePoolingProperties == null ? 43 : $databasePoolingProperties.hashCode());
        final Object $trustStore = this.getTrustStore();
        result = result * PRIME + ($trustStore == null ? 43 : $trustStore.hashCode());
        final Object $trustStorePassword = this.getTrustStorePassword();
        result = result * PRIME + ($trustStorePassword == null ? 43 : $trustStorePassword.hashCode());
        final Object $authLibraryPath = this.getAuthLibraryPath();
        result = result * PRIME + ($authLibraryPath == null ? 43 : $authLibraryPath.hashCode());
        final Object $colDelimiter = this.getColDelimiter();
        result = result * PRIME + ($colDelimiter == null ? 43 : $colDelimiter.hashCode());
        final Object $rowDelimiter = this.getRowDelimiter();
        result = result * PRIME + ($rowDelimiter == null ? 43 : $rowDelimiter.hashCode());
        final Object $resultSetType = this.getResultSetType();
        result = result * PRIME + ($resultSetType == null ? 43 : $resultSetType.hashCode());
        final Object $resultSetConcurrency = this.getResultSetConcurrency();
        result = result * PRIME + ($resultSetConcurrency == null ? 43 : $resultSetConcurrency.hashCode());
        final Object $sqlCommands = this.getSqlCommands();
        result = result * PRIME + ($sqlCommands == null ? 43 : $sqlCommands.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SQLInputs;
    }

    public String toString() {
        return "io.cloudslang.content.database.utils.SQLInputs(sqlCommand=" + this.getSqlCommand() + ", dbServer=" + this.getDbServer() + ", dbName=" + this.getDbName() + ", dbPort=" + this.getDbPort() + ", dbType=" + this.getDbType() + ", key=" + this.getKey() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", authenticationType=" + this.getAuthenticationType() + ", instance=" + this.getInstance() + ", ignoreCase=" + this.isIgnoreCase() + ", timeout=" + this.getTimeout() + ", dbUrl=" + this.getDbUrl() + ", dbClass=" + this.getDbClass() + ", isNetcool=" + this.isNetcool() + ", lRowsFiles=" + this.getLRowsFiles() + ", lRowsNames=" + this.getLRowsNames() + ", skip=" + this.getSkip() + ", strDelim=" + this.getStrDelim() + ", strColumns=" + this.getStrColumns() + ", lRows=" + this.getLRows() + ", iUpdateCount=" + this.getIUpdateCount() + ", databasePoolingProperties=" + this.getDatabasePoolingProperties() + ", trustStore=" + this.getTrustStore() + ", trustStorePassword=" + this.getTrustStorePassword() + ", trustAllRoots=" + this.isTrustAllRoots() + ", authLibraryPath=" + this.getAuthLibraryPath() + ", colDelimiter=" + this.getColDelimiter() + ", rowDelimiter=" + this.getRowDelimiter() + ", resultSetType=" + this.getResultSetType() + ", resultSetConcurrency=" + this.getResultSetConcurrency() + ", sqlCommands=" + this.getSqlCommands() + ")";
    }

    public static class SQLInputsBuilder {
        private String sqlCommand;
        private String dbServer;
        private String dbName;
        private int dbPort;
        private String dbType;
        private String key;
        private String username;
        private String password;
        private String authenticationType;
        private String instance;
        private boolean ignoreCase;
        private int timeout;
        private String dbUrl;
        private String dbClass;
        private boolean isNetcool;
        private List<List<String>> lRowsFiles;
        private List<List<String>> lRowsNames;
        private long skip;
        private String strDelim;
        private String strColumns;
        private List<String> lRows;
        private int iUpdateCount;
        private Properties databasePoolingProperties;
        private String trustStore;
        private String trustStorePassword;
        private boolean trustAllRoots;
        private String authLibraryPath;
        private String colDelimiter;
        private String rowDelimiter;
        private Integer resultSetType;
        private Integer resultSetConcurrency;
        private List<String> sqlCommands;

        SQLInputsBuilder() {
        }

        public SQLInputs.SQLInputsBuilder sqlCommand(String sqlCommand) {
            this.sqlCommand = sqlCommand;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbServer(String dbServer) {
            this.dbServer = dbServer;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbPort(int dbPort) {
            this.dbPort = dbPort;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbType(String dbType) {
            this.dbType = dbType;
            return this;
        }

        public SQLInputs.SQLInputsBuilder key(String key) {
            this.key = key;
            return this;
        }

        public SQLInputs.SQLInputsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public SQLInputs.SQLInputsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SQLInputs.SQLInputsBuilder authenticationType(String authenticationType) {
            this.authenticationType = authenticationType;
            return this;
        }

        public SQLInputs.SQLInputsBuilder instance(String instance) {
            this.instance = instance;
            return this;
        }

        public SQLInputs.SQLInputsBuilder ignoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            return this;
        }

        public SQLInputs.SQLInputsBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbUrl(String dbUrl) {
            this.dbUrl = dbUrl;
            return this;
        }

        public SQLInputs.SQLInputsBuilder dbClass(String dbClass) {
            this.dbClass = dbClass;
            return this;
        }

        public SQLInputs.SQLInputsBuilder isNetcool(boolean isNetcool) {
            this.isNetcool = isNetcool;
            return this;
        }


        public SQLInputs.SQLInputsBuilder strDelim(String strDelim) {
            this.strDelim = strDelim;
            return this;
        }

        public SQLInputs.SQLInputsBuilder databasePoolingProperties(Properties databasePoolingProperties) {
            this.databasePoolingProperties = databasePoolingProperties;
            return this;
        }

        public SQLInputs.SQLInputsBuilder trustStore(String trustStore) {
            this.trustStore = trustStore;
            return this;
        }

        public SQLInputs.SQLInputsBuilder trustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
            return this;
        }

        public SQLInputs.SQLInputsBuilder trustAllRoots(boolean trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public SQLInputs.SQLInputsBuilder authLibraryPath(String authLibraryPath) {
            this.authLibraryPath = authLibraryPath;
            return this;
        }

        public SQLInputs.SQLInputsBuilder colDelimiter(String colDelimiter) {
            this.colDelimiter = colDelimiter;
            return this;
        }

        public SQLInputs.SQLInputsBuilder rowDelimiter(String rowDelimiter) {
            this.rowDelimiter = rowDelimiter;
            return this;
        }

        public SQLInputs.SQLInputsBuilder resultSetType(Integer resultSetType) {
            this.resultSetType = resultSetType;
            return this;
        }

        public SQLInputs.SQLInputsBuilder resultSetConcurrency(Integer resultSetConcurrency) {
            this.resultSetConcurrency = resultSetConcurrency;
            return this;
        }

        public SQLInputs.SQLInputsBuilder sqlCommands(List<String> sqlCommands) {
            this.sqlCommands = sqlCommands;
            return this;
        }

        public SQLInputs build() {
            return new SQLInputs(sqlCommand, dbServer, dbName, dbPort, dbType, key, username, password, authenticationType, instance, ignoreCase, timeout, dbUrl, dbClass, isNetcool, lRowsFiles, lRowsNames, skip, strDelim, strColumns, lRows, iUpdateCount, databasePoolingProperties, trustStore, trustStorePassword, trustAllRoots, authLibraryPath, colDelimiter, rowDelimiter, resultSetType, resultSetConcurrency, sqlCommands);
        }

        public String toString() {
            return "io.cloudslang.content.database.utils.SQLInputs.SQLInputsBuilder(sqlCommand=" + this.sqlCommand + ", dbServer=" + this.dbServer + ", dbName=" + this.dbName + ", dbPort=" + this.dbPort + ", dbType=" + this.dbType + ", key=" + this.key + ", username=" + this.username + ", password=" + this.password + ", authenticationType=" + this.authenticationType + ", instance=" + this.instance + ", ignoreCase=" + this.ignoreCase + ", timeout=" + this.timeout + ", dbUrl=" + this.dbUrl + ", dbClass=" + this.dbClass + ", isNetcool=" + this.isNetcool + ", lRowsFiles=" + this.lRowsFiles + ", lRowsNames=" + this.lRowsNames + ", skip=" + this.skip + ", strDelim=" + this.strDelim + ", strColumns=" + this.strColumns + ", lRows=" + this.lRows + ", iUpdateCount=" + this.iUpdateCount + ", databasePoolingProperties=" + this.databasePoolingProperties + ", trustStore=" + this.trustStore + ", trustStorePassword=" + this.trustStorePassword + ", trustAllRoots=" + this.trustAllRoots + ", authLibraryPath=" + this.authLibraryPath + ", colDelimiter=" + this.colDelimiter + ", rowDelimiter=" + this.rowDelimiter + ", resultSetType=" + this.resultSetType + ", resultSetConcurrency=" + this.resultSetConcurrency + ", sqlCommands=" + this.sqlCommands + ")";
        }
    }
}
