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
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.office365.entities.SendMessageInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;

public class EmailServiceImpl {

    @NotNull
    public static Map<String, String> getMessage(@NotNull final GetMessageInputs getMessageInputs) throws Exception {
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
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> postSendMessage(@NotNull final SendMessageInputs postMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        final Office365CommonInputs commonInputs = postMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(sendMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                postMessageInputs.getMessageId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setBody(postMessageInputs.getBody());
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()) + HEADERS_DELIMITER + CONTENT_LENGTH);

        return new HttpClientService().execute(httpClientInputs);
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
    private static String sendMessageUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(sendMessagePath(userPrincipalName, userId, messageId));
        return uriBuilder.build().toURL().toString();
    }
}
