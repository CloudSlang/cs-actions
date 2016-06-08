package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceAttachmentStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized networkInterfaceAttachmentStatus value: [" + input + "]. " +
                    "Valid values are: attaching, attached, detaching, detached.");
        }
    }
}
