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
import java.util.ArrayList;


import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionService.class, SQLScriptService.class})
public class SQLScriptServiceTest {

    private static final int QUYERY_TIMEOUT = 10;
    public static final String SQL_COMMAND = "select * from dbTable";
    private static final Integer COLUMN_COUNT = 3;
    public static final String DEFAUL_LABEL = "defaulLabel";
    SQLScriptService sqlScriptService = new SQLScriptService();
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
    private ArrayList<String> lines;

    @Before
    public void setUp() throws Exception {
        sqlInputs = new SQLInputs();
        lines = new ArrayList<>();
        lines.add(SQL_COMMAND);
        InputsProcessor.init(sqlInputs);
        PowerMockito.whenNew(ConnectionService.class).withNoArguments().thenReturn(connectionServiceMock);
        when(connectionServiceMock.setUpConnection(sqlInputs)).thenReturn(connectionMock);
        when(connectionMock.createStatement(Matchers.any(Integer.class), Matchers.any(Integer.class))).thenReturn(statementMock);
        when(connectionMock.getAutoCommit()).thenReturn(true);
        when(statementMock.executeQuery(SQL_COMMAND)).thenReturn(resultSetMock);
        when(statementMock.executeBatch()).thenReturn(new int[]{1,2});
    }

    @Test
    public void testExecuteSqlScript() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);

        sqlScriptService.executeSqlScript(lines, sqlInputs);

        verify(connectionMock, Mockito.times(1)).setReadOnly(false);
        verify(connectionMock, Mockito.times(1)).commit();
        verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
        verify(connectionMock, Mockito.times(1)).setAutoCommit(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).execute(SQL_COMMAND);
        verify(statementMock, Mockito.times(0)).executeBatch();
    }

    @Test
    public void testExecuteSqlScriptTwoLines() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        lines.add(SQL_COMMAND);

        sqlScriptService.executeSqlScript(lines, sqlInputs);

        verify(connectionMock, Mockito.times(1)).setReadOnly(false);
        verify(connectionMock, Mockito.times(1)).commit();
        verify(connectionMock, Mockito.times(1)).setAutoCommit(false);
        verify(connectionMock, Mockito.times(1)).setAutoCommit(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeBatch();
        verify(statementMock, Mockito.times(0)).execute(SQL_COMMAND);
    }

    @Test
    public void testExecuteSqlScriptNullLines() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("No SQL command to be executed.");
        sqlScriptService.executeSqlScript(null, sqlInputs);
    }

    @Test
    public void testExecuteSqlScriptEmptyLines() throws Exception {
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("No SQL command to be executed.");
        sqlScriptService.executeSqlScript(new ArrayList<String>(), sqlInputs);
    }
}
