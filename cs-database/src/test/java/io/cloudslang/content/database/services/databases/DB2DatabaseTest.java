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

import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by vranau on 12/10/2014.
 */
public class DB2DatabaseTest {

    public static final String DB_NAME = "dbName";
    public static final String DB_SERVER = "dbServer";
    public static final int DB_PORT = 30;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testSetUpNoDbName() throws ClassNotFoundException, SQLException {
        DB2Database db2Database = new DB2Database();
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(EMPTY);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        final List<String> dbUrls = db2Database.setUp(sqlInputs);
        assertEquals("jdbc:db2://dbServer:30", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }

    @Test
    public void testSetUpNoServerName() throws ClassNotFoundException, SQLException {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("host   not valid");
        DB2Database db2Database = new DB2Database();

        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(null);
        sqlInputs.setDbPort(DB_PORT);


        db2Database.setUp(sqlInputs);
    }

    @Test
    public void testSetUpAll() throws ClassNotFoundException, SQLException {
        DB2Database db2Database = new DB2Database();

        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());

        final List<String> dbUrls = db2Database.setUp(sqlInputs);

        assertEquals("jdbc:db2://dbServer:30/dbName", dbUrls.get(0));
        assertEquals(1, dbUrls.size());
    }
}
