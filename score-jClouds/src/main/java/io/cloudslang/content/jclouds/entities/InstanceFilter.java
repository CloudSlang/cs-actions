package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/2/2016.
 */
public enum InstanceFilter {
    AFFINITY("affinity"),
    AVAILABILITY_ZONE("availability-zone"),
    CLIENT_TOKEN("client-token"),
    DNS_NAME("dns-name"),
    GROUP_ID("group-id"),
    GROUP_NAME("group-name"),
    HOST_ID("host-Id"),
    IAM_INSTANCE_PROFILE_ARN("iam-instance-profile.arn"),
    IMAGE_ID("image-id"),
    INSTANCE_ID("instance-id"),
    INSTANCE_LIFECYCLE("instance-lifecycle"),
    INSTANCE_STATE_CODE("instance-state-code"),
    INSTANCE_STATE_NAME("instance-state-name"),
    INSTANCE_GROUP_ID("instance.group-id"),
    INSTANCE_GROUP_NAME("instance.group-name"),
    IP_ADDRESS("ip-address"),
    KEY_NAME("key-name"),
    LAUNCH_INDEX("launch-index"),
    LAUNCH_TIME("launch-time"),
    MONITORING_STATE("monitoring-state"),
    PLACEMENT_GROUP_NAME("placement-group-name"),
    PRIVATE_DNS_NAME("private-dns-name"),
    PRIVATE_IP_ADDRESS("private-ip-address"),
    REASON("reason"),
    REQUESTER_ID("requester-id"),
    RESERVATION_ID("reservation-id"),
    SOURCE_DESTINATION_CHECK("source-dest-check"),
    SPOT_INSTANCE_REQUEST_ID("spot-instance-request-id"),
    SUBNET_ID("subnet-id"),
    TENANCY("tenancy"),
    VPC_ID("vpc-id");

    private String value;

    InstanceFilter(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}