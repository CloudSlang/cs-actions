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
package io.cloudslang.content.nutanix.prism.actions.disks;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDiskInputs;
import io.cloudslang.content.nutanix.prism.services.DiskImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.updateDisk;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

class UpdateDiskTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-update-disk\"}");

        assertTaskSuccess(result, "task-update-disk");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("409", "{\"message\":\"disk update failed\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("409", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("disk update failed"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void mapsServiceExceptionToActionFailure() {
        final Map<String, String> result = executeWithMockedService(DiskImpl.class,
                service -> service.when(() -> updateDisk(any(NutanixUpdateDiskInputs.class)))
                        .thenThrow(new IllegalStateException("connection failed")),
                this::executeAction,
                service -> service.verify(() -> updateDisk(any(NutanixUpdateDiskInputs.class)), times(1)));

        assertEquals("-1", result.get(RETURN_CODE));
        assertTrue(result.get(EXCEPTION).contains("connection failed"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void rejectsInvalidCommonInputBeforeCallingService() {
        final Map<String, String> result = executeWithoutServiceCall(DiskImpl.class,
                () -> new UpdateDisk().execute(
                        "prism-host", null, "username", "password", "vm-1", "disk-1",
                        "scsi", "1", "container-1", "10",
                        null, null, "invalid-port", null, null, null, null, null, null,
                        null, null, null, null, null),
                service -> service.verify(() -> updateDisk(any(NutanixUpdateDiskInputs.class)), never()));

        assertTrue(result.get(EXCEPTION).contains("proxyPort"));
    }

    @Test
    void mapsMissingTaskUUIDToActionFailure() {
        final Map<String, String> result = execute("200", "{}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(DiskImpl.class,
                service -> service.when(() -> updateDisk(any(NutanixUpdateDiskInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                this::executeAction,
                service -> service.verify(() -> updateDisk(any(NutanixUpdateDiskInputs.class)), times(1)));
    }

    private Map<String, String> executeAction() {
        return new UpdateDisk().execute(
                "prism-host", null, "username", "password", "vm-1", "disk-1",
                "scsi", "1", "container-1", "10",
                null, null, null, null, null, null, null, null, null,
                null, null, null, null, null);
    }
}
