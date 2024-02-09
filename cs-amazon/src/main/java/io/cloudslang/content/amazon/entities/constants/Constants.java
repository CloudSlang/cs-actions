/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class Constants {
    public static class Apis {
        public static final String EC2_API = "ec2";
        public static final String LOAD_BALANCING_API = "elasticloadbalancing";
        public static final String S3_API = "s3";
    }

    public static class DefaultApiVersion {
        public static final String ELASTIC_IP_DEFAULT_API_VERSION = "2016-11-15";
        public static final String IMAGES_DEFAULT_API_VERSION = "2016-04-01";
        public static final String INSTANCES_DEFAULT_API_VERSION = "2016-11-15";
        public static final String LOAD_BALANCER_DEFAULT_API_VERSION = "2015-12-01";
        public static final String NETWORK_DEFAULT_API_VERSION = "2016-11-15";
        public static final String REGIONS_DEFAULT_API_VERSION = "2016-11-15";
        public static final String SNAPSHOTS_DEFAULT_API_VERSION = "2016-11-15";
        public static final String STORAGE_DEFAULT_API_VERSION = "2006-03-01";
        public static final String TAGS_DEFAULT_API_VERSION = "2016-11-15";
        public static final String VOLUMES_DEFAULT_API_VERSION = "2016-11-15";
        public static final String VPC_DEFAULT_API_VERSION = "2016-11-15";
        public static final String DESCRIBE_KEYPAIR_DEFAULT_API_VERSION = "2016-11-15";
        public static final String SECURITY_GROUPS_DEFAULT_API_VERSION = "2016-11-15";
        public static final String DESCRIBE_SUBNET_DEFAULT_API_VERSION = "2016-11-15";
    }

    public static class ErrorMessages {
        public static final String UNSUPPORTED_QUERY_API = "Unsupported Query API.";
    }

    public static class Miscellaneous {
        public static final String AMAZON_HOSTNAME = "amazonaws.com";
        public static final String AMPERSAND = "&";
        public static final String COLON = ":";
        public static final String COMMA_DELIMITER = ",";
        public static final String EBS = "Ebs";
        public static final String DOT = ".";
        public static final String EMPTY = "";
        public static final String ENCODING = "UTF-8";
        public static final String EQUAL = "=";
        public static final String LINE_SEPARATOR = "\n";
        public static final String NETWORK = "network";
        public static final String NOT_RELEVANT = "Not relevant";
        public static final String PIPE_DELIMITER = "|";
        public static final String SCOPE_SEPARATOR = "/";
        public static final String DEFAULT_INSTANCE_TYPE = "m1.small";
    }

    public static class Values {
        public static final int DEFAULT_MAX_KEYS = 1000;
        public static final int ONE = 1;
        public static final int START_INDEX = 0;
    }

    public static class AwsParams {
        public static final String ADD_OPERATION_TYPE = "add";
        public static final String ALLOCATION_ID = "AllocationId";
        public static final String ATTRIBUTE = "Attribute";
        public static final String AUTHORIZATION_HEADER_RESULT = "authorizationHeader";
        public static final String AVAILABILITY_ZONES = "AvailabilityZones";
        public static final String AWS_REQUEST_VERSION = "aws4_request";
        public static final String BLOCK_DEVICE_MAPPING = "BlockDeviceMapping";
        public static final String CIDR_BLOCK = "CidrBlock";
        public static final String DEFAULT_AMAZON_REGION = "us-east-1";
        public static final String DELETE_ON_TERMINATION = "DeleteOnTermination";
        public static final String DESCRIPTION = "Description";
        public static final String DEVICE_INDEX = "DeviceIndex";
        public static final String ENCRYPTED = "Encrypted";
        public static final String FILTER = "Filter";
        public static final String FILTER_NAME = "Filter.%d.Name";
        public static final String FILTER_VALUE = "Filter.%d.Value";
        public static final String FIXED_PREFIX = DOT + "member" + DOT;
        public static final String FORCE = "Force";
        public static final String HEADER_DELIMITER = "\\r?\\n";
        public static final String HTTP_CLIENT_METHOD_GET = "GET";
        public static final String IOPS = "Iops";
        public static final String IMAGE_ID = "ImageId";
        public static final String INSTANCE_ID = "InstanceId";
        public static final String KEY = "Key";
        public static final String LAUNCH_PERMISSION = "launchPermission";
        public static final String LISTENERS = "Listeners";
        public static final String NAME = "Name";
        public static final String NO_REBOOT = "NoReboot";
        public static final String NETWORK_INTERFACE = "NetworkInterface";
        public static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";
        public static final String OPERATION_TYPE = "OperationType";
        public static final String OWNER = "Owner";
        public static final String PRIMARY = "Primary";
        public static final String PRIVATE_IP_ADDRESS = "PrivateIpAddress";
        public static final String PUBLIC_IP = "PublicIp";
        public static final String REGION_NAME = "RegionName";
        public static final String REGION= "region";
        public static final String REMOVE_OPERATION_TYPE = "remove";
        public static final String RESOURCE_ID = "ResourceId";
        public static final String SECURITY_GROUP = "SecurityGroup";
        public static final String SECURITY_GROUPS = "SecurityGroups";
        public static final String SECURITY_GROUP_ID = "SecurityGroupId";
        public static final String SECURITY_GROUP_ID_CONST = "GroupId";
        public static final String SECURITY_GROUP_NAME_CONST = "GroupName";
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
        public static final String OWNERS = "Owner";
        public static final String EXECUTABLE_BY = "ExecutableBy";
    }

    public static class Ec2QueryApiActions {
        public static final String ALLOCATE_ADDRESS = "AllocateAddress";
        public static final String ASSOCIATE_ADDRESS = "AssociateAddress";
        public static final String ATTACH_NETWORK_INTERFACE = "AttachNetworkInterface";
        public static final String ATTACH_VOLUME = "AttachVolume";
        public static final String CREATE_IMAGE = "CreateImage";
        public static final String CREATE_NETWORK_INTERFACE = "CreateNetworkInterface";
        public static final String CREATE_SNAPSHOT = "CreateSnapshot";
        public static final String CREATE_SUBNET = "CreateSubnet";
        public static final String CREATE_VPC = "CreateVpc";
        public static final String CREATE_TAGS = "CreateTags";
        public static final String CREATE_VOLUME = "CreateVolume";
        public static final String DELETE_NETWORK_INTERFACE = "DeleteNetworkInterface";
        public static final String DELETE_SNAPSHOT = "DeleteSnapshot";
        public static final String DELETE_SUBNET = "DeleteSubnet";
        public static final String DELETE_VOLUME = "DeleteVolume";
        public static final String DELETE_VPC = "DeleteVpc";
        public static final String DEREGISTER_IMAGE = "DeregisterImage";
        public static final String DESCRIBE_AVAILABILITY_ZONES = "DescribeAvailabilityZones";
        public static final String DESCRIBE_IMAGES = "DescribeImages";
        public static final String DESCRIBE_IMAGE_ATTRIBUTE = "DescribeImageAttribute";
        public static final String DESCRIBE_INSTANCES = "DescribeInstances";
        public static final String DESCRIBE_INSTANCE_TYPE_OFFERINGS = "DescribeInstanceTypeOfferings";
        public static final String DESCRIBE_SECURITY_GROUPS = "DescribeSecurityGroups";
        public static final String DESCRIBE_REGIONS = "DescribeRegions";
        public static final String DESCRIBE_NETWORK_INTERFACES = "DescribeNetworkInterfaces";
        public static final String DESCRIBE_SUBNETS="DescribeSubnets";
        public static final String DESCRIBE_TAGS = "DescribeTags";
        public static final String DESCRIBE_VOLUMES = "DescribeVolumes";
        public static final String DESCRIBE_VPCS = "DescribeVpcs";
        public static final String DETACH_NETWORK_INTERFACE = "DetachNetworkInterface";
        public static final String DETACH_VOLUME = "DetachVolume";
        public static final String DISASSOCIATE_ADDRESS = "DisassociateAddress";
        public static final String MODIFY_IMAGE_ATTRIBUTE = "ModifyImageAttribute";
        public static final String MODIFY_INSTANCE_ATTRIBUTE = "ModifyInstanceAttribute";
        public static final String REBOOT_INSTANCES = "RebootInstances";
        public static final String RELEASE_ADDRESS = "ReleaseAddress";
        public static final String RESET_IMAGE_ATTRIBUTE = "ResetImageAttribute";
        public static final String RUN_INSTANCES = "RunInstances";
        public static final String START_INSTANCES = "StartInstances";
        public static final String STOP_INSTANCES = "StopInstances";
        public static final String TERMINATE_INSTANCES = "TerminateInstances";
        public static final String DESCRIBE_KEYPAIRS ="DescribeKeyPairs";
    }

    public static class LoadBalancingQueryApiActions {
        public static final String CREATE_LOAD_BALANCER = "CreateLoadBalancer";
        public static final String DELETE_LOAD_BALANCER = "DeleteLoadBalancer";
        public static final String DESCRIBE_LOAD_BALANCERS = "DescribeLoadBalancers";
    }

    public static class S3QueryApiActions {
        public static final String GET_BUCKET = "GET Bucket";
    }

    public static class ServiceCatalogActions {
        public static final String FAILED = "FAILED";
        public static final String CLOUD_FORMATION_STACK_NAME_REGEX = "(SC)-[0-9]{0,63}-[a-z]{0,63}-[a-z0-9]{0,63}";
        public static final String SUCCEEDED = "SUCCEEDED";
        public static final String PROVISION_PRODUCT_FAILED_REASON = "ProvisionProduct failed. Reason: ";
        public static final String UPDATE_PROVISIONED_PRODUCT_FAILED_REASON = "UpdateProvisionedProduct failed. Reason: ";
        public static final String UNPROVISION_PROVISIONED_PRODUCT_FAILED_REASON = "TerminateProvisionedProduct failed. Reason: ";
        public static final Set<String> UPDATE_STATUSES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CREATED", "IN_PROGRESS", "IN_PROGRESS_IN_ERROR")));


    }

    public static class AWSRDSActions {
        public static final String SKIP_FINAL_SNAPSHOT_CONST = "false";
        public static final String DELETE_AUTOMATED_BACKUPS_CONST = "false";
        public static final String TAG_KEY_AND_VALUE_CONST = "Number of tag keys must be equals to number of tag values.";
    }

    public static class SchedulerTimeConstants {
        public static final String SCHEDULER_START_TIME = "schedulerStartTime";
        public static final String SCHEDULER_TIME_ZONE = "schedulerTimeZone";
        public static final String TIME_ZONE = "timeZone";
        public static final String TRIGGER_EXPRESSION = "triggerExpression";
        public static final String SCHEDULER_TIME = "schedulerTime";
        public static final String COLON = ":";
        public static final String NEW_LINE = "\n";
        public static final String FAILURE = "failure";
        public static final String EXCEPTION = "exception";
        public static final String SCHEDULER_TIME_OPERATION_NAME = "Scheduler Time";

        public static final String EXCEPTION_SCHEDULER_TIME = "The %s format should be in HH:MM:SS format.";
        public static final String EXCEPTION_SCHEDULER_HOUR_TIME = "The %s format should be in between 0 to 23.";
        public static final String EXCEPTION_SCHEDULER_MINUTES_TIME = "The %s format should be in between 0 to 59.";
        public static final String EXCEPTION_SCHEDULER_TIMEZONE = "The %s is not a valid.";


    }

    public static class GetTimeFormatConstants {
        public static final String GET_TIME_FORMAT_OPERATION_NAME = "Get Time Format";
        public static final String EPOCH_TIME = "epochTime";
        public static final String DATE_FORMAT = "dateFormat";

        public static final String EXCEPTION_EPOCH_TIME = "The %s is not a valid.";
    }

}
