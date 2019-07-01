/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.database.services.databases;

import io.cloudslang.content.database.services.ConnectionService;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.FORWARD_SLASH;
import static io.cloudslang.content.database.constants.DBOtherValues.ORACLE_DB_TYPE;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by vranau on 12/9/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnectionManager.class, Properties.class, ConnectionService.class, OracleDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
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

    @Rule
    private ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void beforeTest() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);

        mockStatic(DBConnectionManager.class);
        when(DBConnectionManager.getInstance()).thenReturn(dbConnectionManagerMock);
        PowerMockito.when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class))).thenReturn(connectionMock);

    }

    @After
    public void tearDown() throws Exception {
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
