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




package io.cloudslang.content.database.services.dbconnection;

import com.mchange.v2.c3p0.DataSources;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;




import javax.sql.DataSource;
import java.util.Properties;

import static io.cloudslang.content.database.constants.DBInputNames.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class C3P0PooledDataSourceProviderTest {

    private C3P0PooledDataSourceProvider provider;

    /**
     * Will execute before each test.
     */
    @BeforeEach
    public void setUp() {
        Properties propsMock = mock(Properties.class);
        provider = new C3P0PooledDataSourceProvider(propsMock);
    }

    /**
     * Will execute after each test.
     */
    @AfterEach
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
        try (MockedStatic<DataSources> dataSources = mockStatic(DataSources.class)) {
            DataSource unPooledDSMock = mock(DataSource.class);
            DataSource retPooledDSMock = mock(DataSource.class);
            dataSources.when(() -> DataSources.unpooledDataSource(anyString(), anyString(), anyString()))
                    .thenReturn(unPooledDSMock);
            dataSources.when(() -> DataSources.pooledDataSource(any(DataSource.class), anyMap()))
                    .thenReturn(retPooledDSMock);

            assertEquals(retPooledDSMock, provider.openPooledDataSource(DBConnectionManager.DBType.MYSQL
                    , "url", USERNAME, "password"));
            dataSources.verify(() -> DataSources.unpooledDataSource(anyString(), anyString(), anyString()));
            dataSources.verify(() -> DataSources.pooledDataSource(any(DataSource.class), anyMap()));
        }
    }
}
