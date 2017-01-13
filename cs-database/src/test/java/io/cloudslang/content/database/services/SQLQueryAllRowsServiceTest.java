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

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.Format;
import io.cloudslang.content.database.utils.InputsProcessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionService.class, SQLQueryAllRowsService.class, Format.class})
public class SQLQueryAllRowsServiceTest {

    private static final String COL_DELIMITER = ",";
    private static final String ROW_DELIMITER = "|";
    private static final String SQL_QUERY = "select * from dbTable";
    private static final int QUYERY_TIMEOUT = 10;
    private SQLQueryAllRowsService sqlQueryAllRowsService = new SQLQueryAllRowsService();
    private SQLInputs sqlInputs;

    @Mock
    private ConnectionService connectionServiceMock;
    @Mock
    private Connection connectionMock;

    @Mock
    private Statement statementMock;
    @Rule
    private ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ResultSetMetaData resultSetMetadataMock;

    @Before
    public void setUp() throws Exception {
        sqlInputs = new SQLInputs();
        InputsProcessor.init(sqlInputs);
        PowerMockito.whenNew(ConnectionService.class).withNoArguments().thenReturn(connectionServiceMock);
        when(connectionServiceMock.setUpConnection(sqlInputs)).thenReturn(connectionMock);
        when(connectionMock.createStatement(Matchers.any(Integer.class), Matchers.any(Integer.class))).thenReturn(statementMock);
        when(statementMock.executeQuery(SQL_QUERY)).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
    }

    @Test
    public void testExecuteQueryAllRows() throws Exception {
        sqlInputs.setDbType(Constants.ORACLE_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        final String execQueryAllRows = sqlQueryAllRowsService.execQueryAllRows(sqlInputs, COL_DELIMITER, ROW_DELIMITER);

        assertEquals("", execQueryAllRows);
        verify(connectionServiceMock, Mockito.times(1)).closeConnection(connectionMock);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

    @Test
    public void testExecuteQueryAllRowsIsNetcool() throws Exception {
        sqlInputs.setDbType(Constants.ORACLE_DB_TYPE);
        sqlInputs.setDbPort("30");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setNetcool(true);
        final String execQueryAllRows = sqlQueryAllRowsService.execQueryAllRows(sqlInputs, COL_DELIMITER, ROW_DELIMITER);

        assertEquals("", execQueryAllRows);
        verify(connectionServiceMock, Mockito.times(1)).closeConnection(connectionMock);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

    @Test
    public void testExecuteQueryAllRowsPSql() throws Exception {
        sqlInputs.setDbType(Constants.POSTGRES_DB_TYPE);
        sqlInputs.setDbPort("5432");
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setNetcool(true);
        final String execQueryAllRows = sqlQueryAllRowsService.execQueryAllRows(sqlInputs, COL_DELIMITER, ROW_DELIMITER);

        assertEquals("", execQueryAllRows);
        verify(connectionServiceMock, Mockito.times(1)).closeConnection(connectionMock);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

    @Test
    public void testExecuteQueryAllRowsNoCommand() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("command input is empty.");
        sqlQueryAllRowsService.execQueryAllRows(sqlInputs, COL_DELIMITER, ROW_DELIMITER);
    }
}
