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
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.NicImpl.AddNic;

class AddNicTest extends NutanixOperationTestSupport {
    @Test
    void addsNicWithNetworkAndAddressConfiguration() throws Exception {
        final NutanixAddNicInputs inputs = NutanixAddNicInputs.builder()
                .vmUUID("vm-123")
                .networkUUID("network-1")
                .requestedIPAddress("10.0.0.5")
                .isConnected("true")
                .vlanId("123")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> AddNic(inputs));

        assertRequest(request, "POST", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/nics");
        assertJsonEquals("{\"spec_list\":[{\"is_connected\":true,\"requested_ip_address\":\"10.0.0.5\"," +
                "\"vlan_id\":\"123\",\"network_uuid\":\"network-1\"}]}", request.getBody());
    }

    @Test
    void serializesMultipleNICSpecificationsInOrder() throws Exception {
        final NutanixAddNicInputs inputs = NutanixAddNicInputs.builder()
                .vmUUID("vm-123")
                .networkUUID("network-1,network-2")
                .requestedIPAddress("10.0.0.5,10.0.0.6")
                .isConnected("true,false")
                .vlanId("123,456")
                .commonInputs(commonInputs)
                .build();

        final String body = NicImpl.AddNicBody(inputs);

        assertJsonEquals("{\"spec_list\":[" +
                "{\"is_connected\":true,\"requested_ip_address\":\"10.0.0.5\",\"vlan_id\":\"123\"," +
                "\"network_uuid\":\"network-1\"}," +
                "{\"is_connected\":false,\"requested_ip_address\":\"10.0.0.6\",\"vlan_id\":\"456\"," +
                "\"network_uuid\":\"network-2\"}]}", body);
    }
}
