package io.cloudslang.content.jclouds.entities.aws;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum AmazonApiServiceType {
    EC2,
    S3;

    public static String getValue(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return EC2.toString().toLowerCase();
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid Amazon API service value: [" + input + "]. " +
                    "Valid values: ec2, s3.");
        }
    }
}