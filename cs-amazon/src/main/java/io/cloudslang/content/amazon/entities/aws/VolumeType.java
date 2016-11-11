package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
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

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (VolumeType member : VolumeType.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized  volume type value: [" + input + "]. " +
                "Valid values are: standard, io1, gp2, sc1, st1.");
    }
}