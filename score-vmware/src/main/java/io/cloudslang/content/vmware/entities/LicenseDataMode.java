package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public enum LicenseDataMode {
    PER_SERVER,
    PER_SEAT;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unsupported level value: [" + input + "]. Valid values are: perServer, perSeat.");
        }
    }
}