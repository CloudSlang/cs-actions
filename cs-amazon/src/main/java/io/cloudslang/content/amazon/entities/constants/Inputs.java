package io.cloudslang.content.amazon.entities.constants;

/**
 * Created by Mihai Tusa.
 * 2/17/2016.
 */
public class Inputs {
    private Inputs(){

    }

    public static class CommonInputs {
        public static final String CREDENTIAL = "credential";
        public static final String DELIMITER = "delimiter";
        public static final String ENDPOINT = "endpoint";
        public static final String HEADERS = "headers";
        public static final String IDENTITY = "identity";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String QUERY_PARAMS = "queryParams";
        public static final String VERSION = "version";
    }

    public static class CustomInputs {
        public static final String ALLOCATION_ID = "allocationId";
        public static final String AMAZON_API = "amazonApi";
        public static final String ARCHITECTURE = "architecture";
        public static final String ASSOCIATION_ID = "associationId";
        public static final String ATTACHMENT_ID = "attachmentId";
        public static final String AVAILABILITY_ZONE = "availabilityZone";
        public static final String BLOCK_DEVICE_MAPPING_SNAPSHOT_ID = "blockDeviceMappingSnapshotId";
        public static final String BLOCK_MAPPING_DEVICE_NAME = "blockMappingDeviceName";
        public static final String DATE = "date";
        public static final String DELETE_ON_TERMINATION = "deleteOnTermination";
        public static final String DOMAIN = "domain";
        public static final String HOST_ID = "hostId";
        public static final String HTTP_VERB = "httpVerb";
        public static final String HYPERVISOR = "hypervisor";
        public static final String IDENTITY_ID = "identityId";
        public static final String IMAGE_ID = "imageId";
        public static final String INSTANCE_ID = "instanceId";
        public static final String INSTANCE_TYPE = "instanceType";
        public static final String KERNEL_ID = "kernelId";
        public static final String KEY_TAGS_STRING = "keyTagsString";
        public static final String KMS_KEY_ID = "kmsKeyId";
        public static final String OWNER_ALIAS = " ownerAlias ";
        public static final String OWNER_ID = "ownerId";
        public static final String PAYLOAD_HASH = "payloadHash";
        public static final String PLATFORM = "platform";
        public static final String PRODUCT_CODE = "productCode";
        public static final String PRODUCT_CODE_TYPE = "productCodeType";
        public static final String RAMDISK_ID = "ramdiskId";
        public static final String RESOURCE_IDS_STRING = "resourceIdsString";
        public static final String ROOT_DEVICE_NAME = "rootDeviceName";
        public static final String ROOT_DEVICE_TYPE = "rootDeviceType";
        public static final String STATE_REASON_CODE = "stateReasonCode";
        public static final String STATE_REASON_MESSAGE = "stateReasonMessage";
        public static final String SUBNET_ID = "subnetId";
        public static final String URI = "uri";
        public static final String VALUE_TAGS_STRING = "valueTagsString";
        public static final String VIRTUALIZATION_TYPE = "virtualizationType";
        public static final String VOLUME_ID = "volumeId";
        public static final String VOLUME_SIZE = "volumeSize";
        public static final String VOLUME_TYPE = "volumeType";
        public static final String KEY_FILTERS_STRING = "keyFiltersString";
        public static final String VALUE_FILTERS_STRING = "valueFiltersString";
        public static final String REGIONS_STRING = "regionsString";
        public static final String ZONE_NAMES_STRING = "zoneNamesString";
    }

    public static class ElasticIpInputs {
        public static final String ALLOW_REASSOCIATION = "allowReassociation";
        public static final String PUBLIC_IP = "publicIp";
        public static final String PRIVATE_IP_ADDRESS = "privateIpAddress";
        public static final String PRIVATE_IP_ADDRESSES_STRING = "privateIpAddressesString";
    }

    public static class EbsInputs {
        public static final String EBS_OPTIMIZED = "ebsOptimized";
        public static final String BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING = "blockDeviceMappingDeviceNamesString";
        public static final String BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING = "blockDeviceMappingVirtualNamesString";
        public static final String DELETE_ON_TERMINATIONS_STRING = "deleteOnTerminationsString";
        public static final String ENCRYPTED_STRING = "encryptedString";
        public static final String NO_DEVICES_STRING = "noDevicesString";
        public static final String IOPS_STRING = "iopsString";
        public static final String SNAPSHOT_IDS_STRING = "snapshotIdsString";
        public static final String VOLUME_IDS_STRING = "volumeIdsString";
        public static final String VOLUME_SIZES_STRING = "volumeSizesString";
        public static final String VOLUME_TYPES_STRING = "volumeTypesString";
    }

