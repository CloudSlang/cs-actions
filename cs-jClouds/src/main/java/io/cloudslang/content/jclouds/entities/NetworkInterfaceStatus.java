package io.cloudslang.content.jclouds.entities;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceStatus {
    AVAILABLE("available"),
    IN_USE("in-use");

    private String value;

    NetworkInterfaceStatus(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }

        for (NetworkInterfaceStatus member : NetworkInterfaceStatus.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Unrecognized network interface status value: [" + input + "]. " +
                "Valid values are: available, in-use.");
    }
}
