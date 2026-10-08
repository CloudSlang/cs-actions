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
import io.cloudslang.content.database.utils.Format;
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


import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static io.cloudslang.content.database.constants.DBOtherValues.POSTGRES_DB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@ExtendWith(MockitoExtension.class)
public class SQLQueryAllRowsServiceTest {

    private static final String SQL_QUERY = "select * from dbTable";
    private static final int QUYERY_TIMEOUT = 10;
    private SQLInputs sqlInputs;

    @Mock
    private Connection connectionMock;

    @Mock
    private Statement statementMock;

    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ResultSetMetaData resultSetMetadataMock;
    private MockedConstruction<ConnectionService> connectionServices;

    @BeforeEach
    public void setUp() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);
        connectionServices = Mockito.mockConstruction(ConnectionService.class,
                (mock, context) -> Mockito.lenient().when(mock.setUpConnection(sqlInputs)).thenReturn(connectionMock));
        Mockito.lenient().when(connectionMock.createStatement(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class))).thenReturn(statementMock);
        Mockito.lenient().when(statementMock.executeQuery(SQL_QUERY)).thenReturn(resultSetMock);
        Mockito.lenient().when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
    }

    @AfterEach
    public void tearDown() {
        connectionServices.close();
    }

    @Test
    public void testExecuteQueryAllRows() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setRowDelimiter(",");
        sqlInputs.setColDelimiter("|");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        final String execQueryAllRows = SQLQueryAllRowsService.execQueryAllRows(sqlInputs);

        assertEquals("", execQueryAllRows);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

    @Test
    public void testExecuteQueryAllRowsIsNetcool() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setRowDelimiter(",");
        sqlInputs.setColDelimiter("|");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setNetcool(true);
        final String execQueryAllRows = SQLQueryAllRowsService.execQueryAllRows(sqlInputs);

        assertEquals("", execQueryAllRows);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

    @Test
    public void testExecuteQueryAllRowsPSql() throws Exception {
        sqlInputs.setDbType(POSTGRES_DB_TYPE);
        sqlInputs.setDbPort(5432);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setRowDelimiter(",");
        sqlInputs.setColDelimiter("|");
        sqlInputs.setTimeout(QUYERY_TIMEOUT);
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setNetcool(true);
        final String execQueryAllRows = SQLQueryAllRowsService.execQueryAllRows(sqlInputs);

        assertEquals("", execQueryAllRows);
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
        verify(resultSetMock, Mockito.times(1)).close();
    }

}
