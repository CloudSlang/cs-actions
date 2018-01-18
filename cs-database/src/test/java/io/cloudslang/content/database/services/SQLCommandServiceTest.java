/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

import io.cloudslang.content.database.utils.SQLInputs;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.OracleDbmsOutput;
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

import java.sql.*;


import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 12/11/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionService.class, SQLCommandService.class})
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
    private ConnectionService connectionServiceMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private OracleDbmsOutput oracleDbmsOutputMock;
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
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);
        PowerMockito.whenNew(ConnectionService.class).withNoArguments().thenReturn(connectionServiceMock);
        when(connectionServiceMock.setUpConnection(sqlInputs)).thenReturn(connectionMock);
        when(connectionMock.createStatement(anyInt(), anyInt())).thenReturn(statementMock);
        when(statementMock.getResultSet()).thenReturn(resultSetMock);
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
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
        PowerMockito.whenNew(OracleDbmsOutput.class).withArguments(connectionMock).thenReturn(oracleDbmsOutputMock);
        when(connectionMock.prepareStatement(Matchers.any(String.class))).thenReturn(preparedStatementMock);
        when(preparedStatementMock.getUpdateCount()).thenReturn(1);
        when(oracleDbmsOutputMock.getOutput()).thenReturn("Command completed successfully");

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
        verify(oracleDbmsOutputMock, Mockito.times(1)).getOutput();
        verify(oracleDbmsOutputMock, Mockito.times(1)).close();
    }

}
