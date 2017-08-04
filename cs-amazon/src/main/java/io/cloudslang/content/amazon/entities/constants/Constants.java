/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.constants;

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
        public static final String EXECUTABLE_BY = "ExecutableBy";
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
        public static final String REMOVE_OPERATION_TYPE = "remove";
        public static final String RESOURCE_ID = "ResourceId";
        public static final String SECURITY_GROUP = "SecurityGroup";
        public static final String SECURITY_GROUPS = "SecurityGroups";
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
        public static final String DESCRIBE_REGIONS = "DescribeRegions";
        public static final String DESCRIBE_NETWORK_INTERFACES = "DescribeNetworkInterfaces";
        public static final String DESCRIBE_TAGS = "DescribeTags";
        public static final String DESCRIBE_VOLUMES = "DescribeVolumes";
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
    }

    public static class LoadBalancingQueryApiActions {
        public static final String CREATE_LOAD_BALANCER = "CreateLoadBalancer";
        public static final String DELETE_LOAD_BALANCER = "DeleteLoadBalancer";
        public static final String DESCRIBE_LOAD_BALANCERS = "DescribeLoadBalancers";
    }

    public static class S3QueryApiActions {
        public static final String GET_BUCKET = "GET Bucket";
    }
}