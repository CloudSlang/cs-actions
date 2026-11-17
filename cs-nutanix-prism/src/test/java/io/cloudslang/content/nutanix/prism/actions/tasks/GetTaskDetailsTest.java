/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.nutanix.prism.actions.tasks;

import io.cloudslang.content.nutanix.prism.actions.NutanixActionTestSupport;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import io.cloudslang.content.nutanix.prism.services.TaskImpl;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.nutanix.prism.services.TaskImpl.getTaskDetails;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.GetTaskDetailsOutputs.TASK_STATUS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class GetTaskDetailsTest extends NutanixActionTestSupport {
    @Test
    void mapsSuccessfulTaskStatus() {
        final Map<String, String> result = execute("200", "{\"progress_status\":\"Succeeded\"}");

        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("Succeeded", result.get(TASK_STATUS));
    }

    @Test
    void mapsFailedTaskStatusFromSuccessfulRequest() {
        final Map<String, String> result = execute("200",
                "{\"progress_status\":\"Failed\",\"meta_response\":{\"error_detail\":\"task failed\"}}");

        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("Failed", result.get(TASK_STATUS));
    }

    @Test
    void mapsAPIErrorToActionFailure() {
        final Map<String, String> result = execute("500",
                "{\"progress_status\":\"Failed\",\"message\":\"task lookup failed\"}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("500", result.get(STATUS_CODE));
        assertNotNull(result.get(EXCEPTION));
    }

    @Test
    void mapsMissingTaskStatusToExceptionOutput() {
        final Map<String, String> result = execute("200", "{}");

        assertEquals("-1", result.get(RETURN_CODE));
        assertNotNull(result.get(EXCEPTION));
    }

    private Map<String, String> execute(String statusCode, String body) {
        return executeWithMockedService(TaskImpl.class,
                service -> service.when(() -> getTaskDetails(any(NutanixGetTaskDetailsInputs.class)))
                        .thenReturn(serviceResponse(statusCode, body)),
                () -> new GetTaskDetails().execute(
                        "prism-host", null, "username", "password", "task-1", "true",
                        null, null, null, null, null, null, null, null,
                        null, null, null, null, null, null),
                service -> service.verify(() -> getTaskDetails(any(NutanixGetTaskDetailsInputs.class)), times(1)));
    }
}
