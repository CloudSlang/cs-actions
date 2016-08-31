package io.cloudslang.content.utils;

import io.cloudslang.content.constants.BooleanValues;
import io.cloudslang.content.constants.ExceptionsValues;
import lombok.NonNull;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by victor on 31.08.2016.
 */
public class BooleanUtilities {

    @NonNull
    public static boolean isValid(final String booleanStr) {
        if (!StringUtils.isNotBlank(booleanStr)) return false;
        String lowerCaseBoolean = StringUtils.strip(booleanStr).toLowerCase();
        return lowerCaseBoolean.equals(BooleanValues.TRUE) || lowerCaseBoolean.equals(BooleanValues.FALSE);
    }

    @NonNull
    public static boolean toBoolean(final String booleanStr) {
        if (!isValid(booleanStr)) {
            throw new IllegalArgumentException(booleanStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_BOOLEAN_VALUE);
        }
        return BooleanUtils.toBoolean(booleanStr);
    }

    @NonNull
    public static boolean toBoolean(final String booleanStr, final boolean defaultValue) {
        return isValid(booleanStr) ? BooleanUtils.toBoolean(booleanStr) : defaultValue;
    }

}
