/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.services.TaskImpl.getTaskDetailsURL;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TaskImplTest.class)
public class TaskImplTest {
    private static final String EXPECTED_GET_TASK_DETAILS_PATH = "https://myhost:9440/api/nutanix/v2.0/tasks/1234";

    private final NutanixGetTaskDetailsInputs nutanixGetTaskDetailsInputs = NutanixGetTaskDetailsInputs.builder()
            .taskUUID("1234")
            .includeSubtasksInfo("")
            .commonInputs(NutanixCommonInputs.builder()
                    .hostname("myhost")
                    .port("9440")
                    .username("username")
                    .password("password")
                    .apiVersion("v2.0")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .trustAllRoots("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .connectTimeout("")
                    .socketTimeout("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .build()).build();

    @Test
    public void getTaskDetailsPathTest() throws Exception {
        final String path = getTaskDetailsURL(nutanixGetTaskDetailsInputs);
        assertEquals(EXPECTED_GET_TASK_DETAILS_PATH, path);
    }
}
