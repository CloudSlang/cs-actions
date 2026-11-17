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
