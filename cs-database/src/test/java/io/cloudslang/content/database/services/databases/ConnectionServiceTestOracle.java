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


package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.services.ConnectionService;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by vranau on 12/9/2014.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ConnectionServiceTestOracle {


    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String DB_SERVER = "localhost";
    public static final int DB_PORT = 30;
    public static final String DB_NAME = "testDB";
    @Spy
    private ConnectionService connectionServiceSpy = new ConnectionService();
    private SQLInputs sqlInputs;

    @Mock
    private DBConnectionManager dbConnectionManagerMock;

    @Mock
    private Connection connectionMock;
    private MockedStatic<DBConnectionManager> dbConnectionManagerStatic;

    @BeforeEach
    public void beforeTest() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);

        dbConnectionManagerStatic = org.mockito.Mockito.mockStatic(DBConnectionManager.class);
        dbConnectionManagerStatic.when(DBConnectionManager::getInstance).thenReturn(dbConnectionManagerMock);
        when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(Properties.class))).thenReturn(connectionMock);

    }

    @AfterEach
    public void tearDown() throws Exception {
        dbConnectionManagerStatic.close();
        if (connectionMock != null) {
            connectionMock.close();
        }
    }


    @Test
    public void testSetUpConnectionOracleWithoutTns() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
        sqlInputs.setDbName(DB_NAME);

        final List<String> sqlConnections = connectionServiceSpy.getConnectionUrls(sqlInputs);

        assertEquals(2, sqlConnections.size());
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, sqlConnections.get(0));
        assertEquals(ORACLE_URL + DB_SERVER + ":" + DB_PORT + ":" + DB_NAME, sqlConnections.get(1));

        doReturn(sqlConnections).when(connectionServiceSpy).getConnectionUrls(sqlInputs);
        final Connection connection = connectionServiceSpy.setUpConnection(sqlInputs);

        assertEquals(connectionMock, connection);
    }

}
