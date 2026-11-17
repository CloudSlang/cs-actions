package io.cloudslang.content.nutanix.prism.services.models.disks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateDiskRequestBody {
    @JsonProperty("vm_disks")
    private List<VMDisk> vmDisks;

    public List<VMDisk> getVmDisks() {
        return vmDisks;
    }

    public void setVmDisks(List<VMDisk> vmDisks) {
        this.vmDisks = vmDisks;
    }

    public static class VMDisk {
        @JsonProperty("disk_address")
        private DiskAddress diskAddress;
        @JsonProperty("flash_mode_enabled")
        private boolean flashModeEnabled;
        @JsonProperty("is_cdrom")
        private boolean cdrom;
        @JsonProperty("is_empty")
        private boolean empty;
        @JsonProperty("vm_disk_create")
        private VMDiskCreate vmDiskCreate;

        public DiskAddress getDiskAddress() {
            return diskAddress;
        }

        public void setDiskAddress(DiskAddress diskAddress) {
            this.diskAddress = diskAddress;
        }

        public boolean isFlashModeEnabled() {
            return flashModeEnabled;
        }

        public void setFlashModeEnabled(boolean flashModeEnabled) {
            this.flashModeEnabled = flashModeEnabled;
        }

        public boolean isCdrom() {
            return cdrom;
        }

        public void setCdrom(boolean cdrom) {
            this.cdrom = cdrom;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public VMDiskCreate getVmDiskCreate() {
            return vmDiskCreate;
        }

        public void setVmDiskCreate(VMDiskCreate vmDiskCreate) {
            this.vmDiskCreate = vmDiskCreate;
        }
    }

    public static class DiskAddress {
        @JsonProperty("vmdisk_uuid")
        private String vmDiskUUID;
        @JsonProperty("device_index")
        private int deviceIndex;
        @JsonProperty("device_bus")
        private String deviceBus;

        public String getVmDiskUUID() {
            return vmDiskUUID;
        }

        public void setVmDiskUUID(String vmDiskUUID) {
            this.vmDiskUUID = vmDiskUUID;
        }

        public int getDeviceIndex() {
            return deviceIndex;
        }

        public void setDeviceIndex(int deviceIndex) {
            this.deviceIndex = deviceIndex;
        }

        public String getDeviceBus() {
            return deviceBus;
        }

        public void setDeviceBus(String deviceBus) {
            this.deviceBus = deviceBus;
        }
    }

    public static class VMDiskCreate {
        @JsonProperty("storage_container_uuid")
        private String storageContainerUUID;
        @JsonProperty("size")
        private long size;

        public String getStorageContainerUUID() {
            return storageContainerUUID;
        }

        public void setStorageContainerUUID(String storageContainerUUID) {
            this.storageContainerUUID = storageContainerUUID;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
