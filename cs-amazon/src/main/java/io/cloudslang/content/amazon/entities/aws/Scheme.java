package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public enum Scheme {
    INTERNET_FACING("internet-facing"),
    INTERNAL("internal");

    private final String value;

    Scheme(String value) {
        this.value = value;
    }

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return INTERNET_FACING.getSchemeValue();
        }

        for (Scheme scheme : Scheme.values()) {
            if (scheme.getSchemeValue().equalsIgnoreCase(input)) {
                return scheme.getSchemeValue();
            }
        }

        throw new RuntimeException("Invalid Amazon load balancer scheme value: [" + input + "]. " +
                "Valid values: ec2, elasticloadbalancing, s3.");
    }

    private String getSchemeValue() {
        return value;
    }
}