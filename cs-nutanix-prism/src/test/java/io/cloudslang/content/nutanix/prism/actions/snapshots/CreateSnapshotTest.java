package io.cloudslang.content.nutanix.prism.actions.snapshots;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateSnapshotInputs;
import io.cloudslang.content.nutanix.prism.services.SnapshotImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.SnapshotImpl.createSnapshot;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class CreateSnapshotTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulAPIResponseToTaskUUID() {
        final Map<String, String> result = execute("201", "{\"task_uuid\":\"task-create\"}",
                null, null, null);

        assertTaskSuccess(result, "task-create");
    }

    @Test
    void forwardsOptionalSnapshotUUIDToService() {
        final ArgumentCaptor<NutanixCreateSnapshotInputs> inputCaptor =
                ArgumentCaptor.forClass(NutanixCreateSnapshotInputs.class);
        final Map<String, String> result = executeWithMockedService(SnapshotImpl.class,
                service -> service.when(() -> createSnapshot(any(NutanixCreateSnapshotInputs.class)))
                        .thenReturn(serviceResponse("201", "{\"task_uuid\":\"task-create\"}")),
                () -> new CreateSnapshot().execute(
                        "prism-host", null, "username", "password", "vm-123", "snapshot-name",
                        "snapshot-idempotency-key", "88", null, null, null, null,
                        null, null, null, null, null, null, null, null, null, null),
                service -> service.verify(() -> createSnapshot(inputCaptor.capture()), times(1)));

        assertTaskSuccess(result, "task-create");
        assertEquals("snapshot-idempotency-key", inputCaptor.getValue().getSnapshotUUID());
        assertEquals("88", inputCaptor.getValue().getVmLogicalTimestamp());
    }

    @Test
    void mapsUnauthorizedAPIResponseToFailure() {
        final Map<String, String> result = execute("401", "{\"message\":\"unauthorized\"}", null, null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("401", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("unauthorized"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void mapsOtherClientErrorsToFailure() {
        final Map<String, String> result = execute("400", "{\"message\":\"invalid request\"}", null, null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("400", result.get(STATUS_CODE));
        assertTrue(result.get(EXCEPTION).contains("invalid request"));
        assertNull(result.get(TASK_UUID));
    }

    @Test
    void rejectsInvalidCommonInputBeforeCallingService() {
        final Map<String, String> result = executeWithoutServiceCall(SnapshotImpl.class,
                () -> new CreateSnapshot().execute(
                        "prism-host", null, "username", "password", "vm-123", "snapshot-name",
                        null, null, null, null, "invalid-port", null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> createSnapshot(any(NutanixCreateSnapshotInputs.class)),
                        org.mockito.Mockito.never()));

        assertTrue(result.get(EXCEPTION).contains("proxyPort"));
    }

    @Test
    void mapsMissingTaskUUIDToActionFailure() {
        final Map<String, String> result = execute("200", "{}", null, null, null);

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
        assertNull(result.get(TASK_UUID));
    }

    private Map<String, String> execute(String statusCode, String body, String snapshotUUID,
                                        String vmLogicalTimestamp, String proxyPort) {
        return executeWithMockedService(SnapshotImpl.class,
                service -> service.when(() -> createSnapshot(any(NutanixCreateSnapshotInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new CreateSnapshot().execute(
                        "prism-host", null, "username", "password", "vm-123", "snapshot-name",
                        snapshotUUID, vmLogicalTimestamp, null, null, proxyPort, null,
                        null, null, null, null, null, null, null, null, null, null),
                service -> service.verify(() -> createSnapshot(any(NutanixCreateSnapshotInputs.class)), times(1)));
    }
}
