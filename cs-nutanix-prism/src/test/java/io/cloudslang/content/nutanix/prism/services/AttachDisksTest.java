package io.cloudslang.content.nutanix.prism.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixAttachDisksInputs;
import org.junit.jupiter.api.Test;

import static io.cloudslang.content.nutanix.prism.services.DiskImpl.AttachDisk;

class AttachDisksTest extends NutanixOperationTestSupport {
    @Test
    void createsEmptyDiskAndConvertsGiBToBytes() throws Exception {
        final NutanixAttachDisksInputs inputs = attachDiskInputs()
                .isCDROMList("false")
                .isEmptyDiskList("true")
                .deviceBusList("scsi")
                .deviceIndexList("1")
                .vmDiskSizeList("10")
                .storageContainerUUIDList("container-1")
                .build();

        final HttpClientInputs request = invokeAndCapture(() -> AttachDisk(inputs));

        assertRequest(request, "POST", "https://myhost:9440/api/nutanix/v2.0/vms/vm-123/disks/attach");
        assertJsonEquals("{\"vm_disks\":[{\"disk_address\":{\"device_bus\":\"scsi\",\"device_index\":1}," +
                "\"vm_disk_create\":{\"size\":10737418240,\"storage_container_uuid\":\"container-1\"}," +
                "\"is_cdrom\":false,\"is_empty\":true}]}", request.getBody());
    }

    @Test
    void clonesExistingDiskByUUID() throws Exception {
        final NutanixAttachDisksInputs inputs = attachDiskInputs()
                .isCDROMList("false")
                .deviceBusList("scsi")
                .sourceVMDiskUUIDList("source-disk")
                .vmDiskMinimumSizeList("5")
                .storageContainerUUIDList("container-1")
                .build();

        assertJsonEquals("{\"vm_disks\":[{\"disk_address\":{\"device_bus\":\"scsi\"}," +
                "\"vm_disk_clone\":{\"disk_address\":{\"vmdisk_uuid\":\"source-disk\"}," +
                "\"minimum_size\":5368709120,\"storage_container_uuid\":\"container-1\"}," +
                "\"is_cdrom\":false}]}", DiskImpl.AttachDisksBody(inputs));
    }

    @Test
    void clonesDiskFromNdfsFilePath() throws Exception {
        final NutanixAttachDisksInputs inputs = attachDiskInputs()
                .isCDROMList("false")
                .ndfsFilepathList("container/path/disk")
                .vmDiskMinimumSizeList("2")
                .build();

        assertJsonEquals("{\"vm_disks\":[{\"vm_disk_clone\":{\"disk_address\":" +
                "{\"ndfs_filepath\":\"container/path/disk\"},\"minimum_size\":2147483648}," +
                "\"is_cdrom\":false}]}", DiskImpl.AttachDisksBody(inputs));
    }

    private NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder attachDiskInputs() {
        return NutanixAttachDisksInputs.builder()
                .vmUUID("vm-123")
                .deviceBusList("")
                .deviceIndexList("")
                .isCDROMList("")
                .isEmptyDiskList("")
                .sourceVMDiskUUIDList("")
                .vmDiskMinimumSizeList("")
                .ndfsFilepathList("")
                .vmDiskSizeList("")
                .storageContainerUUIDList("")
                .isSCSIPassThroughList("")
                .isThinProvisionedList("")
                .isFlashModeEnabledList("")
                .commonInputs(commonInputs);
    }
}
