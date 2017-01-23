/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.services.ConnectionService;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBDefaultValues.ORACLE_DB_TYPE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by vranau on 12/9/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnectionManager.class, Properties.class, ConnectionService.class, OracleDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
public class ConnectionServiceTestOracle {


    public static final String TNS_PATH = "tnsPath";
    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String TNS_ENTRY = "tnsEntry";
    public static final String DB_SERVER = "localhost";
    public static final String DB_PORT = "30";
    public static final String DB_NAME = "testDB";
    private ConnectionService connectionService;
    private SQLInputs sqlInputs;

    @Mock
    private DBConnectionManager dbConnectionManagerMock;

    @Mock
    private Connection connectionMock;

    @Rule
    private ExpectedException expectedEx = ExpectedException.none();
    @Mock
    private File tnsPathMock;
    @Mock
    private File tnsFileOraMock;


    @Before
    public void beforeTest() throws Exception {
        sqlInputs = new SQLInputs();
        connectionService = new ConnectionService();
        InputsProcessor.init(sqlInputs);

        mockStatic(DBConnectionManager.class);
        when(DBConnectionManager.getInstance()).thenReturn(dbConnectionManagerMock);
        PowerMockito.when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(String.class), any(String.class), any(String.class), any(Properties.class))).thenReturn(connectionMock);

    }

    @After
    public void tearDown() throws Exception {
        if (connectionMock != null) {
            connectionMock.close();
        }
    }

    @Test
    public void testSetUpConnectionOracleEmptyTnsPath() throws Exception {
        checkExpectedSQLException("Empty TNSPath for Oracle.");
        populateOracle();
        sqlInputs.setTnsPath(null);
        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionOracleNotExistingTnsPath() throws Exception {
        checkExpectedSQLException("Invalid TNSPath for Oracle : not existing");
        populateOracle();
        sqlInputs.setTnsPath("not existing");
        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionOracleNotExistingTnsFile() throws Exception {
        populateOracle();

        checkExpectedSQLException("Failed to find tnsnames.ora file from :" + TNS_PATH);

        whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
        when(tnsPathMock.exists()).thenReturn(true);
        when(tnsPathMock.isDirectory()).thenReturn(true);

        whenNew(File.class).withArguments(TNS_PATH + File.separator + "tnsnames.ora").thenReturn(tnsFileOraMock);
        when(tnsFileOraMock.exists()).thenReturn(false);
        connectionService.setUpConnection(sqlInputs);
    }

    private void checkExpectedSQLException(String message) {
        expectedEx.expect(SQLException.class);
        expectedEx.expectMessage(message);
    }

    @Test
    public void testSetUpConnectionOracleNotDirectory() throws Exception {
        populateOracle();
        whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
        when(tnsPathMock.exists()).thenReturn(true);
        checkExpectedSQLException("Invalid TNSPath for Oracle, TNSPath is not a directory: " + TNS_PATH);
        connectionService.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionOracleWithTns() throws Exception {

        String originalTnsPath = System.getProperty("oracle.net.tns_admin");
        try {

            populateOracle();
            sqlInputs.setTnsEntry(TNS_ENTRY);

            whenNew(File.class).withArguments(TNS_PATH).thenReturn(tnsPathMock);
            when(tnsPathMock.exists()).thenReturn(true);
            when(tnsPathMock.isDirectory()).thenReturn(true);

            whenNew(File.class).withArguments(TNS_PATH + File.separator + "tnsnames.ora").thenReturn(tnsFileOraMock);
            when(tnsFileOraMock.exists()).thenReturn(true);

            final Connection connection = connectionService.setUpConnection(sqlInputs);
            assertEquals(1, sqlInputs.getDbUrls().size());
            assertEquals(ORACLE_URL + TNS_ENTRY, sqlInputs.getDbUrls().get(0));
            assertEquals(ORACLE_URL + TNS_ENTRY, sqlInputs.getDbUrl());
            assertEquals(connectionMock, connection);
            assertEquals(TNS_PATH, System.getProperty("oracle.net.tns_admin"));

        } finally {
            if (originalTnsPath != null) {
                System.setProperty("oracle.net.tns_admin", originalTnsPath);
            } else {
                System.setProperty("oracle.net.tns_admin", "");
            }
        }
    }

    @Test
    public void testSetUpConnectionOracleWithoutTns() throws Exception {
        populateOracle();
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
        sqlInputs.setDbName(DB_NAME);
        final Connection connection = connectionService.setUpConnection(sqlInputs);
        assertEquals(2, sqlInputs.getDbUrls().size());
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, sqlInputs.getDbUrls().get(0));
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, sqlInputs.getDbUrl());
        assertEquals(ORACLE_URL + DB_SERVER + ":" + DB_PORT + ":" + DB_NAME, sqlInputs.getDbUrls().get(1));
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, sqlInputs.getDbUrl());
        assertEquals(connectionMock, connection);
    }

    private void populateOracle() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setTnsPath(TNS_PATH);
    }

}
