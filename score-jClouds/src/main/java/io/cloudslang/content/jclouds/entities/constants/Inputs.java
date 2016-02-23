package io.cloudslang.content.jclouds.entities.constants;

import com.hp.oo.sdk.content.annotations.Param;

/**
 * Created by Mihai Tusa.
 * 2/17/2016.
 */
public class Inputs {
    // Common inputs
    public static final String PROVIDER = "provider";
    public static final String ENDPOINT = "endpoint";
    public static final String IDENTITY = "identity";
    public static final String CREDENTIAL = "credential";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";

    // Custom inputs
    public static final String SERVER_NAME = "serverName";
    public static final String REGION = "region";
    public static final String SERVER_ID = "serverId";
    public static final String IMAGE_REF = "imageRef";
    public static final String AVAILABILITY_ZONE = "availabilityZone";
    public static final String MIN_COUNT = "minCount";
    public static final String MAX_COUNT = "maxCount";
    public static final String KERNEL_ID = "kernelId";
    public static final String INSTANCE_TYPE = "instanceType";
    public static final String MONITORING = "monitoring";
    public static final String RAM_DISK_ID = "ramdiskId";
    public static final String CLIENT_TOKEN = "clientToken";
    public static final String SECURITY_TOKEN = "securityToken";
    public static final String SECURITY_GROUPS = "securityGroups";
    public static final String DISABLE_API_TERMINATION = "disableApiTermination";
    public static final String SHUTDOWN_BEHAVIOR = "shutdownBehavior";
    public static final String VIRTUAL_DEVICE_NAME = "virtualDeviceName";
    public static final String SNAPSHOT_ID = "snapshotId";
    public static final String VOLUME_SIZE = "volumeSize";
    public static final String IOPS = "iops";
    public static final String DELETE_ON_TERMINATION = "deleteOnTermination";
    public static final String VOLUME_TYPE = "volumeType";
    public static final String IS_ENCRYPTED = "isEncrypted";

    public static final String PLACEMENT_TENACY = "placementTenacy";
    public static final String NETWORK_ID = "networkId";
    public static final String SUBNET_ID = "subnetId";
    public static final String PRIVATE_IP_ADDRESS = "privateIpAddress";
    public static final String USER_DATA = "userData";

    // Miscellaneous
    public static final String DELIMITER = "delimiter";
}