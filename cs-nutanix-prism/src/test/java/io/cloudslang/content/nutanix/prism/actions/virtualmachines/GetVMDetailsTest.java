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
import io.cloudslang.content.nutanix.prism.entities.NutanixGetVMDetailsInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.getVMDetails;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.GetVMDetailsOutputs.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class GetVMDetailsTest extends NutanixActionTestSupport {
    @Test
    void mapsVMFieldsFromSuccessfulResponse() {
        final Map<String, String> result = execute("200",
                "{\"name\":\"demo\",\"vm_nics\":[{\"ip_address\":\"10.0.0.3\"," +
                        "\"mac_address\":\"00:11:22:33:44:55\"}],\"power_state\":\"ON\"," +
                        "\"vm_disk_info\":[{\"disk_address\":{\"vmdisk_uuid\":\"disk-1\"}," +
                        "\"storage_container_uuid\":\"container-1\"}],\"vm_logical_timestamp\":12," +
                        "\"affinity\":{\"host_uuids\":[\"host-1\"]}}");

        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("demo", result.get(VM_NAME));
        assertEquals("10.0.0.3", result.get(IP_ADDRESS));
        assertEquals("00:11:22:33:44:55", result.get(MAC_ADDRESS));
        assertEquals("ON", result.get(POWER_STATE));
        assertEquals("disk-1", result.get(VM_DISK_UUID));
        assertEquals("container-1", result.get(STORAGE_CONTAINER_UUID));
        assertEquals("12", result.get(VM_LOGICAL_TIMESTAMP));
        assertEquals("host-1", result.get(HOST_UUIDS));
    }

    @Test
    void mapsNotFoundAPIResponseToFailure() {
        final Map<String, String> result = execute("404", "{\"message\":\"missing VM\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("404", result.get(STATUS_CODE));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> getVMDetails(any(NutanixGetVMDetailsInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new GetVMDetails().execute(
                        "prism-host", null, "username", "password", "vm-1", null, null,
                        null, null, null, null, null, null, null, null, null, null,
                        null, null, null, null),
                service -> service.verify(() -> getVMDetails(any(NutanixGetVMDetailsInputs.class)), times(1)));
    }
}
