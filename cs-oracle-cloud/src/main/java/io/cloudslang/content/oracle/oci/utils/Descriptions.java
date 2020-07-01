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

package io.cloudslang.content.oracle.oci.utils;

public class Descriptions {
    public static class Common {
        public static final String TENANCY_OCID_DESC = "Oracle creates a tenancy for your company, which is a secure and isolated partition where you can create, organize, and administer your cloud resources. This is ID of the tenancy.";
        public static final String USER_OCID_DESC = "ID of an individual employee or system that needs to manage or use your company’s Oracle Cloud Infrastructure resources.";
        public static final String FINGER_PRINT_DESC = "Finger print of the public key generated for OCI account.";
        public static final String PRIVATE_KEY_DATA_DESC = "A string representing the private key for the OCI. This string is usually the content of a private key file.";
        public static final String PRIVATE_KEY_FILE_DESC = "The path to the private key file on the machine where is the worker. ";
        public static final String API_VERSION_DESC = "Version of the API of OCI." +
                "Default: '20160918'";
        public static final String REGION_DESC = "The region's name.";
        public static final String PROXY_HOST_DESC = "Proxy server used to access the OCI.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the OCI." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username " +
                "input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it." +
                "Default: 'false'";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\"." +
                "Default: 'strict'";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESC = "The pathname of the Java KeyStore file. You only need this if the" +
                "server requires client authentication. If the protocol (specified by the 'url') is not 'https' or if " +
                "trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)" +
                "Default: <OO_Home>/java/lib/security/cacerts";
        public static final String KEYSTORE_PASSWORD_DESC = "The password associated with the KeyStore file. If " +
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied." +
                "Default: changeit";
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
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that " +
                "verifies if the operation finished is executed." +
                "Default: '1000'";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: '10000'";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP " +
                "response. If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header " +
                "will be used. If responseCharacterSet is empty and the charset from the HTTP response Content-Type " +
                "header is empty, the default value will be used. You should not use this for method=HEAD or OPTIONS." +
                "Default: 'UTF-8'";
        public static final String STATUS_CODE_DESC = "The HTTP status code for OCI API request.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
        public static final String INSTANCE_ID_DESC = "The OCID of the instance.";
        public static final String INSTANCE_NAME_DESC = "The instance name.";
        public static final String VNIC_ID_DESC = "The OCID of the vnic.";
        public static final String VNIC_ATTACHMENT_ID_DESC = "The OCID of the VNIC attachment.";
        public static final String VOLUME_ID_DESC = "The OCID of the volume.";
        public static final String VOLUME_ATTACHMENT_ID_DESC = "The OCID of the volume attachment.";
        public static final String PAGE_DESC = "For list pagination. The value of the opc-next-page response header from the previous \"List\" call.";
        public static final String LIMIT_DESC = "For list pagination. The maximum number of results per page, or items to return in a paginated \"List\" call. ";
    }

    public static class ListInstances {
        public static final String LIST_INSTANCES_OPERATION_DESC = "Lists the instances in the specified compartment and " +
                "the specified availability domain. You can filter the results by specifying an instance name " +
                "(the list will include all the identically-named instances in the compartment).";
        public static final String INSTANCE_NAME_LIST_DESC = "List of all instance names.";
        public static final String COMPARTMENT_OCID_DESC = "Compartments are a fundamental component of Oracle Cloud Infrastructure for organizing and isolating your cloud resources. This is ID of the compartment.";
        public static final String DISPLAY_NAME_DESC = "A filter to return only resources that match the given display name exactly.";
        public static final String SORT_BY_DESC = "The field to sort by. You can provide one sort order (sortOrder). Default order for TIMECREATED is descending. Default order for DISPLAYNAME is ascending. The DISPLAYNAME sort order is case sensitive." +
                "Allowed values are:\n" +
                "TIMECREATED\n" +
                "DISPLAYNAME";
        public static final String SORT_ORDER_DESC = "The sort order to use, either ascending (ASC) or descending (DESC). The DISPLAYNAME sort order is case sensitive." + "\n" +
                "Allowed values are:\n" +
                "ASC\n" +
                "DESC";
        public static final String LIFECYCLE_STATE_DESC = "A filter to only return resources that match the given lifecycle state. The state value is case-insensitive.";
    }

