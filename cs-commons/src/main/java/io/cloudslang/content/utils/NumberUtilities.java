package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionsValues;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

/**
 * Created by victor on 31.08.2016.
 */
public class NumberUtilities {

    /**
     * @param integerStr
     * @return
     */
    public static boolean isValidInt(@Nullable final String integerStr) {
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

    /**
     * @param integerStr
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public static boolean isValidInt(@Nullable final String integerStr, int lowerBound, int upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(ExceptionsValues.INVALID_BOUNDS);
        }
        if (!isValidInt(integerStr)) {
            return false;
        }
        final int aInteger = toInteger(integerStr);
        return lowerBound < aInteger && aInteger < upperBound;
    }

    /**
     * @param integerStr
     * @return
     */
    public static int toInteger(@Nullable final String integerStr) {
        if (!isValidInt(integerStr)) {
            throw new IllegalArgumentException(integerStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_INTEGER_VALUE);
        }
        final String stripedInteger = StringUtils.strip(integerStr);
        return NumberUtils.createInteger(stripedInteger);
    }

    /**
     * @param integerStr
     * @param defaultInteger
     * @return
     */
    public static int toInteger(@Nullable final String integerStr, final int defaultInteger) {
        return StringUtils.isNoneEmpty(integerStr) ? toInteger(integerStr) : defaultInteger;
    }

    /**
     * @param doubleStr
     * @return
     */
    public static boolean isValidDouble(@Nullable final String doubleStr) {
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

    /**
     * @param doubleStr
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public static boolean isValidDouble(@Nullable final String doubleStr, double lowerBound, double upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException(ExceptionsValues.INVALID_BOUNDS);
        }
        if (!isValidDouble(doubleStr)) {
            return false;
        }
        final double aDouble = toDouble(doubleStr);
        return lowerBound < aDouble && aDouble < upperBound;
    }

    /**
     * @param doubleStr
     * @return
     */
    public static double toDouble(@Nullable final String doubleStr) {
        if (!isValidDouble(doubleStr)) {
            throw new IllegalArgumentException(doubleStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_DOUBLE_VALUE);
        }
        final String stripedDouble = StringUtils.strip(doubleStr);
        return NumberUtils.createDouble(stripedDouble);
    }

    /**
     * @param doubleStr
     * @param defaultDouble
     * @return
     */
    public static double toDouble(@Nullable final String doubleStr, final double defaultDouble) {
        return StringUtils.isNoneEmpty(doubleStr) ? toDouble(doubleStr) : defaultDouble;
    }
}
