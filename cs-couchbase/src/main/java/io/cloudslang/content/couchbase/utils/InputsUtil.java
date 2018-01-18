/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import io.cloudslang.content.couchbase.entities.couchbase.BucketType;
import io.cloudslang.content.couchbase.entities.couchbase.ConflictResolutionType;
import io.cloudslang.content.couchbase.entities.couchbase.EvictionPolicy;
import io.cloudslang.content.couchbase.entities.couchbase.RecoveryType;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALLOW_ALL;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.BROWSER_COMPATIBLE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.STRICT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.CONSTRAINS_ERROR_MESSAGE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.AT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.BLANK_SPACE;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.COMMA;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.PORT_REGEX;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.SLASH;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.COUCHBASE_DEFAULT_PROXY_PORT;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.INIT_INDEX;
import static io.cloudslang.content.couchbase.entities.couchbase.SuffixUri.getSuffixUriValue;
import static io.cloudslang.content.couchbase.factory.UriFactory.getUri;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
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
        httpClientInputs.setQueryParamsAreURLEncoded(valueOf(FALSE));
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);

        httpClientInputs.setTrustAllRoots(valueOf(getEnforcedBooleanCondition(trustAllRoots, FALSE)));
        httpClientInputs.setKeepAlive(valueOf(getEnforcedBooleanCondition(keepAlive, TRUE)));
        httpClientInputs.setUseCookies(valueOf(getEnforcedBooleanCondition(useCookies, TRUE)));

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

        if (isBlank(x509HostnameVerifier) || !asList(ALLOW_ALL, BROWSER_COMPATIBLE, STRICT).contains(x509HostnameVerifier)) {
            httpClientInputs.setX509HostnameVerifier(ALLOW_ALL);
        } else {
            httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        }

        return httpClientInputs;
    }

    public static String buildUrl(InputsWrapper wrapper) throws MalformedURLException {
        return getUrl(wrapper.getCommonInputs().getEndpoint()) + getUri(wrapper);
    }

    public static String appendTo(String prefix, String suffix, String action) {
        return (isBlank(suffix)) ? prefix : prefix + SLASH + suffix + getSuffixUriValue(action);
    }

    public static String getPayloadString(Map<String, String> payloadMap, String separator, String suffix, boolean deleteLastChar) {
        if (payloadMap.isEmpty()) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : payloadMap.entrySet()) {
            sb.append(entry.getKey()).append(separator).append(entry.getValue()).append(suffix);
        }

        return deleteLastChar ? sb.deleteCharAt(sb.length() - 1).toString() : sb.toString();
    }

    public static int getValidPort(String input) {
        if (isBlank(input)) {
            return COUCHBASE_DEFAULT_PROXY_PORT;
        }

        if (!compile(PORT_REGEX).matcher(input).matches()) {
            throw new IllegalArgumentException(format("Incorrect provided value: %s input. %s", input, CONSTRAINS_ERROR_MESSAGE));
        }

        return parseInt(input);
    }

    public static String getEnabledString(String input, boolean enforcedBoolean) {
        return getEnforcedBooleanCondition(input, enforcedBoolean) ? valueOf(1) : valueOf(INIT_INDEX);
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
        return (enforcedBoolean) ? isValid(input) == parseBoolean(input) : parseBoolean(input);
    }

    public static int getValidIntValue(String input, Integer minAllowed, Integer maxAllowed, Integer defaultValue) {
        return isBlank(input) ? defaultValue : maxAllowed == null ?
                getIntegerAboveMinimum(input, minAllowed) : getIntegerWithinValidRange(input, minAllowed, maxAllowed);
    }

    public static void setOptionalMapEntry(Map<String, String> inputMap, String key, String value, boolean condition) {
        if (condition) {
            inputMap.put(key, value);
        }
    }

    public static <T extends Enum<T>> String getEnumValidValuesString(Class<T> inputEnum) {
        StringBuilder sb = new StringBuilder();
        for (T enumValue : inputEnum.getEnumConstants()) {
            if (AuthType.class.getCanonicalName().equalsIgnoreCase(enumValue.getClass().getCanonicalName())) {
                sb.append(((AuthType) enumValue).getValue()).append(COMMA).append(BLANK_SPACE);
            } else if (BucketType.class.getCanonicalName().equalsIgnoreCase(enumValue.getClass().getCanonicalName())) {
                sb.append(((BucketType) enumValue).getValue()).append(COMMA).append(BLANK_SPACE);
            } else if (ConflictResolutionType.class.getCanonicalName().equalsIgnoreCase(enumValue.getClass().getCanonicalName())) {
                sb.append(((ConflictResolutionType) enumValue).getValue()).append(COMMA).append(BLANK_SPACE);
            } else if (EvictionPolicy.class.getCanonicalName().equalsIgnoreCase(enumValue.getClass().getCanonicalName())) {
                sb.append(((EvictionPolicy) enumValue).getValue()).append(COMMA).append(BLANK_SPACE);
            } else if (RecoveryType.class.getCanonicalName().equalsIgnoreCase(enumValue.getClass().getCanonicalName())) {
                sb.append(((RecoveryType) enumValue).getValue()).append(COMMA).append(BLANK_SPACE);
            }
        }

        return isBlank(sb.toString()) ? EMPTY : sb.deleteCharAt(sb.length() - 2).toString().trim();
    }

    public static String getValidInternalNodeIpAddress(String input) {
        validateClusterInternalNodeFormat(input);

        return input;
    }

    public static void validateNotBothBlankInputs(String value1, String value2, String name1, String name2) {
        if (isBlank(value1) && isBlank(value2)) {
            throw new RuntimeException(format("The values: %s, %s provided for inputs: %s, %s cannot be both empty. Please provide values for at least one of them.",
                    value1, value2, name1, name2));
        }
    }

    public static void validateRebalancingNodesPayloadInputs(String input, String delimiter) {
        String[] nodesArray = getStringsArray(input, delimiter);
        if (nodesArray != null) {
            for (String node : nodesArray) {
                validateClusterInternalNodeFormat(node);
            }
        }
    }

    public static String getInputWithDefaultValue(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    private static String[] getStringsArray(String input, String delimiter) {
        return isBlank(input) ? null : split(input, delimiter);
    }

    private static void validateClusterInternalNodeFormat(String input) {
        if (!input.contains(AT)) {
            throw new RuntimeException(format("The provided value for: \"%s\" input must be a valid Couchbase internal node format.", input));
        }

        int indexOfAt = input.indexOf(AT);
        String ipv4Address = input.substring(indexOfAt + 1);

        if (!isValidIPv4Address(ipv4Address)) {
            throw new RuntimeException(format("The value of: [%s] input as part of: [%s] input must be a valid IPv4 address.", ipv4Address, input));
        }
    }

    private static boolean isValidIPv4Address(String input) {
        return new InetAddressValidator().isValidInet4Address(input) ? TRUE : FALSE;
    }

    private static int getIntegerWithinValidRange(String input, Integer minAllowed, Integer maxAllowed) {
        if (isValidInt(input, minAllowed, maxAllowed, true, true)) {
            return parseInt(input);
        }

        throw new RuntimeException(format("The provided value: %s is not within valid range. See operation inputs description section for details.", input));
    }

    private static int getIntegerAboveMinimum(String input, Integer minAllowed) {
        try {
            int validInt = parseInt(input);
            if (validInt < minAllowed) {
                throw new RuntimeException(format("The provided value: %s is bellow minimum allowed. See operation inputs description section for details.", input));
            }

            return validInt;
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(format("The provided input value: %s is not integer.", input));
        }
    }

    private static String getUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
    }
}