/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.database.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockedConstruction;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;


import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@ExtendWith(MockitoExtension.class)
public class SQLScriptServiceTest {

    private static final int QUYERY_TIMEOUT = 10;
    public static final String SQL_COMMAND = "select * from dbTable";
    private SQLInputs sqlInputs;

    @Mock
    private Connection connectionMock;

    @Mock
    private Statement statementMock;

    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ResultSetMetaData resultSetMetadataMock;
    private ArrayList<String> lines;
    private MockedConstruction<ConnectionService> connectionServices;

    @BeforeEach
    public void setUp() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        lines = new ArrayList<>();
        lines.add(SQL_COMMAND);
        InputsProcessor.init(sqlInputs);
        connectionServices = Mockito.mockConstruction(ConnectionService.class,
                (mock, context) -> Mockito.lenient().when(mock.setUpConnection(sqlInputs)).thenReturn(connectionMock));
        Mockito.lenient().when(connectionMock.createStatement(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class))).thenReturn(statementMock);
        Mockito.lenient().when(connectionMock.getAutoCommit()).thenReturn(true);
        Mockito.lenient().when(statementMock.executeQuery(SQL_COMMAND)).thenReturn(resultSetMock);
        Mockito.lenient().when(statementMock.executeBatch()).thenReturn(new int[]{1,2});
    }

    @AfterEach
    public void tearDown() {
        connectionServices.close();
    }

    @Test
    public void testExecuteSqlScript() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);

        SQLScriptService.executeSqlScript(lines, sqlInputs);

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

        SQLScriptService.executeSqlScript(lines, sqlInputs);

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
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> SQLScriptService.executeSqlScript(null, sqlInputs));
        org.junit.jupiter.api.Assertions.assertEquals("No SQL command to be executed.", exception.getMessage());
    }

    @Test
    public void testExecuteSqlScriptEmptyLines() throws Exception {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> SQLScriptService.executeSqlScript(new ArrayList<String>(), sqlInputs));
        org.junit.jupiter.api.Assertions.assertEquals("No SQL command to be executed.", exception.getMessage());
    }
}
