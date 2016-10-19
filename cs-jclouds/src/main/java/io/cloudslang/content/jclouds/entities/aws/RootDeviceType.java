package io.cloudslang.content.jclouds.entities.aws;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/17/2016.
 */
public enum RootDeviceType {
    AVAILABLE("available"),
    IN_USE("in-use");

    private String value;

    RootDeviceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (RootDeviceType member : RootDeviceType.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Unrecognized root device type value: [" + input + "]. Valid values are: available, in-use.");
    }
}
