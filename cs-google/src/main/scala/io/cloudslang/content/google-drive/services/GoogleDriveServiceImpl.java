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
package io.cloudslang.content.google-drive.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.google-drive.entities.*;
import io.cloudslang.content.google-drive.utils.PopulateMessageBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.google-drive.utils.Constants.*;
import static io.cloudslang.content.google-drive.utils.HttpUtils.*;

public class GoogleDriveServiceImpl {

    @NotNull
    public static Map<String, String> getFile(@NotNull final GetFileInputs getFileInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final GoogleDriveCommonInputs commonInputs = getFileInputs.getCommonInputs();
        httpClientInputs.setUrl(getFileUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getFileId(),
                getFileInputs.getAcknowledgeAbuse(),
                getFileInputs.getSupportsTeamDrives()));

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

//check ?
    @NotNull
    private static String getFileUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String fileId,
                                        @NotNull final String acknowledgeAbuse,
                                        @NotNull final String supportsTeamDrives) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        if (StringUtils.isEmpty(folderId)) {
            uriBuilder.setPath(getMessagePath(userPrincipalName, fileId, acknowledgeAbuse, supportsTeamDrives));
        } else
            uriBuilder.setPath(getMessagePath(userPrincipalName, userId, messageId, folderId));

        return uriBuilder.build().toURL().toString();
    }

   // de modificat
       @NotNull
    public static Map<String, String> createMessage(@NotNull final CreateMessageInputs createMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = createMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(createMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                createMessageInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setBody(PopulateMessageBody.populateMessageBody(commonInputs, createMessageInputs, DELIMITER));

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String createMessageUrl(@NotNull final String userPrincipalName,
                                           @NotNull final String userId,
                                           @NotNull final String folderId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        if (StringUtils.isEmpty(folderId)) {
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId));
        } else
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId, folderId));

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

//de modifict
    @NotNull
    public static Map<String, String> deleteFile(@NotNull final DeleteFileInputs deleteFileInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final GoogleDriveCommonInputs commonInputs = deleteFileInputs.getCommonInputs();
        httpClientInputs.setUrl(getFileUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                deleteFileInputs.getFileId(),
                deleteFileInputs.getSupportsTeamDrives()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }
}
