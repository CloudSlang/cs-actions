package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum InstanceState {
    PENDING(0),
    RUNNING(16),
    SHUTTING_DOWN(32),
    TERMINATED(48),
    STOPPING(64),
    STOPPED(80);

    public static final String UNSUPPORTED_INSTANCE_STATE = "Invalid instanceStateCode value. Valid values: " +
            "pending, running, shutting-down, terminated, stopping, stopped.";

    private final int key;

    InstanceState(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static int getKey(String input) throws IllegalArgumentException {
        try {
            InstanceState instanceState = InstanceState.valueOf(input.toUpperCase());
            return instanceState.getKey();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException(UNSUPPORTED_INSTANCE_STATE);
        }
    }

    public static String getValue(String input) throws RuntimeException {
        try {
            return InstanceState.valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException(UNSUPPORTED_INSTANCE_STATE);
        }
    }
}