package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 8/10/2016.
 */
public enum HttpClientMethod {
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return GET.name();
        }

        for (HttpClientMethod member : HttpClientMethod.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name();
            }
        }

        throw new RuntimeException("Invalid Http Client method value: [" + input + "]. " +
                "Valid values: DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE.");
    }
}