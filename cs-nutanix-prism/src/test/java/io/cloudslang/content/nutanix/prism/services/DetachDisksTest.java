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
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import io.cloudslang.content.nutanix.prism.exceptions.NutanixDetachDiskException;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.DiskImpl.detachDisks;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.detachDisksBody;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DetachDisksTest extends NutanixOperationTestSupport {
    @Test
    void postsVMAndDiskAddressForDetachment() throws Exception {
        final NutanixDetachDisksInputs inputs = detachInputs("disk-1,disk-2", "scsi,sata", "1,2");

        final HttpClientInputs request = invokeAndCapture(() -> detachDisks(inputs));

        assertRequest(request, "POST", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/disks/detach");
        assertJsonEquals("{\"uuid\":\"vm-123\",\"vm_disks\":[" +
                "{\"disk_address\":{\"device_bus\":\"scsi\",\"device_index\":\"1\",\"vmdisk_uuid\":\"disk-1\"}}," +
                "{\"disk_address\":{\"device_bus\":\"sata\",\"device_index\":\"2\",\"vmdisk_uuid\":\"disk-2\"}}]}",
                request.getBody());
    }

    @Test
    void rejectsDiskListsWithDifferentLengths() {
        final NutanixDetachDisksInputs inputs = detachInputs("disk-1,disk-2", "scsi", "1,2");

        assertThrows(NutanixDetachDiskException.class, () -> detachDisksBody(inputs));
    }

    private NutanixDetachDisksInputs detachInputs(String diskUUIDs, String deviceBuses, String deviceIndexes) {
        return NutanixDetachDisksInputs.builder()
                .vmUUID("vm-123")
                .vmDiskUUIDList(diskUUIDs)
                .deviceBusList(deviceBuses)
                .deviceIndexList(deviceIndexes)
                .commonInputs(commonInputs)
                .build();
    }
}
