/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.amazon.entities.constants;

/**
 * Created by Mihai Tusa.
 * 2/17/2016.
 */
public class Inputs {
    private Inputs() {
        // prevent instantiation
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
        public static final String PREFIX = "prefix";
        public static final String QUERY_PARAMS = "queryParams";
        public static final String VERSION = "version";
        public static final String REGION = "region";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String POLLING_INTERVAL = "pollingInterval";
        public static final String ASYNC = "async";
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
        public static final String KEY_FILTERS_STRING = "keyFiltersString";
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
        public static final String REGIONS_STRING = "regionsString";
        public static final String RESOURCE_IDS_STRING = "resourceIdsString";
        public static final String ROOT_DEVICE_NAME = "rootDeviceName";
        public static final String ROOT_DEVICE_TYPE = "rootDeviceType";
        public static final String STATE_REASON_CODE = "stateReasonCode";
        public static final String STATE_REASON_MESSAGE = "stateReasonMessage";
        public static final String SUBNET_ID = "subnetId";
        public static final String URI = "uri";
        public static final String VALUE_FILTERS_STRING = "valueFiltersString";
        public static final String VALUE_TAGS_STRING = "valueTagsString";
        public static final String VIRTUALIZATION_TYPE = "virtualizationType";
        public static final String VOLUME_ID = "volumeId";
        public static final String VOLUME_SIZE = "volumeSize";
        public static final String VOLUME_TYPE = "volumeType";
        public static final String VPC_ID = "vpcId";
        public static final String ZONE_NAMES_STRING = "zoneNamesString";
        public static final String FUNCTION_NAME = "function";
        public static final String FUNCTION_QUALIFIER = "qualifier";
        public static final String FUNCTION_PAYLOAD = "functionPayload";
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

    public static class LoadBalancerInputs {
        public static final String ARNS_STRING = "arnsString";
        public static final String LOAD_BALANCER_ARN = "loadBalancerArn";
        public static final String LOAD_BALANCER_NAME = "loadBalancerName";
        public static final String MARKER = "marker";
        public static final String MEMBER_NAMES_STRING = "memberNamesString";
        public static final String PAGE_SIZE = "pageSize";
        public static final String SCHEME = "schema";
    }

    public static class NetworkInputs {
        public static final String AMAZON_PROVIDED_IPV6_CIDR_BLOCK = "amazonProvidedIpv6CidrBlock";
        public static final String CIDR_BLOCK = "cidrBlock";
        public static final String DEVICE_INDEX = "deviceIndex";
        public static final String FORCE_DETACH = "forceDetach";
        public static final String NETWORK_INTERFACE_DELETE_ON_TERMINATION = "networkInterfaceDeleteOnTermination";
        public static final String NETWORK_INTERFACE_DESCRIPTION = "networkInterfaceDescription";
        public static final String NETWORK_INTERFACE_DEVICE_INDEX = "networkInterfaceDeviceIndex";
        public static final String NETWORK_INTERFACE_ID = "networkInterfaceId";
        public static final String NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS = "networkInterfaceAssociatePublicIpAddress";
        public static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "secondaryPrivateIpAddressCount";
        public static final String SUBNET_IDS_STRING = "subnetIdsString";
        public static final String FILTER_ADDRESSES_PRIVATE_IP_ADDRESS = "filterAddressesPrivateIpAddress";
        public static final String FILTER_ADDRESSES_PRIMARY = "filterAddressesPrimary";
        public static final String FILTER_ADDRESSES_ASSOCIATION_PUBLIC_IP = "filterAddressesAssociationPublicIp";
        public static final String FILTER_ADDRESSES_ASSOCIATION_OWNER_ID = "filterAddressesAssociationOwnerId";
        public static final String FILTER_ASSOCIATION_ASSOCIATION_ID = "filterAssociationAssociationId";
        public static final String FILTER_ASSOCIATION_ALLOCATION_ID = "filterAssociationAllocationId";
        public static final String FILTER_ASSOCIATION_IP_OWNER_ID = "filterAssociationIpOwnerId";
        public static final String FILTER_ASSOCIATION_PUBLIC_IP = "filterAssociationPublicIp";
        public static final String FILTER_ASSOCIATION_PUBLIC_DNS_NAME = "filterAssociationPublicDnsName";
        public static final String FILTER_ATTACHMENT_ATTACHMENT_ID = "filterAttachmentAttachmentId";
        public static final String FILTER_ATTACHMENT_ATTACH_TIME = "filterAttachmentAttachTime";
        public static final String FILTER_ATTACHMENT_DELETE_ON_TERMINATION = "filterAttachmentDeleteOnTermination";
        public static final String FILTER_ATTACHMENT_DEVICE_INDEX = "filterAttachmentDeviceIndex";
        public static final String FILTER_ATTACHMENT_INSTANCE_ID = "filterAttachmentInstanceId";
        public static final String FILTER_ATTACHMENT_INSTANCE_OWNER_ID = "filterAttachmentInstanceOwnerId";
        public static final String FILTER_ATTACHMENT_NAT_GATEWAY_ID = "filterAttachmentNatGatewayId";
        public static final String FILTER_ATTACHMENT_STATUS = "filterAttachmentStatus";
        public static final String FILTER_AVAILABILITY_ZONE = "filterAvailabilityZone";
        public static final String FILTER_DESCRIPTION = "filterDescription";
        public static final String FILTER_GROUP_ID = "filterGroupId";
        public static final String FILTER_GROUP_NAME = "filterGroupName";
        public static final String FILTER_IPV6_ADDRESSES_IPV6_ADDRESS = "filterIpv6AddressesIpv6Address";
        public static final String FILTER_MAC_ADDRESS = "filterMacAddress";
        public static final String FILTER_NETWORK_INTERFACE_ID = "filterNetworkInterfaceId";
        public static final String FILTER_OWNER_ID = "filterOwnerId";
        public static final String FILTER_PRIVATE_IP_ADDRESS = "filterPrivateIpAddress";
        public static final String FILTER_PRIVATE_DNS_NAME = "filterPrivateDnsName";
        public static final String FILTER_REQUESTER_ID = "filterRequesterId";
        public static final String FILTER_REQUESTER_MANAGED = "filterRequesterManaged";
        public static final String FILTER_SOURCE_DEST_CHECK = "filterSourceDestCheck";
        public static final String FILTER_STATUS = "filterStatus";
        public static final String FILTER_SUBNET_ID = "filterSubnetId";
        public static final String FILTER_TAG = "filterTag";
        public static final String FILTER_TAG_KEY = "filterTagKey";
        public static final String FILTER_TAG_VALUE = "filterTagValue";
        public static final String FILTER_VPC_ID = "filterVpcId";
    }

    public static class StorageInputs {
        public static final String BUCKET_NAME = "bucketName";
        public static final String CONTINUATION_TOKEN = "continuationToken";
        public static final String ENCODING_TYPE = "encodingType";
        public static final String FETCH_OWNER = "fetchOwner";
        public static final String MAX_KEYS = "maxKeys";
        public static final String PREFIX = "prefix";
        public static final String START_AFTER = "startAfter";
    }

    public static class VolumeInputs {
        public static final String DEVICE_NAME = "deviceName";
        public static final String ENCRYPTED = "encrypted";
        public static final String FORCE = "force";
        public static final String IOPS = "iops";
        public static final String SIZE = "size";
        public static final String SNAPSHOT_DESCRIPTION = "snapshotDescription";
        public static final String SNAPSHOT_ID = "snapshotId";
        public static final String VOLUME_IDS_STRING = "volumeIdsString";
        public static final String FILTER_ATTACHMENT_ATTACH_TIME = "filterAttachmentAttachTime";
        public static final String FILTER_ATTACHMENT_DELETE_ON_TERMINATION = "filterAttachmentDeleteOnTermination";
        public static final String FILTER_ATTACHMENT_DEVICE = "filterAttachmentDevice";
        public static final String FILTER_ATTACHMENT_INSTANCE_ID = "filterAttachmentInstanceId";
        public static final String FILTER_ATTACHMENT_STATUS = "filterAttachmentStatus";
        public static final String FILTER_AVAILABILITY_ZONE = "filterAvailabilityZone";
        public static final String FILTER_CREATE_TIME = "filterCreateTime";
        public static final String FILTER_ENCRYPTED = "filterEncrypted";
        public static final String FILTER_SIZE = "filterSize";
        public static final String FILTER_SNAPSHOT_ID = "filterSnapshotId";
        public static final String FILTER_STATUS = "filterStatus";
        public static final String FILTER_TAG = "filterTag";
        public static final String FILTER_TAG_KEY = "filterTagKey";
        public static final String FILTER_TAG_VALUE = "filterTagValue";
        public static final String FILTER_VOLUME_ID = "filterVolumeId";
        public static final String FILTER_VOLUME_TYPE = "filterVolumeType";
    }

    public static class TagsInputs {
        public static final String FILTER_KEY = "filterKey";
        public static final String FILTER_RESOURCE_ID = "filterResourceId";
        public static final String FILTER_RESOURCE_TYPE = "filterResourceType";
        public static final String FILTER_VALUE = "filterValue";
    }

    public static class CloudFormationInputs {
        public static final String STACK_NAME = "stackName";
        public static final String TEMPLATE_BODY = "templateBody";
        public static final String PARAMETERS = "parameters";
        public static final String CAPABILITIES = "capabilities";
    }

    public static class SNSInputs {
        public static final String TOPIC_NAME = "topicName";
    }

    public static class ServiceCatalogInputs {
        public static final String PRODUCT_ID = "productId";
        public static final String PROVISIONED_PRODUCT_NAME = "provisionedProductName";
        public static final String PROVISIONING_ARTIFACT_ID = "provisioningArtifactId";
        public static final String PROVISIONING_PARAMETERS = "provisioningParameters";
        public static final String TAGS = "tags";
        public static final String PROVISION_TOKEN = "provisionToken";
        public static final String ACCEPT_LANGUAGE = "acceptLanguage";
        public static final String NOTIFICATION_ARNS = "notificationArns";
        public static final String PATH_ID = "pathId";
        public static final String ACCEPTED_LANGUAGE = "acceptedLanguage";
        public static final String PROVISIONED_PRODUCT_ID = "provisionedProductId";
        public static final String UPDATE_TOKEN = "updateToken";
    }

}
