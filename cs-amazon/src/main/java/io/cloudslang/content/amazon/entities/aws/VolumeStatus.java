/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by sandorr
 * 2/14/2017.
 */
public enum VolumeStatus {
    CREATING("creating"),
    AVAILABLE("available"),
    IN_USE("in-use"),
    DELETING("deleting"),
    DELETED("deleted"),
    ERROR("error");

    private final String key;

    VolumeStatus(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (VolumeStatus volumeStatus : VolumeStatus.values()) {
            if (volumeStatus.getKey().equalsIgnoreCase(input)) {
                return volumeStatus.getKey().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid status value: [" + input + "]. Valid values: creating, available, in-use, deleting, deleted, error.");
    }
}
