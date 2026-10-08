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

import io.cloudslang.content.database.services.databases.CustomDatabase;
import io.cloudslang.content.database.services.databases.MSSqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;


import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockedStatic;




import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by vranau on 12/10/2014.
 */
@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {

    public static final String CUSTOM_CLASS_DRIVER = "org.h2.Driver";
    public static String CUSTOM_URL = "jdbc:h2:tcp://localhost/~/test";

    private SQLInputs sqlInputs;
    @Spy
    private ConnectionService connectionServiceSpy = new ConnectionService();

    @Mock
    private DBConnectionManager dbConnectionManagerMock;

    @Mock
    private Connection connectionMock;
    private MockedStatic<DBConnectionManager> dbConnectionManager;

    private void assertConnection(SQLInputs sqlInputs, int noUrls, String resultedUrl, String url) throws SQLException, ClassNotFoundException {
        final List<String> sqlConnections = connectionServiceSpy.getConnectionUrls(sqlInputs);
        assertEquals(noUrls, sqlConnections.size());
        assertEquals(resultedUrl, sqlConnections.get(0));
        assertEquals(url, sqlInputs.getDbUrl());

        doReturn(sqlConnections).when(connectionServiceSpy).getConnectionUrls(sqlInputs);
        final Connection connection = connectionServiceSpy.setUpConnection(sqlInputs);
        assertEquals(connectionMock, connection);
    }

    @BeforeEach
    public void beforeTest() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);
        dbConnectionManager = mockStatic(DBConnectionManager.class);
        dbConnectionManager.when(DBConnectionManager::getInstance).thenReturn(dbConnectionManagerMock);
        lenient().when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(), any(), any(), any(), any()))
                .thenReturn(connectionMock);
    }

    @AfterEach
    public void tearDown() {
        dbConnectionManager.close();
    }

    @Test
    public void testSetUpConnectionCustom() throws Exception {
        sqlInputs.setDbClass(CUSTOM_CLASS_DRIVER);
        sqlInputs.setDbType(CUSTOM_DB_TYPE);

        sqlInputs.setDbUrl(CUSTOM_URL);
        assertConnection(sqlInputs, 1, CUSTOM_URL, CUSTOM_URL);
    }

    @Test
    public void testSetUpConnectionMSSql() throws Exception {
        sqlInputs.setDbClass(SQLSERVER_JDBC_DRIVER);
        sqlInputs.setDbType(MSSQL_DB_TYPE);
        sqlInputs.setDbPort(1433);
        sqlInputs.setDbServer("dbServer");
        sqlInputs.setAuthenticationType(Constants.AUTH_SQL);
        sqlInputs.setDbName("dbName");
        sqlInputs.setInstance("instance");
        sqlInputs.setTrustAllRoots(true);
        assertConnection(sqlInputs, 1, "jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;encrypt=true;trustServerCertificate=true", null);
    }

    @Test
    public void testSetUpConnectionOracle() throws Exception {
        sqlInputs.setDbType(ORACLE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("dbName");
        assertConnection(sqlInputs, 2, "jdbc:oracle:thin:@//localhost:30/dbName", null);
    }

    @Test
    public void testSetUpConnectionSybase() throws Exception {
        sqlInputs.setDbType(SYBASE_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("dbName");
        assertConnection(sqlInputs, 1, "jdbc:jtds:sybase://localhost:30/dbName;prepareSQL=1;useLOBs=false;TDS=4.2;", null);
    }

    @Test
    public void testSetUpConnectionDB2() throws Exception {
        sqlInputs.setDbType(DB2_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("dbName");
        assertConnection(sqlInputs, 1, "jdbc:db2://localhost:30/dbName", null);
    }

    @Test
    public void testSetUpConnectionNetcool() throws Exception {
        sqlInputs.setDbPort(30);
        sqlInputs.setDbType(NETCOOL_DB_TYPE);
        sqlInputs.setDbName("");
        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class, () -> connectionServiceSpy.setUpConnection(sqlInputs));
        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("Could not locate either jconn2.jar or jconn3.jar file in the classpath!"));
    }

    @Test
    public void testSetUpConnectionMySQL() throws Exception {
        sqlInputs.setDbType(MYSQL_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("dbName");
        assertConnection(sqlInputs, 1, "jdbc:mysql://localhost:30/dbName?zeroDateTimeBehavior=convertToNull", null);
    }

}
