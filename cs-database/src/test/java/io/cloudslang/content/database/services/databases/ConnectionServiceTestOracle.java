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

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.cloudslang.content.database.services.ConnectionService;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockedStatic;




import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.FORWARD_SLASH;
import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by vranau on 12/9/2014.
 */
@ExtendWith(MockitoExtension.class)
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
    private MockedStatic<DBConnectionManager> dbConnectionManager;

    @BeforeEach
    public void beforeTest() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);

        dbConnectionManager = mockStatic(DBConnectionManager.class);
        dbConnectionManager.when(DBConnectionManager::getInstance).thenReturn(dbConnectionManagerMock);
        when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class))).thenReturn(connectionMock);

    }

    @AfterEach
    public void tearDown() throws Exception {
        dbConnectionManager.close();
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
