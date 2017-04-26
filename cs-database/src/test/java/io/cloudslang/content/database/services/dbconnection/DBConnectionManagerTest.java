/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.dbconnection;

import com.mchange.v2.c3p0.PooledDataSource;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager.DBType;
import io.cloudslang.content.database.utils.TripleDES;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by victor on 10.01.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TripleDES.class)
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
public class DBConnectionManagerTest {

    private static final String DHARMA_PASSWORD = "dharma_password";
    private static final String DHARMA_USER = "dharma_user";
    private static final String EMPTY_STRING = "";
    private static final String DB_URL = "dbUrl";
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    private static final String ENCRYPTED_PASS = "encryptedPass";
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private DBConnectionManager dbcManager;
    private DBType aDbType;

    /**
     * Will execute before each test.
     */
    @Before
    public void setUp() {
        aDbType = null;
    }

    /**
     * Will execute after each test.
     */
    @After
    public void tearDown() {
        aDbType = null;
        dbcManager = null;
        DBConnectionManager.instance = null;
    }

    /**
     * Test class constructor whith enabled pooling.
     */
    @Test
    public void testDBConnectionManagerConstructor() {
        Properties dbPoolingPropertiesMock = mock(Properties.class);
        when(dbPoolingPropertiesMock.getProperty(any(String.class), any(String.class)))
                .thenReturn(TRUE)
                .thenReturn("1");

        //uses singleton
        dbcManager = DBConnectionManager.getInstance();
        assertNotNull(dbcManager);
        assertEquals(false, dbcManager.isPoolingEnabled);
        assertNull(dbcManager.dbmsPoolTable);
    }

    /**
     * Test class constructor whith disabled pooling.
     */
    @Test
    public void testDBConnectionManagerConstructor2() {
        Properties dbPoolingPropertiesMock = mock(Properties.class);
        when(dbPoolingPropertiesMock.getProperty(any(String.class), any(String.class)))
                .thenReturn(FALSE);

        //uses singleton
        DBConnectionManager dbcManager = DBConnectionManager.getInstance();
        assertNotNull(dbcManager);
        assertFalse(dbcManager.isPoolingEnabled);
        assertNull(dbcManager.dbmsPoolTable);
    }

    /**
     * Test the 'destructor'.
     * Purpose of this method: when pooling is enbaled everything is cleaned up.
     *
     * @throws Exception
     */
    @Test
    public void testFinalize() throws Exception {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dbcManagerSpy).getPooledConnection(any(DBType.class)
                , any(String.class), any(String.class), any(String.class));
        dbcManagerSpy.getConnection(DBType.DB2, DB_URL, DHARMA_USER, DHARMA_PASSWORD, getPoolingProperties());

        doNothing().when(dbcManagerSpy).shutdownDbmsPools();

