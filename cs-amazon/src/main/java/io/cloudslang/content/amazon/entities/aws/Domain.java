package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public enum Domain {
    STANDARD,
    VPC;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return STANDARD.name().toLowerCase();
        }

        for (Domain member : Domain.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid Amazon domain: [" + input + "]. Valid values: standard, vpc.");
    }
}