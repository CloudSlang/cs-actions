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
        public static final String AVAILABILITY_ZONE = "availabilityZone";
        public static final String MIN_COUNT = "minCount";
        public static final String MAX_COUNT = "maxCount";
        public static final String SERVER_TYPE = "serverType";
        public static final String OPERATION_TIMEOUT = "operationTimeout";
        public static final String POOLING_INTERVAL = "poolingInterval";
        public static final String IMAGE_ID = "imageId";
        public static final String IDENTITY_ID = "identityId";
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
}