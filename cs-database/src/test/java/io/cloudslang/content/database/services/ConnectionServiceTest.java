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

import io.cloudslang.content.database.services.databases.CustomDatabase;
import io.cloudslang.content.database.services.databases.MSSqlDatabase;
import io.cloudslang.content.database.services.dbconnection.DBConnectionManager;
import io.cloudslang.content.database.utils.Constants;
import io.cloudslang.content.database.utils.InputsProcessor;
import io.cloudslang.content.database.utils.SQLInputs;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBOtherValues.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by vranau on 12/10/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBConnectionManager.class, Properties.class, ConnectionService.class, CustomDatabase.class, MSSqlDatabase.class})
@PowerMockIgnore({"javax.management.*", "org.apache.commons.logging.*"})
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

    @Rule
    private ExpectedException expectedEx = ExpectedException.none();

    private void assertConnection(SQLInputs sqlInputs, int noUrls, String resultedUrl, String url) throws SQLException, ClassNotFoundException {
        final List<String> sqlConnections = connectionServiceSpy.getConnectionUrls(sqlInputs);
        assertEquals(noUrls, sqlConnections.size());
        assertEquals(resultedUrl, sqlConnections.get(0));
        assertEquals(url, sqlInputs.getDbUrl());

        doReturn(sqlConnections).when(connectionServiceSpy).getConnectionUrls(sqlInputs);
        final Connection connection = connectionServiceSpy.setUpConnection(sqlInputs);
        assertEquals(connectionMock, connection);
    }

    @Before
    public void beforeTest() throws Exception {
        sqlInputs = SQLInputs.builder().build();
        InputsProcessor.init(sqlInputs);
        mockStatic(DBConnectionManager.class);

        PowerMockito.mockStatic(MSSqlDatabase.class);

        doCallRealMethod().when(MSSqlDatabase.class, "addSslEncryptionToConnection", anyBoolean(), anyString(), anyString(), anyString());

        doNothing().when(MSSqlDatabase.class, "loadWindowsAuthentication", anyString());

        when(DBConnectionManager.getInstance()).thenReturn(dbConnectionManagerMock);
        when(dbConnectionManagerMock.getConnection(any(DBConnectionManager.DBType.class), any(String.class), any(String.class), any(String.class), any(String.class), any(Properties.class))).thenReturn(connectionMock);
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
        sqlInputs.setAuthenticationType(Constants.AUTH_WINDOWS);
        sqlInputs.setDbName("dbName");
        sqlInputs.setInstance("instance");
        sqlInputs.setTrustAllRoots(true);
        assertConnection(sqlInputs, 1, "jdbc:sqlserver://dbServer:1433;DatabaseName=dbName;instance=instance;integratedSecurity=true;encrypt=true;trustServerCertificate=true", null);
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
        expectedEx.expect(RuntimeException.class);
        sqlInputs.setDbPort(30);
        expectedEx.expectMessage("Could not locate either jconn2.jar or jconn3.jar file in the classpath!");
        sqlInputs.setDbType(NETCOOL_DB_TYPE);
        sqlInputs.setDbName("");
        connectionServiceSpy.setUpConnection(sqlInputs);
    }

    @Test
    public void testSetUpConnectionMySQL() throws Exception {
        sqlInputs.setDbType(MYSQL_DB_TYPE);
        sqlInputs.setDbPort(30);
        sqlInputs.setDbServer("localhost");
        sqlInputs.setDbName("dbName");
        assertConnection(sqlInputs, 1, "jdbc:mysql://localhost:30/dbName", null);
    }

}