    public static class IamInputs {
        public static final String IAM_INSTANCE_PROFILE_ARN = "iamInstanceProfileArn";
        public static final String IAM_INSTANCE_PROFILE_NAME = "iamInstanceProfileName";
        public static final String KEY_PAIR_NAME = "keyPairName";
        public static final String SECURITY_GROUP_IDS_STRING = "securityGroupIdsString";
        public static final String SECURITY_GROUP_NAMES_STRING = "networkInterfaceGroupNamesString";
        public static final String SECURITY_TOKEN = "securityToken";
    }

    public static class ImageInputs {
        public static final String IDS_STRING = "idsString";
        public static final String IMAGE_DESCRIPTION = "description";
        public static final String IS_PUBLIC = "isPublic";
        public static final String MANIFEST_LOCATION = "manifestLocation";
        public static final String NAME = "name";
        public static final String NO_REBOOT = "noReboot";
        public static final String OWNERS_STRING = "ownersString";
        public static final String STATE = "state";
        public static final String TYPE = "type";
        public static final String USER_GROUPS_STRING = "userGroupsString";
        public static final String USER_IDS_STRING = "userIdsString";
    }

    public static class InstanceInputs {
        public static final String AFFINITY = "affinity";
        public static final String ATTRIBUTE = "attribute";
        public static final String ATTRIBUTE_VALUE = "attributeValue";
        public static final String CLIENT_TOKEN = "clientToken";
        public static final String ENA_SUPPORT = "enaSupport";
        public static final String FILTER_NAMES_STRING = "filterNamesString";
        public static final String FILTER_VALUES_STRING = "filterValuesString";
        public static final String FORCE_STOP = "forceStop";
        public static final String INSTANCE_IDS_STRING = "instanceIdsString";
        public static final String LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR = "instanceInitiatedShutdownBehavior";
        public static final String LOWER_CASE_DISABLE_API_TERMINATION = "disableApiTermination";
        public static final String LOWER_CASE_KERNEL = "kernel";
        public static final String LOWER_CASE_RAMDISK = "ramdisk";
        public static final String LOWER_CASE_USER_DATA = "userData";
        public static final String MAX_COUNT = "maxCount";
        public static final String MAX_RESULTS = "maxResults";
        public static final String MIN_COUNT = "minCount";
        public static final String MONITORING = "monitoring";
        public static final String NEXT_TOKEN = "nextToken";
        public static final String PLACEMENT_GROUP_NAME = "placementGroupName";
        public static final String SOURCE_DESTINATION_CHECK = "sourceDestinationCheck";
        public static final String SRIOV_NET_SUPPORT = "sriovNetSupport";
        public static final String TENANCY = "tenancy";
    }

    public static class LoadBalancingInputs {
        public static final String LISTENER_INSTANCE_PORTS_STRING = "listenerInstancePortsString";
        public static final String LISTENER_INSTANCE_PROTOCOLS_STRING = "listenerInstanceProtocolsString";
    }

    public static class NetworkInputs {
        public static final String DEVICE_INDEX = "deviceIndex";
        public static final String FORCE_DETACH = "forceDetach";
        public static final String NETWORK_INTERFACE_DELETE_ON_TERMINATION = "networkInterfaceDeleteOnTermination";
        public static final String NETWORK_INTERFACE_DESCRIPTION = "networkInterfaceDescription";
        public static final String NETWORK_INTERFACE_DEVICE_INDEX = "networkInterfaceDeviceIndex";
        public static final String NETWORK_INTERFACE_ID = "networkInterfaceId";
        public static final String NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS = "networkInterfaceAssociatePublicIpAddress";
        public static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "secondaryPrivateIpAddressCount";
    }

    public static class VolumeInputs {
        public static final String DEVICE_NAME = "deviceName";
        public static final String ENCRYPTED = "encrypted";
        public static final String FORCE = "force";
        public static final String IOPS = "iops";
        public static final String SIZE = "size";
        public static final String SNAPSHOT_DESCRIPTION = "snapshotDescription";
        public static final String SNAPSHOT_ID = "snapshotId";
    }
}