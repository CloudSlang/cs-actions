package io.cloudslang.content.utils;

import io.cloudslang.content.constants.Constants;
import io.cloudslang.content.constants.ErrorMessages;
import io.cloudslang.content.entities.http.HttpInputs;
import io.cloudslang.content.entities.http.Protocol;
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

    public static String getUrlString(HttpInputs httpInputs) throws Exception {
        String protocolString = Protocol.getValue(httpInputs.getProtocol());
        String urlString = protocolString + "://" + httpInputs.getHost() + ":" + httpInputs.getPort() + Constants.URI_PATH;
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