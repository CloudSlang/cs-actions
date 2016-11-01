package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 11/1/2016.
 */
public enum BlockRootDeviceType {
    EBS("ebs"),
    INSTANCE_STORE("instance-store");

    private String value;

    BlockRootDeviceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (BlockRootDeviceType member : BlockRootDeviceType.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Unrecognized block root device type value: [" + input + "]. Valid values are: ebs, instance-store.");
    }
}