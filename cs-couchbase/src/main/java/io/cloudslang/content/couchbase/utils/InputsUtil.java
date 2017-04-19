/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.UriSuffix;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALLOW_ALL;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.BROWSER_COMPATIBLE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.STRICT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.CONSTRAINS_ERROR_MESSAGE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.PORT_REGEX;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.COUCHBASE_DEFAULT_PROXY_PORT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.INIT_INDEX;
import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.client.config.AuthSchemes.BASIC;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */

public class InputsUtil {
    private InputsUtil() {
        // prevent instantiation
    }

    public static HttpClientInputs getHttpClientInputs(String username, String password, String proxyHost, String proxyPort,
                                                       String proxyUsername, String proxyPassword, String trustAllRoots,
                                                       String x509HostnameVerifier, String trustKeystore, String trustPassword,
                                                       String keystore, String keystorePassword, String connectTimeout,
                                                       String socketTimeout, String useCookies, String keepAlive, String method) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setMethod(method);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setQueryParamsAreURLEncoded(FALSE);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);

        httpClientInputs.setTrustAllRoots(valueOf(getEnforcedBooleanCondition(trustAllRoots, true)));
        httpClientInputs.setKeepAlive(valueOf(getEnforcedBooleanCondition(keepAlive, true)));
        httpClientInputs.setUseCookies(valueOf(getEnforcedBooleanCondition(useCookies, true)));

        if (isNotBlank(proxyHost) && isNotBlank(proxyPort)) {
            httpClientInputs.setProxyHost(proxyHost);
            httpClientInputs.setProxyPort(proxyPort);
        }

        if (isNotBlank(proxyUsername) && isNotBlank(proxyPassword)) {
            httpClientInputs.setProxyUsername(proxyUsername);
            httpClientInputs.setProxyPassword(proxyPassword);
        }

        if (isNotBlank(trustKeystore) && isNotBlank(trustPassword)) {
            httpClientInputs.setTrustKeystore(trustKeystore);
            httpClientInputs.setTrustPassword(trustPassword);
        }

        if (isNotBlank(keystore) && isNotBlank(keystorePassword)) {
            httpClientInputs.setKeystore(keystore);
            httpClientInputs.setKeystorePassword(keystorePassword);
        }

        httpClientInputs.setConnectTimeout(getInputWithDefaultValue(connectTimeout, valueOf(INIT_INDEX)));
        httpClientInputs.setSocketTimeout(getInputWithDefaultValue(socketTimeout, valueOf(INIT_INDEX)));

        if (isBlank(x509HostnameVerifier)) {
            httpClientInputs.setX509HostnameVerifier(ALLOW_ALL);
        } else {
            if (asList(ALLOW_ALL, BROWSER_COMPATIBLE, STRICT).contains(x509HostnameVerifier)) {
                httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
            }
        }

        return httpClientInputs;
    }

    public static String getUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
    }

    public static String appendTo(String prefix, String suffix, String action) {
        if (isBlank(suffix)) {
            return prefix;
        }

        return prefix + "/" + suffix + UriSuffix.getUriSuffix(action);
    }

    public static String getPayloadString(Map<String, String> headersOrParamsMap, String separator, String suffix, boolean deleteLastChar) {
        if (headersOrParamsMap.isEmpty()) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headersOrParamsMap.entrySet()) {
            sb.append(entry.getKey()).append(separator).append(entry.getValue()).append(suffix);
        }

        return deleteLastChar ? sb.deleteCharAt(sb.length() - 1).toString() : sb.toString();
    }

    public static int getValidPort(String input) {
        if (isBlank(input)) {
            return COUCHBASE_DEFAULT_PROXY_PORT;
        }

        patternCheck(input, PORT_REGEX);

        return Integer.parseInt(input);
    }

    public static String getEnabledString(String input, boolean enforcedBoolean) {
        if (getEnforcedBooleanCondition(input, enforcedBoolean)) {
            return valueOf(1);
        }

        return valueOf(INIT_INDEX);
    }

    /**
     * If enforcedBoolean is "true" and string input is: null, empty, many empty chars, TrUe, tRuE... but not "false"
     * then returns "true".
     * If enforcedBoolean is "false" and string input is: null, empty, many empty chars, FaLsE, fAlSe... but not "true"
     * then returns "false"
     * This behavior is needed for inputs like: "imageNoReboot" when we want them to be set to "true" disregarding the
     * value provided (null, empty, many empty chars, TrUe, tRuE) except the case when is "false"
     *
     * @param input           String to be evaluated.
     * @param enforcedBoolean Enforcement boolean.
     * @return A boolean according with above description.
     */
    public static boolean getEnforcedBooleanCondition(String input, boolean enforcedBoolean) {
        return (enforcedBoolean) ? isValid(input) == Boolean.parseBoolean(input) : Boolean.parseBoolean(input);
    }

    public static int getValidIntValue(String input, Integer minAllowed, Integer maxAllowed, Integer defaultValue) {
        return isBlank(input) ? defaultValue : getIntegerWithinValidRange(input, minAllowed, maxAllowed);
    }

    public static void setOptionalMapEntry(Map<String, String> inputMap, String key, String value, boolean condition) {
        if (condition) {
            inputMap.put(key, value);
        }
    }

    private static String getInputWithDefaultValue(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    private static int getIntegerWithinValidRange(String input, Integer minAllowed, Integer maxAllowed) {
        if (isValidInt(input, minAllowed, maxAllowed, true, true)) {
            return Integer.parseInt(input);
        }

        throw new RuntimeException(CONSTRAINS_ERROR_MESSAGE);
    }

    private static void patternCheck(String input, String regex) {
        if (!compile(regex).matcher(input).matches()) {
            throw new IllegalArgumentException("Incorrect provided value: " + input + " input. "  + CONSTRAINS_ERROR_MESSAGE);
        }
    }
}