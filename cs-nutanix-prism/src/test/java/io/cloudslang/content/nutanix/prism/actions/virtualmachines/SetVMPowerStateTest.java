package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixSetVMPowerStateInputs;
import io.cloudslang.content.nutanix.prism.services.VMImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.VMImpl.setVMPowerState;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class SetVMPowerStateTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-power\"}", "on", "host-1", "42");

        assertTaskSuccess(result, "task-power");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("400", "{\"message\":\"invalid transition\"}",
                "invalid", "", "");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("400", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("invalid transition"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String transition,
                                        String hostUUID, String timestamp) {
        return executeWithMockedService(VMImpl.class,
                service -> service.when(() -> setVMPowerState(any(NutanixSetVMPowerStateInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new SetVMPowerState().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        transition, hostUUID, timestamp, null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null),
                service -> service.verify(() -> setVMPowerState(any(NutanixSetVMPowerStateInputs.class)),
                        times(1)));
    }
}
