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
import io.cloudslang.content.nutanix.prism.entities.NutanixUpdateDiskInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.DiskImpl.updateDisk;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.updateDiskBody;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateDiskTest extends NutanixOperationTestSupport {
    @Test
    void createsPutRequestAndConvertsGiBToBytes() throws Exception {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().build();

        final HttpClientInputs request = invokeAndCapture(() -> updateDisk(inputs));

        assertRequest(request, "PUT", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/disks/update");
        assertJsonEquals("{\"vm_disks\":[{\"disk_address\":{\"vmdisk_uuid\":\"disk-123\"}," +
                "\"vm_disk_create\":{\"size\":10737418240}}]}", request.getBody());
    }

    @Test
    void rejectsNonPositiveDiskSize() {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().vmDiskSizeGiB("0").build();

        assertThrows(IllegalArgumentException.class, () -> updateDiskBody(inputs));
    }

    @Test
    void rejectsDiskSizeThatOverflowsByteConversion() {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().vmDiskSizeGiB(Long.toString(Long.MAX_VALUE)).build();

        assertThrows(ArithmeticException.class, () -> updateDiskBody(inputs));
    }

    @Test
    void rejectsMissingDiskUUID() {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().vmDiskUUID("").build();

        assertThrows(IllegalArgumentException.class, () -> updateDiskBody(inputs));
    }

    @Test
    void rejectsMissingVmUUID() {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().vmUUID("").build();

        assertThrows(IllegalArgumentException.class, () -> updateDiskBody(inputs));
    }

    private NutanixUpdateDiskInputs.NutanixUpdateDiskInputsBuilder updateDiskInputs() {
        return NutanixUpdateDiskInputs.builder()
                .vmUUID("vm-123")
                .vmDiskUUID("disk-123")
                .vmDiskSizeGiB("10")
                .commonInputs(commonInputs);
    }
}
