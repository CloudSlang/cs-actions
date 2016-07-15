package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public enum Device {
    CPU,
    MEMORY,
    DISK,
    CD,
    NIC;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized device: [" + input + "]. Valid values are: cpu, memory, disk, cd, nic.");
        }
    }
}
