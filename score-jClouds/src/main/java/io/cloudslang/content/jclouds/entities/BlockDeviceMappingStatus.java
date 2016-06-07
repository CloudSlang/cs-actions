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

    public static String getValue(String input) throws RuntimeException {
        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid status value: [" + input + "]. Valid values: attaching, attached, detaching, detached.");
        }
    }
}