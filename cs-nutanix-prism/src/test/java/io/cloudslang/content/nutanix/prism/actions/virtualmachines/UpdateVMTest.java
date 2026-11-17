package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixUpdateVMInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.updateVM;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class UpdateVMTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("200", "{\"task_uuid\":\"task-update\"}");

        assertTaskSuccess(result, "task-update");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("500", "{\"message\":\"update failed\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("500", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("update failed"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> updateVM(any(NutanixUpdateVMInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new UpdateVM().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        "renamed", "updated", "16", "8", "2", "UTC", "",
                        "true", null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null),
                service -> service.verify(() -> updateVM(any(NutanixUpdateVMInputs.class)), times(1)));
    }
}
