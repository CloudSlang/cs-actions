package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum MonitoringState {
    DISABLED,
    ENABLED;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid monitoringState value: [" + input + "]. Valid values: disabled, enabled.");
        }
    }
}