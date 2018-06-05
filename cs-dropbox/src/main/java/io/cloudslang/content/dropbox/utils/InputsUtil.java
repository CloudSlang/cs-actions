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

package io.cloudslang.content.dropbox.utils;

import io.cloudslang.content.dropbox.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import java.net.MalformedURLException;
import java.net.URL;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.dropbox.entities.constants.Constants.ErrorMessages.CONSTRAINS_ERROR_MESSAGE;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.ALLOW_ALL;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.BROWSER_COMPATIBLE;
import static io.cloudslang.content.dropbox.entities.constants.Constants.HttpClientInputsValues.STRICT;
import static io.cloudslang.content.dropbox.entities.constants.Constants.Values.INIT_INDEX;
import static io.cloudslang.content.dropbox.factory.UriFactory.getUri;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.ANONYMOUS;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by TusaM
 * 5/26/2017.
 */
public class InputsUtil {
    private InputsUtil() {
        // prevent instantiation
    }

    public static HttpClientInputs getHttpClientInputs(String proxyHost, String proxyPort, String proxyUsername,
                                                       String proxyPassword, String trustAllRoots, String x509HostnameVerifier,
                                                       String trustKeystore, String trustPassword, String keystore,
                                                       String keystorePassword, String connectTimeout, String socketTimeout,
                                                       String useCookies, String keepAlive, String method) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(method);
        httpClientInputs.setQueryParamsAreURLEncoded(FALSE);

        httpClientInputs.setTrustAllRoots(valueOf(getEnforcedBooleanCondition(trustAllRoots, false)));
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

    public static String buildUrl(InputsWrapper wrapper) throws MalformedURLException {
        return getUrl(wrapper.getCommonInputs().getEndpoint()) + getUri(wrapper);
    }

    public static String toAppend(String prefix, String suffix) {
        return (isBlank(suffix)) ? prefix : prefix + suffix;
    }

    public static String getValidPath(String input, String regex) {
        if (!compile(regex).matcher(input).matches()) {
            throw new IllegalArgumentException(format("Incorrect provided value: %s input. %s", input, CONSTRAINS_ERROR_MESSAGE));
        }

        return input;
    }

    public static String getInputWithDefaultValue(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    private static String getUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
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
    private static boolean getEnforcedBooleanCondition(String input, boolean enforcedBoolean) {
        return (enforcedBoolean) ? isValid(input) == Boolean.parseBoolean(input) : Boolean.parseBoolean(input);
    }
}