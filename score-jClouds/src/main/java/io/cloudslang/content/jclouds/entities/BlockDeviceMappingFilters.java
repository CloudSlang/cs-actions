package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/14/2016.
 */
public enum BlockDeviceMappingFilters {
    DEVICE_NAME("block-device-mapping.device-name"),
    STATUS("block-device-mapping.status"),
    VOLUME_ID("block-device-mapping.volume-id"),
    ATTACH_TIME("block-device-mapping.attach-time"),
    DELETE_ON_TERMINATION("block-device-mapping.delete-on-termination");

    private String value;

    BlockDeviceMappingFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}