package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 6/17/2016.
 */
public enum ProductCodeType {
    DEVPAY,
    MARKETPLACE;

    public static String getValue(String input) throws Exception {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (ProductCodeType member : ProductCodeType.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized product code type value: [" + input + "]. Valid values are: devpay, marketplace.");
    }
}