        dbcManagerSpy.finalize();
        //since isPoolingEnabled equals true then the method below should be called
        verify(dbcManagerSpy, times(1)).shutdownDbmsPools();
    }

    /**
     * Test getConnection(...) method with a null DbUrl.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithNullDbUrl() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();

        exception.expect(SQLException.class);
        exception.expectMessage("Failed to check out connection dbUrl is empty");
        dbcManagerSpy.getConnection(aDbType, EMPTY_STRING, DHARMA_USER, DHARMA_PASSWORD, null);
    }

    /**
     * Test getConnection(...) method with a null username.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithNullUsername() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();

        exception.expect(SQLException.class);
        exception.expectMessage("Failed to check out connection,username is empty. dburl = " + DB_URL);
        dbcManagerSpy.getConnection(aDbType, DB_URL, EMPTY_STRING, DHARMA_PASSWORD, null);
    }

    /**
     * Test getConnection(...) method with a null password.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithNullPassword() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();

        exception.expect(SQLException.class);
        exception.expectMessage("Failed to check out connection, password is empty. username = "
                + DHARMA_USER + " dbUrl = " + DB_URL);
        dbcManagerSpy.getConnection(aDbType, DB_URL, DHARMA_USER, EMPTY_STRING, null);
    }

    /**
     * Test getConnection(...) method with a null DbType.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithNullDbType() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();

        exception.expect(SQLException.class);
        exception.expectMessage("Failed to check out connection db type is null");
        dbcManagerSpy.getConnection(null, DB_URL, DHARMA_USER, DHARMA_PASSWORD, getPoolingProperties());
    }

    /**
     * Test getConnection(...) method with pooling enabled.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithPooling() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        aDbType = DBType.MYSQL;
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dbcManagerSpy).getPooledConnection(any(DBType.class)
                , any(String.class), any(String.class), any(String.class));

        Connection connection = dbcManagerSpy.getConnection(aDbType, DB_URL, DHARMA_USER, DHARMA_PASSWORD, getPoolingProperties());
        verify(dbcManagerSpy, times(1)).getPooledConnection(any(DBType.class)
                , any(String.class), any(String.class), any(String.class));
        assertEquals(connMock, connection);
    }

    /**
     * Test getConnection(...) method without pooling.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionWithoutPooling() throws SQLException {
        Properties dbPoolingPropertiesMock = mock(Properties.class);
        when(dbPoolingPropertiesMock.getProperty(any(String.class), any(String.class)))
                .thenReturn(FALSE);
        DBConnectionManager dbcManagerSpy = spy(DBConnectionManager.getInstance());
        aDbType = DBType.MYSQL;
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dbcManagerSpy).getPlainConnection(anyString(), anyString(), anyString());

        Connection connection = dbcManagerSpy.getConnection(aDbType, DB_URL, DHARMA_USER, DHARMA_PASSWORD, dbPoolingPropertiesMock);
        verify(dbcManagerSpy, times(1)).getPlainConnection(anyString(), anyString(), anyString());
        assertEquals(connMock, connection);
    }

    /**
     * Test cleanDataSources().
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testCleanDataSources() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        Hashtable<String, Hashtable<String, DataSource>> dbmsPoolTable = getHashTableObject1(dataSourceMock);

        Hashtable<String, PooledDataSourceProvider> providerTable = new Hashtable<>();
        PooledDataSourceProvider dataSourceProviderMock = mock(PooledDataSourceProvider.class);

        doNothing().when(dataSourceProviderMock).closePooledDataSource(any(DataSource.class));
        providerTable.put(C3P0PooledDataSourceProvider.C3P0_DATASOURCE_PROVIDER_NAME, dataSourceProviderMock);
        dbcManagerSpy.providerTable = providerTable;
        dbcManagerSpy.dbmsPoolTable = dbmsPoolTable;

        dbcManagerSpy.cleanDataSources();
        verify(dataSourceMock, times(1)).getNumConnectionsAllUsers();
        verify(dataSourceProviderMock, times(1)).closePooledDataSource(any(DataSource.class));
        assertTrue(dbcManagerSpy.dbmsPoolTable.isEmpty());
    }

    /**
     * Test shutdownDbmsPools().
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testShutDownDbmsPools() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        dbcManagerSpy.dbmsPoolTable = getHashTableObject1(dataSourceMock);
        Hashtable<String, PooledDataSourceProvider> providerTable = new Hashtable<>();
        PooledDataSourceProvider dataSourceProviderMock = mock(PooledDataSourceProvider.class);
        doNothing().when(dataSourceProviderMock).closePooledDataSource(any(DataSource.class));
        providerTable.put(C3P0PooledDataSourceProvider.C3P0_DATASOURCE_PROVIDER_NAME, dataSourceProviderMock);
        dbcManagerSpy.providerTable = providerTable;
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dbcManagerSpy).getPooledConnection(any(DBType.class)
                , any(String.class), any(String.class), any(String.class));
        dbcManagerSpy.getConnection(DBType.DB2, DB_URL, DHARMA_USER, DHARMA_PASSWORD, getPoolingProperties());

        dbcManagerSpy.shutdownDbmsPools();
        verify(dataSourceProviderMock, times(1)).closePooledDataSource(any(DataSource.class));
        assertNull(dbcManagerSpy.dbmsPoolTable);
    }

    /**
     * Test method getPropBooleanValue(...).
     */
    @Test
    public void testGetPropBooleanValue() {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        Properties propsMock = mock(Properties.class);
        doReturn(TRUE).when(propsMock).getProperty(anyString(), anyString());
        dbcManagerSpy.dbPoolingProperties = propsMock;

        assertTrue(dbcManagerSpy.getPropBooleanValue("db.pooling.enable", FALSE));
    }

    /**
     * Test default value of getPropBooleanValue(...).
     */
    @Test
    public void testGetPropBooleanValueReturnsDefaultValue() {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        Properties propsMock = mock(Properties.class);
        doReturn("invalidBooleanValue").when(propsMock).getProperty(any(String.class), any(String.class));
        dbcManagerSpy.dbPoolingProperties = propsMock;

        assertFalse(dbcManagerSpy.getPropBooleanValue("db.pooling.enable", FALSE));
    }

    /**
     * Test method getPropIntValue(...).
     */
    @Test
    public void testGetPropIntValue() {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        Properties propsMock = mock(Properties.class);
        doReturn("8000").when(propsMock).getProperty(anyString(), anyString());
        dbcManagerSpy.dbPoolingProperties = propsMock;

        assertEquals(8000, dbcManagerSpy.getPropIntValue("db.datasource.clean.interval", "7200"));
    }

    /**
     * Test method getPooledConnection(...).
     *
     * @throws Exception
     */
    @Test
    public void testGetPooledConnection() throws Exception {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dataSourceMock).getConnection();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject2(dataSourceMock, connMock);

        PowerMockito.mockStatic(TripleDES.class);
        Mockito.when(TripleDES.encryptPassword(any(String.class))).thenReturn(ENCRYPTED_PASS);

        assertEquals(connMock, dbcManagerSpy.getPooledConnection(DBType.MYSQL, DB_URL, DHARMA_USER, DHARMA_PASSWORD));
        verify(dataSourceMock, times(1)).getConnection();
    }

    /**
     * Test method getPooledConnection(...) throws encryption failed exception.
     *
     * @throws Exception
     */
    @Test
    public void testGetPooledConnectionThrowsEncryptionException() throws Exception {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        Connection connMock = mock(Connection.class);
        Hashtable<String, Hashtable<String, DataSource>> dbmsPoolTable = getHashTableObject2(dataSourceMock, connMock);
        dbcManagerSpy.dbmsPoolTable = dbmsPoolTable;
        PowerMockito.mockStatic(TripleDES.class);
        Mockito.when(TripleDES.encryptPassword(any(String.class))).thenThrow(new Exception("encryption failed"));

        exception.expect(Exception.class);
        exception.expectMessage("Failed to encrypt password for key = ");
        dbcManagerSpy.getPooledConnection(DBType.MYSQL, DB_URL, DHARMA_USER, DHARMA_PASSWORD);
    }

    /**
     * Test method getPooledConnection(...) with no DataSource.
     *
     * @throws Exception
     */
    @Test
    public void testGetPooledConnectionWithNoDataSource() throws Exception {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        Connection connMock = mock(Connection.class);
        doReturn(connMock).when(dataSourceMock).getConnection();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject2(dataSourceMock, connMock);
        doReturn(dataSourceMock).when(dbcManagerSpy).createDataSource(any(DBType.class)
                , anyString(), anyString(), anyString(), any(Hashtable.class));

        PowerMockito.mockStatic(TripleDES.class);
        Mockito.when(TripleDES.encryptPassword(any(String.class))).thenReturn(ENCRYPTED_PASS);

        assertEquals(connMock, dbcManagerSpy.getPooledConnection(DBType.MYSQL, DB_URL, DHARMA_USER, DHARMA_PASSWORD));
        verify(dataSourceMock, times(1)).getConnection();
        verify(dbcManagerSpy.createDataSource(any(DBType.class)
                , anyString(), anyString(), anyString(), any(Hashtable.class)), times(1));
    }

    /**
     * Test method getConnectionSize(...).
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        doReturn(10).when(providerMock).getAllConnectionNumber(any(DataSource.class));
        doReturn(providerMock).when(providerTableMock).get(anyString());
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getConnectionSize(DBType.MYSQL, DB_URL));
        verifyNumberOfInvocationsOnMockObjects(providerTableMock, providerMock);
    }

    /**
     * Test method getCheckedOutConnectionSize(...).
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetCheckedOutConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        setUpPooledDataSourceProviderMockForCheckedOutConnectionNumber(providerTableMock, providerMock);
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getCheckedOutConnectionSize(DBType.MYSQL, DB_URL));
        verifyNumberOfInvocationsOnMockObjects3(providerTableMock, providerMock);
    }

    /**
     * Test method getCheckedInConnectionSize(...).
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetCheckedInConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        setUpPooledDataSourceProviderMockForCheckedInConnectionNumber(providerTableMock, providerMock);
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getCheckedInConnectionSize(DBType.MYSQL, DB_URL));
        verifyNumberOfInvocationsOnMockObject2(providerTableMock, providerMock);
    }

    /**
     * Test method getTotalConnectionSize().
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetTotalConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        doReturn(10).when(providerMock).getAllConnectionNumber(any(DataSource.class));
        doReturn(providerMock).when(providerTableMock).get(anyString());
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getTotalConnectionSize());
        verifyNumberOfInvocationsOnMockObjects(providerTableMock, providerMock);
    }

    /**
     * Test method getTotalCheckedOutConnectionSize().
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetTotalCheckedOutConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        setUpPooledDataSourceProviderMockForCheckedOutConnectionNumber(providerTableMock, providerMock);
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getTotalCheckedOutConnectionSize());
        verifyNumberOfInvocationsOnMockObjects3(providerTableMock, providerMock);
    }

    /**
     * Test method getTotalCheckedInConnectionSize().
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testGetTotalCheckedInConnectionSize() throws SQLException {
        DBConnectionManager dbcManagerSpy = getDBConnectionManagerSpyWithPooling();
        dbcManagerSpy.dbmsPoolTable = getHashTableObject3();
        Hashtable<String, PooledDataSourceProvider> providerTableMock = mock(Hashtable.class);
        PooledDataSourceProvider providerMock = mock(PooledDataSourceProvider.class);
        setUpPooledDataSourceProviderMockForCheckedInConnectionNumber(providerTableMock, providerMock);
        dbcManagerSpy.providerTable = providerTableMock;

        assertEquals(10, dbcManagerSpy.getTotalCheckedInConnectionSize());
        verifyNumberOfInvocationsOnMockObject2(providerTableMock, providerMock);
    }

    /**
     * Common setUp for PooledDataSourceProvider Mock object.
     *
     * @param providerTableMock
     * @param providerMock
     * @throws java.sql.SQLException
     */
    private void setUpPooledDataSourceProviderMockForCheckedInConnectionNumber(Hashtable<String, PooledDataSourceProvider> providerTableMock, PooledDataSourceProvider providerMock) throws SQLException {
        doReturn(10).when(providerMock).getCheckedInConnectionNumber(any(DataSource.class));
        doReturn(providerMock).when(providerTableMock).get(anyString());
    }

    /**
     * Common setUp for PooledDataSourceProvider Mock object.
     *
     * @param providerTableMock
     * @param providerMock
     * @throws java.sql.SQLException
     */
    private void setUpPooledDataSourceProviderMockForCheckedOutConnectionNumber(Hashtable<String, PooledDataSourceProvider> providerTableMock, PooledDataSourceProvider providerMock) throws SQLException {
        doReturn(10).when(providerMock).getCheckedOutConnectionNumber(any(DataSource.class));
        doReturn(providerMock).when(providerTableMock).get(anyString());
    }

    /**
     * Common method for verifying invocations on Mock objects.
     *
     * @param providerTableMock
     * @param providerMock
     * @throws java.sql.SQLException
     */
    private void verifyNumberOfInvocationsOnMockObjects(Hashtable<String, PooledDataSourceProvider> providerTableMock, PooledDataSourceProvider providerMock) throws SQLException {
        verify(providerMock, times(1)).getAllConnectionNumber(any(DataSource.class));
        verify(providerTableMock, times(1)).get(anyString());
    }

    /**
     * Common method for verifying invocations on Mock objects.
     *
     * @param providerTableMock
     * @param providerMock
     * @throws java.sql.SQLException
     */
    private void verifyNumberOfInvocationsOnMockObjects3(Hashtable<String, PooledDataSourceProvider> providerTableMock, PooledDataSourceProvider providerMock) throws SQLException {
        verify(providerMock, times(1)).getCheckedOutConnectionNumber(any(DataSource.class));
        verify(providerTableMock, times(1)).get(anyString());
    }

    /**
     * Common method for verifying invocations on Mock objects.
     *
     * @param providerTableMock
     * @param providerMock
     * @throws java.sql.SQLException
     */
    private void verifyNumberOfInvocationsOnMockObject2(Hashtable<String, PooledDataSourceProvider> providerTableMock, PooledDataSourceProvider providerMock) throws SQLException {
        verify(providerMock, times(1)).getCheckedInConnectionNumber(any(DataSource.class));
        verify(providerTableMock, times(1)).get(anyString());
    }

    /**
     * Method for configuring a DBConnection Spy object.
     *
     * @return
     */
    private DBConnectionManager getDBConnectionManagerSpyWithPooling() {
        return spy(DBConnectionManager.getInstance());
    }

    private Properties getPoolingProperties() {
        Properties dbPoolingPropertiesMock = new Properties();
        dbPoolingPropertiesMock.put("db.pooling.enable", "true");
        return dbPoolingPropertiesMock;
    }

    private Hashtable getHashTableObject1(PooledDataSource dataSourceMock) throws SQLException {
        Hashtable<String, Hashtable<String, DataSource>> dbmsPoolTable
                = new Hashtable<String, Hashtable<String, DataSource>>();
        String tableKey1 = "key1";
        Hashtable<String, DataSource> hashTable1 = new Hashtable<String, DataSource>();
        doReturn(0).when(dataSourceMock).getNumConnectionsAllUsers();
        hashTable1.put(tableKey1, dataSourceMock);
        dbmsPoolTable.put(tableKey1, hashTable1);
        return dbmsPoolTable;
    }

    private Hashtable<String, Hashtable<String, DataSource>> getHashTableObject2(PooledDataSource dataSourceMock, Connection connMock) throws SQLException {
        Hashtable<String, Hashtable<String, DataSource>> dbmsPoolTable = new Hashtable<>();
        String dsTableKey = DB_URL + "." + DHARMA_USER + "." + ENCRYPTED_PASS;
        String tableKey1 = DBType.MYSQL + "." + DB_URL;
        Hashtable<String, DataSource> hashTable1 = new Hashtable<String, DataSource>();
        doReturn(connMock).when(dataSourceMock).getConnection();
        hashTable1.put(dsTableKey, dataSourceMock);
        dbmsPoolTable.put(tableKey1, hashTable1);
        return dbmsPoolTable;
    }

    private Hashtable<String, Hashtable<String, DataSource>> getHashTableObject3() {
        Hashtable<String, Hashtable<String, DataSource>> dbmsPoolTable
                = new Hashtable<String, Hashtable<String, DataSource>>();
        Hashtable<String, DataSource> hashTable = new Hashtable<String, DataSource>();
        PooledDataSource dataSourceMock = mock(PooledDataSource.class);
        String dbmsPoolKey = DBType.MYSQL + "." + DB_URL;
        hashTable.put("key", dataSourceMock);
        dbmsPoolTable.put(dbmsPoolKey, hashTable);
        return dbmsPoolTable;
    }
}