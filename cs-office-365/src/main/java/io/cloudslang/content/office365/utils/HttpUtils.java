/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

import static io.cloudslang.content.office365.utils.Constants.AUTHORIZATION;
import static io.cloudslang.content.office365.utils.Constants.BASE_GRAPH_PATH;
import static io.cloudslang.content.office365.utils.Constants.BEARER;
import static io.cloudslang.content.office365.utils.Constants.DATA_QUERY;
import static io.cloudslang.content.office365.utils.Constants.GRAPH_HOST;
import static io.cloudslang.content.office365.utils.Constants.HTTPS;
import static io.cloudslang.content.office365.utils.Constants.MAIL_FOLDERS_PATH;
import static io.cloudslang.content.office365.utils.Constants.MESSAGES_PATH;
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
    public static String getMessagePath(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId) {
        StringBuilder messagepathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId))
                .append(messageId);
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
                .append(messageId);
        return pathString.toString();
    }

    @NotNull
    public static String listMessagesPath(@NotNull final String userPrincipalName,
                                          @NotNull final String userId) {
        StringBuilder pathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId));
        return pathString.toString();
    }

    @NotNull
    public static String listMessagesPath(@NotNull final String userPrincipalName,
                                          @NotNull final String userId,
                                          @NotNull final String folderId) {
        StringBuilder pathString = new StringBuilder()
                .append(getMessagesPath(userPrincipalName, userId, folderId));
        return pathString.toString();
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
    public static String getQueryParams(@NotNull final String oDataQuery) {
        final StringBuilder oDataQueryParam = new StringBuilder()
                .append(DATA_QUERY)
                .append(oDataQuery);
        return oDataQueryParam.toString();
    }

    private static String getLoginType(@NotNull final String userPrincipalName,
                                       @NotNull final String userId) {
        if (isEmpty(userId))
            return userPrincipalName;
        return userId;
    }

}
