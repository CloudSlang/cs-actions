/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionValues;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Nullable;

/**
 * A Number utility class that offers integer and double validation and conversion.
 * Created by victor on 31.08.2016.
 */
public final class NumberUtilities {

    private NumberUtilities() {
    }

    /**
     * Given an integer string, it checks if it's a valid integer (based on apaches NumberUtils.createInteger)
     *
     * @param integerStr the integer string to check
     * @return true if it's valid, otherwise false
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
     * Given an integer string, it checks if it's a valid integer (base on apaches NumberUtils.createInteger) and if
     * it's between the lowerBound and upperBound.
     *
     * @param integerStr        the integer string to check
     * @param lowerBound        the lower bound of the interval
     * @param upperBound        the upper bound of the interval
     * @param includeLowerBound boolean if to include the lower bound of the interval
     * @param includeUpperBound boolean if to include the upper bound of the interval
     * @return true if the integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidInt(@Nullable final String integerStr, final int lowerBound, final int upperBound, final boolean includeLowerBound, final boolean includeUpperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException(ExceptionValues.INVALID_BOUNDS);
        } else if (!isValidInt(integerStr)) {
            return false;
        }
        final int aInteger = toInteger(integerStr);
        final boolean respectsLowerBound = includeLowerBound ? lowerBound <= aInteger : lowerBound < aInteger;
        final boolean respectsUpperBound = includeUpperBound ? aInteger <= upperBound : aInteger < upperBound;
        return respectsLowerBound && respectsUpperBound;
    }

    /**
     * Given an integer string, it checks if it's a valid integer (base on apaches NumberUtils.createInteger) and if
     * it's between the lowerBound and upperBound (including the lowerBound and excluding the upperBound).
     *
     * @param integerStr the integer string to check
     * @param lowerBound the lower bound of the interval
     * @param upperBound the upper bound of the interval
     * @return true if the integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidInt(@Nullable final String integerStr, final int lowerBound, final int upperBound) {
        return isValidInt(integerStr, lowerBound, upperBound, true, false);
    }

    /**
     * Given an integer string if it's a valid int (see isValidInt) it converts it into an integer otherwise it throws an exception
     *
     * @param integerStr the integer to convert
     * @return the integer value of the integerStr
     * @throws IllegalArgumentException if the passed integer string is not a valid integer
     */
    public static int toInteger(@Nullable final String integerStr) {
        if (!isValidInt(integerStr)) {
            throw new IllegalArgumentException(integerStr + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_INTEGER_VALUE);
        }
        final String stripedInteger = StringUtils.strip(integerStr);
        return NumberUtils.createInteger(stripedInteger);
    }

    /**
     * If the integer string is null or empty, it returns the defaultInteger otherwise it returns the integer value (see toInteger)
     *
     * @param integerStr     the integer to convert
     * @param defaultInteger the default value if the integerStr is null or the empty string
     * @return the integer value of the string or the defaultInteger is the integer string is empty
     * @throws IllegalArgumentException if the passed integer string is not a valid integer
     */
    public static int toInteger(@Nullable final String integerStr, final int defaultInteger) {
        return StringUtils.isNoneEmpty(integerStr) ? toInteger(integerStr) : defaultInteger;
    }


    /**
     * Given an double string, it checks if it's a valid double (base on apaches NumberUtils.createDouble) and if
     * it's between the lowerBound and upperBound.
     *
     * @param doubleStr         the integer string to check
     * @param lowerBound        the lower bound of the interval
     * @param upperBound        the upper bound of the interval
     * @param includeLowerBound boolean if to include the lower bound of the interval
     * @param includeUpperBound boolean if to include the upper bound of the interval
     * @return true if the integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidDouble(@Nullable final String doubleStr, double lowerBound, double upperBound, final boolean includeLowerBound, final boolean includeUpperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException(ExceptionValues.INVALID_BOUNDS);
        } else if (!isValidDouble(doubleStr)) {
            return false;
        }
        final double aDouble = toDouble(doubleStr);
        final boolean respectsLowerBound = includeLowerBound ? Double.compare(lowerBound, aDouble) <= 0 : lowerBound < aDouble;
        final boolean respectsUpperBound = includeUpperBound ? Double.compare(aDouble, upperBound) <= 0 : aDouble < upperBound;
        return respectsLowerBound && respectsUpperBound;
    }

    /**
     * Given an double string, it checks if it's a valid double (base on apaches NumberUtils.createDouble) and if
     * it's between the lowerBound and upperBound (including the lower bound and excluding the upper one)
     *
     * @param doubleStr  the integer string to check
     * @param lowerBound the lower bound of the interval
     * @param upperBound the upper bound of the interval
     * @return true if the integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidDouble(@Nullable final String doubleStr, double lowerBound, double upperBound) {
        return isValidDouble(doubleStr, lowerBound, upperBound, true, false);
    }


    /**
     * Given an double string, it checks if it's a valid double (based on apaches NumberUtils.createDouble)
     *
     * @param doubleStr the double string to check
     * @return true if it's valid, otherwise false
     */
    public static boolean isValidDouble(@Nullable final String doubleStr) {
        if (StringUtils.isBlank(doubleStr)) {
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
     * Given an double string if it's a valid double (see isValidDouble) it converts it into an double otherwise it throws an exception
     *
     * @param doubleStr the double to convert
     * @return the double value of the doubleStr
     * @throws IllegalArgumentException if the passed double string is not a valid double
     */
    public static double toDouble(@Nullable final String doubleStr) {
        if (!isValidDouble(doubleStr)) {
            throw new IllegalArgumentException(doubleStr + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_DOUBLE_VALUE);
        }
        final String stripedDouble = StringUtils.strip(doubleStr);
        return NumberUtils.createDouble(stripedDouble);
    }

    /**
     * If the double string is null or empty, it returns the defaultDouble otherwise it returns the duoble value (see toDouble)
     *
     * @param doubleStr     the double to convert
     * @param defaultDouble the default value if the doubleStr is null or the empty string
     * @return the double value of the string or the defaultDouble is the double string is empty
     * @throws IllegalArgumentException if the passed double string is not a valid double
     */
    public static double toDouble(@Nullable final String doubleStr, final double defaultDouble) {
        return StringUtils.isNoneEmpty(doubleStr) ? toDouble(doubleStr) : defaultDouble;
    }
}
