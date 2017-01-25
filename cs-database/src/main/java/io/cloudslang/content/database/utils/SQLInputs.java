/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    private String strKeyCol;
    private String strKeyFiles;
    private String strKeyNames;
    private String strKeySkip;
    private String username;
    private String password;
    private String authenticationType;
    private String instance;
    private String ignoreCase;
    private int timeout;
    private String dbUrl;  //the final dbUrl that was used to connect to the database
    private List<String> dbUrls; //a list of dbUrls that are used to try to connect to the database
    private String dbClass;
    private String trimRowstat = "true";
    private boolean isNetcool = false; // needs to be visible for SQLQueryTabular to do check
    private List<List<String>> lRowsFiles = new ArrayList<>();
    private List<List<String>> lRowsNames = new ArrayList<>();
    private long skip = 0L;
    private String strDelim;
    private String strColumns;
    private List<String> lRows;
    private int iUpdateCount;
    private Properties databasePoolingProperties;
    private String windowsDomain;
    private String trustStore;
    private String trustStorePassword;
    private boolean trustAllRoots;
    private String colDelimiter;
    private String rowDelimiter;
    private Integer resultSetType;
    private Integer resultSetConcurrency;
    private List<String> sqlCommands;

    public SQLInputs() {
    }

    public String getStrDelim() {
        return strDelim;
    }

    public void setStrDelim(String strDelim) {
        this.strDelim = strDelim;
    }

    public String getStrColumns() {
        return strColumns;
    }

    public void setStrColumns(String strColumns) {
        this.strColumns = strColumns;
    }

    public List<String> getlRows() {
        return lRows;
    }

    public void setlRows(List<String> lRows) {
        this.lRows = lRows;
    }

    public String getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(String ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public int getiUpdateCount() {
        return iUpdateCount;
    }

    public void setiUpdateCount(int iUpdateCount) {
        this.iUpdateCount = iUpdateCount;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStrKeyCol() {
        return strKeyCol;
    }

    public void setStrKeyCol(String strKeyCol) {
        this.strKeyCol = strKeyCol;
    }

    public String getStrKeyFiles() {
        return strKeyFiles;
    }

    public void setStrKeyFiles(String strKeyFiles) {
        this.strKeyFiles = strKeyFiles;
    }

    public String getStrKeyNames() {
        return strKeyNames;
    }

    public void setStrKeyNames(String strKeyNames) {
        this.strKeyNames = strKeyNames;
    }

    public String getStrKeySkip() {
        return strKeySkip;
    }

    public void setStrKeySkip(String strKeySkip) {
        this.strKeySkip = strKeySkip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public List<String> getDbUrls() {
        return dbUrls;
    }

    public void setDbUrls(List<String> dbUrls) {
        this.dbUrls = dbUrls;
    }

    public String getDbClass() {
        return dbClass;
    }

    public void setDbClass(String dbClass) {
        this.dbClass = dbClass;
    }

    public String getTrimRowstat() {
        return trimRowstat;
    }

    public void setTrimRowstat(String trimRowstat) {
        this.trimRowstat = trimRowstat;
    }

    public boolean isNetcool() {
        return isNetcool;
    }

    public void setNetcool(boolean isNetcool) {
        this.isNetcool = isNetcool;
    }

    public List<List<String>> getlRowsFiles() {
        return lRowsFiles;
    }

    public void setlRowsFiles(List<List<String>> lRowsFiles) {
        this.lRowsFiles = lRowsFiles;
    }

    public List<List<String>> getlRowsNames() {
        return lRowsNames;
    }

    public void setlRowsNames(List<List<String>> lRowsNames) {
        this.lRowsNames = lRowsNames;
    }

    public long getSkip() {
        return skip;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public Properties getDatabasePoolingProperties() {
        return databasePoolingProperties;
    }

    public void setDatabasePoolingProperties(Properties databasePoolingProperties) {
        this.databasePoolingProperties = databasePoolingProperties;
    }

    public Integer getResultSetType() {
        return resultSetType;
    }

    public void setResultSetType(Integer resultSetType) {
        this.resultSetType = resultSetType;
    }

    public Integer getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    public void setResultSetConcurrency(Integer resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    public String getWindowsDomain() {
        return windowsDomain;
    }

    public void setWindowsDomain(String windowsDomain) {
        this.windowsDomain = windowsDomain;
    }

    public String getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public boolean getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(boolean trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
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

    public void setSqlCommands(final List<String> sqlCommands) {
        this.sqlCommands = sqlCommands;
    }
}
