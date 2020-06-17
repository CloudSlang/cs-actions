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

import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDetachDisksInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.services.DiskImpl.AttachDisksURL;
import static io.cloudslang.content.nutanix.prism.services.DiskImpl.detachDisksURL;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VMImpl.class)
public class DiskImplTest {
    private static final String EXPECTED_DETACH_DISKS_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/myvm/disks/detach";
    private static final String EXPECTED_ATTACH_DISKS_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/myvm/disks/attach";

    private final NutanixDetachDisksInputs nutanixDetachDisksInputs = NutanixDetachDisksInputs.builder()
            .vmUUID("myvm")
            .vmDiskUUIDList("diskid")
            .deviceIndexList("0")
            .deviceBusList("deviceBus")
            .commonInputs(NutanixCommonInputs.builder()
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

    private final NutanixAttachDisksInputs nutanixAttachDisksInputs = NutanixAttachDisksInputs.builder()
            .vmUUID("myvm")
            .deviceBusList("scsi")
            .deviceIndexList("0")
            .vmDisksizeList("10737418240")
            .storagecontainerUUIDDiskList("891a9fab-9484-4947-8c7d-155701cdee20")
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

    @Test
    public void detachDisksPathTest() throws Exception {
        final String path = detachDisksURL(nutanixDetachDisksInputs);
        assertEquals(EXPECTED_DETACH_DISKS_PATH, path);
    }

    @Test
    public void attachDisksPathTest() throws Exception {
        final String path = AttachDisksURL(nutanixAttachDisksInputs);
        assertEquals(EXPECTED_ATTACH_DISKS_PATH, path);
    }
}
