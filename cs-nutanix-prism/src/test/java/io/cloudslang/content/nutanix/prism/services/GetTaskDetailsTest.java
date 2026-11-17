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
