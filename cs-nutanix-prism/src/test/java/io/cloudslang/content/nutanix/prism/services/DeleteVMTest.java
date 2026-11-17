package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteVMInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.deleteVM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteVMTest extends NutanixOperationTestSupport {
    @Test
    void deletesVMWithSnapshotAndLogicalTimestampOptions() throws Exception {
        final NutanixDeleteVMInputs inputs = NutanixDeleteVMInputs.builder()
                .vmUUID("vm-123")
                .deleteSnapshots("true")
                .logicalTimestamp("88")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> deleteVM(inputs));

        assertRequest(request, "DELETE", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123");
        assertEquals("delete_snapshots=true&logical_timestamp=88", request.getQueryParams());
    }

    @Test
    void omitsDeleteOptionsWhenLogicalTimestampIsEmpty() throws Exception {
        final NutanixDeleteVMInputs inputs = NutanixDeleteVMInputs.builder()
                .vmUUID("vm-123")
                .deleteSnapshots("false")
                .logicalTimestamp("")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> deleteVM(inputs));

        assertEquals("", request.getQueryParams());
    }
}
