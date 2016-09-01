package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionsValues;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by victor on 31.08.2016.
 */
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtilities {

    public static boolean isValidInt(final String integerStr) {
        if (StringUtils.isBlank(integerStr)) {
            return false;
        }
        final String stripedInteger = StringUtils.strip(integerStr);
        try {
            NumberUtils.createInteger(stripedInteger);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidInt(final String integerStr, int lowerBound, int upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(ExceptionsValues.INVALID_BOUNDS);
        }
        if (!isValidInt(integerStr)) {
            return false;
        }
        final int aInteger = toInteger(integerStr);
        return lowerBound < aInteger && aInteger < upperBound;
    }

    public static int toInteger(final String integerStr) {
        if (!isValidInt(integerStr)) {
            throw new IllegalArgumentException(integerStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_INTEGER_VALUE);
        }
        final String stripedInteger = StringUtils.strip(integerStr);
        return NumberUtils.createInteger(stripedInteger);
    }

    public static int toInteger(final String integerStr, final int defaultInteger) {
        return StringUtils.isNoneEmpty(integerStr) ? toInteger(integerStr) : defaultInteger;
    }

    public static boolean isValidDouble(final String doubleStr) {
        if (StringUtils.isBlank(doubleStr)){
            return false;
        }
        final String stripedDouble = StringUtils.strip(doubleStr);
        try {
            NumberUtils.createDouble(stripedDouble);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(final String doubleStr, double lowerBound, double upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(ExceptionsValues.INVALID_BOUNDS);
        }
        if (!isValidDouble(doubleStr)) {
            return false;
        }
        final double aDouble = toDouble(doubleStr);
        return lowerBound < aDouble && aDouble < upperBound;
    }

    public static double toDouble(final String doubleStr) {
        if (!isValidInt(doubleStr)) {
            throw new IllegalArgumentException(doubleStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_DOUBLE_VALUE);
        }
        final String stripedDouble = StringUtils.strip(doubleStr);
        return NumberUtils.createDouble(stripedDouble);
    }

    public static double toDouble(final String doubleStr, final double defaultDouble) {
        return StringUtils.isNoneEmpty(doubleStr) ? toDouble(doubleStr) : defaultDouble;
    }
}
