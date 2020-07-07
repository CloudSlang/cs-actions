/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class HttpUtils {
    @org.jetbrains.annotations.NotNull
    public static Proxy getProxy(@org.jetbrains.annotations.NotNull final String proxyHost, final int proxyPort, @org.jetbrains.annotations.NotNull final String proxyUser, @org.jetbrains.annotations.NotNull final String proxyPassword) {
        if (StringUtilities.isBlank(proxyHost)) {
            return Proxy.NO_PROXY;
        }
        if (StringUtilities.isNotEmpty(proxyUser)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
        }
        return new Proxy(HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort));
    }

    public static void setProxy(@org.jetbrains.annotations.NotNull final HttpClientInputs httpClientInputs,
                                @org.jetbrains.annotations.NotNull final String proxyHost,
                                @org.jetbrains.annotations.NotNull final String proxyPort,
                                @org.jetbrains.annotations.NotNull final String proxyUsername,
                                @org.jetbrains.annotations.NotNull final String proxyPassword) {
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
    }

    @NotNull
    public static URIBuilder getUriBuilder() {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(TERRAFORM_HOST);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final String authToken) {
        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(AUTHORIZATION).append(BEARER).append(authToken);
        return headerBuilder.toString();
    }

    @NotNull
    public static Map<String, String> getOperationResults(@NotNull final Map<String, String> result,
                                                          @NotNull final String successMessage,
                                                          final String failureMessage,
                                                          final String document) {
        final Map<String, String> results;
        final String statusCode = result.get(STATUS_CODE);
        if (Integer.parseInt(statusCode) >= 200 && Integer.parseInt(statusCode) < 300) {
            results = getSuccessResultsMap(successMessage);
            if (!isEmpty(document))
                results.put(DOCUMENT, document);
        } else {
            results = getFailureResultsMap(failureMessage);
        }
        results.put(STATUS_CODE, statusCode);
        return results;
    }

    public static void setSecurityInputs(@org.jetbrains.annotations.NotNull final HttpClientInputs httpClientInputs,
                                         @org.jetbrains.annotations.NotNull final String trustAllRoots,
                                         @org.jetbrains.annotations.NotNull final String x509HostnameVerifier,
                                         @org.jetbrains.annotations.NotNull final String trustKeystore,
                                         @org.jetbrains.annotations.NotNull final String trustPassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
    }

    public static void setConnectionParameters(HttpClientInputs httpClientInputs,
                                               @NotNull final String connectTimeout,
                                               @NotNull final String socketTimeout,
                                               @NotNull final String keepAlive,
                                               @NotNull final String connectionsMaxPerRoot,
                                               @NotNull final String connectionsMaxTotal) {
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
    }

    public static void setTLSParameters(HttpClientInputs httpClientInputs) {
        httpClientInputs.setTlsVersion(io.cloudslang.content.httpclient.entities.Constants.TLSv12);
        httpClientInputs.setAllowedCyphers(ALLOWED_CYPHERS);

    }

    @NotNull
    public static String getQueryParams(String pageNumber,
                                        final String pageSize) {
        final StringBuilder queryParams = new StringBuilder()
                .append(QUERY)
                .append(PAGE_NUMBER)
                .append(pageNumber)
                .append(AND)
                .append(PAGE_SIZE)
                .append(pageSize);
        return queryParams.toString();
    }

    @NotNull
    public static Map<String, String> getFailureResults(@NotNull String inputName, @NotNull Integer statusCode, @NotNull String throwable) {
        Map<String, String> results = new HashMap();
        results.put("returnCode", "-1");
        results.put("statusCode",statusCode.toString());
        if (statusCode.equals(404)) {
            results.put("returnResult", inputName + " not found, or user unauthorized to perform action");
            results.put("exception ", "status : " + statusCode + ", Title :  " + inputName + " not found, or user unauthorized to perform action");
        } else {
            results.put("returnResult", throwable);
            results.put("exception", throwable);
        }
        return results;
    }

}
