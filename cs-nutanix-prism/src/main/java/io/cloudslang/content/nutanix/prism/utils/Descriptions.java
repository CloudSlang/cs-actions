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

package io.cloudslang.content.nutanix.prism.utils;

public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Nutanix Prism service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Nutanix Prism service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server username.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username " +
                "input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if it is not issued by a trusted certification authority." +
                "Default: 'false'";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\", the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\"." +
                "Default: 'strict'";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from certificate authorities " +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total." +
                "Default: '20'";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis." +
                "Default: '2'";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it." +
                "Default: 'true'";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The timeout to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: '10000'";
        public static final String HOSTNAME_DESC = "The hostname for Nutanix Prism.";
        public static final String PORT_DESC = "The port to connect to Nutanix Prism. " +
                "Default: '9440'";
        public static final String USERNAME_DESC = "The username for Nutanix Prism.";
        public static final String PASSWORD_DESC = "The password for Nutanix Prism.";
        public static final String API_VERSION_DESC = "The api version for Nutanix Prism." +
                "Default: 'v2.0'";

        public static final String STATUS_CODE_DESC = "The HTTP status code for Nutanix Prism API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the " +
                "request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. " +
                "In case of an error this output will contain the error message.";
        public static final String TASK_UUID_DESC = "The UUID of the task that will be created in Nutanix Prism after " +
                "submission of the API request.";
        public static final String VM_UUID_DESC = "The UUID of the virtual machine.";
    }


    public static class GetVMDetails {
        public static final String GET_VM_DETAILS_OPERATION_DESC = "Gets the details of a specific virtual machines. " +
                "virtual machine disk information and network information are included by default. " +
                "These can be included by setting the includeVMDiskConfig and includeVMNicConfig flags " +
                "respectively.";
        public static final String INCLUDE_VM_DISK_CONFIG_INFO_DESC = "Whether to include virtual machine disk " +
                "information." +
                "Default : 'true'";
        public static final String INCLUDE_VM_NIC_CONFIG_INFO_DESC = "Whether to include network information." +
                "Default : 'true'.";
        public static final String VM_NAME_DESC = "Name of the virtual machine.";
        public static final String IP_ADDRESS_DESC = "The IP address/es of the virtual machine.";
        public static final String MAC_ADDRESS_DESC = "The MAC address/es of the virtual machine.";
        public static final String POWER_STATE_DESC = "The current power state of the virtual machine.";
        public static final String VM_DISK_UUID_DESC = "The UUID of the disk attached to the virtual machine.";
        public static final String STORAGE_CONTAINER_UUID_DESC = "The UUID of the storage container of the virtual " +
                "machine.";
        public static final String VM_LOGICAL_TIMESTAMP_DESC = "The virtual logical timestamp of the virtual machine.";
    }

    public static class ListVMsInputs {
        public static final String LIST_VMS_OPERATION_DESC = "Gets a list of virtual machines." +
                "virtual machine disk information and network information are not included by default as fetching " +
                "these are expensive operations. These can be included by setting the include_vmdisk_config and " +
                "include_vmnic_config flags respectively.";
        public static final String FILTER_DESC = "Filter criteria - semicolon for AND, comma for OR.";
        public static final String OFFSET_DESC = "Offset.";
        public static final String LENGTH_DESC = "Number of VMs to retrieve.";
        public static final String SORT_ORDER_DESC = "Sort order.";
        public static final String SORT_ATTRIBUTE_DESC = "Sort attribute.";
        public static final String VM_LIST_DESC = "List of VM's.";
    }

    public static class CreateVM {
        public static final String CREATE_VM_OPERATION_DESC = "Creates a virtual machine with specified " +
                "configuration. This is an asynchronous operation that results in the creation of a task object. " +
                "The UUID of this task object is returned as the response of this operation. " +
                "This task can be monitored by using the /tasks/poll API.";
        public static final String VM_NAME_DESC = "Name of the virtual machine that will be created.";
        public static final String VM_DESCRIPTION_DESC = "The description of the virtual machine.";
        public static final String VM_MEMORY_SIZE_DESC = "The memory amount (in GiB) attached to the virtual machine " +
                "that will will be created.";
        public static final String NUM_VCPUS_DESC = "The number of processors of the virtual machine.";
        public static final String NUM_CORES_PER_VCPU_DESC = "The number of cores per vCPU of the virtual machine.";
        public static final String TIME_ZONE_DESC = "The timezone for the VM's hardware clock. Any updates to the " +
                "timezone will be applied during the next VM power cycle." +
                "Default : 'UTC'";
        public static final String HYPERVISOR_TYPE_DESC = "The type of hypervisor." +
                "Example : ACROPOLIS.";
        public static final String FLASH_MODE_ENABLED_DESC = "State of the storage policy to pin virtual disks to" +
                " the hot tier. When specified as a VM attribute, the storage policy applies to all virtual disks of " +
                "the VM unless overridden by the same attribute specified for a virtual disk." +
                "Default : 'false'";
        public static final String IS_SCSI_PASS_THROUGH_DESC = "Whether the SCSI disk should be attached in " +
                "passthrough mode to pass all SCSI commands directly to Stargate via iSCSI." +
                "Default : 'true'";
        public static final String IS_THIN_PROVISIONED_DESC = "If the value is 'true' then virtual machine creates " +
                "with thin provision." +
                "Default : 'false'";
        public static final String IS_CDROM_DESC = "If the value is 'true' then virtual machine creates with CD-ROM, " +
                "if the value is 'false' virtual machine creates with empty disk.";
        public static final String IS_EMPTY_DISK_DESC = "If the value is 'true' then virtual machine will created " +
                "with empty disk." +
                "Default : 'true'";
        public static final String DEVICE_BUS_DESC = "The device bus for the virtual disk device." +
                "Valid values: 'sata, scsi, ide, pci, spapr'.";
        public static final String DISK_LABEL_DESC = "The label for the disk.";
        public static final String DEVICE_INDEX_DESC = "The index of the disk device." +
                "Default : '0'";
        public static final String NDFS_FILE_PATH_DESC = "The reference ndfs file location from which the disk will be " +
                "created.";
        public static final String SOURCE_VM_DISK_UUID_DESC = "The reference disk UUID from which new disk will be " +
                "created.";
        public static final String VM_DISK_MINIMUM_SIZE_DESC = "The size of reference disk in GiB." +
                "Default : '0'";
        public static final String EXTERNAL_DISK_URL_DESC = "The URL of the external reference disk which will be " +
                "used to create a new disk.";
        public static final String EXTERNAL_DISK_SIZE_DESC = "The size of the external disk to be created." +
                "Default : '0'";
        public static final String STORAGE_CONTAINER_UUID_DESC = "The reference storage container UUID on which disk " +
                "will be created.";
        public static final String VM_DISK_SIZE_DESC = "The size of disk in GiB." +
                "Default : '0'";
        public static final String NETWORK_UUID_DESC = "The network UUID which will be attached to the virtual machine";
        public static final String REQUESTED_IP_ADDRESS_DESC = "The static IP address that assigns to " +
                "the virtual machine.";
        public static final String IS_CONNECTED_DESC = "If the value of this property is 'true' the network will be " +
                "connected while booting the virtual machine.";
        public static final String HOST_UUIDS_DESC = "The UUID identifying the host on which the virtual machine is " +
                "currently running. If virtual machine is powered off, then this field is empty.";
        public static final String AGENT_VM_DESC = "Indicates whether the VM is an agent VM. When their host enters " +
                "maintenance mode, after normal VMs are evacuated, agent VMs are powered off. When the host is " +
                "restored, agent VMs are powered on before normal VMs are restored. In other words, agent VMs cannot " +
                "be HA-protected or live migrated." +
                "Default : 'false'";
    }

    public static class DeleteVM {
        public static final String DELETE_VM_OPERATION_DESC = "Deletes a virtual machine." +
                "This is an idempotent operation. If the virtual machine is currently powered on, it will be " +
                "forcefully powered off." +
                "The logical timestamp can optionally be provided for consistency. If a logical timestamp is " +
                "specified, then this operation will be rejected if the logical timestamp specified is not the value" +
                " of the virtual machine logical timestamp. The logical timestamp can be obtained from the virtual" +
                " machine object." +
                "This is an asynchronous operation that results in the creation of a task object. The UUID of " +
                "this task object is returned as the response of this operation. This task can be monitored by using " +
                "the /tasks/poll API.";
        public static final String DELETE_SNAPSHOTS_DESC = "Delete virtual machine snapshots";
        public static final String LOGICAL_TIMESTAMP_DESC = "The virtual logical timestamp of the virtual machine.";
    }

    public static class SetVMPowerState {
        public static final String SET_VM_POWER_STATE_OPERATION_DESC = "Set power state of a virtual machine." +
                "If the virtual machine is being powered on and no host is specified, the scheduler will pick the " +
                "one with the most available CPU and memory that can support the virtual machine. Note that no such " +
                "host may not be available." +
                "If the virtual machine is being power cycled, a different host can be specified to start it on." +
                "The logical timestamp can optionally be provided for consistency. If a logical timestamp is " +
                "specified, then this operation will be rejected if the logical timestamp specified is not the " +
                "value of the virtual machine logical timestamp. The logical timestamp can be obtained from the " +
                "virtual machine object.";
        public static final String HOST_UUID_DESC = "The host UUID on which to power on the VM. This parameter is " +
                "only honored for kPowerOn, or kPowerCycle when change host is also requested explicitly.";
        public static final String POWER_STATE_DESC = "The desired power state of the virtual machine." +
                " Allowed Values: 'ON', 'OFF', 'POWERCYCLE', 'RESET', 'PAUSE', 'SUSPEND', 'RESUME', 'SAVE', " +
                "'ACPI_SHUTDOWN', 'ACPI_REBOOT'";
        public static final String VM_LOGICAL_TIMESTAMP_DESC = "The virtual logical timestamp of the virtual machine.";
    }

    public static class GetTaskDetails {
        public static final String GET_TASK_DETAILS_OPERATION_DESC = "Gets the details of the specified task.";
        public static final String INCLUDE_SUBTASKS_INFO_DESC = "Whether to include a detailed information of the " +
                "immediate subtasks. " +
                "Default: 'false'";
        public static final String TASK_STATUS_DESC = "Status of the task.";

    }

    public static class DetachDisks {
        public static final String DETACH_DISKS_OPERATION_DESC = "Detaches disks from virtual machine.";
        public static final String VM_DISK_UUID_LIST_DESC = "The VM disk UUID list. If multiple disks need to be " +
                "removed, add comma separated UUIDs.";
        public static final String DEVICE_BUS_LIST_DESC = "The device bus List. List the device buses in the same " +
                "order that the disk UUIDs are listed, separated by commas."
                + "Valid values: sata,scsi,ide,pci";
        public static final String DEVICE_INDEX_LIST_DESC = "The device indices list. List the device indices in the " +
                "same order that the disk UUIDs are listed, separated by commas.";
    }

    public static class AttachDisks {
        public static final String ATTACH_DISKS_OPERATION_DESC = "Attaches disks to virtual machine. A disk drive may " +
                "either be a regular disk drive, or a CD-ROM drive. Only CD-ROM drives may be empty.";
        public static final String DEVICE_BUS_LIST_DESC = "The device bus List. List the device buses in the same  " +
                "order that the disk UUIDs are listed, separated by commas."
                + "Valid values: sata,scsi,ide,pci";
        public static final String IS_CDROM_LIST_DESC = "Whether disk drive is CD-ROM drive or disk drive. If " +
                "multiple disks needs to attach to the virtual machine, add comma separated boolean values." +
                "Example: To create 2 CD-ROM dives need to provide input value as 'true,true'.";
        public static final String IS_EMPTY_DISK_LIST_DESC = "Whether the drive should be empty. This field only " +
                "applies to CD-ROM drives, otherwise it is ignored. If multiple empty CD-ROM disks needs to attach " +
                "to the virtual machine, add comma separated boolean values." +
                "Example: To create 2 empty CD-ROM dives need to provide input value as 'true,true'.";
        public static final String SOURCE_VM_DISK_UUID_LIST_DESC = "The source VM disk UUID List. If multiple disks " +
                "need to be attached to the virtual machine, add comma separated UUIDs.";
        public static final String VM_DISK_MINIMUM_SIZE_LIST_DESC = "The minimum size of the disk. If multiple disks " +
                "need to be attached to the virtual machine, add comma separated disk sizes in GiB.";
        public static final String NDFS_FILE_PATH_LIST_DESC = "NDFS path to existing virtual disk. List the path in " +
                "the same order as of isCDROM is listed, separated by commas.";
        public static final String DEVICE_INDEX_LIST_DESC = "The device indices list. List the device indices in the " +
                "same order that the disk UUIDs are listed, separated by commas.";
        public static final String VM_DISK_SIZE_LIST_DESC = "The size of the each disk in GiB, to be attached to the " +
                "VM.";
        public static final String STORAGE_CONTAINER_UUID_DISK_LIST_DESC = "The storage container UUID in which each " +
                "disk is created. ";
        public static final String IS_SCSI_PASS_THROUGH_LIST_DESC = "Whether the SCSI disk should be attached in " +
                "passthrough mode to pass all SCSI commands directly to Stargate via iSCSI. Provide a list of comma" +
                " separated boolean values.";
        public static final String IS_THIN_PROVISIONED_LIST_DESC = "If the value is 'true' then virtual machine " +
                "creates with thin provision. Provide a list of comma separated boolean values.";
        public static final String IS_FLASH_MODE_ENABLED_LIST_DESC = "If the value is 'true' then flash mode will be " +
                "enabled for the particular disk. Provide a list of comma separated boolean values.";
    }

    public static class DeleteNIC {
        public static final String DELETE_NIC_OPERATION_DESC = "Deletes a NIC from a virtual machine.";
        public static final String VM_LOGICAL_TIMESTAMP_DESC = "The virtual logical timestamp of the virtual machine.";
        public static final String NIC_MAC_ADDRESS_DESC = "The MAC address of virtual machine NIC identifier.";
    }

    public static class CreateNic {
        public static final String NETWORK_UUID_DESC = "The network UUID which will be attached to the virtual machine";
        public static final String REQUESTED_IP_ADDRESS_DESC = "The static IP address that assigns to " +
                "the virtual machine.";
        public static final String IS_CONNECTED_DESC = "If the value of this property is 'true' the network will be " +
                "connected while booting the virtual machine.";
        public static final String VLAN_ID_DESC = "The vlan id of the network.";
        public static final String ADD_NIC_OPERATION_DESC = "Adds a NIC to a virtual machine. " +
                "A VM NIC must be associated with a virtual network. It is not possible to change this association." +
                "To connect a VM to a different virtual network, it is necessary to create a new NIC";
    }


    public static class Counter {
        public static final String FAILURE_MESSAGE = "Something went wrong";
        public static final String COUNTER_DESC = "Counts from one number to another number.";
        public static final String RESULT_STRING_DESC = "The primary result is resultString, Result can also be used. " +
                "result (All lower case) should not be used as it is the response code.";
        public static final String RESULT_DESC = "If successful, returns the complete API response. In case of an " +
                "error this output will contain the error message.";
        public static final String FROM_DESC = "The number to start counting at.";
        public static final String TO_DESC = "The number to count to.";
        public static final String RESET_DESC = "If true, then the counter will restart counting from the beginning.";
        public static final String INCREMENT_BY_DESC = "The number to increment by while counting. If unspecified " +
                "this is 1. If you wanted to count 2,4,6,8 this would be 2.";
    }
}
