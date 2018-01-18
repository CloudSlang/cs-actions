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

package io.cloudslang.content.database.services.dbconnection;

import com.mchange.v2.c3p0.DataSources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.sql.DataSource;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBInputNames.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataSources.class)
public class C3P0PooledDataSourceProviderTest {

    private C3P0PooledDataSourceProvider provider;

    /**
     * Will execute before each test.
     */
    @Before
    public void setUp() {
        Properties propsMock = mock(Properties.class);
        provider = new C3P0PooledDataSourceProvider(propsMock);
    }

    /**
     * Will execute after each test.
     */
    @After
    public void tearDown() {
        provider = null;
    }

    /**
     * Test openPoolesDataSource(...) method.
     *
     * @throws Exception
     */
    @Test
    public void testOpenPooledDataSource() throws Exception {
        PowerMockito.mockStatic(DataSources.class);
        DataSource unPooledDSMock = mock(DataSource.class);
        DataSource retPooledDSMock = mock(DataSource.class);
        PowerMockito.doReturn(unPooledDSMock).when(DataSources.class, "unpooledDataSource"
                , anyString(), anyString(), anyString());

        PowerMockito.doReturn(retPooledDSMock).when(DataSources.class, "pooledDataSource"
                , any(DataSource.class), anyMap());

        assertEquals(retPooledDSMock, provider.openPooledDataSource(DBConnectionManager.DBType.MYSQL
                , "url", USERNAME, "password"));
        //Call PowerMockito.verifyStatic() to start verifying behavior
        PowerMockito.verifyStatic();
        //Use EasyMock-like semantic to verify behavior:
        DataSources.unpooledDataSource(anyString(), anyString(), anyString());
        DataSources.pooledDataSource(any(DataSource.class), anyMap());
    }
}
