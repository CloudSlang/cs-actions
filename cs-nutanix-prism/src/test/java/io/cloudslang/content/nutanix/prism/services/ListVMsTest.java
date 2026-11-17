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
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMsInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.listVMs;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ListVMsTest extends NutanixOperationTestSupport {
    @Test
    void sendsGetRequestWithAllFilters() throws Exception {
        final NutanixListVMsInputs inputs = NutanixListVMsInputs.builder()
                .filter("vm_name==demo")
                .offset("2")
                .length("10")
                .sortOrder("ASCENDING")
                .sortAttribute("vm_name")
                .includeVMDiskConfigInfo("true")
                .includeVMNicConfigInfo("false")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> listVMs(inputs));

        assertRequest(request, "GET", "https://myhost:9440/api/nutanix/v2.0/vms");
        assertEquals("?filter=vm_name==demo&offset=2&length=10&sortorder=ASCENDING&sortattribute=vm_name" +
                "&include_vm_disk_config=true&include_vm_nic_config=false", request.getQueryParams());
    }
}
