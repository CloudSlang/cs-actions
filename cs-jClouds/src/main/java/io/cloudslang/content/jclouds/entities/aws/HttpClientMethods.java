package io.cloudslang.content.jclouds.entities.aws;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum HttpClientMethods {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    HEAD,
    TRACE;

    public static String getValue(String input) throws RuntimeException {
        if (StringUtils.isBlank(input)) {
            return GET.toString();
        }

        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid Http Client method value: [" + input + "]. " +
                    "Valid values: GET, POST, PUT, DELETE, PATCH, HEAD, TRACE.");
        }
    }
}