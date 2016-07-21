package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.InstanceState;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public final class InputsUtil {
    private static final String COMMA_DELIMITER = ",";

    private static final int MAXIMUM_INSTANCES_NUMBER = 50;
    private static final int MINIMUM_INSTANCES_NUMBER = 1;

    private static final float MINIMUM_VOLUME_AMOUNT = 0.5f;
    private static final float MAXIMUM_VOLUME_AMOUNT = 16000f;

    private InputsUtil() {
    }

    public static String[] getStringsArray(String input, String condition, String delimiter) {
        if (condition.equals(input)) {
            return null;
        }
        return StringUtils.split(input, delimiter);
    }

    public static Set<String> getStringsSet(String input, String delimiter) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return new HashSet<>(Arrays.asList(input.split(Pattern.quote(getDefaultDelimiter(delimiter)))));
    }

    public static String getDefaultDelimiter(String delimiter) {
        return StringUtils.isBlank(delimiter) ? COMMA_DELIMITER : delimiter;
    }

    public static String getAmazonRegion(String region) {
        return StringUtils.isBlank(region) ? Constants.Miscellaneous.DEFAULT_AMAZON_REGION : region;
    }

    public static int getValidInstanceStateCode(String input) {
        return InstanceState.getKey(input);
    }

    public static boolean getImageNoRebootFlag(String input) {
        return !isTrueOrFalse(input) || Boolean.parseBoolean(input);
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

    public static int getValidInstancesCount(String input) {
        return StringUtils.isBlank(input) ? MINIMUM_INSTANCES_NUMBER :
                getValidInt(input, MINIMUM_INSTANCES_NUMBER, MAXIMUM_INSTANCES_NUMBER, getValidationException(input, true),
                        getValidationException(input, false));
    }

    public static int getVolumeValidInt(String input, int min, int max, String errorMessage) {
        return getValidInt(input, min, max, getValidationException(input, true), errorMessage);
    }

    public static String getRelevantBooleanString(String input) {
        if (StringUtils.isNotBlank(input)
                && (Boolean.TRUE.toString().equalsIgnoreCase(input) || Boolean.FALSE.toString().equalsIgnoreCase(input))) {
            return input.toLowerCase();
        }
        return Constants.Miscellaneous.NOT_RELEVANT;
    }

    public static String getValidVolumeAmount(String input) {
        if (StringUtils.isBlank(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }
        try {
            float floatInput = Float.parseFloat(input);
            if (floatInput < MINIMUM_VOLUME_AMOUNT || floatInput > MAXIMUM_VOLUME_AMOUNT) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive floats " +
                        "between 0.5f and 16000.0f.");
            }
            return String.valueOf(floatInput);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be float.");
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

    private static String getValidationException(String input, boolean invalid) {
        if (invalid) {
            return "The provided value: " + input + " input must be integer.";
        }
        return "Incorrect provided value: " + input + " input. The value doesn't meet conditions for general purpose usage.";
    }

    private static boolean isTrueOrFalse(String input){
        return Boolean.FALSE.toString().equalsIgnoreCase(input) || Boolean.TRUE.toString().equalsIgnoreCase(input) ;
    }
}
