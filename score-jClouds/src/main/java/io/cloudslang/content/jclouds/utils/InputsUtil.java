package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by persdana on 7/13/2015.
 */
public final class InputsUtil {
    private InputsUtil() {
    }

    public static String[] getStringsArray(String input, String condition, String delimiter) {
        if (condition.equals(input)) {
            return null;
        }
        return input.split(Pattern.quote(delimiter));
    }

    public static Set<String> getStringsSet(String input, String delimiter) {
        if (StringUtils.isBlank(input)) {
            return null;
        }

        return new HashSet<>(Arrays.asList(input.split(Pattern.quote(getDefaultDelimiter(delimiter)))));
    }

    public static void validateInput(String input, String inputName) {
        if (StringUtils.isBlank(input)) {
            throw new RuntimeException("The required " + inputName + " input is not specified!");
        }
    }

    public static long getValidLong(String input, long defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }
        try {
            long longInput = Long.parseLong(input);
            if (longInput < 0) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive longs.");
            }
            return longInput;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be long.");
        }
    }

    public static String getDefaultDelimiter(String delimiter) {
        return StringUtils.isBlank(delimiter) ? Constants.Miscellaneous.COMMA_DELIMITER : delimiter;
    }

    public static int getMinInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER_KEY) :
                getValidInt(input, validValues.get(Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER_KEY),
                        validValues.get(Constants.Miscellaneous.MAXIMUM_INSTANCES_NUMBER_KEY), getValidationException(input, true),
                        getValidationException(input, false));
    }

    public static int getMaxInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER_KEY) :
                getValidInt(input, validValues.get(Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER_KEY),
                        validValues.get(Constants.Miscellaneous.MAXIMUM_INSTANCES_NUMBER_KEY), getValidationException(input, true),
                        getValidationException(input, false));
    }

    public static boolean getBoolean(String input) {
        return StringUtils.isBlank(input) || Boolean.parseBoolean(input);
    }

    private static Map<String, Integer> getValidLimits() {
        Map<String, Integer> validLimits = new HashMap<>();
        validLimits.put(Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER_KEY, Constants.Miscellaneous.MINIMUM_INSTANCES_NUMBER);
        validLimits.put(Constants.Miscellaneous.MAXIMUM_INSTANCES_NUMBER_KEY, Constants.Miscellaneous.MAXIMUM_INSTANCES_NUMBER);

        return validLimits;
    }

    private static int getValidInt(String input, int minAllowed, int maxAllowed, String noIntError, String constrainsError) {
        if (!isInt(input)) {
            throw new RuntimeException(noIntError);
        }
        int intInput = Integer.parseInt(input);
        if (intInput < minAllowed || intInput > maxAllowed) {
            throw new RuntimeException(constrainsError);
        }
        return intInput;
    }

    private static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static String getValidationException(String input, boolean invalid) {
        if (invalid) {
            return "The provided value: " + input + " input must be integer.";
        }
        return "Incorrect provided value: " + input + " input. The value doesn't meet conditions for general purpose usage.";
    }
}