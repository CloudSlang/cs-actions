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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.office365.entities.*;
import io.cloudslang.content.office365.utils.PopulateMessageBody;
import io.cloudslang.content.office365.utils.PopulateMoveMessageBody;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.*;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.CONTENT_BYTES;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.CONTENT_SIZE;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.CONTENT_TYPE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class EmailServiceImpl {

    @NotNull
    public static Map<String, String> getMessage(@NotNull final GetMessageInputs getMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(getMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                getMessageInputs.getMessageId(),
                getMessageInputs.getFolderId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

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
    public static Map<String, String> sendMessage(@NotNull final SendMessageInputs postMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();

        final Office365CommonInputs commonInputs = postMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(sendMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                postMessageInputs.getMessageId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

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
    public static Map<String, String> listMessages(@NotNull final ListMessagesInputs listMessagesInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = listMessagesInputs.getCommonInputs();
        httpClientInputs.setUrl(listMessagesUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                listMessagesInputs.getFolderId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
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
    private static String getAttachmentsUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId,
                                        @NotNull final String parentFolderId,
                                        @NotNull final String attachmentId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
            uriBuilder.setPath(getAttachmentsPath(userPrincipalName, userId, messageId, parentFolderId, attachmentId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String moveMessageUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(moveMessagePath(userPrincipalName, userId, messageId));

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

    @NotNull
    public static Map<String, String> createMessage(@NotNull final CreateMessageInputs createMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = createMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(createMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                createMessageInputs.getFolderId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

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

    @NotNull
    public static Map<String, String> deleteMessage(@NotNull final DeleteMessageInputs deleteMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = deleteMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(getMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                deleteMessageInputs.getMessageId(),
                deleteMessageInputs.getFolderId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> moveMessage(@NotNull final MoveMessageInputs moveMessageInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = moveMessageInputs.getCommonInputs();
        httpClientInputs.setUrl(moveMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                moveMessageInputs.getMessageId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setBody(PopulateMoveMessageBody.populateMoveMessageBody(moveMessageInputs.getDestinationId()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getAttachments(@NotNull final GetAttachmentsInputs getAttachmentsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getAttachmentsInputs.getCommonInputs();
        final String parentFolderId = getParentFolderId(getAttachmentsInputs);
        httpClientInputs.setUrl(getAttachmentsUrl(commonInputs.getUserPrincipalName(),
                                                    commonInputs.getUserId(),
                                                    getAttachmentsInputs.getMessageId(),
                                                    parentFolderId,
                                                    getAttachmentsInputs.getAttachmentId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    private static String getParentFolderId(final GetAttachmentsInputs getAttachmentsInputs) throws Exception {
        final Map <String, String> getMessageResponse = getMessage(GetMessageInputs.builder()
                .messageId(getAttachmentsInputs.getMessageId())
                .commonInputs(Office365CommonInputs.builder()
                        .authToken(getAttachmentsInputs.getCommonInputs().getAuthToken())
                        .connectionsMaxPerRoute(getAttachmentsInputs.getCommonInputs().getConnectionsMaxPerRoute())
                        .connectionsMaxTotal(getAttachmentsInputs.getCommonInputs().getConnectionsMaxTotal())
                        .proxyHost(getAttachmentsInputs.getCommonInputs().getProxyHost())
                        .proxyPort(getAttachmentsInputs.getCommonInputs().getProxyPort())
                        .proxyUsername(getAttachmentsInputs.getCommonInputs().getProxyUsername())
                        .proxyPassword(getAttachmentsInputs.getCommonInputs().getProxyPassword())
                        .keepAlive(getAttachmentsInputs.getCommonInputs().getKeepAlive())
                        .socketTimeout(getAttachmentsInputs.getCommonInputs().getSocketTimeout())
                        .responseCharacterSet(getAttachmentsInputs.getCommonInputs().getResponseCharacterSet())
                        .connectTimeout(getAttachmentsInputs.getCommonInputs().getConnectTimeout())
                        .trustAllRoots(getAttachmentsInputs.getCommonInputs().getTrustAllRoots())
                        .userId(getAttachmentsInputs.getCommonInputs().getUserId())
                        .userPrincipalName(getAttachmentsInputs.getCommonInputs().getUserPrincipalName())
                        .x509HostnameVerifier(getAttachmentsInputs.getCommonInputs().getX509HostnameVerifier())
                        .trustKeystore(getAttachmentsInputs.getCommonInputs().getTrustKeystore())
                        .trustPassword(getAttachmentsInputs.getCommonInputs().getTrustPassword())
                        .build())
                .build());

        final String getMessageDocument = getMessageResponse.get(RETURN_RESULT);
        final Map<String, String> result = getOperationResults(getMessageResponse, getMessageDocument, getMessageDocument, getMessageDocument);
        if (Integer.parseInt(result.get(RETURN_CODE)) == 1)
            throw new Exception(result.get(EXCEPTION));
        else {
            final JsonParser parser = new JsonParser();
            final JsonObject responseJson = parser.parse(result.get(DOCUMENT)).getAsJsonObject();
            return responseJson.get(ID).getAsString();
        }
    }

    private static void downloadAttachment(String filePath, String contentBytes, String contentName) throws IOException {
        byte[] data = Base64.decodeBase64(contentBytes);
        final String finalPath = filePath + File.separator + contentName;
        try (OutputStream stream = new FileOutputStream(finalPath)) {
            stream.write(data);
        }
    }

    public static void addAditionalOutputs(Map<String, String> results, Map<String, String> result, String returnMessage, String filePath) throws IOException {
        final Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));

        if (statusCode >= 200 && statusCode < 300) {
            final JsonParser parser = new JsonParser();
            final JsonObject responseJson = parser.parse(returnMessage).getAsJsonObject();
            if (responseJson.has(NAME))
                results.put(CONTENT_NAME, responseJson.get(NAME).getAsString());
            else
                results.put(CONTENT_NAME, EMPTY);
            if (responseJson.has(CONTENT_TYPE))
                results.put(CONTENT_TYPE, responseJson.get(CONTENT_TYPE).getAsString());
            else
                results.put(CONTENT_TYPE, EMPTY);
            if (responseJson.has(CONTENT_BYTES))
                results.put(CONTENT_BYTES, responseJson.get(CONTENT_BYTES).getAsString());
            else
                results.put(CONTENT_BYTES, EMPTY);
            if (responseJson.has(SIZE))
                results.put(CONTENT_SIZE, responseJson.get(SIZE).getAsString());
            else
                results.put(CONTENT_SIZE, EMPTY);

            if (!isEmpty(filePath))
                downloadAttachment(filePath, responseJson.get(CONTENT_BYTES).getAsString(), responseJson.get(NAME).getAsString());
        }
    }

    @NotNull
    public static Map<String, String> ListAttachment(@NotNull final ListAttachmentsInputs getListAttachmentInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Office365CommonInputs commonInputs = getListAttachmentInputs.getCommonInputs();
        httpClientInputs.setUrl(ListAttachmentsUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId(), getListAttachmentInputs.getMessageId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setKeystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.setKeystorePassword(CHANGEIT);
        httpClientInputs.setContentType(APPLICATION_JSON);

        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String ListAttachmentsUrl(String userPrincipalName, String userId, String messageId) throws Exception {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = GET_USER_REQUEST_URL + userPrincipalName;
        else
            finalUrl = GET_USER_REQUEST_URL + userId;
        if (!StringUtils.isEmpty(messageId))
            finalUrl = finalUrl + MESSAGES_PATH + messageId;
        finalUrl = finalUrl + ATTACHMENTS;
        return finalUrl;
    }

    public static String retrieveAttachmentIdList(String returnMessage) {
        final List<JsonElement> attachmentIdList = new ArrayList();
        final JsonParser parser = new JsonParser();
        final JsonObject responseJson = parser.parse(returnMessage).getAsJsonObject();
        if (responseJson.has(VALUE)) {
            final JsonArray valueList = responseJson.getAsJsonArray(VALUE);
            for (int i = 0; i < valueList.size(); i++) {
                final JsonObject valueObject = (JsonObject) valueList.get(i);
                if (valueObject.has(ID)) {
                    attachmentIdList.add(valueObject.get(ID));
                }
            }
        }
        return StringUtils.join(attachmentIdList, COMMA);
    }
}
