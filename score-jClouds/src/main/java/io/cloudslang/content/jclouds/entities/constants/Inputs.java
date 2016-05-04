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
        public static final String SERVER_ID = "serverId";
        public static final String IMAGE_REF = "imageRef";
        public static final String AVAILABILITY_ZONE = "availabilityZone";
        public static final String MIN_COUNT = "minCount";
        public static final String MAX_COUNT = "maxCount";
        public static final String SERVER_TYPE = "serverType";
        public static final String OPERATION_TIMEOUT = "operationTimeout";
        public static final String POOLING_INTERVAl = "poolingInterval";

    }

    public static final class CreateServerInputs {
        public static final String IMAGE_URI = "imageUri";
        public static final String HARDWARE_URI = "hardwareUri";
        public static final String PRODUCT_CODES = "productCodes";
        public static final String IMAGE_TAGS = "imageTags";
        public static final String GROUP = "group";
        public static final String IMAGE_PROVIDER_ID = "imageProviderId";
        public static final String IMAGE_NAME = "imageName";
        public static final String IMAGE_ID = "imageId";
        public static final String IMAGE_DESCRIPTION = "imageDescription";
        public static final String LOCATION_ID = "locationId";
        public static final String LOCATION_DESCRIPTION = "locationDescription";
        public static final String OS_FAMILY = "osFamily";
        public static final String OS_DESCRIPTION = "osDescription";
        public static final String HARDWARE_PROVIDER_ID = "hardwareProviderId";
        public static final String HARDWARE_NAME = "hardwareName";
        public static final String HARDWARE_ID = "hardwareId";
        public static final String HARDWARE_USER_METADATA_KEYS = "hardwareUserMetadataKeys";
        public static final String HARDWARE_USER_METADATA_VALUES = "hardwareUserMetadataValues";
        public static final String HARDWARE_TAGS = "hardwareTags";
        public static final String CORES_PER_PROCESSOR = "coresPerProcessor";
        public static final String PROCESSORS_GHZ_SPEED = "processorsGhzSpeed";
        public static final String VOLUME_ID = "volumeId";
        public static final String VOLUME_TYPE = "volumeType";
        public static final String VOLUME_NAME = "volumeName";
        public static final String HYPERVISOR = "hypervisor";
        public static final String INBOUND_PORTS = "inboundPorts";
        public static final String PUBLIC_KEY = "publicKey";
        public static final String PRIVATE_KEY = "privateKey";
        public static final String RUN_SCRIPT = "runScript";
        public static final String TEMPLATE_TAGS_STRING = "templateTagsString";
        public static final String NETWORKS_STRING = "networksString";
        public static final String NODE_NAMES = "nodeNames";
        public static final String SECURITY_GROUPS = "securityGroups";
        public static final String TEMPLATE_USER_METADATA_KEYS = "templateUserMetadataKeys";
        public static final String TEMPLATE_USER_METADATA_VALUES = "templateUserMetadataValues";
        public static final String VOLUME_GB_SIZE = "volumeGbSize";
        public static final String NODES_COUNT = "nodesCount";
        public static final String PROCESSORS_NUMBER = "processorsNumber";
        public static final String RAM_GB_AMOUNT = "ramGbAmount";
        public static final String BLOCK_PORT = "blockPort";
        public static final String BLOCK_PORT_SECONDS = "blockPortSeconds";
        public static final String IS_64_BIT = "is64Bit";
        public static final String BOOT_DEVICE = "bootDevice";
        public static final String IS_DURABLE = "isDurable";
        public static final String BLOCK_UNTIL_RUNNING = "blockUntilRunning";
        public static final String BLOCK_ON_COMPLETE = "blockOnComplete";
    }
}