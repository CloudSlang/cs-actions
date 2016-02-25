package io.cloudslang.content.jclouds.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 7/13/2015.
 */
public final class InputsUtil {
    private static final String DEFAULT_DELIMITER = ";" + System.lineSeparator();

    private static final String MINIMUM_INSTANCES_NUMBER = "minimumInstancesNumber";
    private static final String MAXIMUM_INSTANCES_NUMBER = "maximumInstancesNumber";

    private static final long LONG_DEFAULT_VALUE = 20000;

    private InputsUtil() {
    }

    public static void validateInput(String input, String inputName) {
        if (StringUtils.isBlank(input)) {
            throw new RuntimeException("The required " + inputName + " input is not specified!");
        }
    }

    public static int getMinInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(MINIMUM_INSTANCES_NUMBER) : getValidInt(input,
                validValues.get(MINIMUM_INSTANCES_NUMBER), validValues.get(MAXIMUM_INSTANCES_NUMBER),
                getValidationException(input, true), getValidationException(input, false));
    }

    public static int getMaxInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(MINIMUM_INSTANCES_NUMBER) : getValidInt(input,
                validValues.get(MINIMUM_INSTANCES_NUMBER), validValues.get(MAXIMUM_INSTANCES_NUMBER),
                getValidationException(input, true), getValidationException(input, false));
    }

    public static String getDefaultDelimiter(String delimiter) {
        return StringUtils.isBlank(delimiter) ? DEFAULT_DELIMITER : delimiter;
    }

    public static long getValidLong(String input) {
        if (StringUtils.isBlank(input)) {
            return LONG_DEFAULT_VALUE;
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

    private static Map<String, Integer> getValidLimits() {
        Map<String, Integer> validLimits = new HashMap<>();
        validLimits.put(MINIMUM_INSTANCES_NUMBER, 1);
        validLimits.put(MAXIMUM_INSTANCES_NUMBER, 50);

        return validLimits;
    }

    private static String getValidationException(String input, boolean invalid) {
        if (invalid) {
            return "The provided value: " + input + " input must be integer.";
        }
        return "Incorrect provided value: " + input + " input. The value doesn't meet conditions for " +
                "general purpose usage.";
    }
}