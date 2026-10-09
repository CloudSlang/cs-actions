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
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PooledDataSourceCleanerTest {

    private long interval = 0;

    private DBConnectionManager aManager;
    private PooledDataSourceCleaner cleaner;

    /**
     * Will execute before each test.
     */
    @BeforeEach
    public void setUp() {
        aManager = mock(DBConnectionManager.class);
        cleaner = new PooledDataSourceCleaner(aManager, interval);
    }

    /**
     * Will execute after each test.
     */
    @AfterEach
    public void tearDown() {
        aManager = null;
        cleaner = null;
    }

    /**
     * Test the void run() method with mocking of static void method.
     *
     * @throws Exception
     */
    @Test
    public void testRun() throws Exception {
        doNothing().when(aManager).cleanDataSources();
        doReturn(0).when(aManager).getDbmsPoolSize();
        cleaner.run();
        verify(aManager, times(1)).cleanDataSources();
        verify(aManager, times(1)).getDbmsPoolSize();
    }
}