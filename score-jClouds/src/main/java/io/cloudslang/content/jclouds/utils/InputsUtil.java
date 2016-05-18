package io.cloudslang.content.jclouds.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 7/13/2015.
 */
public final class InputsUtil {
    private static final String COMMA_DELIMITER = ",";
    private static final String MINIMUM_INSTANCES_NUMBER_KEY = "minimumInstancesNumber";
    private static final String MAXIMUM_INSTANCES_NUMBER_KEY = "maximumInstancesNumber";

    private static final int MINIMUM_INSTANCES_NUMBER = 1;
    private static final int MAXIMUM_INSTANCES_NUMBER = 50;
    private static final int MINIMUM_NODES_NUMBER = 1;
    private static final int MAXIMUM_NODES_NUMBER = 50;
    private static final int MINIMUM_PROCESSORS_NUMBER = 1;
    private static final int MAXIMUM_PROCESSORS_NUMBER = 40;
    private static final int MINIMUM_MEMORY_GB_AMOUNT = 1;
    private static final int MAXIMUM_MEMORY_GB_AMOUNT = 244;
    private static final int MINIMUM_PORT_VALUE = 0;
    private static final int MAXIMUM_PORT_VALUE = 65535;

    private static final float DEFAULT_VOLUME_GB_SIZE = 8.0f;

    private InputsUtil() {
    }

    public static void validateInput(String input, String inputName) {
        if (StringUtils.isBlank(input)) {
            throw new RuntimeException("The required " + inputName + " input is not specified!");
        }
    }

    public static int getValidNodesCount(String input) {
        return getValidInt(input, MINIMUM_NODES_NUMBER, MAXIMUM_NODES_NUMBER, getValidationException(input, true),
                getValidationException(input, false));
    }

    public static int getValidProcessorsNumber(String input) {
        return getValidInt(input, MINIMUM_PROCESSORS_NUMBER, MAXIMUM_PROCESSORS_NUMBER, getValidationException(input, true),
                getValidationException(input, false));
    }

    public static int getValidMemoryAmount(String input) {
        return getValidInt(input, MINIMUM_MEMORY_GB_AMOUNT, MAXIMUM_MEMORY_GB_AMOUNT, getValidationException(input, true),
                getValidationException(input, false));
    }

    public static float getValidVolumeAmount(String input) {
        return getValidFloat(input, DEFAULT_VOLUME_GB_SIZE);
    }

    public static int getValidInt(String input, int defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }
        try {
            int intInput = Integer.parseInt(input);
            if (intInput < 0) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive integers.");
            }
            return intInput;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be integer.");
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

    public static double getValidDouble(String input, double defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }
        try {
            double doubleInput = Double.parseDouble(input);
            if (doubleInput < 0) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive doubles.");
            }
            return doubleInput;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be double.");
        }
    }

    public static String getDefaultDelimiter(String delimiter) {
        return StringUtils.isBlank(delimiter) ? COMMA_DELIMITER : delimiter;
    }

    public static int getMinInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(MINIMUM_INSTANCES_NUMBER_KEY) : getValidInt(input,
                validValues.get(MINIMUM_INSTANCES_NUMBER_KEY), validValues.get(MAXIMUM_INSTANCES_NUMBER_KEY),
                getValidationException(input, true), getValidationException(input, false));
    }

    public static int getMaxInstancesCount(String input) {
        Map<String, Integer> validValues = getValidLimits();

        return StringUtils.isBlank(input) ? validValues.get(MINIMUM_INSTANCES_NUMBER_KEY) : getValidInt(input,
                validValues.get(MINIMUM_INSTANCES_NUMBER_KEY), validValues.get(MAXIMUM_INSTANCES_NUMBER_KEY),
                getValidationException(input, true), getValidationException(input, false));
    }

    public static int getValidPort(String input) {
        return getValidInt(input, MINIMUM_PORT_VALUE, MAXIMUM_PORT_VALUE, getValidationException(input, true),
                getValidationException(input, false));
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

    private static float getValidFloat(String input, float defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }
        try {
            float floatInput = Float.parseFloat(input);
            if (floatInput < 0) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive floats.");
            }
            return floatInput;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be float.");
        }
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
        validLimits.put(MINIMUM_INSTANCES_NUMBER_KEY, MINIMUM_INSTANCES_NUMBER);
        validLimits.put(MAXIMUM_INSTANCES_NUMBER_KEY, MAXIMUM_INSTANCES_NUMBER);

        return validLimits;
    }

    private static String getValidationException(String input, boolean invalid) {
        if (invalid) {
            return "The provided value: " + input + " input must be integer.";
        }
        return "Incorrect provided value: " + input + " input. The value doesn't meet conditions for general purpose usage.";
    }
}