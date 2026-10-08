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

import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;






import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by vranau on 12/10/2014.
 */
public class OracleDatabaseTest {
    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String DB_SERVER = "localhost";
    public static final int DB_PORT = 30;
    public static final String DB_NAME = "testDB";
    private OracleDatabase oracleDatabase = null;

    @BeforeEach
    public void setUp() {
        oracleDatabase = new OracleDatabase();
    }

    @Test
    public void testSetUpConnectionOracleWithoutTns() throws Exception {
        //set up method needs a SLASH before dbName (if dbName is provided)
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName(DB_NAME);
        sqlInputs.setDbServer(DB_SERVER);
        sqlInputs.setDbPort(DB_PORT);
//        sqlInputs.setDbUrls(new ArrayList<String>());
        final List<String> dbUrls = oracleDatabase.setUp(sqlInputs);
        assertEquals(2, dbUrls.size());
        assertEquals(ORACLE_URL + "//" + DB_SERVER + ":" + DB_PORT + "/" + DB_NAME, dbUrls.get(0));
        assertEquals(ORACLE_URL + DB_SERVER + ":" + DB_PORT + ":" + DB_NAME, dbUrls.get(1));
    }

}
