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

package io.cloudslang.content.nutanix.prism.utils;

import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.DeleteNICConstants.VM_LOGICAL_TIMESTAMP_QUERY_PARAM;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetTaskDetailsConstants.FAILED;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetTaskDetailsConstants.TASK_FAILURE_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
    public static URIBuilder getUriBuilder(String url) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(url);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final String username, @NotNull final String password) {
        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(AUTHORIZATION).append(username).append(":").append(password);
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
                                         @NotNull final String trustPassword,
                                         @NotNull final String keystore,
                                         @NotNull final String keystorePassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
    }

    public static void setConnectionParameters(HttpClientInputs httpClientInputs,
                                               @NotNull final String connectTimeout,
                                               @NotNull final String socketTimeout,
                                               @NotNull final String keepAlive,
                                               @NotNull final String connectionsMaxPerRoot,
                                               @NotNull final String connectionsMaxTotal,
                                               @NotNull final String preemptiveAuth) {
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
        httpClientInputs.setPreemptiveAuth(String.valueOf(true));
    }

    public static void setTLSParameters(HttpClientInputs httpClientInputs) {
        httpClientInputs.setTlsVersion(io.cloudslang.content.httpclient.entities.Constants.TLSv12);
        httpClientInputs.setAllowedCyphers(ALLOWED_CYPHERS);
    }

    @NotNull
    public static URIBuilder getUriBuilder(NutanixCommonInputs nutanixCommonInputs) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(nutanixCommonInputs.getHostname());
        uriBuilder.setPort(Integer.parseInt(nutanixCommonInputs.getPort()));
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getQueryParams(String includeVMDiskConfigInfo,
                                        final String includeVMNicConfigInfo) {
        final StringBuilder queryParams = new StringBuilder()
                .append(Constants.GetVMDetailsConstants.INCLUDE_VM_DISK_CONFIG_INFO)
                .append(includeVMDiskConfigInfo)
                .append(AND)
                .append(Constants.GetVMDetailsConstants.INCLUDE_VM_NIC_CONFIG_INFO)
                .append(includeVMNicConfigInfo);
        return queryParams.toString();
    }

    @NotNull
    public static String getQueryParams(String vmLogicalTimestamp) {
        final StringBuilder queryParams = new StringBuilder()
                .append(QUERY)
                .append(VM_LOGICAL_TIMESTAMP_QUERY_PARAM)
                .append(vmLogicalTimestamp);
        return queryParams.toString();
    }

  
    @NotNull
    public static String getTaskDetailsQueryParams(@NotNull String includeSubtasksInfo) {

        final StringBuilder queryParams = new StringBuilder()
                .append(Constants.GetTaskDetailsConstants.INCLUDE_SUBTASKS_INFO)
                .append(includeSubtasksInfo);
        return queryParams.toString();
    }

    @NotNull
    public static String getDeleteVMQueryParams(@NotNull String deleteSnapshots,
                                                @NotNull String logicalTimestamp) {
        final StringBuilder queryParams = new StringBuilder()
                .append(Constants.DeleteVMConstants.DELETE_SNAPSHOTS)
                .append(deleteSnapshots)
                .append(Constants.Common.AND)
                .append(Constants.DeleteVMConstants.LOGICAL_TIMESTAMP)
                .append(logicalTimestamp);
        return queryParams.toString();
    }

    @NotNull
    public static String getQueryParams(String filter, String offset, String length,
                                        String sortOrder, String sortAttribute, String includeVMDiskConfigInfo, final String includeVMNicConfigInfo) {
        final StringBuilder queryParams = new StringBuilder()
                .append(Constants.Common.QUERY)
                .append(Constants.ListVMsConstants.FILTER)
                .append(filter)
                .append(Constants.Common.AND)
                .append(Constants.ListVMsConstants.OFFSET)
                .append(offset)
                .append(Constants.Common.AND)
                .append(Constants.ListVMsConstants.LENGTH)
                .append(length)
                .append(Constants.Common.AND)
                .append(Constants.ListVMsConstants.SORT_ORDER)
                .append(sortOrder)
                .append(Constants.Common.AND)
                .append(Constants.ListVMsConstants.SORT_ATTRIBUTE)
                .append(sortAttribute)
                .append(Constants.Common.AND)
                .append(Constants.GetVMDetailsConstants.INCLUDE_VM_DISK_CONFIG_INFO)
                .append(includeVMDiskConfigInfo)
                .append(Constants.Common.AND)
                .append(Constants.GetVMDetailsConstants.INCLUDE_VM_NIC_CONFIG_INFO)
                .append(includeVMNicConfigInfo);
        return queryParams.toString();

    }

    @NotNull
    public static Map<String, String> getFailureResults(@NotNull String inputName, @NotNull Integer statusCode,
                                                        @NotNull String taskStatus, @NotNull String returnMessage,
                                                        @NotNull String throwable) {
        Map<String, String> results = new HashMap();
        results.put("returnCode", "-1");
        results.put("statusCode", statusCode.toString());
        if (statusCode.equals(401)) {
            results.put("returnResult", inputName + " not found, or user unauthorized to perform action");
            results.put("exception ", "status : " + statusCode + ", Title :  " + inputName + " not found, or user unauthorized to perform action");
        } else if (statusCode.equals(201) && taskStatus.equals(FAILED)) {
            final String errorDetail = JsonPath.read(returnMessage, TASK_FAILURE_PATH);
            results.put("returnResult", errorDetail);
            results.put("exception", " status : " + statusCode + ", Title :  " + errorDetail);
        } else if (statusCode.equals(500)) {
            final String errorDetail = JsonPath.read(returnMessage, "message");
            results.put("returnResult ", "  error Message : " + errorDetail);
            results.put("exception ", " statusCode : " + statusCode + ", Title : message " + errorDetail);
        } else {
            results.put("returnResult", throwable);
            results.put("exception", throwable);
        }
        return results;
    }
}
