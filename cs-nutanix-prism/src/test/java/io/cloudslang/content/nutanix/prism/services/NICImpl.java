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
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteNICInputs;
import io.cloudslang.content.nutanix.prism.service.VMImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.service.NicImpl.deleteNicURL;
import static io.cloudslang.content.nutanix.prism.service.VMImpl.AddNicBody;
import static io.cloudslang.content.nutanix.prism.service.VMImpl.AddNicURL;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VMImpl.class)
public class NICImpl {

    private static final String EXPECTED_DELETE_NIC_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/myvm/nics/4e:5b:7f:aa:bb";
    private static final String EXPECTED_ADD_NIC_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/0b5d5c1c-40c8-4591-9f02-72e2ce/nics";
    private static final String EXPECTED_ADD_NIC_REQUEST_BODY = "{\"spec_list\":[{\"is_connected\":false,\"vlan_id\":\"\",\"network_uuid\":\"\"}]}";

    private final NutanixDeleteNICInputs nutanixDeleteNICInputs = NutanixDeleteNICInputs.builder()
            .vmUUID("myvm")
            .nicMacAddress("4e:5b:7f:aa:bb")
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

    private final NutanixAddNicInputs nutanixAddNicInputs = NutanixAddNicInputs.builder()
            .vmUUID("0b5d5c1c-40c8-4591-9f02-72e2ce")
            .networkUUID("923f260b-21ca-4617-b327-b4a9526d0589")
            .requestedIPAddress("15.119.80.141")
            .vlanId("521")
            .isConnected("true")
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

    private final NutanixAddNicInputs nutanixNicInputs = NutanixAddNicInputs.builder()
            .vmUUID("0b5d5c1c-40c8-4591-9f02-72e2ce")
            .networkUUID("")
            .requestedIPAddress("")
            .isConnected("")
            .vlanId("")
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
    public void attachNicPathTest() throws Exception {
        final String path = AddNicURL(nutanixAddNicInputs);
        assertEquals(EXPECTED_ADD_NIC_PATH, path);
    }

    @Test
    public void getAddNicrequestBody() throws JsonProcessingException {
        final String requestBody = AddNicBody(nutanixNicInputs);
        assertEquals(EXPECTED_ADD_NIC_REQUEST_BODY, requestBody);
    }

    @Test
    public void deleteNicPathTest() throws Exception {
        final String path = deleteNicURL(nutanixDeleteNICInputs);
        assertEquals(EXPECTED_DELETE_NIC_PATH, path);
    }
}
