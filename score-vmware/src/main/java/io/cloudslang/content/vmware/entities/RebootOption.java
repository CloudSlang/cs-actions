package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public enum RebootOption {
    REBOOT,
    NOREBOOT,
    SHUTDOWN;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unsupported level value: [" + input + "]. Valid values are: reboot, noreboot, shutdown.");
        }
    }
}