package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum BlockDeviceMappingStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static final String UNSUPPORTED_BLOCK_DEVICE_MAPPING_STATUS = "Invalid block device mapping status value. " +
            "Valid values: attaching, attached, detaching, detached.";

    public static String getValue(String input) throws RuntimeException {
        try {
            return Architecture.valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException(UNSUPPORTED_BLOCK_DEVICE_MAPPING_STATUS);
        }
    }
}
