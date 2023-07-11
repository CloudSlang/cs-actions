

package io.cloudslang.content.oracle.oci.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {

    public static class CommonInputs {
        public static final String TENANCY_OCID = "tenancyOcid";
        public static final String USER_OCID = "userOcid";
        public static final String FINGER_PRINT = "fingerPrint";
        public static final String PRIVATE_KEY_DATA = "privateKeyData";
        public static final String PRIVATE_KEY_FILE = "privateKeyFile";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String API_VERSION = "apiVersion";
        public static final String REGION = "region";
        public static final String INSTANCE_ID = "instanceId";
        public static final String AVAILABILITY_DOMAIN = "availabilityDomain";
        public static final String VNIC_ID = "vnicId";
        public static final String VNIC_ATTACHMENT_ID = "vnicAttachmentId";
        public static final String VOLUME_ID = "volumeId";
        public static final String VOLUME_ATTACHMENT_ID = "volumeAttachmentId";
        public static final String PAGE = "page";
        public static final String LIMIT = "limit";
    }

    public static class ListInstancesInputs {
        public static final String COMPARTMENT_OCID = "compartmentOcid";
    }

    public static class AttachVolumeInputs {
        public static final String VOLUME_TYPE = "volumeType";
        public static final String DEVICE_NAME = "deviceName";
        public static final String DISPLAY_NAME = "displayName";
        public static final String IS_READ_ONLY = "isReadOnly";
        public static final String IS_SHAREABLE = "isShareable";
    }

}
