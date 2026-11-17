package io.cloudslang.content.nutanix.prism.actions.nics;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import io.cloudslang.content.nutanix.prism.services.NicImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.NicImpl.AddNic;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class AddNicTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-add-nic\"}");

        assertTaskSuccess(result, "task-add-nic");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("400", "{\"message\":\"network not found\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("400", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("network not found"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(NicImpl.class,
                service -> service.when(() -> AddNic(any(NutanixAddNicInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new AddNic().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        "network-1", "10.0.0.5", "123", "true",
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> AddNic(any(NutanixAddNicInputs.class)), times(1)));
    }
}
