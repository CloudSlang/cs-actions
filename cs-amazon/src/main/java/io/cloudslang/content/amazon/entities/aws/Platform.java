package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (Platform member : Platform.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }

        throw new RuntimeException("Invalid platform value: [" + input + "]. Valid values: \"\" (empty string), windows.");
    }
}
