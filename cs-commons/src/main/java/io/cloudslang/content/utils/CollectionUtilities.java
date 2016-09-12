package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionsValues;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A Collection utility class that offers array, list and map conversions from a string.
 * Created by victor on 01.09.2016.
 */
public final class CollectionUtilities {

    private CollectionUtilities() {
    }

    /**
     * Splits the stringArray by the delimiter into an array of strings without ignoring the escaped delimiters
     *
     * @param stringArray the string to be split
     * @param delimiter   the delimiter by which to split the stringArray
     * @return an array of Strings
     */
    @NotNull
    public static String[] toArrayWithEscaped(@Nullable final String stringArray, @NotNull final String delimiter) {
        if (StringUtils.isEmpty(stringArray)) {
            return new String[0];
        }
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(stringArray, delimiter);
    }

    /**
     * Splits the stringArray by the delimiter into an array of strings ignoring the escaped delimiters
     *
     * @param stringArray the string to be split
     * @param delimiter   the delimiter by which to split the stringArray
     * @return an array of Strings
     */
    @NotNull
    public static String[] toArray(@Nullable final String stringArray, @NotNull final String delimiter) {
        if (StringUtils.isEmpty(stringArray)) {
            return new String[0];
        }
        final String regex = "(?<!\\\\)" + Pattern.quote(delimiter);
        return stringArray.split(regex);
    }

    /**
     * Splits the stringList by the delimiter into a List of strings without ignoring the escaped delimiters
     *
     * @param stringList the string to be split
     * @param delimiter  the delimiter by which to split the stringArray
     * @return a list of Strings
     */
    @NotNull
    public static List<String> toListWithEscaped(@Nullable final String stringList, @NotNull final String delimiter) {
        return new ArrayList<>(Arrays.asList(toArrayWithEscaped(stringList, delimiter)));
    }

    /**
     * Splits the stringList by the delimiter into a List of strings ignoring the escaped delimiters
     *
     * @param stringList the string to be split
     * @param delimiter  the delimiter by which to split the stringArray
     * @return a list of Strings
     */
    @NotNull
    public static List<String> toList(@Nullable final String stringList, @NotNull final String delimiter) {
        return new ArrayList<>(Arrays.asList(toArray(stringList, delimiter)));
    }

    /**
     * Splits a stringMap in the format key1<keyValueDelimiter>value1 <pairDelimiter> key2<keyValueDelimiter>value2 ... into a Map<String, String>
     *
     * @param stringMap         the string to be split
     * @param pairDelimiter     the delimiter between pairs of key, value
     * @param keyValueDelimiter the delimiter between the key and it's corresponding value
     * @return a Map<String, String> from the specified stringMap with the specified delimiters
     * @throws IllegalArgumentException if the stringMap doesn't respect the specified format
     */
    @NotNull
    public static Map<String, String> toMap(@Nullable final String stringMap, @NotNull final String pairDelimiter, @NotNull final String keyValueDelimiter) {
        Map<String, String> theMap = new HashMap<>();
        for (final String pairStr : toArray(stringMap, pairDelimiter)) {
            final String[] arrayPair = toArray(pairStr, keyValueDelimiter);
            if (arrayPair.length != 2 && arrayPair.length != 0) {
                throw new IllegalArgumentException(pairStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_KEY_VALUE_PAIR);
            } else if (arrayPair.length == 2) {
                theMap.put(arrayPair[0], arrayPair[1]);
            }
        }
        return theMap;
    }

}
