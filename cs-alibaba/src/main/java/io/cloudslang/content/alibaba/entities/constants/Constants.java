/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.alibaba.entities.constants;

import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.DOT;

public class Constants {
    public static class Apis {
        public static final String ECS_API = "ecs";

    }

    public static class DefaultApiVersion {

        public static final String INSTANCES_DEFAULT_API_VERSION = "2014-05-26";

    }

    public static class ErrorMessages {
        public static final String UNSUPPORTED_QUERY_API = "Unsupported Query API.";
    }

    public static class Miscellaneous {
        public static final String ALIBABA_HOSTNAME = "aliyuncs.com";
        public static final String AMPERSAND = "&";
        public static final String COLON = ":";
        public static final String COMMA_DELIMITER = ",";
        public static final String DOT = ".";
        public static final String EMPTY = "";
        public static final String ENCODING = "UTF-8";
        public static final String EQUAL = "=";
        public static final String LINE_SEPARATOR = "\n";

        public static final String NOT_RELEVANT = "Not relevant";
        public static final String PIPE_DELIMITER = "|";
        public static final String SCOPE_SEPARATOR = "/";
    }

    public static class Values {
        public static final int DEFAULT_MAX_KEYS = 5;
        public static final int ONE = 1;
        public static final int START_INDEX = 0;
    }


    public static class AlibabaSignatureParams {
        public static final String TIMESTAMP = "Timestamp";
        public static final String SIGNATURE_METHOD = "SignatureMethod";
        public static final String SIGNATURE_VERSION = "SignatureVersion";
        public static final String SIGNATURE_NONCE = "SignatureNonce";
        public static final String SIGNATURE = "Signature";
        public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        public static final String ALIBABA_SIGNATURE_METHOD = "HMAC-SHA1";
        public static final String ALIBABA_SIGNATURE_VERSION = "1.0";
        public static final String ALGORITHM = "HmacSHA1";
    }
    public static class AlibabaParams {
        public static final String AUTHORIZATION_HEADER_RESULT = "authorizationHeader";
        public static final String DESCRIPTION = "Description";
        public static final String DEVICE_INDEX = "DeviceIndex";
        public static final String ENCRYPTED = "Encrypted";
        public static final String EXECUTABLE_BY = "ExecutableBy";
        public static final String FILTER = "Filter";
        public static final String FILTER_NAME = "Filter.%d.Name";
        public static final String FILTER_VALUE = "Filter.%d.Value";
        public static final String FIXED_PREFIX = DOT + "member" + DOT;
        public static final String HEADER_DELIMITER = "\\r?\\n";
        public static final String HTTP_CLIENT_METHOD_GET = "GET";
        public static final String IMAGE_ID = "ImageId";
        public static final String INSTANCE_TYPE="InstanceType";
        public static final String INTERNET_MAX_BANDWITH_OUT="InternetMaxBandwidthOut";
        public static final String INSTANCE_NAME="InstanceName";
        public static final String FORMAT="Format";
        public static final String FORMAT_TYPE="XML";

        public static final String INSTANCE_ID = "InstanceId";
        public static final String KEY = "Key";

        public static final String LISTENERS = "Listeners";
        public static final String NAME = "Name";

        public static final String NETWORK_INTERFACE = "NetworkInterface";
        public static final String PRIVATE_IP_ADDRESS = "PrivateIpAddress";
        public static final String PUBLIC_IP = "PublicIp";
        public static final String REGION_ID = "RegionId";
        public static final String REMOVE_OPERATION_TYPE = "remove";
        public static final String RESOURCE_ID = "ResourceId";
        public static final String SECURITY_GROUP_ID = "SecurityGroupId";
        public static final String SIGNATURE_RESULT = "signature";
        public static final String SNAPSHOT_ID = "SnapshotId";
        public static final String STANDARD = "standard";
        public static final String SUBNET_ID = "SubnetId";
        public static final String SUBNETS = "Subnets";
        public static final String TAG = "Tag";
        public static final String USER_GROUP = "UserGroup";
        public static final String USER_ID = "UserId";
        public static final String VALUE = "Value";
        public static final String VALUES = "Values";
        public static final String VOLUME_ID = "VolumeId";
        public static final String VOLUME_TYPE = "VolumeType";
        public static final String VPC_ID = "VpcId";
        public static final String ZONE_NAME = "ZoneName";
    }

    public static class EcsQueryApiActions {
        public static final String ALLOCATE_ADDRESS = "AllocateAddress";
        public static final String ASSOCIATE_ADDRESS = "AssociateAddress";

        public static final String CREATE_INSTANCES = "CreateInstance";
        public static final String START_INSTANCES = "StartInstances";
        public static final String STOP_INSTANCES = "StopInstances";
        public static final String TERMINATE_INSTANCES = "TerminateInstances";
    }


}