    public static class GetInstanceDetails {
        public static final String GET_INSTANCE_DETAILS_OPERATION_DESC = "Gets information about the instance.";
        public static final String INSTANCE_STATE_DESC = "The current state of the instance.";
    }

    public static class TerminateInstance {
        public static final String TERMINATE_INSTANCE_OPERATION_DESC = "Terminates the specified instance. Any attached VNICs and volumes are automatically detached when the instance terminates.\n" +
                "To preserve the boot volume associated with the instance, specify true for PreserveBootVolumeQueryParam.To delete the boot volume when the instance is deleted, specify false or do not specify a value for PreserveBootVolume.";
        public static final String PRESERVE_BOOT_VOLUME_DESC = "Specifies whether to delete or preserve the boot volume when terminating an instance." +
                "Default: 'false'";
        public static final String TERMINATE_INSTANCE_SUCCESS_MESSAGE_DESC = "Instance terminated successfully.";
    }

    public static class InstanceAction {
        public static final String INSTANCE_ACTION_OPERATION_DESC = "Performs one of the following power actions on the specified instance: START - Powers on the instance.\n" +
                "\n" +
                "STOP - Powers off the instance.\n" +
                "\n" +
                "RESET - Powers off the instance and then powers it back on.";
        public static final String ACTION_NAME_DESC = "The action to perform on the instance.\n" +
                "\n" +
                "Allowed values are:\n" +
                "\n" +
                "STOP\n" +
                "START\n" +
                "RESET";
    }

    public static class ListVnicAttachments {
        public static final String LIST_VNIC_ATTACHMENTS_OPERATION_DESC = "Lists the VNIC attachments in the specified compartment. A VNIC attachment resides in the same compartment as the attached instance.The list can be filtered by instance, VNIC, or availability domain.";
        public static final String VNIC_LIST_DESC = "List of Vnics OCIDs.";
        public static final String VNIC_ATTACHMENTS_LIST_DESC = "List of Vnic attachment OCIDs.";
    }

    public static class GetVnicDetails {
        public static final String GET_VNIC_DETAILS_OPERATION_DESC = "Gets the information for the specified virtual network interface card (VNIC).";
        public static final String PRIVATE_IP_DESC = "The private IP address of the primary privateIp object on the VNIC. The address is within the CIDR of the VNIC's subnet.";
        public static final String PUBLIC_IP_DESC = "The public IP address of the VNIC.";
        public static final String VNIC_NAME_DESC = "Name of the VNIC.";
        public static final String VNIC_HOSTNAME_DESC = "The hostname for the VNIC's primary private IP. Used for DNS.";
        public static final String VNIC_STATE_DESC = "The current state of the VNIC.";
        public static final String MAC_ADDRESS_DESC = "The MAC address of the VNIC.";
    }

    public static class GetInstanceDefaultCredentials {
        public static final String GET_INSTANCE_DEFAULT_CREDENTIALS_OPERATION_DESC = "Gets the generated credentials for the instance. Only works for instances that require a password to log in, such as Windows.";
        public static final String INSTANCE_USERNAME_DESC = "Username of the instance.";
        public static final String INSTANCE_PASSWORD_DESC = "Password of the instance.";
    }

    public static class UpdateInstance {
        public static final String UPDATE_INSTANCE_OPERATION_NAME_DESC = "Updates certain fields on the specified " +
                "instance. Fields that are not provided in the request will not be updated.";
    }

    public static class DetachVnic {
        public static final String DETACH_VNIC_OPERATION_DESC = "Detaches and deletes the specified secondary VNIC. " +
                "This operation cannot be used on the instance's primary VNIC.";
        public static final String DETACH_VNIC_SUCCESS_MESSAGE_DESC = "Vnic detached and deleted successfully.";
    }

