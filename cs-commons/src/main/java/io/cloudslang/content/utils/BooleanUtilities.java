package io.cloudslang.content.utils;

import io.cloudslang.content.constants.BooleanValues;
import io.cloudslang.content.constants.ExceptionValues;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Boolean utility class that offers boolean string validation and conversion
 * Created by victor on 31.08.2016.
 */
public final class BooleanUtilities {

    private BooleanUtilities() {
    }

    /**
     * Given a string, it lowercase it and strips the blank spaces from the ends
     *
     * @param string the string to check
     * @return the string in lowercase
     */
    @NotNull
    private static String getLowerCaseString(@NotNull final String string) {
        return StringUtils.strip(string).toLowerCase();
    }

    /**
     * Given a boolean in string format, it checks if it's 'true' or 'false' (case insensitive)
     *
     * @param booleanStr the string to check
     * @return true if booleanStr is 'true' or 'false' otherwise false
     */
    public static boolean isValid(@Nullable final String booleanStr) {
        if (StringUtils.isBlank(booleanStr)) {
            return false;
        }
        final String lowerCaseBoolean = getLowerCaseString(booleanStr);
        return lowerCaseBoolean.equals(BooleanValues.TRUE) || lowerCaseBoolean.equals(BooleanValues.FALSE);
    }

    /**
     * If the booleanStr is a valid boolean (see isValid) it returns the boolean value, otherwise it throws an exception
     *
     * @param booleanStr the string to convert to boolean
     * @return true if the booleanStr is 'true', false if it's 'false'
     * @throws IllegalArgumentException if the booleanStr is not a valid boolean
     */
    public static boolean toBoolean(@Nullable final String booleanStr) {
        if (!isValid(booleanStr)) {
            throw new IllegalArgumentException(booleanStr + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_BOOLEAN_VALUE);
        }
        return BooleanUtils.toBoolean(getLowerCaseString(booleanStr));
    }

    /**
     * If the string is null or empty string, it returns the defaultValue otherwise it returns the boolean value (see tooBoolean)
     *
     * @param booleanStr   the string to convert to boolean
     * @param defaultValue the default value if the string is empty or null
     * @return true if the booleanStr is 'true', false if it's 'false'
     * @throws IllegalArgumentException if the booleanStr is not a valid boolean
     */
    public static boolean toBoolean(@Nullable final String booleanStr, final boolean defaultValue) {
        return StringUtils.isNoneEmpty(booleanStr) ? toBoolean(booleanStr) : defaultValue;
    }


}
