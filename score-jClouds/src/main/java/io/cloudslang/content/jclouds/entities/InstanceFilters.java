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
    GROUP_NAME("group-name");

    private String parameter;

    InstanceFilters(String input) {
        this.parameter = input;
    }

    public String getValue() {
        return parameter;
    }
}
