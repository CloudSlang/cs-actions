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
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.OracleDbmsOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockedConstruction;



import java.sql.*;


import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@ExtendWith(MockitoExtension.class)
public class SQLCommandServiceTest {

    private static final int QUYERY_TIMEOUT = 10;
    private static final String SQL_COMMAND = "select * from dbTable";

    private static final String SQL_COMMAND_DBMS_OUTPUT = "DECLARE\n" +
                                                         "a number(10)  :=10;\n" +
                                                         "BEGIN\n" +
                                                         "dbms_output.enable();\n" +
                                                         "dbms_output.put_line(a) ;\n" +
                                                         "dbms_output.put_line('Hello World ! ')  ;\n" +
                                                         "END ;";
    private SQLInputs sqlInputs;

    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private Statement statementMock;

    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ResultSetMetaData resultSetMetadataMock;
    private MockedConstruction<ConnectionService> connectionServices;
    private MockedConstruction<OracleDbmsOutput> dbmsOutputs;

    @BeforeEach
    public void setUp() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);
        connectionServices = Mockito.mockConstruction(ConnectionService.class,
                (mock, context) -> Mockito.lenient().when(mock.setUpConnection(sqlInputs)).thenReturn(connectionMock));
        dbmsOutputs = Mockito.mockConstruction(OracleDbmsOutput.class,
                (mock, context) -> Mockito.lenient().when(mock.getOutput()).thenReturn("Command completed successfully"));
        Mockito.lenient().when(connectionMock.createStatement(anyInt(), anyInt())).thenReturn(statementMock);
        Mockito.lenient().when(statementMock.getResultSet()).thenReturn(resultSetMock);
        Mockito.lenient().when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
    }

    @AfterEach
    public void tearDown() {
        dbmsOutputs.close();
        connectionServices.close();
    }

    @Test
    public void testExecuteSqlCommand() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setSqlCommand(SQL_COMMAND);
        sqlInputs.setTimeout(QUYERY_TIMEOUT);

        final String executeSqlCommand = SQLCommandService.executeSqlCommand(sqlInputs);

        assertEquals("Command completed successfully", executeSqlCommand);
        verify(connectionMock, Mockito.times(1)).setReadOnly(false);
        verify(resultSetMock, Mockito.times(1)).close();
        verify(statementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(statementMock, Mockito.times(1)).execute(SQL_COMMAND);
    }

    @Test
    public void testExecuteSqlCommandDBMS_OUTPUT() throws Exception {
        when(connectionMock.prepareStatement(ArgumentMatchers.any(String.class))).thenReturn(preparedStatementMock);
        when(preparedStatementMock.getUpdateCount()).thenReturn(1);

        sqlInputs.setDbType(ORACLE_DB_TYPE);

        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("/dbName");
        sqlInputs.setSqlCommand(SQL_COMMAND_DBMS_OUTPUT);
        sqlInputs.setTimeout(QUYERY_TIMEOUT);

        final String executeSqlCommand = SQLCommandService.executeSqlCommand(sqlInputs);

        assertEquals("Command completed successfully", executeSqlCommand);
        verify(connectionMock, Mockito.times(1)).setReadOnly(false);
        verify(preparedStatementMock, Mockito.times(1)).close();
        verify(preparedStatementMock, Mockito.times(1)).setQueryTimeout(QUYERY_TIMEOUT);
        verify(preparedStatementMock, Mockito.times(1)).executeQuery();
        verify(dbmsOutputs.constructed().get(0), Mockito.times(1)).getOutput();
        verify(dbmsOutputs.constructed().get(0), Mockito.times(1)).close();
    }

}
