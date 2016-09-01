package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionsValues;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by victor on 01.09.2016.
 */
public class CollectionUtilities {

    @NotNull
    public static String[] toArrayWithEscaped(final String stringArray, @NotNull final String delimiter) {
        if (StringUtils.isEmpty(stringArray)) {
            return new String[0];
        }
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(stringArray, delimiter);
    }

    @NotNull
    public static String[] toArray(final String stringArray, @NotNull final String delimiter) {
        if (StringUtils.isEmpty(stringArray)) {
            return new String[0];
        }
        String regex = "(?<!\\\\)" + Pattern.quote(delimiter);
        return stringArray.split(regex);
    }

    @NotNull
    public static List<String> toListWithEscaped(final String stringList, @NotNull final String delimiter) {
        return Arrays.asList(toArrayWithEscaped(stringList, delimiter));
    }

    @NotNull
    public static List<String> toList(final String stringList, @NotNull final String delimiter) {
        return Arrays.asList(toArray(stringList, delimiter));
    }

    @NotNull
    public static Map<String, String> toMap(final String stringArray, @NotNull final String pairDelimiter, @NotNull final String keyValueDelimiter) {
        Map<String, String> theMap = new HashMap<>();
        for (final String pairStr: toArray(stringArray, pairDelimiter)) {
            final String[] arrayPair = toArray(pairStr, keyValueDelimiter);
            if (arrayPair.length != 2 && arrayPair.length != 0) {
                throw new IllegalArgumentException(pairStr + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_KEY_VALUE_PAIR);
            }
            if (arrayPair.length == 2) {
                theMap.put(arrayPair[0], arrayPair[1]);
            }
        }
        return theMap;
    }

    public static void main(String[] args) {
        String a = "a:b,c:d,e\\,\\::f";
        Map<String, String> aMap = toMap(a, ",", ":");
        System.out.println(aMap);
    }
}
