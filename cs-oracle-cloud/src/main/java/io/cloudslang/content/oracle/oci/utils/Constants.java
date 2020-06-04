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

import org.omg.CORBA.PUBLIC_MEMBER;

public class Constants {
    public static class Common {
        public static final String API_VERSION = "/20160918";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_API_VERSION = "20160918";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final String STRICT = "strict";
        public static final String EMPTY = "";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String ANONYMOUS = "anonymous";
        public static final String GET = "get";
        public static final String HEAD = "head";
        public static final String DELETE = "delete";
        public static final String POST = "post";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String UTF8 = "UTF-8";
        public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
        public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
        public static final String AUTHORIZATION = "Authorization:";
        public static final String IAAS = "iaas";
        public static final String OCI_HOST = "oraclecloud.com";
        public static final String LIST_INSTANCES = "/instances/";
        public static final String COMPARTMENT_ID = "compartmentId=";
        public static final String INSTANCE_ID = "instanceId=";
        public static final String AVAILABILITY_DOMAIN = "availabilityDomain=";
        public static final String PAGE = "page=";
        public static final String LIMIT = "limit=";
        public static final String VNIC_ID = "vnicId=";
        public static final String QUERY = "?";
        public static final String FORWARD_SLASH = "/";
        public static final String HTTPS = "https";
        public static final String STATUS_CODE = "statusCode";
        public static final String DELIMITER = ",";
        public static final String DATE = "date";
        public static final String REQUEST_TARGET = "(request-target)";
        public static final String HOST = "host";
        public static final String CONTENT_LENGTH = "content-length";
        public static final String COLON = ":";
        public static final String PUT = "put";
        public static final String AND = "&";
        public static final String CONTENT_TYPE = "content-type";
        public static final String X_CONTENT_SHA256 = "x-content-sha256";
        public static final String APPLICATION_JSON = "application/json";
        public static final String ALLOWED_CYPHERS = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256," +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384," +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256," +
                "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";

    }


    public static class ListInstancesConstants {
        public static final String LIST_INSTANCES_OPERATION_NAME = "List Instances";
        public static final String INSTANCES_LIST_JSON_PATH = "$.[*].displayName";
    }

    public static class GetInstancesDetailsConstants {
        public static final String GET_INSTANCE_DETAILS_OPERATION_NAME = "Get Instance Details";
        public static final String INSTANCE_STATE_JSON_PATH = "$.lifecycleState";
    }

    public static class ListVnicAttachmentsConstants {
        public static final String LIST_VNIC_ATTACHMENTS_OPERATION_NAME = "List Vnics";
        public static final String LIST_VNIC_JSON_PATH = "$.[*].vnicId";
    }

    public static class GetVnicDetailsConstants {
        public static final String GET_VNIC_DETAILS_OPERATION_NAME = "Get Vnic Details";
        public static final String PRIVATE_IP_JSON_PATH = "$.privateIp";
        public static final String PUBLIC_IP_JSON_PATH = "$.publicIp";
    }
    public static class GetInstanceDefaultCredentialsConstants {

        public static final String GET_INSTANCE_DEFAULT_CREDENTIALS_OPERATION_NAME = "Get Instance default Credentials";
        public static final String GET_INSTANCE_DEFAULT_CREDENTIALS_OPERATION_PATH = "/defaultCredentials";
        public static final String INSTANCE_USERNAME_JSON_PATH = "$.username";
        public static final String INSTANCE_PASSWORD_JSON_PATH = "$.password";
    }

    public static class CreateInstancesConstants {
        public static final String CREATE_INSTANCE_OPERATION_NAME = "Create Instance";
        public static final String IS_MANAGEMENT_DISABLED = "isManagementDisabled";
        public static final String IS_MONITORING_DISABLED = "isMonitoringDisabled";
        public static final String AVAILABILITY_DOMAIN = "availabilityDomain";
        public static final String ASSIGN_PUBLIC_IP = "assignPublicIP";
        public static final String DEFINED_TAGS = "definedTags";
        public static final String DISPLAYNAME = "displayName";
        public static final String FREEFORM_TAGS = "freeformTags";
        public static final String HOSTNAME_LABEL = "hostnameLabel";
        public static final String NETWORK_SECURITY_GROUP_IDS = "networkSecurityGroupIds";
        public static final String PRIVATE_IP = "privateIP";
        public static final String SKIP_SOURCE_DEST_CHECK = "skipSourceDestCheck";
        public static final String SUBNET_ID = "subnetId";
        public static final String DEDICATED_VM_HOST_ID = "dedicatedVmHostId";
        public static final String VNIC_DEFINED_TAGS = "vnicDefinedTags";
        public static final String VNIC_FREEFORM_TAGS = "vnicFreeformTags";
        ;
        public static final String VNIC_DISPLAY_NAME = "vnicDisplayName";
        public static final String EXTENDED_METADATA = "extendedMetadata";
        public static final String FAULT_DOMAIN = "faultDomain";
        public static final String IPXE_SCRIPT = "ipxeScript";
        public static final String IS_PV_ENCRYPTION_IN_TRANSIT_ENABLED = "isPvEncryptionInTransitEnabled";
        public static final String LAUNCH_MODE = "launchMode";
        public static final String BOOT_VOLUME_TYPE = "bootVolumeType";
        public static final String FIRMWARE = "firmware";
        public static final String IS_CONSISTENT_VOLUME_NAMING_ENABLED = "isConsistentVolumeNamingEnabled";
        public static final String NETWORKTYPE = "networkType";
        public static final String REMOTE_DATA_VOLUME_TYPE = "remoteDataVolumeType";
        public static final String SHAPE = "shape";
        public static final String SSH_AUTHORIZED_KEYS = "sshAuthorizedKeys";
        public static final String USERDATA = "userdata";
        public static final String OCPUS = "ocpus";
        public static final String SOURCE_TYPE = "sourceType";
        public static final String BOOT_VOLUME_SIZE_IN_GBS = "bootVolumeSizeInGBs";
        public static final String IMAGE_ID = "imageId";
        public static final String KMS_KEY_ID = "kmsKeyId";
        public static final String BOOT_VOLUME_ID = "bootVolumeId";
        public static final String INSTANCE_ID_JSON_PATH = "$.id";
    }

}
