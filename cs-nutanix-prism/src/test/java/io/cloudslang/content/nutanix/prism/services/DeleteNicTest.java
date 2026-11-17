package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteNICInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.NicImpl.deleteNic;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteNicTest extends NutanixOperationTestSupport {
    @Test
    void deletesNicWithEncodedMacAddressAndLogicalTimestamp() throws Exception {
        final NutanixDeleteNICInputs inputs = NutanixDeleteNICInputs.builder()
                .vmUUID("vm-123")
                .nicMacAddress("00:11:22:33:44:55")
                .vmLogicalTimestamp("42")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> deleteNic(inputs));

        assertRequest(request, "DELETE",
                "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/nics/00:11:22:33:44:55");
        assertEquals("?vm_logical_timestamp=42", request.getQueryParams());
    }

    @Test
    void omitsLogicalTimestampQueryWhenNotProvided() throws Exception {
        final NutanixDeleteNICInputs inputs = NutanixDeleteNICInputs.builder()
                .vmUUID("vm-123")
                .nicMacAddress("00:11:22:33:44:55")
                .vmLogicalTimestamp("")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> deleteNic(inputs));

        assertEquals("", request.getQueryParams());
    }
}
