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
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Tirla Alin
 * 2/14/2017.
 */
public enum AttachmentStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    private static final String INVALID_VALUE = "Invalid attachment status value: [%s]. Valid values: attaching, attached, detaching, detached.";

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (AttachmentStatus member : AttachmentStatus.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException(format(INVALID_VALUE, input));
    }
}
