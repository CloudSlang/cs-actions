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

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum Platform {
    OTHERS("others"),
    WINDOWS("windows");

    private final String value;

    Platform(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (Platform member : Platform.values()) {
            if (OTHERS.value.equals(input.toLowerCase())) {
                return "";
            } else if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }

        throw new RuntimeException("Invalid platform value: [" + input + "]. Valid values: \"\" others, windows.");
    }
}
