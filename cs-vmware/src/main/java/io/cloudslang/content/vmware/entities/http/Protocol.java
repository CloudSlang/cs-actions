package io.cloudslang.content.vmware.entities.http;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 10/30/2015.
 */
public enum Protocol {
    HTTP,
    HTTPS;

    public static String getValue(String input) throws Exception {
        if (StringUtils.isBlank(input)) {
            return HTTPS.toString();
        }
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unsupported protocol value: [" + input + "]. Valid values are: https, http.");
        }
    }
}
