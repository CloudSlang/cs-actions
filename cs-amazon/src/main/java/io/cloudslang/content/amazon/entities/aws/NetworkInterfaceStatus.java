package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceStatus {
    AVAILABLE("available"),
    IN_USE("in-use");

    private final String value;

    NetworkInterfaceStatus(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (NetworkInterfaceStatus member : NetworkInterfaceStatus.values()) {
            if (member.value.equalsIgnoreCase(input)) {
                return member.value;
            }
        }

        throw new RuntimeException("Unrecognized network interface status value: [" + input + "]. " +
                "Valid values are: available, in-use.");
    }
}
