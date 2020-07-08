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

package io.cloudslang.content.oracle.oci.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.InstanceActionConstants.ACTION_NAME_QUERY_PARAM;
import static io.cloudslang.content.oracle.oci.utils.Constants.TerminateInstanceConstants.PRESERVE_BOOT_VOLUME_QUERY_PARAM;
import static io.cloudslang.content.oracle.oci.utils.Outputs.CommonOutputs.DOCUMENT;
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
    public static URIBuilder getUriBuilder(@NotNull final String region) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(IAAS + "." + region + "." + OCI_HOST);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final Map<String, String> headers) {
        final StringBuilder headerBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey()).append(entry.getValue()).append(NEW_LINE);
        }

        return headerBuilder.toString().trim();
    }

    public static void setTLSParameters(HttpClientInputs httpClientInputs) {
        httpClientInputs.setTlsVersion(io.cloudslang.content.httpclient.entities.Constants.TLSv12);
        httpClientInputs.setAllowedCyphers(ALLOWED_CYPHERS);

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

    public static void setSecurityInputs(@org.jetbrains.annotations.NotNull final HttpClientInputs httpClientInputs) {
    }

    public static void setConnectionParameters(HttpClientInputs httpClientInputs) {
    }

    @NotNull
    public static String getQueryParams(@NotNull final String compartmentId) {
        final StringBuilder queryParams = new StringBuilder()
                .append(COMPARTMENT_ID_QUERY_PARAM)
                .append(compartmentId);
        return queryParams.toString();
    }

    @NotNull
    public static String getQueryParamForTerminateInstance(@NotNull final String preserveBootVolume) {
        final StringBuilder queryParams = new StringBuilder()
                .append(PRESERVE_BOOT_VOLUME_QUERY_PARAM)
                .append(preserveBootVolume);
        return queryParams.toString();
    }

    @NotNull
    public static String getQueryParamForInstanceAction(@NotNull final String instanceAction) {
        final StringBuilder queryParams = new StringBuilder()
                .append(ACTION_NAME_QUERY_PARAM)
                .append(instanceAction);
        return queryParams.toString();
    }

    @NotNull
    public static String getQueryParams(@NotNull final String availabilityDomain, @NotNull final String compartmentId, @NotNull final String instanceId, @NotNull final String page, @NotNull final String limit, @NotNull final String vnicId) {
        StringBuilder queryParams = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put(compartmentId, COMPARTMENT_ID_QUERY_PARAM);
        map.put(availabilityDomain, AVAILABILITY_DOMAIN_QUERY_PARAM);
        map.put(instanceId, INSTANCE_ID_QUERY_PARAM);
        map.put(vnicId, VNIC_ID_QUERY_PARAM);
        map.put(page, PAGE_QUERY_PARAM);
        map.put(limit, LIMIT_QUERY_PARAM);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!isEmpty(entry.getKey())) {
                queryParams = queryParams.append((entry.getValue() + entry.getKey()) + AND);
            }
        }

        return queryParams.substring(0, queryParams.length() - 1);
    }

    @NotNull
    public static String getQueryParams(@NotNull final String availabilityDomain, @NotNull final String compartmentId, @NotNull final String displayName, @NotNull final String limit, @NotNull final String page, @NotNull final String sortBy, @NotNull final String sortOrder, @NotNull final String instanceName) {
        StringBuilder queryParams = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        map.put(compartmentId, COMPARTMENT_ID_QUERY_PARAM);
        map.put(availabilityDomain, AVAILABILITY_DOMAIN_QUERY_PARAM);
        map.put(displayName, DISPLAYNAME_QUERY_PARAM);
        map.put(instanceName, LIFECYCLE_STATE_QUERY_PARAM);
        map.put(page, PAGE_QUERY_PARAM);
        map.put(limit, LIMIT_QUERY_PARAM);
        map.put(sortBy, SORT_BY_QUERY_PARAM);
        map.put(sortOrder, SORT_ORDER_QUERY_PARAM);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!isEmpty(entry.getKey())) {
                queryParams = queryParams.append((entry.getValue() + entry.getKey()) + AND);
            }
        }

        return queryParams.substring(0, queryParams.length() - 1);
    }

    @NotNull
    public static Map<String, String> getFailureResults(@NotNull String inputName, @NotNull Integer statusCode, @NotNull String throwable) {
        Map<String, String> results = new HashMap();
        results.put("returnCode", "-1");
        results.put("statusCode", statusCode.toString());
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