    public static class AttachVolume {
        public static final String ATTACH_VOLUME_OPERATION_DESC = "Attaches the specified storage volume to the " +
                "specified instance.";
        public static final String VOLUME_TYPE_DESC = "The type of volume. " +
                "Allowed values: ''iscsi' and 'paravirtualized''.";
        public static final String DEVICE_NAME_DESC = "The device name.";
        public static final String DISPLAY_NAME_DESC = "A user-friendly name. Does not have to be unique, and it " +
                "cannot be changed. Avoid entering confidential information.";
        public static final String IS_READ_ONLY_DESC = "Whether the attachment was created in read-only mode.";
        public static final String IS_SHAREABLE_DESC = "Whether the attachment should be created in shareable mode. " +
                "If an attachment is created in shareable mode, then other instances can attach the same volume, " +
                "provided that they also create their attachments in shareable mode. Only certain volume types can " +
                "be attached in shareable mode. Defaults to false if not specified.";
        public static final String LIFECYCLE_STATE_DESC = "The current state of the volume attachment.";
    }

    public static class GetVolumeAttachmentDetails {
        public static final String GET_VOLUME_ATTACHMENT_DETAILS_OPERATION_DESC = "Gets information about the " +
                "specified volume attachment.";

    public static class AttachVnic {
        public static final String ATTACH_VNIC_OPERATION_DESC = "Creates a secondary VNIC and attaches it to the specified instance.";
        public static final String NIC_INDEX_DESC = "Which physical network interface card (NIC) the VNIC will use. Defaults to 0. Certain bare metal instance shapes have two active physical NICs (0 and 1). If you add a secondary VNIC to one of these instances, you can specify which NIC the VNIC will use.";
        public static final String VNIC_ATTACHMENT_DISPLAY_NAME_DESC = "A user-friendly name for the attachment. Does not have to be unique, and it cannot be changed.";
        public static final String VNIC_ID_DESC = "The OCID of the vnic.";
        public static final String VNIC_ATTACHMENTS_ID_DESC = "The OCID of the vnic attachment.";
        public static final String VNIC_ATTACHMENTS_STATE_DESC = "Life cycle state of the vnic attachment.";
    }

    public static class GetVnicAttachmentDetails {
        public static final String GET_VNIC_ATTACHMENT_DETAILS_OPERATION_NAME_DESC = "Gets the information for the specified VNIC attachment.";
    }

    public static class DetachVolume {
        public static final String DETACH_VOLUME_OPERATION_DESC = "Detaches a storage volume from an instance." +
                " You must specify the OCID of the volume attachment.";
        public static final String DETACH_VOLUME_SUCCESS_MESSAGE_DESC = "Volume detached successfully.";
    }

    public static class CreateInstance {
        public static final String CREATE_INSTANCE_OPERATION_NAME_DESC = "Creates a new instance in the specified " +
                "compartment and the specified availability domain.";
        public static final String IS_MANAGEMENT_DISABLED_DESC = "Whether the agent running on the instance can run " +
                "all the available management plugins." +
                "Default: 'false'";
        public static final String IS_MONITORING_DISABLED_DESC = "Whether the agent running on the instance can " +
                "gather performance metrics and monitor the instance" +
                "Default: 'false'";
        public static final String AVAILABILITY_DOMAIN_DESC = "The availability domain of the instance.";
        public static final String ASSIGN_PUBLIC_IP_DESC = "Whether the VNIC should be assigned a public IP address. " +
                "Defaults to whether the subnet is public or private.";
        public static final String DEFINED_TAGS_DESC = "Defined tags for this resource. Each key is predefined and " +
                "scoped to a namespace." +
                "Ex: {\"Operations\": {\"CostCenter\": \"42\"}}";

