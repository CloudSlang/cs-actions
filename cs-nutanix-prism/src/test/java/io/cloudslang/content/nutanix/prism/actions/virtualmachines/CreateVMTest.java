/*
 * Copyright 2026-2027 Open Text
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
package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateVMInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.createVM;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class CreateVMTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("201", "{\"task_uuid\":\"task-create\"}");

        assertTaskSuccess(result, "task-create");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("400", "{\"message\":\"invalid VM settings\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("400", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("invalid VM settings"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void mapsMissingTaskUUIDToActionFailure() {
        final Map<String, String> result = execute("201", "{}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> createVM(any(NutanixCreateVMInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new CreateVM().execute(
                        "prism-host", null, "username", "password",
                        "demo", "test VM", "8", "4", "2", null, null,
                        null, null, null, "false", "true", "scsi", null,
                        "0", null, null, null, null, null, "container-1",
                        "10", "network-1", null, null, null, null,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> createVM(any(NutanixCreateVMInputs.class)), times(1)));
    }
}
