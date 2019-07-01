

package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public enum Level {
    HIGH,
    NORMAL,
    LOW;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unsupported level value: [" + input + "]. Valid values are: high, normal, low.");
        }
    }
}
