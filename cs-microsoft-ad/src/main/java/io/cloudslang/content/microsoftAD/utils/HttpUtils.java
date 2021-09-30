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

package io.cloudslang.content.microsoftAD.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;

import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Outputs.CommonOutputs.EXCEPTION;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang3.StringUtils.EMPTY;
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

    @NotNull
    public static URIBuilder getUriBuilder() {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(GRAPH_HOST);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }

    @NotNull
    public static String getMessagesPath(@NotNull final String userPrincipalName,
                                         @NotNull final String userId) {

        StringBuilder pathString = new StringBuilder()
                .append(BASE_GRAPH_PATH)
                .append(getLoginType(userPrincipalName, userId))
                .append(MESSAGES_PATH);
        return pathString.toString();
    }

    @NotNull
    public static String updateUserPath(String userPrincipalName, String userId) {
        StringBuilder pathString = new StringBuilder()
                .append(BASE_GRAPH_PATH)
                .append(getLoginType(userPrincipalName, userId));
        return pathString.toString();
    }

    @NotNull
    public static String getMessagePath(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId) {
        StringBuilder messagepathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId))
                .append(PATH_SEPARATOR)
                .append(messageId);
        return messagepathString.toString();
    }

    @NotNull
    public static String addAttachmentPath(@NotNull final String userPrincipalName,
                                           @NotNull final String userId,
                                           @NotNull final String messageId) {
        final StringBuilder addAttachmentString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId))
                .append(PATH_SEPARATOR)
                .append(messageId)
                .append(ATTACHMENTS);
        return addAttachmentString.toString();
    }

    @NotNull
    public static String getAttachmentsPath(@NotNull final String userPrincipalName,
                                            @NotNull final String userId,
                                            @NotNull final String messageId,
                                            @NotNull final String attachmentId) {
        StringBuilder messagepathString = new StringBuilder()
                .append(BASE_GRAPH_PATH)
                .append(getLoginType(userPrincipalName, userId))
                .append(MESSAGES_PATH)
                .append(PATH_SEPARATOR)
                .append(messageId)
                .append(ATTACHMENTS_PATH)
                .append(attachmentId);
        return messagepathString.toString();
    }


    @NotNull
    public static String sendMessagePath(@NotNull final String userPrincipalName,
                                         @NotNull final String userId,
                                         @NotNull final String messageId) {
        StringBuilder messagepathString = new StringBuilder()
                .append(getMessagePath(userPrincipalName, userId, messageId))
                .append(SEND);
        return messagepathString.toString();
    }

    @NotNull
    public static String getMessagesPath(@NotNull final String userPrincipalName,
                                         @NotNull final String userId,
                                         @NotNull final String folderId) {

        StringBuilder pathString = new StringBuilder()
                .append(BASE_GRAPH_PATH)
                .append(getLoginType(userPrincipalName, userId))
                .append(MAIL_FOLDERS_PATH)
                .append(folderId)
                .append(MESSAGES_PATH);
        return pathString.toString();
    }

    @NotNull
    public static String getMessagePath(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId,
                                        @NotNull final String folderId) {

        StringBuilder pathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId, folderId))
                .append(PATH_SEPARATOR)
                .append(messageId);
        return pathString.toString();
    }

    @NotNull
    public static String moveMessagePath(@NotNull final String userPrincipalName,
                                         @NotNull final String userId,
                                         @NotNull final String messageId) {
        StringBuilder messagepathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId))
                .append(PATH_SEPARATOR)
                .append(messageId)
                .append(MOVE);
        return messagepathString.toString();
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
    }

    @NotNull
    public static String getAuthHeaders(@NotNull final String authToken) {
        final StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(AUTHORIZATION).append(BEARER).append(authToken);
        return headerBuilder.toString();
    }

    @NotNull
    public static String getQueryParams(@NotNull final String topQuery,
                                        @NotNull final String selectQuery,
                                        @NotNull String oDataQuery) {
        final StringBuilder oDataQueryParam = new StringBuilder()
                .append(TOP_QUERY)
                .append(topQuery);
        if (!isEmpty(selectQuery)) {
            oDataQueryParam.append(AND)
                    .append(SELECT_PATH)
                    .append(selectQuery);
        }
        if (!isEmpty(oDataQuery)) {
            if (oDataQuery.startsWith(QUERY)) {
                oDataQuery = oDataQuery.substring(1);
            }
            if (!oDataQuery.startsWith(AND)) {
                oDataQuery = AND + oDataQuery;
            }
            oDataQueryParam.append(oDataQuery);
        }

        return oDataQueryParam.toString();
    }

    @NotNull
    public static String getQueryParams(@NotNull final String selectQuery,
                                        @NotNull String oDataQuery) {
        final StringBuilder oDataQueryParam = new StringBuilder();
        if (!isEmpty(selectQuery)) {
            oDataQueryParam.append(SELECT_PATH)
                    .append(selectQuery);
        }
        if (!isEmpty(oDataQuery)) {
            if (oDataQuery.startsWith(QUERY)) {
                oDataQuery = oDataQuery.substring(1);
            }
            if (oDataQuery.startsWith(AND) && isEmpty(selectQuery)) {
                oDataQuery = oDataQuery.substring(1);
            }
            if (!oDataQuery.startsWith(AND) && !isEmpty(selectQuery)) {
                oDataQuery = AND + oDataQuery;
            }
            oDataQueryParam.append(oDataQuery);
        }

        return oDataQueryParam.toString();
    }

    @NotNull
    private static String getLoginType(@NotNull final String userPrincipalName,
                                       @NotNull final String userId) {
        if (isEmpty(userId))
            return userPrincipalName;
        return userId;
    }

    @NotNull
    public static Map<String, String> getOperationResults(@NotNull final Map<String, String> result,
                                                          @NotNull final String successMessage,
                                                          final String failureMessage) {
        final Map<String, String> results;
        final String statusCode = result.get(STATUS_CODE);


        //Validation for empty status code
        if (statusCode.equals(EMPTY)) {

            results = getFailureResultsMap(failureMessage);

            if (result.get(EXCEPTION) != null)
                results.put(EXCEPTION, result.get(EXCEPTION));

            return results;
        }


        if (Integer.parseInt(statusCode) >= 200 && Integer.parseInt(statusCode) < 300)
            results = getSuccessResultsMap(successMessage);
        else
            results = getFailureResultsMap(failureMessage);

        results.put(STATUS_CODE, statusCode);

        if (result.get(EXCEPTION) != null)
            results.put(EXCEPTION, result.get(EXCEPTION));

        return results;
    }
}
