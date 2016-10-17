package io.cloudslang.content.jclouds.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public enum InstanceInitiatedShutdownBehavior {
    STOP,
    TERMINATE;

    public static String getValue(String input) throws Exception {
        if (isBlank(input)) {
            return STOP.toString().toLowerCase();
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized Instance Initiated Shutdown Behavior value: [" + input + "]. " +
                    "Valid values are: stop, terminate.");

        }
    }
}