        public static final String DISPLAY_NAME_DESC = "A user-friendly name. Does not have to be unique, and it's " +
                "changeable" +
                "Ex: My bare metal instance";
        public static final String FREEFORM_TAGS_DESC = "Free-form tags for this resource. Each tag is a simple " +
                "key-value pair with no predefined name, type, or namespace." +
                "Ex: {\"Department\": \"Finance\"}";
        public static final String HOSTNAME_LABEL_DESC = "The hostname for the VNIC's primary private IP. Used for" +
                " DNS. The value is the hostname portion of the primary private IP's fully qualified domain name.";
        public static final String NETWORK_SECURITY_GROUP_IDS_DESC = "A list of the OCIDs of the network security " +
                "groups (NSGs) to add the VNIC to. Maximum allowed security groups are 5" +
                "Ex: [nsg1,nsg2]";
        public static final String PRIVATE_IP_DESC = "A private IP address of your choice to assign to the VNIC. " +
                "Must be an available IP address within the subnet's CIDR. If you don't specify a value, Oracle " +
                "automatically assigns a private IP address from the subnet. This is the VNIC's primary private " +
                "IP address.";
        public static final String SKIP_SOURCE_DEST_CHECK_DESC = "Whether the source/destination check is disabled " +
                "on the VNIC." +
                "Default: 'false'";
        public static final String SUBNET_ID_DESC = "The OCID of the subnet to create the VNIC in.";
        public static final String DEDICATED_VM_HOST_ID_DESC = "The OCID of the dedicated VM host.";
        public static final String VNIC_DEFINED_TAGS_DESC = "Defined tags for VNIC. Each key is predefined and " +
                "scoped to a namespace." +
                "Ex: {\"Operations\": {\"CostCenter\": \"42\"}}";
        public static final String VNIC_FREEFORM_TAGS_DESC = "Free-form tags for VNIC. Each tag is a simple key-value" +
                " pair with no predefined name, type, or namespace." +
                "Ex: {\"Department\": \"Finance\"}";
        ;
        public static final String VNIC_DISPLAY_NAME_DESC = "A user-friendly name for the VNIC. Does not have to be " +
                "unique.";
        public static final String EXTENDED_METADATA_DESC = "Additional metadata key/value pairs that you provide. " +
                "They serve the same purpose and functionality as fields in the 'metadata' object.\n" +
                "They are distinguished from 'metadata' fields in that these can be nested JSON objects (whereas " +
                "'metadata' fields are string/string maps only).";
        public static final String FAULT_DOMAIN_DESC = "A fault domain is a grouping of hardware and infrastructure" +
                " within an availability domain. Each availability domain contains three fault domains. Fault domains " +
                "let you distribute your instances so that they are not on the same physical hardware within a single availability domain. A hardware failure or Compute hardware maintenance that affects one fault domain does not affect instances in other fault domains.\n" +
                "If you do not specify the fault domain, the system selects one for you. ";
        public static final String IPXE_SCRIPT_DESC = "When a bare metal or virtual machine instance boots, the iPXE firmware that runs on the instance is configured to run an iPXE script to continue the boot process.\n" +
                "If you want more control over the boot process, you can provide your own custom iPXE script that will run when the instance boots; however, you should be aware that the same iPXE script will run every time an instance boots; not only after the initial LaunchInstance call.";
        public static final String IS_PV_ENCRYPTION_IN_TRANSIT_ENABLED_DESC = "Whether to enable in-transit encryption for the data volume's paravirtualized attachment." +
                "Default: 'false'";
        public static final String LAUNCH_MODE_DESC = "Specifies the configuration mode for launching virtual machine (VM) instances. The configuration modes are:\n" +
                "NATIVE - VM instances launch with iSCSI boot and VFIO devices. The default value for Oracle-provided images.\n" +
                "EMULATED - VM instances launch with emulated devices, such as the E1000 network driver and emulated SCSI disk controller.\n" +
                "PARAVIRTUALIZED - VM instances launch with paravirtualized devices using virtio drivers.\n" +
                "CUSTOM - VM instances launch with custom configuration settings specified in the LaunchOptions parameter.";
        public static final String BOOT_VOLUME_TYPE_DESC = "Emulation type for volume.\n" +
                "ISCSI - ISCSI attached block storage device.\n" +
                "SCSI - Emulated SCSI disk.\n" +
                "IDE - Emulated IDE disk.\n" +
                "VFIO - Direct attached Virtual Function storage. This is the default option for Local data volumes on Oracle provided images.\n" +
                "PARAVIRTUALIZED - Paravirtualized disk. This is the default for Boot Volumes and Remote Block Storage volumes on Oracle provided images.";
        public static final String FIRMWARE_DESC = "Firmware used to boot VM. Select the option that matches your operating system.\n" +
                "BIOS - Boot VM using BIOS style firmware. This is compatible with both 32 bit and 64 bit operating systems that boot using MBR style bootloaders.\n" +
                "UEFI_64 - Boot VM using UEFI style firmware compatible with 64 bit operating systems. This is the default for Oracle provided images.";
        public static final String IS_CONSISTENT_VOLUME_NAMING_ENABLED_DESC = "Whether to enable consistent volume naming feature. Defaults to false.";
        public static final String NETWORKTYPE_DESC = "Emulation type for the physical network interface card (NIC).\n" +
                "E1000 - Emulated Gigabit ethernet controller. Compatible with Linux e1000 network driver.\n" +
                "VFIO - Direct attached Virtual Function network controller. This is the networking type when you launch an instance using hardware-assisted (SR-IOV) networking.\n" +
                "PARAVIRTUALIZED - VM instances launch with paravirtualized devices using virtio drivers.";
        public static final String REMOTE_DATA_VOLUME_TYPE_DESC = "Emulation type for volume.\n" +
                "ISCSI - ISCSI attached block storage device.\n" +
                "SCSI - Emulated SCSI disk.\n" +
                "IDE - Emulated IDE disk.\n" +
                "VFIO - Direct attached Virtual Function storage. This is the default option for Local data volumes on Oracle provided images.\n" +
                "PARAVIRTUALIZED - Paravirtualized disk.This is the default for Boot Volumes and Remote Block Storage volumes on Oracle provided images.";
        public static final String SHAPE_DESC = "The shape of an instance. The shape determines the number of CPUs, amount of memory, and other resources allocated to the instance.";
//        public static final String METADATA_DESC = "Custom metadata key/value pairs that you provide, such as the SSH public key required to connect to the instance." +
//                "You can use the following metadata key names to provide information to Cloud-Init:\n" +
//                "\"ssh_authorized_keys\" - Provide one or more public SSH keys to be included in the ~/.ssh/authorized_keys file for the default user on the instance. Use a newline character to separate multiple keys. The SSH keys must be in the format necessary for the authorized_keys file, as shown in the example below.\n" +
//                "\"user_data\" - Provide your own base64-encoded data to be used by Cloud-Init to run custom scripts or provide custom Cloud-Init configuration." +
//                "Ex:  \"metadata\" : {\n" +
//                "     \"quake_bot_level\" : \"Severe\",\n" +
//                "     \"ssh_authorized_keys\" : \"ssh-rsa <your_public_SSH_key>== rsa-key-20160227\",\n" +
//                "     \"user_data\" : \"<your_public_SSH_key>==\"\n" +
//                "  }";

