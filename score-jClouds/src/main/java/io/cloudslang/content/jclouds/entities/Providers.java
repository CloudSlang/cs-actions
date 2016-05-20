package io.cloudslang.content.jclouds.entities;

/**
 * Created by persdana on 7/14/2015.
 */
public enum Providers {
    OPENSTACK,
    AMAZON;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized provider value: [" + input + "]. " +
                    "Valid values are: openstack, amazon.");
        }
    }
}