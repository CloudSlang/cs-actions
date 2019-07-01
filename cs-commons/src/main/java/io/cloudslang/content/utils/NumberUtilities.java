/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
     * Given a long integer string, it checks if it's a valid long integer (based on apaches NumberUtils.createLong)
     *
     * @param longStr the long integer string to check
     * @return true if it's valid, otherwise false
     */
    public static boolean isValidLong(@Nullable final String longStr) {
        if (StringUtils.isBlank(longStr)) {
            return false;
        }
        final String stripedLong = StringUtils.strip(longStr);
        try {
            NumberUtils.createLong(stripedLong);
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
     * Given a long integer string, it checks if it's a valid long (base on apaches NumberUtils.createLong) and if
     * it's between the lowerBound and upperBound.
     *
     * @param longStr        the long integer string to check
     * @param lowerBound        the lower bound of the interval
     * @param upperBound        the upper bound of the interval
     * @param includeLowerBound boolean if to include the lower bound of the interval
     * @param includeUpperBound boolean if to include the upper bound of the interval
     * @return true if the long integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidLong(@Nullable final String longStr, final long lowerBound, final long upperBound, final boolean includeLowerBound, final boolean includeUpperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException(ExceptionValues.INVALID_BOUNDS);
        } else if (!isValidLong(longStr)) {
            return false;
        }
        final long aLong = toLong(longStr);
        final boolean respectsLowerBound = includeLowerBound ? lowerBound <= aLong : lowerBound < aLong;
        final boolean respectsUpperBound = includeUpperBound ? aLong <= upperBound : aLong < upperBound;
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
     * Given a long integer string, it checks if it's a valid long (base on apaches NumberUtils.createLong) and if
     * it's between the lowerBound and upperBound (including the lowerBound and excluding the upperBound).
     *
     * @param longStr the long integer string to check
     * @param lowerBound the lower bound of the interval
     * @param upperBound the upper bound of the interval
     * @return true if the long integer string is valid and in between the lowerBound and upperBound, false otherwise
     * @throws IllegalArgumentException if the lowerBound is not less than the upperBound
     */
    public static boolean isValidLong(@Nullable final String longStr, final long lowerBound, final long upperBound) {
        return isValidLong(longStr, lowerBound, upperBound, true, false);
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
     * Given a long integer string if it's a valid long (see isValidLong) it converts it into a long integer otherwise it throws an exception
     *
     * @param longStr the long integer to convert
     * @return the long integer value of the longStr
     * @throws IllegalArgumentException if the passed long integer string is not a valid long value
     */
    public static long toLong(@Nullable final String longStr) {
        if (!isValidLong(longStr)) {
            throw new IllegalArgumentException(longStr + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_LONG_VALUE);
        }
        final String stripedLong = StringUtils.strip(longStr);
        return NumberUtils.createLong(stripedLong);
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
     * If the long integer string is null or empty, it returns the defaultLong otherwise it returns the long integer value (see toLong)
     *
     * @param longStr     the long integer to convert
     * @param defaultLong the default value if the longStr is null or the empty string
     * @return the long integer value of the string or the defaultLong if the long integer string is empty
     * @throws IllegalArgumentException if the passed long integer string is not a valid long integer
     */
    public static long toLong(@Nullable final String longStr, final long defaultLong) {
        return StringUtils.isNoneEmpty(longStr) ? toLong(longStr) : defaultLong;
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
