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
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateVMInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.VMImpl.createVM;

class CreateVMTest extends NutanixOperationTestSupport {
    @Test
    void postsConfigurationForNewVMWithEmptyDisk() throws Exception {
        final NutanixCreateVMInputs inputs = NutanixCreateVMInputs.builder()
                .vmName("demo")
                .description("test VM")
                .vmMemorySize("8")
                .numVCPUs("4")
                .numCoresPerVCPU("2")
                .timeZone("UTC")
                .hypervisorType("ACROPOLIS")
                .isCDROM("false")
                .isEmpty("true")
                .deviceBus("scsi")
                .deviceIndex("0")
                .vmDiskSize("10")
                .storageContainerUUID("container-1")
                .networkUUID("network-1")
                .requestedIPAddress("10.0.0.5")
                .isConnected("true")
                .hostUUIDs("host-1,host-2")
                .commonInputs(commonInputs)
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> createVM(inputs));

        assertRequest(request, "POST", "https://myhost:9440/api/nutanix/v2.0/vms");
        assertJsonEquals("{\"name\":\"demo\",\"description\":\"test VM\",\"memory_mb\":8192," +
                "\"num_vcpus\":4,\"num_cores_per_vcpu\":2,\"timezone\":\"UTC\"," +
                "\"vm_disks\":[{\"disk_address\":{\"device_bus\":\"scsi\"}," +
                "\"vm_disk_create\":{\"size\":10737418240,\"storage_container_uuid\":\"container-1\"}," +
                "\"flash_mode_enabled\":false,\"is_cdrom\":false,\"is_empty\":true," +
                "\"is_scsi_pass_through\":false,\"is_thin_provisioned\":false}],\"vm_nics\":[{\"is_connected\":true," +
                "\"network_uuid\":\"network-1\",\"requested_ip_address\":\"10.0.0.5\"}],\"vm_features\":" +
                "{\"agent_VM\":false},\"affinity\":{\"policy\":\"AFFINITY\"," +
                "\"host_uuids\":[\"host-1\",\"host-2\"]}}", request.getBody());
    }
}
