package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.aws.InstanceState;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import static java.util.Arrays.asList;
import static java.util.regex.Pattern.quote;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.BLOCK_DEVICE_MAPPING;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.DELETE_ON_TERMINATION;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.DEVICE_INDEX;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.TAG;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.RESOURCE_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.SUBNET_ID;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.STANDARD;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EBS;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.COMMA_DELIMITER;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NETWORK;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public final class InputsUtil {
    private static final String ACTION = "Action";
    private static final String ASSOCIATE_PUBLIC_IP_ADDRESS = "AssociatePublicIpAddress";
    private static final String EXCEPTED_KEY_STRING = "aws:";
    private static final String GP2 = "gp2";
    private static final String IO1 = "io1";
    private static final String PRIVATE_IP_ADDRESSES = "PrivateIpAddresses";
    private static final String SC1 = "sc1";
    private static final String SPECIFIC_QUERY_PARAM_PREFIX = NETWORK_INTERFACE + DOT;
    private static final String ST1 = "st1";
    private static final String SUBNET_ID_INPUT = "subnetId";
    private static final String VERSION = "Version";

    private static final int KEY_TAG_LENGTH_CONSTRAIN = 127;
    private static final int MAXIMUM_EBS_SIZE = 16384;
    private static final int MINIMUM_IO1_EBS_SIZE = 4;
    private static final int MAXIMUM_INSTANCES_NUMBER = 50;
    private static final int MINIMUM_INSTANCES_NUMBER = 1;
    private static final int MAXIMUM_STANDARD_EBS_SIZE = 1024;
    private static final int MINIMUM_SC1_AND_ST1_EBS_SIZE = 500;
    private static final int VALUE_TAG_LENGTH_CONSTRAIN = 255;

    private static final float MAXIMUM_VOLUME_AMOUNT = 16000f;
    private static final float MINIMUM_VOLUME_AMOUNT = 0.5f;

    private InputsUtil() {
    }

    public static Map<String, String> getHeadersOrQueryParamsMap(Map<String, String> inputMap, String stringToSplit,
                                                                 String delimiter, String customDelimiter, boolean trim) {
        String[] headersOrParamsArray = stringToSplit.split(delimiter);
        String[] values;

        for (String headersOrParamsItem : headersOrParamsArray) {
            values = headersOrParamsItem.split(quote(customDelimiter), 2);
            String key = trim ? values[START_INDEX].trim().toLowerCase() : values[START_INDEX];

            if (values.length > ONE) {
                inputMap.put(key, values[ONE]);
            } else {
                inputMap.put(key, EMPTY);
            }
        }

        return inputMap;
    }

    public static String getEndpointFromUrl(String input) throws MalformedURLException {
        URL url = new URL(input);
        String endpoint = url.getHost();
        if (url.getPort() > START_INDEX) {
            endpoint += COLON + url.getPort();
        }
        return endpoint;
    }

    public static String getParamsString(Map<String, String> paramsMap, String separator, String suffix) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(separator);
            sb.append(entry.getValue());
            sb.append(suffix);
        }
        return sb.deleteCharAt(sb.length() - ONE).toString();
    }

    public static String[] getStringsArray(String input, String condition, String delimiter) {
        if (condition.equals(input)) {
            return null;
        }
        return split(input, delimiter);
    }

    public static Set<String> getStringsSet(String input, String delimiter) {
        if (isBlank(input)) {
            return null;
        }
        return new HashSet<>(asList(input.split(quote(getDefaultStringInput(delimiter, COMMA_DELIMITER)))));
    }

    public static String getDefaultStringInput(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    public static int getValidInstanceStateCode(String input) {
        return InstanceState.getKey(input);
    }

    public static String[] getArrayWithoutDuplicateEntries(String inputString, String inputName, String delimiter) {
        String[] currentArray = InputsUtil.getStringsArray(inputString, EMPTY, delimiter);
        InputsUtil.validateArrayAgainstDuplicateElements(currentArray, inputString, delimiter, inputName);

        return currentArray;
    }

    public static void validateArrayAgainstDuplicateElements(String[] toBeValidated, String inputString, String delimiter,
                                                             String inputName) {
        if (toBeValidated != null && isNotBlank(inputString)) {
            Set<String> stringSet = getStringsSet(inputString, delimiter);
            if (stringSet != null && toBeValidated.length != stringSet.size()) {
                throw new RuntimeException("The value provided for: " + inputName + " input contain duplicate elements. " +
                        "Please provide unique elements!");
            }
        }
    }

    public static void validateAgainstDifferentArraysLength(String[] firstArray, String[] secondArray,
                                                            String firstInputName, String secondInputName) {
        if (firstArray != null && firstArray.length > START_INDEX && secondArray != null
                && secondArray.length > START_INDEX && firstArray.length != secondArray.length) {
            throw new RuntimeException("The values provided: [" + firstInputName + "] and [" + secondInputName + "] " +
                    "cannot have different length!");
        }
    }

    public static void setNetworkInterfaceSpecificQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper,
                                                              String[] referenceArray, int index) {
        if (isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                    PRIVATE_IP_ADDRESSES_STRING, NETWORK_INTERFACE_DESCRIPTION, DESCRIPTION, wrapper.getCommonInputs().getDelimiter(), index);
        }
        if (isNotBlank(wrapper.getCustomInputs().getSubnetId())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getCustomInputs().getSubnetId(),
                    PRIVATE_IP_ADDRESSES_STRING, SUBNET_ID_INPUT, SUBNET_ID, wrapper.getCommonInputs().getDelimiter(), index);
        }
        if (isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacesAssociatePublicIpAddressesString())) {
            setSpecificBooleanQueryParam(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfacesAssociatePublicIpAddressesString(),
                    PRIVATE_IP_ADDRESSES_STRING, NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS, ASSOCIATE_PUBLIC_IP_ADDRESS,
                    wrapper.getCommonInputs().getDelimiter(), index, false);
        }
        if (isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDeleteOnTermination())) {
            setSpecificBooleanQueryParam(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDeleteOnTermination(),
                    PRIVATE_IP_ADDRESSES_STRING, NETWORK_INTERFACE_DELETE_ON_TERMINATION, DELETE_ON_TERMINATION,
                    wrapper.getCommonInputs().getDelimiter(), index, true);
        }

        if (isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDeviceIndex())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDeviceIndex(),
                    PRIVATE_IP_ADDRESSES_STRING, NETWORK_INTERFACE_DEVICE_INDEX, DEVICE_INDEX, wrapper.getCommonInputs().getDelimiter(), index);
        } else {
            queryParamsMap.put(NETWORK_INTERFACE + DOT + String.valueOf(index + ONE) + DOT + DEVICE_INDEX, String.valueOf(index));
        }

    }

    /**
     * If enforcedBoolean is "true" and string input is: null, empty, many empty chars, TrUe, tRuE... but not "false"
     * then returns "true".
     * <p>
     * If enforcedBoolean is "false" and string input is: null, empty, many empty chars, FaLsE, fAlSe... but not "true"
     * then returns "false"
     * <p>
     * This behavior is needed for inputs like: "imageNoReboot" when we want them to be set to "true" disregarding the
     * value provided (null, empty, many empty chars, TrUe, tRuE) except the case when is "false"
     *
     * @param input           String to be evaluated.
     * @param enforcedBoolean Enforcement boolean.
     * @return A boolean according with above description.
     */
    public static boolean getEnforcedBooleanCondition(String input, boolean enforcedBoolean) {
        return (enforcedBoolean) ? isTrueOrFalse(input) == Boolean.parseBoolean(input) : Boolean.parseBoolean(input);
    }

    public static long getValidLong(String input, long defaultValue) {
        if (isBlank(input)) {
            return defaultValue;
        }
        try {
            long longInput = Long.parseLong(input);
            if (longInput < START_INDEX) {
                throw new RuntimeException("Incorrect provided value: " + input + ". Valid values are positive longs.");
            }
            return longInput;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("The provided value: " + input + " input must be long.");
        }
    }

    public static int getValidInstancesCount(String input) {
        return isBlank(input) ? MINIMUM_INSTANCES_NUMBER :
                getValidInt(input, MINIMUM_INSTANCES_NUMBER, MAXIMUM_INSTANCES_NUMBER, getValidationException(input, true),
                        getValidationException(input, false));
    }

    public static String getRelevantBooleanString(String input) {
        if (isNotBlank(input)
                && (Boolean.TRUE.toString().equalsIgnoreCase(input) || Boolean.FALSE.toString().equalsIgnoreCase(input))) {
            return input.toLowerCase();
        }
        return NOT_RELEVANT;
    }

    public static String getValidVolumeAmount(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
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

    public static String getValidKeyOrValueTag(String input, boolean isKey) {
        if (isKey && (input.startsWith(EXCEPTED_KEY_STRING) || input.length() > KEY_TAG_LENGTH_CONSTRAIN)) {
            throw new RuntimeException(getValidationException(input, false));
        } else if (!isKey && input.length() > VALUE_TAG_LENGTH_CONSTRAIN) {
            throw new RuntimeException(getValidationException(input, false));
        }

        return input;
    }

    public static void setOptionalMapEntry(Map<String, String> inputMap, String key, String value, boolean condition) {
        if (condition) {
            inputMap.put(key, value);
        }
    }

    public static void setCommonQueryParamsMap(Map<String, String> queryParamsMap, String action, String version) {
        queryParamsMap.put(ACTION, action);
        queryParamsMap.put(VERSION, version);
    }

    public static String getQueryParamsSpecificString(String specificArea, int index) {
        if (NETWORK.equalsIgnoreCase(specificArea)) {
            return PRIVATE_IP_ADDRESSES + DOT + String.valueOf(index + ONE) + DOT;
        } else if (BLOCK_DEVICE_MAPPING.equalsIgnoreCase(specificArea)) {
            return BLOCK_DEVICE_MAPPING + DOT + String.valueOf(index + ONE) + DOT;
        } else if (EBS.equalsIgnoreCase(specificArea)) {
            return BLOCK_DEVICE_MAPPING + DOT + String.valueOf(index + ONE) + DOT + EBS + DOT;
        } else if (NETWORK_INTERFACE.equalsIgnoreCase(specificArea)) {
            return NETWORK_INTERFACE + DOT + String.valueOf(index + ONE) + DOT + PRIVATE_IP_ADDRESSES + DOT +
                    String.valueOf(index + ONE) + DOT;
        } else if (RESOURCE_ID.equalsIgnoreCase(specificArea)) {
            return RESOURCE_ID + DOT + String.valueOf(index + ONE);
        } else if (KEY.equalsIgnoreCase(specificArea)) {
            return TAG + DOT + String.valueOf(index + ONE) + DOT + KEY;
        } else if (VALUE.equalsIgnoreCase(specificArea)) {
            return TAG + DOT + String.valueOf(index + ONE) + DOT + VALUE;
        } else {
            return EMPTY;
        }
    }

    public static String getValidIPv4Address(String input) {
        if (isNotBlank(input) && !isValidIPv4Address(input)) {
            throw new RuntimeException("The provided value for: " + input + " input must be a valid IPv4 address.");
        }
        return input;
    }

    public static String getValidEbsSize(String input, String ebsType) throws Exception {
        if (NOT_RELEVANT.equalsIgnoreCase(input)) {
            return NOT_RELEVANT;
        }
        switch (ebsType) {
            case STANDARD:
                return (isBlank(input)) ? String.valueOf(ONE) :
                        String.valueOf(getValidInt(input, ONE, MAXIMUM_STANDARD_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case IO1:
                return isBlank(input) ? String.valueOf(MINIMUM_IO1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_IO1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case GP2:
                return isBlank(input) ? String.valueOf(ONE) :
                        String.valueOf(getValidInt(input, ONE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case SC1:
                return isBlank(input) ? String.valueOf(MINIMUM_SC1_AND_ST1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_SC1_AND_ST1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case ST1:
                return isBlank(input) ? String.valueOf(MINIMUM_SC1_AND_ST1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_SC1_AND_ST1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            default:
                return String.valueOf(getValidInt(input, ONE, MAXIMUM_STANDARD_EBS_SIZE,
                        getValidationException(input, true), getValidationException(input, false)));
        }
    }

    public static String getValidPositiveIntegerAsStringValue(String input, int minimumValue) {
        return isBlank(input) ? EMPTY :
                String.valueOf(getValidInt(input, minimumValue, Integer.MAX_VALUE,
                        getValidationException(input, true), getValidationException(input, false)));

    }

    private static void setSpecificQueryParamValue(Map<String, String> queryParamsMap, String[] referenceArray, String inputString,
                                                   String referenceInputName, String currentInputName, String suffix, String delimiter,
                                                   int index) {
        String[] currentArray = getValidStringArray(referenceArray, inputString, EMPTY, delimiter, referenceInputName, currentInputName);
        setOptionalMapEntry(queryParamsMap, SPECIFIC_QUERY_PARAM_PREFIX + String.valueOf(index + ONE) + DOT + suffix,
                currentArray[index], currentArray.length > START_INDEX);
    }

    private static void setSpecificBooleanQueryParam(Map<String, String> queryParamsMap, String[] referenceArray,
                                                     String inputString, String referenceInputName, String currentInputName,
                                                     String suffix, String delimiter, int index, boolean enforcedBoolean) {
        String[] currentArray = getValidStringArray(referenceArray, inputString, EMPTY, delimiter, referenceInputName, currentInputName);
        setOptionalMapEntry(queryParamsMap, SPECIFIC_QUERY_PARAM_PREFIX + String.valueOf(index + ONE) + DOT + suffix,
                String.valueOf(getEnforcedBooleanCondition(currentArray[index], enforcedBoolean)), currentArray.length > START_INDEX);
    }

    private static String[] getValidStringArray(String[] referenceArray, String inputString, String condition,
                                                String delimiter, String firstInputName, String secondInputName) {
        String[] toValidateArray = getStringsArray(inputString, condition, delimiter);
        validateAgainstDifferentArraysLength(referenceArray, toValidateArray, firstInputName, secondInputName);

        return toValidateArray;
    }

    private static boolean isValidIPv4Address(String input) {
        return new InetAddressValidator().isValidInet4Address(input);
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
            //noinspection ResultOfMethodCallIgnored
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

    private static boolean isTrueOrFalse(String input) {
        return Boolean.FALSE.toString().equalsIgnoreCase(input) || Boolean.TRUE.toString().equalsIgnoreCase(input);
    }
}