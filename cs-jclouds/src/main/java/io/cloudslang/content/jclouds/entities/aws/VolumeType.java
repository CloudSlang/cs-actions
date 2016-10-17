package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized  volume type value: [" + input + "]. " +
                    "Valid values are: standard, io1, gp2, sc1, st1.");
        }
    }
}
