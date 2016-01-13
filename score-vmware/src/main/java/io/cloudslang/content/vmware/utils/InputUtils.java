package io.cloudslang.content.vmware.utils;

import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.entities.http.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created by Mihai Tusa.
 * 10/21/2015.
 */
public class InputUtils {
    public static int getIntInput(String input, int defaultValue) {
        int intInput;
        try {
            intInput = StringUtils.isBlank(input) ? defaultValue : Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_INTEGER);
        }

        return intInput;
    }

    public static long getLongInput(String input, long defaultValue) {
        long longInput;
        try {
            longInput = StringUtils.isBlank(input) ? defaultValue : Long.parseLong(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_LONG);
        }

        return longInput;
    }

    public static String getDefaultDelimiter(String input, String defaultValue) {
        return StringUtils.isBlank(input) ? defaultValue : input;
    }

    public static String getUrlString(HttpInputs httpInputs) throws Exception {
        String protocolString = Protocol.getValue(httpInputs.getProtocol());
        String urlString = protocolString + "://" + httpInputs.getHost() + ":" + httpInputs.getPort() + Constants.URI_PATH;
        URL url = new URL(urlString.toLowerCase());

        return url.toString();
    }
}