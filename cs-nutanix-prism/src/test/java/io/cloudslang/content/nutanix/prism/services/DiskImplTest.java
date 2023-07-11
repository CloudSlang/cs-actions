

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
    private static final String EXPECTED_DETACH_DISKS_PATH = "https://myhost:9440/api/nutanix/v2.0/vms/myvm/disks/detach";
    private static final String EXPECTED_ATTACH_DISKS_PATH = "https://myhost:9440/api/nutanix/v2.0/vms/myvm/disks/attach";

    private final NutanixDetachDisksInputs nutanixDetachDisksInputs = NutanixDetachDisksInputs.builder()
            .vmUUID("myvm")
            .vmDiskUUIDList("diskid")
            .deviceIndexList("0")
            .deviceBusList("deviceBus")
            .commonInputs(NutanixCommonInputs.builder()
                    .hostname("myhost")
                    .port("9440")
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
            .vmDiskSizeList("10")
            .storageContainerUUIDList("891a9fab-9484-4947-8c7d-155701cdee20")
            .commonInputs(
                    NutanixCommonInputs.builder()
                            .hostname("myhost")
                            .port("9440")
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
