/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;
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

    private static final String NOT_RELEVANT_STRING = "not relevant";
    private final Integer key;

    InstanceState(Integer key) {
        this.key = key;
    }

    private Integer getKey() {
        return key;
    }

    public static int getKey(String input) throws RuntimeException {
        return isBlank(input) ? NOT_RELEVANT.getKey() : (Integer) getKeyOrValue(input, true);
    }

    public static String getValue(String input) throws RuntimeException {
        return isBlank(input) ? NOT_RELEVANT_STRING : (String) getKeyOrValue(input, false);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getKeyOrValue(String input, boolean isKey) {
        for (InstanceState member : InstanceState.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return isKey ? (T) member.getKey() : (T) member.name().toLowerCase();
            }
        }

        throw new RuntimeException(getErrorMessage(input, isKey));
    }

    private static String getErrorMessage(String input, boolean isKey) {
        return isKey ? "Invalid instanceStateCode value: [" + input + "]. Valid values: pending, running, shutting-down, " +
                "terminated, stopping, stopped." : "Invalid instanceStateName value: [" + input + "]. Valid values: " +
                "pending, running, shutting-down, terminated, stopping, stopped.";
    }
}