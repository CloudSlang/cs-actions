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
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by Mihai Tusa.
 * 6/17/2016.
 */
public enum RootDeviceType {
    AVAILABLE("available"),
    IN_USE("in-use");

    private final String value;

    RootDeviceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (RootDeviceType member : RootDeviceType.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }

        throw new RuntimeException("Unrecognized root device type value: [" + input + "]. Valid values are: available, in-use.");
    }
}
