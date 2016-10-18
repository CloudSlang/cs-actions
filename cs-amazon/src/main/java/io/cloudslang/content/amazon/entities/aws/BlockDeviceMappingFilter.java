package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by Mihai Tusa.
 * 6/14/2016.
 */
public enum BlockDeviceMappingFilter {
    DEVICE_NAME("block-device-mapping.device-name"),
    STATUS("block-device-mapping.status"),
    VOLUME_ID("block-device-mapping.volume-id"),
    VOLUME_SIZE("block-device-mapping.volume-size"),
    VOLUME_TYPE("block-device-mapping.volume-type"),
    SNAPSHOT_ID("block-device-mapping.snapshot-id"),
    ATTACH_TIME("block-device-mapping.attach-time"),
    DELETE_ON_TERMINATION("block-device-mapping.delete-on-termination");

    private String value;

    BlockDeviceMappingFilter(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}