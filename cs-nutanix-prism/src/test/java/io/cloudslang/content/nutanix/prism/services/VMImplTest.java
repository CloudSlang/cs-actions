/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateVMInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetVMDetailsInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMsInputs;
import io.cloudslang.content.nutanix.prism.service.VMImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.service.VMImpl.*;
import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(VMImpl.class)
public class VMImplTest {

    public static final String DELIMITER = ",";
    private static final String EXPECTED_GET_VM_DETAILS_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/1234";
    private static final String EXPECTED_CREATE_VM_PATH = "https://myhost:9080/api/nutanix/v2.0/vms";
    private static final String EXPECTED_CREATE_VM_REQUEST_BODY = "{\"name\":\"OO_test\",\"description\":\"OO_test\"," +
            "\"memory_mb\":10240,\"num_vcpus\":2,\"num_cores_per_vcpu\":2,\"timezone\":\"Asia/Calcutta\"," +
            "\"vm_disks\":[{\"disk_address\":{\"device_bus\":\"sata\"},\"vm_disk_clone\":{\"disk_address\":" +
            "{\"vmdisk_uuid\":\"1234\"}},\"flash_mode_enabled\":false,\"is_cdrom\":true,\"is_scsi_pass_through\":" +
            "false,\"is_thin_provisioned\":false}],\"vm_nics\":[{\"is_connected\":false,\"network_uuid\":\"3478\"}]," +
            "\"vm_features\":{\"agent_VM\":false},\"affinity\":{}}";
    private static final String EXPECTED_LIST_VM_PATH = "https://myhost:9080/api/nutanix/v2.0/vms";

    private final NutanixGetVMDetailsInputs nutanixGetVMDetailsInputs = NutanixGetVMDetailsInputs.builder()
            .vmUUID("1234")
            .includeVMDiskConfigInfo("")
            .includeVMNicConfigInfo("")
            .commonInputs(
                    NutanixCommonInputs.builder()
                            .hostname("myhost")
                            .port("9080")
                            .username("username")
                            .password("password")
                            .apiVersion("v2.0")
                            .proxyHost("")
                            .proxyPort("")
                            .proxyUsername("")
                            .proxyPassword("")
                            .trustAllRoots("")
                            .x509HostnameVerifier("")
                            .trustKeystore("")
                            .trustPassword("")
                            .connectTimeout("")
                            .socketTimeout("")
                            .keepAlive("")
                            .connectionsMaxPerRoot("")
                            .connectionsMaxTotal("")
                            .build()).build();

    private final NutanixCreateVMInputs nutanixCreateVMInputs = NutanixCreateVMInputs.builder()
            .vmName("OO_test")
            .description("OO_test")
            .vmMemorySize("10")
            .numVCPUs("2")
            .numCoresPerVCPU("2")
            .timeZone("Asia/Calcutta")
            .hypervisorType("ACROPOLIS")
            .flashModeEnabled("")
            .isSCSIPassThrough("")
            .isThinProvisioned("")
            .isCDROM("true")
            .deviceBus("sata")
            .deviceIndex("0")
            .diskLabel("")
            .sourceVMDiskUUID("1234")
            .vmDiskMinimumSize("0")
            .networkUUID("3478")
            .isConnected("")
            .agentVM("")
            .commonInputs(
                    NutanixCommonInputs.builder()
                            .hostname("myhost")
                            .port("9080")
                            .username("username")
                            .password("password")
                            .apiVersion("v2.0")
                            .proxyHost("")
                            .proxyPort("")
                            .proxyUsername("")
                            .proxyPassword("")
                            .trustAllRoots("")
                            .x509HostnameVerifier("")
                            .trustKeystore("")
                            .trustPassword("")
                            .connectTimeout("")
                            .socketTimeout("")
                            .keepAlive("")
                            .connectionsMaxPerRoot("")
                            .connectionsMaxTotal("")
                            .build()).build();


    private final NutanixListVMsInputs nutanixListVMsInputs = NutanixListVMsInputs.builder()
            .filter("")
            .offset("")
            .length("")
            .sortOrder("")
            .sortAttribute("")
            .includeVMDiskConfigInfo("")
            .includeVMNicConfigInfo("")
            .commonInputs(
                    NutanixCommonInputs.builder()
                            .hostname("myhost")
                            .port("9080")
                            .username("username")
                            .password("password")
                            .apiVersion("v2.0")
                            .proxyHost("")
                            .proxyPort("")
                            .proxyUsername("")
                            .proxyPassword("")
                            .trustAllRoots("")
                            .x509HostnameVerifier("")
                            .trustKeystore("")
                            .trustPassword("")
                            .keystore("")
                            .keystorePassword("")
                            .connectTimeout("")
                            .socketTimeout("")
                            .keepAlive("")
                            .connectionsMaxPerRoot("")
                            .connectionsMaxTotal("")
                            .responseCharacterSet("")
                            .build()).build();

    @Test
    public void getVMDetailsPathTest() throws Exception {
        final String path = getVMDetailsURL(nutanixGetVMDetailsInputs);
        assertEquals(EXPECTED_GET_VM_DETAILS_PATH, path);
    }

    @Test
    public void listVMPathTest() throws Exception {
        final String path = listVMsURL(nutanixListVMsInputs);
        assertEquals(EXPECTED_LIST_VM_PATH, path);
    }

    @Test
    public void createVMPathTest() throws Exception {
        final String path = createVMURL(nutanixCreateVMInputs);
        assertEquals(EXPECTED_CREATE_VM_PATH, path);
    }

    @Test
    public void getCreateVMRequestBody() throws JsonProcessingException {
        final String requestBody = createVMBody(nutanixCreateVMInputs, DELIMITER);
        assertEquals(EXPECTED_CREATE_VM_REQUEST_BODY, requestBody);
    }
}