        public static final String SSH_AUTHORIZED_KEYS_DESC = " Provide one or more public SSH keys  for the default user on the instance. Use a newline character to separate multiple keys.";
        public static final String USERDATA_DESC = "Provide your own base64-encoded data to be used by Cloud-Init to run custom scripts or provide custom Cloud-Init configuration.";
        public static final String OCPUS_DESC = "The total number of OCPUs available to the instance.";
        public static final String SOURCE_TYPE_DESC = "The source type for the instance. Use image when specifying the image OCID. Use bootVolume when specifying the boot volume OCID.";
        public static final String BOOT_VOLUME_SIZE_IN_GBS_DESC = "The size of the boot volume in GBs. Minimum value is 50 GB and maximum value is 16384 GB (16TB).";
        public static final String IMAGE_ID_DESC = "The OCID of the image used to boot the instance. If the sourceType is 'image', then this value is required.";
        public static final String KMS_KEY_ID_DESC = "The OCID of the Key Management key to assign as the master encryption key for the boot volume.";
        public static final String BOOT_VOLUME_ID_DESC = "The OCID of the boot volume used to boot the instance. If the sourceType is 'bootVolume', then this value is required.";
    }


    public static class Counter {
        public static final String FAILURE_MESSAGE = "Something went wrong";
        public static final String COUNTER_DESC = "Counts from one number to another number.";
        public static final String RESULT_STRING_DESC = "The primary result is resultString, Result can also be used. result (All lower case) should not be used as it is the response code.";
        public static final String RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
        public static final String FROM_DESC = "The number to start counting at.";
        public static final String TO_DESC = "The number to count to.";
        public static final String RESET_DESC = "If true, then the counter will restart counting from the beginning.";
        public static final String INCREMENT_BY_DESC = "The number to increment by while counting. If unspecified this is 1. If you wanted to count 2,4,6,8 this would be 2.";


    }
}
