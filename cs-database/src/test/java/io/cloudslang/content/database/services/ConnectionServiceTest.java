/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services;

import io.cloudslang.content.database.services.databases.CustomDatabase;
import io.cloudslang.content.database.services.databases.MSSqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static io.cloudslang.content.database.utils.Constants.SQLSERVER_JDBC_DRIVER;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by vranau on 12/10/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnectionManager.class, Properties.class, ConnectionService.class, CustomDatabase.class, MSSqlDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
public class ConnectionServiceTest {

    public static final String CUSTOM_CLASS_DRIVER = "org.h2.Driver";
    public static String CUSTOM_URL = "jdbc:h2:tcp://localhost/~/test";
    private ConnectionService connectionService = new ConnectionService();
    private SQLInputs sqlInputs;

    @Mock
    private DBConnectionManager dbConnectionManagerMock;
    @Mock
    private Connection connectionMock;

    @Rule
    private ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void setUp() throws Exception {
//        dbConnectionManagerMock = mock(DBConnectionManager.class);
//        connectionMock = mock(Connection.class);
    }

    // this method is used in most of the tests. If you comment the code, you could deactivate the (sometimes) failing tests.
    // sometimes it fails to mock the dbConnectionManagerMock and the tests are failing.
    private void assertConnection(SQLInputs sqlInputs, int noUrls, String url) throws SQLException, ClassNotFoundException {
        Connection connection = connectionService.setUpConnection(sqlInputs);
        assertEquals(noUrls, sqlInputs.getDbUrls().size());
        assertEquals(url, sqlInputs.getDbUrls().get(0));
        assertEquals(url, sqlInputs.getDbUrl());
        assertEquals(connectionMock, connection);
    }

    @Before
    public void beforeTest() throws Exception {
        sqlInputs = new SQLInputs();
        InputsProcessor.init(sqlInputs);
        mockStatic(DBConnectionManager.class);
        when(DBConnectionManager.getInstance()).thenReturn(dbConnectionManagerMock);
        when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(String.class), any(String.class), any(String.class), any(Properties.class))).thenReturn(connectionMock);
    }

    @Test
    public void testSetUpConnectionCustom() throws Exception {
        sqlInputs.setDbClass(CUSTOM_CLASS_DRIVER);
        sqlInputs.setDbType(Constants.CUSTOM_DB_TYPE);

        sqlInputs.getDbUrls().add(CUSTOM_URL);
        assertConnection(sqlInputs, 1, CUSTOM_URL);
    }


    @Test
    public void testSetUpConnectionMSSql() throws Exception {
        sqlInputs.setDbClass(SQLSERVER_JDBC_DRIVER);
        sqlInputs.setDbType(Constants.MSSQL_DB_TYPE);
        sqlInputs.setDbPort("1433");
        sqlInputs.setDbServer("dbServer");
        sqlInputs.setAuthenticationType(Constants.AUTH_WINDOWS);
        sqlInputs.setDbName("dbName");
        sqlInputs.setInstance("instance");
        sqlInputs.setTrustAllRoots("True");
        assertConnection(sqlInputs, 1, "jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;integratedSecurity=true;encrypt=true;trustServerCertificate=true");
    }

    @Test
    public void testSetUpConnectionOracle() throws Exception {
        sqlInputs.setDbType(Constants.ORACLE_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        assertConnection(sqlInputs, 2, "jdbc:oracle:thin:@//localhost:30/dbName");
    }

    @Test
    public void testSetUpConnectionSybase() throws Exception {
        sqlInputs.setDbType(Constants.SYBASE_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        assertConnection(sqlInputs, 1, "jdbc:jtds:sybase://localhost:30//dbName;prepareSQL=1;useLOBs=false;TDS=4.2;");
    }

    @Test
    public void testSetUpConnectionDB2() throws Exception {
        sqlInputs.setDbType(Constants.DB2_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        assertConnection(sqlInputs, 1, "jdbc:db2://localhost:30//dbName");
    }

    @Test
    public void testSetUpConnectionNetcool() throws Exception {
        expectedEx.expect(ClassNotFoundException.class);
        sqlInputs.setDbPort("30");
        expectedEx.expectMessage("Could not locate either jconn2.jar or jconn3.jar file in the classpath!");
        sqlInputs.setDbType(Constants.NETCOOL_DB_TYPE);

        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionMySQL() throws Exception {
        sqlInputs.setDbType(Constants.MYSQL_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        assertConnection(sqlInputs, 1, "jdbc:mysql://localhost:30//dbName");
    }

    @Test
    public void testSetUpConnectionEmptyDbType() throws SQLException, ClassNotFoundException {
        checkExpectedSQLException("Invalid database type : null");
        sqlInputs.setDbType(null);
        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionEmptyUrls() throws SQLException, ClassNotFoundException {
        checkExpectedSQLException("No database URL was provided");
        sqlInputs.setDbUrls(null);
        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionEmptySqlInputs() throws SQLException, ClassNotFoundException {
        checkExpectedSQLException("No connection inputs are provided!");
        connectionService.setUpConnection(null);
    }

    @Test
    public void testCloseConnection() throws SQLException {
        connectionService.closeConnection(connectionMock);
        connectionService.closeConnection(null);

        Mockito.verify(connectionMock, Mockito.times(1)).close();
    }

    private void checkExpectedSQLException(String message) {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage(message);
    }

}
