package io.cloudslang.content.jclouds.entities.aws;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum Platform {
    OTHERS(""),
    WINDOWS("windows");

    private String value;

    Platform(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        for (Platform member : Platform.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Invalid platform value: [" + input + "]. Valid values: \"\" (empty string), windows.");
    }
}
