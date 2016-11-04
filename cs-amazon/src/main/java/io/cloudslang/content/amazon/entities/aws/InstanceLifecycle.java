package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 11/1/2016.
 */
public enum InstanceLifecycle {
    SPOT,
    SCHEDULED;

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (InstanceLifecycle member : InstanceLifecycle.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized instance lifecycle value: [" + input + "]. Valid values are: spot, scheduled.");
    }
}