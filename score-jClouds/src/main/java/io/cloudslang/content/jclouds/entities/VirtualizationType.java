package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public enum VirtualizationType {
    PARAVIRTUAL,
    HVM;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized  virtualization type value: [" + input + "]. Valid values are: paravirtual, hvm.");
        }
    }
}