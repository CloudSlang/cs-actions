package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/2/2016.
 */
public enum InstanceFilters {
    AFFINITY("affinity"),
    ARCHITECTURE("architecture"),
    AVAILABILITY_ZONE("availability-zone"),
    BLOCK_DEVICE_MAPPING_ATTACH_TIME("block-device-mapping.attach-time"),
    BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION("block-device-mapping.delete-on-termination"),
    BLOCK_DEVICE_MAPPING_DEVICE_NAME("block-device-mapping.device-name"),
    BLOCK_DEVICE_MAPPING_STATUS("block-device-mapping.status"),
    BLOCK_DEVICE_MAPPING_VOLUME_ID("block-device-mapping.volume-id"),
    CLIENT_TOKEN("client-token"),
    DNS_NAME("dns-name"),
    GROUP_ID("group-id"),
    GROUP_NAME("group-name"),
    HOST_ID("host-Id"),
    HYPERVISOR("hypervisor"),
    IAM_INSTANCE_PROFILE_ARN("iam-instance-profile.arn"),
    IMAGE_ID("image-id"),
    INSTANCE_ID("instance-id"),
    INSTANCE_LIFECYCLE("instance-lifecycle"),
    INSTANCE_STATE_CODE("instance-state-code"),
    INSTANCE_STATE_NAME("instance-state-name"),
    INSTANCE_TYPE("instance-type"),
    INSTANCE_GROUP_ID("instance.group-id"),
    INSTANCE_GROUP_NAME("instance.group-name"),
    IP_ADDRESS("ip-address"),
    KERNEL_ID("kernel-id"),
    KEY_NAME("key-name"),
    LAUNCH_INDEX("launch-index"),
    LAUNCH_TIME("launch-time"),
    MONITORING_STATE("monitoring-state"),
    OWNER_ID("owner-id"),
    PLACEMENT_GROUP_NAME("placement-group-name");

    private String parameter;

    InstanceFilters(String input) {
        this.parameter = input;
    }

    public String getValue() {
        return parameter;
    }
}
