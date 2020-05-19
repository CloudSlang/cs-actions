package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;

import io.cloudslang.content.nutanix.prism.entities.NutanixListVMdetailsInputs;
import io.cloudslang.content.nutanix.prism.service.VMImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.nutanix.prism.service.VMImpl.getListVMsURL;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ListVMTest.class)
public class ListVMTest {

    private static final String EXPECTED_LIST_VM_DETAILS_PATH = "https://myhost:9080/api/nutanix/v2/vms";

    private final NutanixListVMdetailsInputs nutanixListVMDetailsInputs = NutanixListVMdetailsInputs.builder()
            .filter("")
            .offset("")
            .length("")
            .sortorder("")
            .sortattribute("")
            .includeVMDiskConfigInfo("")
            .includeVMNicConfigInfo("")
            .commonInputs(
                    NutanixCommonInputs.builder()
                            .protocol("https")
                            .hostname("myhost")
                            .port("9080")
                            .username("username")
                            .password("password")
                            .apiVersion("v2")
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
    public void listVMDetailsPathTest() throws Exception {
        final String path = getListVMsURL(nutanixListVMDetailsInputs);
        assertEquals(EXPECTED_LIST_VM_DETAILS_PATH, path);
    }
}
