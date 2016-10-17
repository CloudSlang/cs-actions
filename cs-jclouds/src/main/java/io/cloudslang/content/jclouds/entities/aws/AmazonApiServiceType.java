package io.cloudslang.content.jclouds.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum AmazonApiServiceType {
    EC2,
    S3;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return EC2.name().toLowerCase();
        }

        for (AmazonApiServiceType member : AmazonApiServiceType.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid Amazon API service value: [" + input + "]. Valid values: ec2, s3.");
    }
}