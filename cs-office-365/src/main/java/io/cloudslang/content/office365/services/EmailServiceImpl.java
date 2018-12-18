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
package io.cloudslang.content.office365.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.GetMessageInputs;
import io.cloudslang.content.office365.entities.ListMessagesInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.httpclient.services.HttpClientService.RETURN_RESULT;
import static io.cloudslang.content.office365.utils.Constants.ANONYMOUS;
import static io.cloudslang.content.office365.utils.Constants.CHANGEIT;
import static io.cloudslang.content.office365.utils.Constants.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.office365.utils.Constants.GET;
import static io.cloudslang.content.office365.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.office365.utils.HttpUtils.getMessagePath;
import static io.cloudslang.content.office365.utils.HttpUtils.getMessagesPath;
import static io.cloudslang.content.office365.utils.HttpUtils.getQueryParams;
import static io.cloudslang.content.office365.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.office365.utils.HttpUtils.setConnectionParameters;
import static io.cloudslang.content.office365.utils.HttpUtils.setProxy;
import static io.cloudslang.content.office365.utils.HttpUtils.setSecurityInputs;

public class EmailServiceImpl {

    @NotNull
    public static String getMessage(@NotNull final GetMessageInputs getMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(getMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                getMessageInputs.getMessageId(),
                getMessageInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        if (!StringUtils.isEmpty(getMessageInputs.getoDataQuery())) {
            httpClientInputs.setQueryParams(getQueryParams(getMessageInputs.getoDataQuery()));
        }

        return new HttpClientService().execute(httpClientInputs).get(RETURN_RESULT);
    }

    @NotNull
    public static String listMessages(@NotNull final ListMessagesInputs listMessagesInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = listMessagesInputs.getCommonInputs();
        httpClientInputs.setUrl(listMessagesUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                listMessagesInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs).get(RETURN_RESULT);
    }

    @NotNull
    private static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                            @NotNull final Office365CommonInputs commonInputs) {
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

        setConnectionParameters(httpClientInputs,
                commonInputs.getConnectTimeout(),
                commonInputs.getSocketTimeout(),
                commonInputs.getKeepAlive(),
                commonInputs.getConnectionsMaxPerRoute(),
                commonInputs.getConnectionsMaxTotal());
    }


    @NotNull
    private static String getMessageUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId,
                                        @NotNull final String folderId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        if (StringUtils.isEmpty(folderId)) {
            uriBuilder.setPath(getMessagePath(userPrincipalName, userId, messageId));
        } else
            uriBuilder.setPath(getMessagePath(userPrincipalName, userId, messageId, folderId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String listMessagesUrl(@NotNull final String userPrincipalName,
                                          @NotNull final String userId,
                                          @NotNull final String folderId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();

        if (StringUtils.isEmpty(folderId)) {
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId));
        } else
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId, folderId));

        return uriBuilder.build().toURL().toString();
    }
}
