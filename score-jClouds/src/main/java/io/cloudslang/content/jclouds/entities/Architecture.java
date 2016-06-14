package io.cloudslang.content.jclouds.entities;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum Architecture {
    I386,
    X86_64;

    public static String getValue(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid architecture value: [" + input + "]. Valid values: i386, x86_64.");
        }
    }
}
