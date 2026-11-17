package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.TaskImpl.getTaskDetails;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GetTaskDetailsTest extends NutanixOperationTestSupport {
    @Test
    void sendsGetRequestWithSubtaskOption() throws Exception {
        final NutanixGetTaskDetailsInputs inputs = NutanixGetTaskDetailsInputs.builder()
                .taskUUID("task-1")
                .includeSubtasksInfo("true")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> getTaskDetails(inputs));

        assertRequest(request, "GET", "https://myhost:9440/api/nutanix/v2.0/tasks/task-1");
        assertEquals("include_subtasks_info=true", request.getQueryParams());
    }
}
