package io.cloudslang.content.nutanix.prism.actions.snapshots;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteSnapshotInputs;
import io.cloudslang.content.nutanix.prism.services.SnapshotImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.SnapshotImpl.deleteSnapshot;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class DeleteSnapshotTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("200", "{\"task_uuid\":\"task-delete\"}", "88", null);

        assertTaskSuccess(result, "task-delete");
    }

    @Test
    void mapsServerErrorToActionFailure() {
        final Map<String, String> result = execute("500", "{\"message\":\"backend failure\"}", null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("500", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("backend failure"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void rejectsInvalidCommonInputBeforeCallingService() {
        final Map<String, String> result = executeWithoutServiceCall(SnapshotImpl.class,
                () -> new DeleteSnapshot().execute(
                        "prism-host", null, "username", "password", "snapshot-123",
                        null, null, null, "invalid-port", null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> deleteSnapshot(any(NutanixDeleteSnapshotInputs.class)),
                        org.mockito.Mockito.never()));

        assertTrue(result.get(EXCEPTION).contains("proxyPort"));
    }

    @Test
    void mapsMissingTaskUUIDToActionFailure() {
        final Map<String, String> result = execute("200", "{}", null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void mapsNonNumericStatusCodeToActionFailure() {
        final Map<String, String> result = execute("invalid", "{}", null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String timestamp, String proxyPort) {
        return executeWithMockedService(SnapshotImpl.class,
                service -> service.when(() -> deleteSnapshot(any(NutanixDeleteSnapshotInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new DeleteSnapshot().execute(
                        "prism-host", null, "username", "password", "snapshot-123",
                        timestamp, null, null, proxyPort, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> deleteSnapshot(any(NutanixDeleteSnapshotInputs.class)), times(1)));
    }
}
