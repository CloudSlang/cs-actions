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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceStatus {
    AVAILABLE("available"),
    IN_USE("in-use");

    private final String value;

    NetworkInterfaceStatus(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (NetworkInterfaceStatus member : NetworkInterfaceStatus.values()) {
            if (member.value.equalsIgnoreCase(input)) {
                return member.value;
            }
        }

        throw new RuntimeException("Unrecognized network interface status value: [" + input + "]. Valid values are: available, in-use.");
    }
}
