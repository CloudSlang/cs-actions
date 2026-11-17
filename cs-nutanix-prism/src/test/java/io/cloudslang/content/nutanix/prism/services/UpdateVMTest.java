package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixUpdateVMInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.updateVM;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.updateVMBody;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateVMTest extends NutanixOperationTestSupport {
    @Test
    void updatesNameMemoryCpuAffinityAndAgentVM() throws Exception {
        final NutanixUpdateVMInputs inputs = NutanixUpdateVMInputs.builder()
                .vmUUID("vm-123")
                .vmName("renamed")
                .description("updated")
                .vmMemorySize("16")
                .numVCPUs("8")
                .numCoresPerVCPU("2")
                .timeZone("UTC")
                .hostUUIDs("host-1,host-2")
                .agentVM("true")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> updateVM(inputs));

        assertRequest(request, "PUT", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123");
        assertJsonEquals("{\"name\":\"renamed\",\"description\":\"updated\",\"memory_mb\":16384," +
                "\"num_vcpus\":8,\"num_cores_per_vcpu\":2,\"timezone\":\"UTC\",\"vm_features\":" +
                "{\"agent_VM\":true},\"affinity\":{\"policy\":\"AFFINITY\",\"host_uuids\":" +
                "[\"host-1\",\"host-2\"]}}", request.getBody());
    }

    @Test
    void omitsUnspecifiedSizingAndAffinityFields() throws Exception {
        final NutanixUpdateVMInputs inputs = NutanixUpdateVMInputs.builder()
                .vmUUID("vm-123")
                .vmName("renamed")
                .description("updated")
                .commonInputs(commonInputs)
                .build();

        assertEquals("{\"name\":\"renamed\",\"description\":\"updated\"}", updateVMBody(inputs, ","));
    }
}
