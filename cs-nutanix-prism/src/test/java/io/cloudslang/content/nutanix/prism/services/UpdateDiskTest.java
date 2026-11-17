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
        assertJsonEquals("{\"vm_disks\":[{\"disk_address\":{\"vmdisk_uuid\":\"disk-123\"," +
                "\"device_index\":1,\"device_bus\":\"scsi\"},\"flash_mode_enabled\":false," +
                "\"is_cdrom\":false,\"is_empty\":false,\"vm_disk_create\":" +
                "{\"storage_container_uuid\":\"container-1\",\"size\":10737418240}}]}", request.getBody());
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
    void rejectsUnsupportedDeviceBus() {
        final NutanixUpdateDiskInputs inputs = updateDiskInputs().deviceBus("usb").build();

        assertThrows(IllegalArgumentException.class, () -> updateDiskBody(inputs));
    }

    private NutanixUpdateDiskInputs.NutanixUpdateDiskInputsBuilder updateDiskInputs() {
        return NutanixUpdateDiskInputs.builder()
                .vmUUID("vm-123")
                .vmDiskUUID("disk-123")
                .deviceBus("scsi")
                .deviceIndex("1")
                .storageContainerUUID("container-1")
                .vmDiskSizeGiB("10")
                .commonInputs(commonInputs);
    }
}
