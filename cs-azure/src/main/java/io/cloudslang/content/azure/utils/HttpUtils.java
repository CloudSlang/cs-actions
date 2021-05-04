
/*
 * (c) Copyright 2021 Micro Focus, L.P.
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


package io.cloudslang.content.azure.utils;

import io.cloudslang.content.azure.entities.AuthorizationTokenUsingWebAPIInputs;
import io.cloudslang.content.azure.entities.AzureCommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang3.StringUtils.isEmpty;


/**
 * Created by victor on 31.10.2016.
 */
public class HttpUtils {
    @NotNull
    public static Proxy getProxy(@NotNull final String proxyHost, final int proxyPort, @NotNull final String proxyUser, @NotNull final String proxyPassword) {
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

    @NotNull
    public static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                           @NotNull final AzureCommonInputs commonInputs) {
        setProxy(httpClientInputs,
                commonInputs.getProxyHost(),
                commonInputs.getProxyPort(),
                commonInputs.getProxyUsername(),
                commonInputs.getProxyPassword());

        setSecurityInputs(httpClientInputs,
                commonInputs.getTrustAllRoots(),
                commonInputs.getX509HostnameVerifier(),
                commonInputs.getTrustKeystore(),
                commonInputs.getTrustPassword());
        setTLSParameters(httpClientInputs);
    }

    public static void setProxy(@NotNull final HttpClientInputs httpClientInputs,
                                @NotNull final String proxyHost,
                                @NotNull final String proxyPort,
                                @NotNull final String proxyUsername,
                                @NotNull final String proxyPassword) {
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
    }

    @NotNull
    public static URIBuilder getUriBuilder() {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(AZURE_HOST);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final String authToken) {
        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(AUTHORIZATION).append(authToken);
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

    public static void setSecurityInputs(@NotNull final HttpClientInputs httpClientInputs,
                                         @NotNull final String trustAllRoots,
                                         @NotNull final String x509HostnameVerifier,
                                         @NotNull final String trustKeystore,
                                         @NotNull final String trustPassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
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
    public static String setAPIVersion(String apiVersion) {
        final StringBuilder queryParams = new StringBuilder()
//                .append(QUERY)
                .append("api-version=")
                .append(apiVersion);
        return queryParams.toString();
    }

    @NotNull
    public static String setAPIVersion(String apiVersion, String expand) {
        final StringBuilder queryParams = new StringBuilder()
//                .append(QUERY)
                .append("api-version=")
                .append(apiVersion)
                .append(AND)
                .append("$expand")
                .append(expand);
        return queryParams.toString();
    }

    @NotNull
    public static String setAuthenticationBody(String clientId, String clientSecret, String resource) {
        final StringBuilder authTokenBody = new StringBuilder()
                .append("grant_type=client_credentials")
                .append(AND)
                .append("client_id=")
                .append(clientId)
                .append(AND)
                .append("client_secret=")
                .append(clientSecret)
                .append(AND)
                .append("resource=")
                .append(resource);
        return authTokenBody.toString();

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

    @NotNull
    public static Object getTokenValue(@NotNull String results) throws ParseException {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(results);
        return jsonObject.get("access_token");
    }
}
