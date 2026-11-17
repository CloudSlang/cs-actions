package io.cloudslang.content.nutanix.prism.actions.disks;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.services.DiskImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.detachDisks;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DetachDisksTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-detach-disk\"}",
                "disk-1", "scsi", "1");

        assertTaskSuccess(result, "task-detach-disk");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("404", "{\"message\":\"disk not found\"}",
                "disk-1", "scsi", "1");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("404", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("disk not found"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String diskUUIDs,
                                        String buses, String indexes) {
        return executeWithMockedService(DiskImpl.class,
                service -> service.when(() -> detachDisks(any(NutanixDetachDisksInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new DetachDisks().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        diskUUIDs, buses, indexes,
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> detachDisks(any(NutanixDetachDisksInputs.class)), times(1)));
    }
}
