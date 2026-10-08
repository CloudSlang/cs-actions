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
import io.cloudslang.content.database.utils.SQLInputsUtils;
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


import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getResultSetConcurrency;
import static io.cloudslang.content.database.utils.SQLInputsUtils.getResultSetType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@ExtendWith(MockitoExtension.class)
public class SQLQueryServiceTest {

    private static final int SQL_QUERY_TIMEOUT = 10;
    private static final String SQL_QUERY = "select * from dbTable";
    private static final java.lang.Integer COLUMN_COUNT = 3;
    private static final String DEFAUL_LABEL = "defaulLabel";
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
        Mockito.lenient().when(resultSetMetadataMock.getColumnCount()).thenReturn(COLUMN_COUNT);
        Mockito.lenient().when(resultSetMetadataMock.getColumnLabel(ArgumentMatchers.any(Integer.class))).thenReturn(DEFAUL_LABEL);
    }

    @AfterEach
    public void tearDown() {
        connectionServices.close();
    }

    @Test
    public void testExecuteSqlQuery() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setTimeout(SQL_QUERY_TIMEOUT);

        SQLQueryService.executeSqlQuery(sqlInputs);

        assertEquals("defaulLabel,defaulLabel,defaulLabel", sqlInputs.getStrColumns());
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(SQL_QUERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
    }

    @Test
    public void testExecuteSqlQueryPSQLLocal() throws Exception {
        sqlInputs.setDbType(POSTGRES_DB_TYPE);
        sqlInputs.setDbPort(5432);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setSqlCommand(SQL_QUERY);
        sqlInputs.setResultSetType(getResultSetType(TYPE_SCROLL_INSENSITIVE));
        sqlInputs.setResultSetConcurrency(getResultSetConcurrency(CONCUR_READ_ONLY));
        sqlInputs.setTimeout(SQL_QUERY_TIMEOUT);

        SQLQueryService.executeSqlQuery(sqlInputs);

        assertEquals("defaulLabel,defaulLabel,defaulLabel", sqlInputs.getStrColumns());
        verify(connectionMock, Mockito.times(1)).setReadOnly(true);
        verify(statementMock, Mockito.times(1)).setQueryTimeout(SQL_QUERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).executeQuery(SQL_QUERY);
    }

    @Test
    public void testExecuteSqlQueryNoCommand() throws Exception {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                Exception.class, () -> SQLQueryService.executeSqlQuery(sqlInputs));
        assertEquals("command input is empty.", exception.getMessage());
    }
}
