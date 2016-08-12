package io.cloudslang.content.jclouds.entities.aws;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public enum VolumeType {
    STANDARD,
    IO1,
    GP2,
    SC1,
    ST1;

    public static String getValue(String input) throws Exception {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized  volume type value: [" + input + "]. " +
                    "Valid values are: standard, io1, gp2, sc1, st1.");
        }
    }
}
