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
import static org.mockito.Mockito.*;

import io.cloudslang.content.database.utils.SQLInputs;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by vranau on 12/10/2014.
 */

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
public class NetcoolDatabaseTest {

    @Spy
    private NetcoolDatabase netcoolDatabase = new NetcoolDatabase();
    @Test
    public void testSetUpInvalid() throws Exception {
        final SQLInputs sqlInputs = SQLInputs.builder().build();
        sqlInputs.setDbName("dbName");
        sqlInputs.setDbServer(null);
        sqlInputs.setDbPort(30);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> netcoolDatabase.setUp(sqlInputs));
        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("Could not locate either jconn2.jar or jconn3.jar file in the classpath"));
    }

    @Test
    public void testSetUpValid() throws Exception {

//todo
//        final SQLInputs sqlInputs = SQLInputs.builder().build();
//        sqlInputs.setDbName("dbName");
//        sqlInputs.setDbServer(null);
//        sqlInputs.setDbPort(30);
//
//        Class cls = any(Class.class);
//
//        given(Class.forName("com.sybase.jdbc3.jdbc.SybDriver")).willReturn(cls);
//
//        netcoolDatabase.setUp(sqlInputs);

    }
}
