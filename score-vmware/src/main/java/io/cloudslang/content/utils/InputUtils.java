package io.cloudslang.content.utils;

import io.cloudslang.content.constants.Constants;
import io.cloudslang.content.constants.ErrorMessages;
import io.cloudslang.content.constants.Inputs;
import io.cloudslang.content.entities.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created by Mihai Tusa.
 * 10/21/2015.
 */
public class InputUtils {
    public static int getIntInput(String input, int defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }

        return getIntFromStringInput(input);
    }

    public static long getLongInput(String input, long defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue;
        }

        return getLongFromStringInput(input);
    }

    public static boolean getBooleanInput(String input) {
        if (StringUtils.isBlank(input)) {
            return Boolean.FALSE;
        }
        return Boolean.parseBoolean(input.toLowerCase());
    }

    public static String getUrlString(String host, String port, String protocol) throws Exception {
        String protocolString = Protocol.getValue(protocol);
        String urlString = protocolString + Constants.COLON + Constants.URL_SLASHES + host + Constants.COLON + port + Inputs.URI_PATH;
        URL url = new URL(urlString.toLowerCase());

        return url.toString();
    }

    private static int getIntFromStringInput(String input) {
        int intInput;
        try {
            intInput = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_INTEGER);
        }

        return intInput;
    }

    private static long getLongFromStringInput(String input) {
        long longInput;
        try {
            longInput = Long.parseLong(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_LONG);
        }

        return longInput;
    }
}