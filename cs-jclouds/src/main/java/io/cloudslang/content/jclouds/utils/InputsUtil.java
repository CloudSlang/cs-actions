package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.aws.InstanceState;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public final class InputsUtil {
    private static final String ACTION = "Action";
    private static final String ASSOCIATE_PUBLIC_IP_ADDRESS = "AssociatePublicIpAddress";
    private static final String GP2 = "gp2";
    private static final String IO1 = "io1";
    private static final String PRIVATE_IP_ADDRESSES = "PrivateIpAddresses";
    private static final String SC1 = "sc1";
    private static final String SPECIFIC_QUERY_PARAM_PREFIX = Constants.AwsParams.NETWORK_INTERFACE + Constants.Miscellaneous.DOT;
    private static final String ST1 = "st1";
    private static final String VERSION = "Version";

    private static final int MAXIMUM_EBS_SIZE = 16384;
    private static final int MINIMUM_IO1_EBS_SIZE = 4;
    private static final int MAXIMUM_INSTANCES_NUMBER = 50;
    private static final int MINIMUM_INSTANCES_NUMBER = 1;
    private static final int MAXIMUM_STANDARD_EBS_SIZE = 1024;
    private static final int MINIMUM_SC1_AND_ST1_EBS_SIZE = 500;

    private static final float MAXIMUM_VOLUME_AMOUNT = 16000f;
    private static final float MINIMUM_VOLUME_AMOUNT = 0.5f;

    private InputsUtil() {
    }

    public static Map<String, String> getHeadersOrQueryParamsMap(Map<String, String> inputMap, String stringToSplit,
                                                                 String delimiter, String customDelimiter, boolean trim) {
        String[] headersOrParamsArray = stringToSplit.split(delimiter);
        String[] values;

        for (String headersOrParamsItem : headersOrParamsArray) {
            values = headersOrParamsItem.split(Pattern.quote(customDelimiter), 2);
            String key = trim ? values[0].trim().toLowerCase() : values[0];

            if (values.length > 1) {
                inputMap.put(key, values[1]);
            } else {
                inputMap.put(key, Constants.Miscellaneous.EMPTY);
            }
        }

        return inputMap;
    }

    public static String getEndpointFromUrl(String input) throws MalformedURLException {
        URL url = new URL(input);
        String endpoint = url.getHost();
        if (url.getPort() > 0) {
            endpoint += Constants.Miscellaneous.COLON + url.getPort();
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
        return sb.deleteCharAt(sb.length() - 1).toString();
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
        return new HashSet<>(Arrays.asList(input.split(Pattern.quote(getDefaultStringInput(delimiter, Constants.Miscellaneous.COMMA_DELIMITER)))));
    }

    public static String getDefaultStringInput(String input, String defaultValue) {
        return StringUtils.isBlank(input) ? defaultValue : input;
    }

    public static int getValidInstanceStateCode(String input) {
        return InstanceState.getKey(input);
    }

    public static String[] getArrayWithoutDuplicateEntries(String inputString, String inputName, String delimiter) {
        String[] currentArray = InputsUtil.getStringsArray(inputString, Constants.Miscellaneous.EMPTY, delimiter);
        InputsUtil.validateArrayAgainstDuplicateElements(currentArray, inputString, delimiter, inputName);

        return currentArray;
    }

    public static void validateArrayAgainstDuplicateElements(String[] toBeValidated, String inputString, String delimiter,
                                                             String inputName) {
        if (toBeValidated != null && StringUtils.isNotBlank(inputString)) {
            Set<String> stringSet = getStringsSet(inputString, delimiter);
            if (stringSet != null && toBeValidated.length != stringSet.size()) {
                throw new RuntimeException("The value provided for: " + inputName + " input contain duplicate elements. " +
                        "Please provide unique elements!");
            }
        }
    }

    public static void validateAgainstDifferentArraysLength(String[] firstArray, String[] secondArray,
                                                            String firstInputName, String secondInputName) {
        if (firstArray != null && firstArray.length > Constants.Values.START_INDEX && secondArray != null
                && secondArray.length > Constants.Values.START_INDEX && firstArray.length != secondArray.length) {
            throw new RuntimeException("The values provided: [" + firstInputName + "] and [" + secondInputName + "] " +
                    "cannot have different length!");
        }
    }

    public static void setNetworkInterfaceSpecificQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper,
                                                              String[] referenceArray, int index) {
        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                    Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING, Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION,
                    Constants.AwsParams.DESCRIPTION, wrapper.getCommonInputs().getDelimiter(), index);
        }
        if (StringUtils.isNotBlank(wrapper.getCustomInputs().getSubnetId())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getCustomInputs().getSubnetId(),
                    Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING, Inputs.CustomInputs.SUBNET_ID, Constants.AwsParams.SUBNET_ID,
                    wrapper.getCommonInputs().getDelimiter(), index);
        }
        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacesAssociatePublicIpAddressesString())) {
            setSpecificBooleanQueryParam(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfacesAssociatePublicIpAddressesString(),
                    Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING, Inputs.NetworkInputs.NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS,
                    ASSOCIATE_PUBLIC_IP_ADDRESS, wrapper.getCommonInputs().getDelimiter(), index, false);
        }
        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDeleteOnTermination())) {
            setSpecificBooleanQueryParam(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDeleteOnTermination(),
                    Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING, Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION,
                    Constants.AwsParams.DELETE_ON_TERMINATION, wrapper.getCommonInputs().getDelimiter(), index, true);
        }

        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDeviceIndex())) {
            setSpecificQueryParamValue(queryParamsMap, referenceArray, wrapper.getNetworkInputs().getNetworkInterfaceDeviceIndex(),
                    Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING, Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX,
                    Constants.AwsParams.DEVICE_INDEX, wrapper.getCommonInputs().getDelimiter(), index);
        } else {
            queryParamsMap.put(Constants.AwsParams.NETWORK_INTERFACE + Constants.Miscellaneous.DOT +
                    String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT +
                    Constants.AwsParams.DEVICE_INDEX, String.valueOf(index));
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
        if (Constants.Miscellaneous.NETWORK.equalsIgnoreCase(specificArea)) {
            return PRIVATE_IP_ADDRESSES + Constants.Miscellaneous.DOT + String.valueOf(index + Constants.Values.ONE) +
                    Constants.Miscellaneous.DOT;
        } else if (Constants.AwsParams.BLOCK_DEVICE_MAPPING.equalsIgnoreCase(specificArea)) {
            return Constants.AwsParams.BLOCK_DEVICE_MAPPING + Constants.Miscellaneous.DOT +
                    String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT;
        } else if (Constants.Miscellaneous.EBS.equalsIgnoreCase(specificArea)) {
            return Constants.AwsParams.BLOCK_DEVICE_MAPPING + Constants.Miscellaneous.DOT +
                    String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT + Constants.Miscellaneous.EBS +
                    Constants.Miscellaneous.DOT;
        } else if (Constants.AwsParams.NETWORK_INTERFACE.equalsIgnoreCase(specificArea)) {
            return Constants.AwsParams.NETWORK_INTERFACE + Constants.Miscellaneous.DOT +
                    String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT + PRIVATE_IP_ADDRESSES +
                    Constants.Miscellaneous.DOT + String.valueOf(index + Constants.Values.ONE) +
                    Constants.Miscellaneous.DOT;
        } else {
            return Constants.Miscellaneous.EMPTY;
        }
    }

    public static String getValidIPv4Address(String input) {
        if (StringUtils.isNotBlank(input) && !isValidIPv4Address(input)) {
            throw new RuntimeException("The provided value for: " + input + " input must be a valid IPv4 address.");
        }
        return input;
    }

    public static String getValidEbsSize(String input, String ebsType) throws Exception {
        if (Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(input)) {
            return Constants.Miscellaneous.NOT_RELEVANT;
        }
        switch (ebsType) {
            case Constants.AwsParams.STANDARD:
                return (StringUtils.isBlank(input)) ? String.valueOf(Constants.Values.ONE) :
                        String.valueOf(getValidInt(input, Constants.Values.ONE, MAXIMUM_STANDARD_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case IO1:
                return StringUtils.isBlank(input) ? String.valueOf(MINIMUM_IO1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_IO1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case GP2:
                return StringUtils.isBlank(input) ? String.valueOf(Constants.Values.ONE) :
                        String.valueOf(getValidInt(input, Constants.Values.ONE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case SC1:
                return StringUtils.isBlank(input) ? String.valueOf(MINIMUM_SC1_AND_ST1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_SC1_AND_ST1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            case ST1:
                return StringUtils.isBlank(input) ? String.valueOf(MINIMUM_SC1_AND_ST1_EBS_SIZE) :
                        String.valueOf(getValidInt(input, MINIMUM_SC1_AND_ST1_EBS_SIZE, MAXIMUM_EBS_SIZE,
                                getValidationException(input, true), getValidationException(input, false)));
            default:
                return String.valueOf(getValidInt(input, Constants.Values.ONE, MAXIMUM_STANDARD_EBS_SIZE,
                        getValidationException(input, true), getValidationException(input, false)));
        }
    }

    public static String getValidPositiveIntegerAsStringValue(String input, int minimumValue) {
        return StringUtils.isBlank(input) ? Constants.Miscellaneous.EMPTY :
                String.valueOf(getValidInt(input, minimumValue, Integer.MAX_VALUE,
                        getValidationException(input, true), getValidationException(input, false)));

    }

    private static void setSpecificQueryParamValue(Map<String, String> queryParamsMap, String[] referenceArray, String inputString,
                                                   String referenceInputName, String currentInputName, String suffix, String delimiter,
                                                   int index) {
        String[] currentArray = getValidStringArray(referenceArray, inputString, Constants.Miscellaneous.EMPTY,
                delimiter, referenceInputName, currentInputName);
        setOptionalMapEntry(queryParamsMap, SPECIFIC_QUERY_PARAM_PREFIX + String.valueOf(index + Constants.Values.ONE) +
                Constants.Miscellaneous.DOT + suffix, currentArray[index], currentArray.length > Constants.Values.START_INDEX);
    }

    private static void setSpecificBooleanQueryParam(Map<String, String> queryParamsMap, String[] referenceArray,
                                                     String inputString, String referenceInputName, String currentInputName,
                                                     String suffix, String delimiter, int index, boolean enforcedBoolean) {
        String[] currentArray = getValidStringArray(referenceArray, inputString, Constants.Miscellaneous.EMPTY, delimiter,
                referenceInputName, currentInputName);
        setOptionalMapEntry(queryParamsMap,
                SPECIFIC_QUERY_PARAM_PREFIX + String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT + suffix,
                String.valueOf(getEnforcedBooleanCondition(currentArray[index], enforcedBoolean)),
                currentArray.length > Constants.Values.START_INDEX);
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