

package io.cloudslang.content.nutanix.prism.utils;

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class CommonOutputs {
        public static final String DOCUMENT = "document";
        public static final String TASK_UUID = "taskUUID";
    }

    public static class GetVMDetailsOutputs {
        public static final String VM_NAME = "vmName";
        public static final String IP_ADDRESS = "ipAddress";
        public static final String MAC_ADDRESS = "macAddress";
        public static final String POWER_STATE = "powerState";
        public static final String VM_DISK_UUID = "vmDiskUUID";
        public static final String STORAGE_CONTAINER_UUID = "storageContainerUUID";
        public static final String VM_LOGICAL_TIMESTAMP = "vmLogicalTimestamp";
        public static final String HOST_UUIDS = "hostUUIDs";
    }

    public static class ListVMOutputs {
        public static final String VM_LIST = "vmList";
    }

    public static class GetTaskDetailsOutputs {
        public static final String VM_UUID = "vmUUID";
        public static final String TASK_STATUS = "taskStatus";
    }

}
