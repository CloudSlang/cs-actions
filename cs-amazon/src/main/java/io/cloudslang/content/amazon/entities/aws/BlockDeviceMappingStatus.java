package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum BlockDeviceMappingStatus {
    ATTACHING,
    ATTACHED,
    DETACHING,
    DETACHED;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (BlockDeviceMappingStatus member : BlockDeviceMappingStatus.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid status value: [" + input + "]. Valid values: attaching, attached, detaching, detached.");
    }
}