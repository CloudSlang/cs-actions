package io.cloudslang.content.jclouds.entities.aws;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public enum Domain {
    STANDARD,
    VPC;

    public static String getValue(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return STANDARD.toString().toLowerCase();
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid Amazon domain: [" + input + "]. Valid values: standard, vpc.");
        }
    }
}