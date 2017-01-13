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

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLInputs {
    protected String sqlCommand;
    protected String dbServer;
    protected String dbName;
    protected String dbPort;
    protected String dbType;
    protected String tnsPath;
    protected String tnsEntry;
    protected String key;
    protected String strKey;
    protected String strKeyCol;
    protected String strKeyFiles;
    protected String strKeyNames;
    protected String strKeySkip;
    protected String username;
    protected String password;
    protected String authenticationType;
    protected String instance;
    protected String ignoreCase;
    protected int timeout;
    protected String dbUrl;  //the final dbUrl that was used to connect to the database
    protected List<String> dbUrls = null; //a list of dbUrls that are used to try to connect to the database
    protected String dbClass;
    protected String trimRowstat = "true";
    protected boolean isNetcool = false; // needs to be visible for SQLQueryTabular to do check
    protected List<String> lFiles = null;
    protected List<ArrayList<String>> lRowsFiles = new ArrayList<ArrayList<String>>();
    protected List<String> lNames = null;
    protected List<ArrayList<String>> lRowsNames = new ArrayList<ArrayList<String>>();
    protected long skip = 0L;
    private String strDelim;
    private String strColumns;
    private ArrayList<String> lRows;
    private int iQuerys;
    private int iUpdateCount;
    private Connection connection;
    private Statement stmt;
    private Properties databasePoolingProperties = null;
    private String windowsDomain;
    private String trustStore;
    private String trustStorePassword;
    private String trustAllRoots;

    private OOResultSet resultSetType;
    private OOResultSet resultSetConcurrency;

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

    public ArrayList<String> getlRows() {
        return lRows;
    }

    public void setlRows(ArrayList<String> lRows) {
        this.lRows = lRows;
    }

    public int getiQuerys() {
        return iQuerys;
    }

    public void setiQuerys(int iQuerys) {
        this.iQuerys = iQuerys;
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

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getTnsPath() {
        return tnsPath;
    }

    public void setTnsPath(String tnsPath) {
        this.tnsPath = tnsPath;
    }

    public String getTnsEntry() {
        return tnsEntry;
    }

    public void setTnsEntry(String tnsEntry) {
        this.tnsEntry = tnsEntry;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
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

    public List<String> getlFiles() {
        return lFiles;
    }

    public void setlFiles(List<String> lFiles) {
        this.lFiles = lFiles;
    }

    public List<ArrayList<String>> getlRowsFiles() {
        return lRowsFiles;
    }

    public void setlRowsFiles(List<ArrayList<String>> lRowsFiles) {
        this.lRowsFiles = lRowsFiles;
    }

    public List<String> getlNames() {
        return lNames;
    }

    public void setlNames(List<String> lNames) {
        this.lNames = lNames;
    }

    public List<ArrayList<String>> getlRowsNames() {
        return lRowsNames;
    }

    public void setlRowsNames(List<ArrayList<String>> lRowsNames) {
        this.lRowsNames = lRowsNames;
    }

    public long getSkip() {
        return skip;
    }

    public void setSkip(long skip) {
        this.skip = skip;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Properties getDatabasePoolingProperties() {
        return databasePoolingProperties;
    }

    public void setDatabasePoolingProperties(Properties databasePoolingProperties) {
        this.databasePoolingProperties = databasePoolingProperties;
    }

    public OOResultSet getResultSetType() {
        return resultSetType;
    }

    public void setResultSetType(OOResultSet resultSetType) {
        this.resultSetType = resultSetType;
    }

    public OOResultSet getResultSetConcurrency() {
        return resultSetConcurrency;
    }

    public void setResultSetConcurrency(OOResultSet resultSetConcurrency) {
        this.resultSetConcurrency = resultSetConcurrency;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
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

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(String trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }
}
