package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum Architecture {
    I386,
    X86_64;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (Architecture member : Architecture.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid architecture value: [" + input + "]. Valid values: i386, x86_64.");
    }
}