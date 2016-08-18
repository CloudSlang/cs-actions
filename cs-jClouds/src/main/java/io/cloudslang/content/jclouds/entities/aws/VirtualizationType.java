package io.cloudslang.content.jclouds.entities.aws;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public enum VirtualizationType {
    PARAVIRTUAL,
    HVM;

    public static String getValue(String input) throws Exception {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        try {
            return valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized  virtualization type value: [" + input + "]. Valid values are: paravirtual, hvm.");
        }
    }
}
