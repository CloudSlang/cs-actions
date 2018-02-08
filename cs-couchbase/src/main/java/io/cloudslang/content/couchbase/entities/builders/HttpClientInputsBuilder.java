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
package io.cloudslang.content.couchbase.entities.builders;

import io.cloudslang.content.httpclient.HttpClientInputs;

import static io.cloudslang.content.couchbase.validate.Validators.areBothValuesPresent;
import static io.cloudslang.content.couchbase.validate.Validators.getValidOrDefaultValue;
import static io.cloudslang.content.httpclient.build.auth.AuthTypes.BASIC;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static java.lang.Boolean.FALSE;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HttpClientInputsBuilder {
    private static final String[] VALID_HOSTNAME_VERIFIERS = new String[]{"allow_all", "browser_compatible", "strict"};
    private static final String ALLOW_ALL = "allow_all";

    private HttpClientInputsBuilder() {
        // prevent instantiation
    }

    public static HttpClientInputs buildHttpClientInputs(String username, String password, String proxyHost, String proxyPort,
                                                         String proxyUsername, String proxyPassword, String trustAllRoots,
                                                         String x509HostnameVerifier, String trustKeystore, String trustPassword,
                                                         String keystore, String keystorePassword, String connectTimeout,
                                                         String socketTimeout, String useCookies, String keepAlive, String method) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();

        httpClientInputs.setMethod(method);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setQueryParamsAreURLEncoded(valueOf(FALSE));
        httpClientInputs.setRequestCharacterSet(UTF_8.toString());
        httpClientInputs.setQueryParamsAreURLEncoded(valueOf(FALSE));

        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);

        httpClientInputs.setTrustAllRoots(valueOf(Boolean.valueOf(trustAllRoots)));
        httpClientInputs.setKeepAlive(valueOf(!Boolean.valueOf(keepAlive)));
        httpClientInputs.setUseCookies(valueOf(useCookies));
        httpClientInputs.setConnectTimeout(valueOf(isValidInt(connectTimeout, 0, Integer.MAX_VALUE) ? parseInt(connectTimeout) : 0));
        httpClientInputs.setSocketTimeout(valueOf(isValidInt(socketTimeout, 0, Integer.MAX_VALUE) ? parseInt(socketTimeout) : 0));
        httpClientInputs.setX509HostnameVerifier(getValidOrDefaultValue(x509HostnameVerifier, ALLOW_ALL, VALID_HOSTNAME_VERIFIERS));

        if (isNotBlank(trustKeystore)) {
            httpClientInputs.setTrustKeystore(trustKeystore);
        }

        if (isNotBlank(trustPassword)) {
            httpClientInputs.setTrustPassword(trustPassword);
        }

        if (isNotBlank(keystore)) {
            httpClientInputs.setKeystore(keystore);
        }

        if (isNotBlank(keystorePassword)) {
            httpClientInputs.setKeystorePassword(keystorePassword);
        }

        if (isNotBlank(proxyUsername)) {
            httpClientInputs.setProxyUsername(proxyUsername);
        }

        if (isNotBlank(proxyUsername) && isNotBlank(proxyPassword)) {
            httpClientInputs.setProxyPassword(proxyPassword);
        }

        if (areBothValuesPresent(proxyHost, proxyPort)) {
            httpClientInputs.setProxyHost(proxyHost);
            httpClientInputs.setProxyPort(proxyPort);
        }

        return httpClientInputs;
    }
}
