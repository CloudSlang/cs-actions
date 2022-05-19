package io.cloudslang.content.cyberark.utils;

import static io.cloudslang.content.constants.OtherValues.EMPTY_STRING;

public class StringUtils {
    public static boolean isEmpty(Object val) {
        return (val == null) || (EMPTY_STRING.equals(val));
    }
}
