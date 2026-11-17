package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateVMInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.createVM;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class CreateVMTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("201", "{\"task_uuid\":\"task-create\"}");

        assertTaskSuccess(result, "task-create");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("400", "{\"message\":\"invalid VM settings\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("400", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("invalid VM settings"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void mapsMissingTaskUUIDToActionFailure() {
        final Map<String, String> result = execute("201", "{}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> createVM(any(NutanixCreateVMInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new CreateVM().execute(
                        "prism-host", null, "username", "password",
                        "demo", "test VM", "8", "4", "2", null, null,
                        null, null, null, "false", "true", "scsi", null,
                        "0", null, null, null, null, null, "container-1",
                        "10", "network-1", null, null, null, null,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> createVM(any(NutanixCreateVMInputs.class)), times(1)));
    }
}
