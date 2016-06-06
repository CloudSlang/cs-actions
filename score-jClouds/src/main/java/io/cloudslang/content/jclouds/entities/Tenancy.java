package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum Tenancy {
    DEDICATED,
    DEFAULT,
    HOST;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized provider value: [" + input + "]. Valid values are: dedicated, default, host.");
        }
    }
}