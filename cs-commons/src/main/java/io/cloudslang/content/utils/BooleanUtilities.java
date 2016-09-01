package io.cloudslang.content.utils;

import io.cloudslang.content.constants.BooleanValues;
import io.cloudslang.content.constants.ExceptionsValues;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by victor on 31.08.2016.
 */
public class BooleanUtilities {

    @NotNull
    private static String getLowerCaseString(@NotNull final String string) {
        return StringUtils.strip(string).toLowerCase();
    }

    public static boolean isValid(@Nullable final String booleanStr) {
        if (StringUtils.isBlank(booleanStr)) {
            return false;
        }
        final String lowerCaseBoolean = getLowerCaseString(booleanStr);
        return lowerCaseBoolean.equals(BooleanValues.TRUE) || lowerCaseBoolean.equals(BooleanValues.FALSE);
    }

    public static boolean toBoolean(@Nullable final String booleanStr) {
        if (!isValid(booleanStr)) {
            throw new IllegalArgumentException(booleanStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_BOOLEAN_VALUE);
        }
        return BooleanUtils.toBoolean(getLowerCaseString(booleanStr));
    }

    public static boolean toBoolean(@Nullable  final String booleanStr, final boolean defaultValue) {
        return StringUtils.isNoneEmpty(booleanStr) ? toBoolean(booleanStr) : defaultValue;
    }


}
