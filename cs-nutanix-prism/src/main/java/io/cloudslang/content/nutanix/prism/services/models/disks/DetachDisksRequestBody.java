package io.cloudslang.content.nutanix.prism.services.models.disks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class DetachDisksRequestBody {
    @JsonProperty("uuid")
    String vmUUID;
    @JsonProperty("vm_disks")
    ArrayList vmDisks;

    public String getVmUUID() {
        return vmUUID;
    }

    public void setVmUUID(String vmUUID) {
        this.vmUUID = vmUUID;
    }

    public ArrayList getVmDisks() {
        return vmDisks;
    }

    public void setVmDisks(ArrayList vmDisks) {
        this.vmDisks = vmDisks;
    }

    public class VMDisks {
        @JsonProperty("disk_address")
        DiskAddress diskAddress;

        public DiskAddress getDiskAddress() {
            return diskAddress;
        }

        public void setDiskAddress(DiskAddress diskAddress) {
            this.diskAddress = diskAddress;
        }
    }
    public class DiskAddress {
        @JsonProperty("device_bus")
        String deviceBus;
        @JsonProperty("device_index")
        String deviceIndex;
        @JsonProperty("vmdisk_uuid")
        String vmDiskUUID;

        public String getDeviceBus() {
            return deviceBus;
        }

        public void setDeviceBus(String deviceBus) {
            this.deviceBus = deviceBus;
        }

        public String getDeviceIndex() {
            return deviceIndex;
        }

        public void setDeviceIndex(String deviceIndex) {
            this.deviceIndex = deviceIndex;
        }

        public String getVmDiskUUID() {
            return vmDiskUUID;
        }

        public void setVmDiskUUID(String vmDiskUUID) {
            this.vmDiskUUID = vmDiskUUID;
        }
    }


}
