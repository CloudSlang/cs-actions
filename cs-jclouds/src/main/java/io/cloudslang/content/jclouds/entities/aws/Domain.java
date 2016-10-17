package io.cloudslang.content.jclouds.entities.aws;

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
            return STANDARD.toString().toLowerCase();
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid Amazon domain: [" + input + "]. Valid values: standard, vpc.");
        }
    }
}