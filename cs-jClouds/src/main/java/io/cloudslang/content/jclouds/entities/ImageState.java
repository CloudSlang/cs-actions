package io.cloudslang.content.jclouds.entities;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/17/2016.
 */
public enum ImageState {
    AVAILABLE,
    PENDING,
    FAILED;

    public static String getValue(String input) throws Exception {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized image state value: [" + input + "]. Valid values are: available, pending, failed.");
        }
    }
}
