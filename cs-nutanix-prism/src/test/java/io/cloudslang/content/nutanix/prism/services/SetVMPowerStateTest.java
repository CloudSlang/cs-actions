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
package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixSetVMPowerStateInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.setVMPowerState;

class SetVMPowerStateTest extends NutanixOperationTestSupport {
    @Test
    void sendsPowerTransitionWithHostAndLogicalTimestamp() throws Exception {
        final NutanixSetVMPowerStateInputs inputs = NutanixSetVMPowerStateInputs.builder()
                .vmUUID("vm-123")
                .transition("on")
                .hostUUID("host-1")
                .vmLogicalTimestamp("42")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> setVMPowerState(inputs));

        assertRequest(request, "POST", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/set_power_state");
        assertJsonEquals("{\"transition\":\"on\",\"uuid\":\"vm-123\",\"host_uuid\":\"host-1\"," +
                "\"vm_logical_timestamp\":42}", request.getBody());
    }
}
