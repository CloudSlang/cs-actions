package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteVMInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.deleteVM;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DeleteVMTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-delete\"}", "true", "42");

        assertTaskSuccess(result, "task-delete");
    }

    @Test
    void mapsNotFoundAPIResponseToFailure() {
        final Map<String, String> result = execute("404", "{\"message\":\"missing VM\"}", null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("404", result.get(STATUS_CODE));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String deleteSnapshots,
                                        String logicalTimestamp) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> deleteVM(any(NutanixDeleteVMInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new DeleteVM().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        deleteSnapshots, logicalTimestamp, null, null, null, null, null, null,
                        null, null, null, null, null, null, null, null),
                service -> service.verify(() -> deleteVM(any(NutanixDeleteVMInputs.class)), times(1)));
    }
}
