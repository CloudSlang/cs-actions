/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.nutanix.prism.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String HOSTNAME = "hostname";
        public static final String PROTOCOL = "protocol";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String API_VERSION = "apiVersion";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String VM_UUID = "vmUUID";
    }

    public static class GetVMDetailsInputs {
        public static final String VM_UUID = "vmUUID";
        public static final String INCLUDE_VM_DISK_CONFIG_INFO = "includeVMDiskConfigInfo";
        public static final String INCLUDE_VM_NIC_CONFIG_INFO = "includeVMNicConfigInfo";
        public static final String FILTER = "filter";
        public static final String OFFSET = "offset";
        public static final String LENGTH = "length";
        public static final String SORT_ORDER = "sortOrder";
        public static final String SORT_ATTRIBUTE = "sortAttribute";

    }

    public static class CreateVMInputs {
        public static final String VM_NAME = "vmName";
        public static final String VM_DESCRIPTION = "vmDescription";
        public static final String VM_MEMORY_SIZE = "vmMemorySize";
        public static final String NUM_VCPUS = "numVCPUs";
        public static final String NUM_CORES_PER_VCPU = "numCoresPerVCPU";
        public static final String TIME_ZONE = "timeZone";
        public static final String HYPERVISOR_TYPE = "hypervisorType";
        public static final String FLASH_MODE_ENABLED = "flashModeEnabled";
        public static final String IS_SCSI_PASS_THROUGH = "isSCSIPassThrough";
        public static final String IS_THIN_PROVISIONED = "isThinProvisioned";
        public static final String IS_CDROM = "isCDROM";
        public static final String IS_EMPTY_DISK = "isEmptyDisk";
        public static final String DEVICE_BUS = "deviceBus";
        public static final String DISK_LABEL = "diskLabel";
        public static final String DEVICE_INDEX = "deviceIndex";
        public static final String NDFS_FILE_PATH = "ndfsFilepath";
        public static final String SOURCE_VM_DISK_UUID = "sourceVMDiskUUID";
        public static final String VM_DISK_MINIMUM_SIZE = "vmDiskMinimumSize";
        public static final String EXTERNAL_DISK_URL = "externalDiskUrl";
        public static final String EXTERNAL_DISK_SIZE = "externalDiskSize";
        public static final String STORAGE_CONTAINER_UUID = "storageContainerUUID";
        public static final String VM_DISK_SIZE = "vmDiskSize";
        public static final String NETWORK_UUID = "networkUUID";
        public static final String REQUESTED_IP_ADDRESS = "requestedIPAddress";
        public static final String IS_CONNECTED = "isConnected";
        public static final String HOST_UUIDS = "hostUUIDs";
        public static final String AGENT_VM = "agentVM";
    }

    public static class DeleteVMInputs {
        public static final String DELETE_SNAPSHOTS = "deleteSnapshots";
        public static final String LOGICAL_TIMESTAMP = "logicalTimestamp";
    }

    public static class SetVMPowerStateInputs {
        public static final String HOST_UUID = "hostUUID";
        public static final String POWER_STATE = "powerState";
        public static final String VM_LOGICAL_TIMESTAMP = "vmLogicalTimestamp";
    }

    public static class GetTaskDetailsInputs {
        public static final String TASK_UUID = "taskUUID";
        public static final String INCLUDE_SUBTASKS_INFO = "includeSubtasksInfo";
    }

    public static class DetachDisksInputs {
        public static final String VM_DISK_UUID_LIST = "vmDiskUUIDList";
        public static final String DEVICE_BUS_LIST = "deviceBusList";
        public static final String DEVICE_INDEX_LIST = "deviceIndexList";

    }

    public static class AttachDisksInputs {
        public static final String DEVICE_BUS_LIST = "deviceBusList";
        public static final String DEVICE_INDEX_LIST = "deviceIndexList";
        public static final String IS_CDROM_LIST = "isCDROMList";
        public static final String IS_EMPTY_DISK_LIST = "isEmptyDiskList";
        public static final String SOURCE_VM_DISK_UUID_LIST = "sourceVMDiskUUIDList";
        public static final String VM_DISK_MINIMUM_SIZE_LIST = "vmDiskMinimumSizeList";
        public static final String NDFS_FILE_PATH_LIST = "ndfsFilepathList";
        public static final String VM_DISK_SIZE_LIST = "deviceDiskSizeList";
        public static final String STORAGE_CONTAINER_UUID_LIST = "storageContainerUUIDList";
        public static final String IS_SCSI_PASS_THROUGH_LIST = "isSCSIPassThroughList";
        public static final String IS_THIN_PROVISIONED_LIST = "isThinProvisionedList";
        public static final String IS_FLASH_MODE_ENABLED_LIST = "isFlashModeEnabledList";

    }

    public static class DeleteNICInputs {
        public static final String VM_LOGICAL_TIMESTAMP = "vmLogicalTimestamp";
        public static final String NIC_MAC_ADDRESS = "nicMacAddress";
    }
    public static class AddNicInput{
        public static final String NETWORK_UUID = "networkUUID";
        public static final String REQUESTED_IP_ADDRESS = "requestedIPAddress";
        public static final String IS_CONNECTED = "isConnected";
        public static final String VLAN_ID = "vlanId";


    }
}
