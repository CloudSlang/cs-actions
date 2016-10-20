package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/7/2016.
 */
public enum VirtualizationType {
    PARAVIRTUAL,
    HVM;

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (VirtualizationType member : VirtualizationType.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized  virtualization type value: [" + input + "]. Valid values are: paravirtual, hvm.");
    }
}
