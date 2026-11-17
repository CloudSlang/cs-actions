package io.cloudslang.content.nutanix.prism.actions.disks;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import io.cloudslang.content.nutanix.prism.services.DiskImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.AttachDisk;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class AttachDisksTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("202", "{\"task_uuid\":\"task-attach-disk\"}");

        assertTaskSuccess(result, "task-attach-disk");
    }

    @Test
    void mapsAPIFailureToActionFailure() {
        final Map<String, String> result = execute("409", "{\"message\":\"disk already attached\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("409", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("disk already attached"));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(DiskImpl.class,
                service -> service.when(() -> AttachDisk(any(NutanixAttachDisksInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new AttachDisks().execute(
                        "prism-host", null, "username", "password", "vm-1",
                        "false", "true", "scsi", "1", null, null, null,
                        "10", "container-1", null, null, null,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null, null),
                service -> service.verify(() -> AttachDisk(any(NutanixAttachDisksInputs.class)), times(1)));
    }
}
