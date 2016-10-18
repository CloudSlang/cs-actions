package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public enum InstanceInitiatedShutdownBehavior {
    STOP,
    TERMINATE;

    public static String getValue(String input) throws Exception {
        if (isBlank(input)) {
            return STOP.name().toLowerCase();
        }

        for (ImageType member : ImageType.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized Instance Initiated Shutdown Behavior value: [" + input + "]. " +
                "Valid values are: stop, terminate.");

    }
}