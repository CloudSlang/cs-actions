package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cloudslang.content.nutanix.prism.entities.NutanixAddNicInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.service.VMImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.service.VMImpl.AddNicBody;
import static io.cloudslang.content.nutanix.prism.service.VMImpl.AddNicURL;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VMImpl.class)
public class AddNicImpTest {
    private static final String EXPECTED_ADD_NIC_PATH = "https://myhost:9080/api/nutanix/v2.0/vms/0b5d5c1c-40c8-4591-9f02-72e2ce/nics";

    private static final String EXPECTED_ADD_NIC_REQUEST_BODY = "{\"spec_list\":[{\"is_connected\":false,\"vlan_id\":\"\",\"network_uuid\":\"\"}]}";
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
}
