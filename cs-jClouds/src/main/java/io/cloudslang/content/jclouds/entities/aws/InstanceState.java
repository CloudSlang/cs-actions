package io.cloudslang.content.jclouds.entities.aws;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum InstanceState {
    NOT_RELEVANT(-1),
    PENDING(0),
    RUNNING(16),
    SHUTTING_DOWN(32),
    TERMINATED(48),
    STOPPING(64),
    STOPPED(80);

    private final int key;

    InstanceState(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static int getKey(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return NOT_RELEVANT.getKey();
        }
        try {
            return valueOf(input.toUpperCase()).getKey();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid instanceStateCode value: [" + input + "]. Valid values: " +
                    "pending, running, shutting-down, terminated, stopping, stopped.");
        }
    }

    public static String getValue(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid instanceStateName value: [" + input + "]. Valid values: " +
                    "pending, running, shutting-down, terminated, stopping, stopped.");
        }
    }
}
