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
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.services.DiskImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.detachDisks;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DetachDisksTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-detach-disk\"}",
                "disk-1", "scsi", "1");

        assertTaskSuccess(result, "task-detach-disk");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("404", "{\"message\":\"disk not found\"}",
                "disk-1", "scsi", "1");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("404", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("disk not found"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String diskUUIDs,
                                        String buses, String indexes) {
        return executeWithMockedService(DiskImpl.class,
                service -> service.when(() -> detachDisks(any(NutanixDetachDisksInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new DetachDisks().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        diskUUIDs, buses, indexes,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> detachDisks(any(NutanixDetachDisksInputs.class)), times(1)));
    }
}
