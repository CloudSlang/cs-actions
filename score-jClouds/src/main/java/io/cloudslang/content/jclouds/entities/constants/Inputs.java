package io.cloudslang.content.jclouds.entities.constants;

/**
 * Created by Mihai Tusa.
 * 2/17/2016.
 */
public class Inputs {
    public static final class CommonInputs {
        public static final String PROVIDER = "provider";
        public static final String ENDPOINT = "endpoint";
        public static final String IDENTITY = "identity";
        public static final String CREDENTIAL = "credential";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String DELIMITER = "delimiter";
    }

    public static final class CustomInputs {
        public static final String REGION = "region";
        public static final String INSTANCE_ID = "serverId";
        public static final String IMAGE_ID = "imageId";
        public static final String IDENTITY_ID = "identityId";
        public static final String VOLUME_ID = "volumeId";
        public static final String GROUP_ID = "groupId";
        public static final String HOST_ID = "hostId";
        public static final String KERNEL_ID = "kernelId";
        public static final String OWNER_ID = "ownerId";
        public static final String RAMDISK_ID = "ramdiskId";
        public static final String RESERVATION_ID = "reservationId";
        public static final String SUBNET_ID = "subnetId";
    }

    public static final class ImageInputs {
        public static final String IMAGE_NAME = "imageName";
        public static final String IMAGE_DESCRIPTION = "imageDescription";
        public static final String IMAGE_NO_REBOOT = "imageNoReboot";
        public static final String IMAGE_IDS_STRING = "imageIdsString";
        public static final String OWNERS_STRING = "ownersString";
        public static final String USER_IDS_STRING = "userIdsString";
        public static final String USER_GROUPS_STRING = "userGroupsString";
    }

    public static final class InstanceInputs {
        public static final String INSTANCE_TYPE = "instanceType";
        public static final String AVAILABILITY_ZONE = "availabilityZone";
        public static final String MIN_COUNT = "minCount";
        public static final String MAX_COUNT = "maxCount";
        public static final String OPERATION_TIMEOUT = "operationTimeout";
        public static final String POOLING_INTERVAL = "poolingInterval";
        public static final String AFFINITY = "affinity";
        public static final String ARCHITECTURE = "architecture";
        public static final String ATTACH_TIME = "attachTime";
        public static final String DELETE_ON_TERMINATION = "deleteOnTermination";
        public static final String DEVICE_NAME = "deviceName";
        public static final String STATUS = "status";
        public static final String CLIENT_TOKEN = "clientToken";
        public static final String DNS_NAME = "dnsName";
        public static final String GROUP_NAME = "groupName";
        public static final String HYPERVISOR = "hypervisor";
        public static final String IAM_ARN = "iamArn";
        public static final String INSTANCE_LIFECYCLE = "instanceLifecycle";
        public static final String INSTANCE_STATE_CODE = "instanceStateCode";
        public static final String INSTANCE_STATE_NAME = "instanceStateName";
        public static final String INSTANCE_GROUP_ID = "instanceGroupId";
        public static final String INSTANCE_GROUP_NAME = "instanceGroupName";
        public static final String IP_ADDRESS = "ipAddress";
        public static final String KEY_NAME = "keyName";
        public static final String LAUNCH_INDEX = "launchIndex";
        public static final String LAUNCH_TIME = "launchTime";
        public static final String MONITORING_STATE = "monitoringState";
        public static final String PLACEMENT_GROUP_NAME = "placementGroupName";
        public static final String PLATFORM = "platform";
        public static final String PRIVATE_DNS_NAME = "privateDnsName";
        public static final String PRIVATE_IP_ADDRESS = "privateIpAddress";
        public static final String PRODUCT_CODE = "productCode";
        public static final String PRODUCT_CODE_TYPE = "productCodeType";
        public static final String REASON = "reason";
        public static final String REQUESTER_ID = "requesterId";
        public static final String ROOT_DEVICE_NAME = "rootDeviceName";
        public static final String ROOT_DEVICE_TYPE = "rootDeviceType";
        public static final String SOURCE_DESTINATION_CHECK = "sourceDestinationCheck";
        public static final String SPOT_INSTANCE_REQUEST_ID = "spotInstanceRequestId";
        public static final String STATE_REASON_CODE = "stateReasonCode";
        public static final String STATE_REASON_MESSAGE = "stateReasonMessage";
    }
}