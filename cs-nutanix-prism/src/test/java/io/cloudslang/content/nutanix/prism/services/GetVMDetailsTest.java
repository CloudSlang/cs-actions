package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetVMDetailsInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.getVMDetails;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetVMDetailsTest extends NutanixOperationTestSupport {
    @Test
    void sendsGetRequestWithDiskAndNicOptions() throws Exception {
        final NutanixGetVMDetailsInputs inputs = NutanixGetVMDetailsInputs.builder()
                .vmUUID("vm-123")
                .includeVMDiskConfigInfo("true")
                .includeVMNicConfigInfo("false")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> getVMDetails(inputs));

        assertRequest(request, "GET", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123");
        assertEquals("include_vm_disk_config=true&include_vm_nic_config=false", request.getQueryParams());
    }
}
