/*
 * Copyright 2024-2025 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.httpclient.entities.HttpClientInputs.HttpClientInputsBuilder;
import io.cloudslang.content.office365.entities.*;
import io.cloudslang.content.office365.utils.PopulateMessageBody;
import io.cloudslang.content.office365.utils.PopulateMoveMessageBody;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.office365.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.CONTENT_TYPE;
import static io.cloudslang.content.office365.utils.Outputs.GetAttachmentsOutputs.*;
import static io.cloudslang.content.office365.utils.PopulateAttachmentBody.populateAddAttachmentBody;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class EmailServiceImpl {

    @NotNull
    public static Map<String, String> getMessage(@NotNull final GetMessageInputs getMessageInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = getMessageInputs.getCommonInputs();
        httpClientInputs.url(getMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                getMessageInputs.getMessageId(),
                getMessageInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));

        if (!isEmpty(getMessageInputs.getSelectQuery()) || !isEmpty(getMessageInputs.getoDataQuery())) {
            httpClientInputs.queryParams(getQueryParams(getMessageInputs.getSelectQuery(), getMessageInputs.getoDataQuery()));
        }
        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> sendMessage(@NotNull final SendMessageInputs postMessageInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();

        final Office365CommonInputs commonInputs = postMessageInputs.getCommonInputs();
        httpClientInputs.url(sendMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                postMessageInputs.getMessageId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.body(postMessageInputs.getBody());
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()) + HEADERS_DELIMITER + CONTENT_LENGTH);

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> listMessages(@NotNull final ListMessagesInputs listMessagesInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = listMessagesInputs.getCommonInputs();
        httpClientInputs.url(listMessagesUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                listMessagesInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.queryParams(getQueryParams(listMessagesInputs.getTopQuery(), listMessagesInputs.getSelectQuery(), listMessagesInputs.getoDataQuery()));

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    private static String getMessageUrl(@NotNull final String userPrincipalName,
                                        @NotNull final String userId,
                                        @NotNull final String messageId,
                                        @NotNull final String folderId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        if (isEmpty(folderId)) {
            uriBuilder.setPath(getMessagePath(userPrincipalName, userId, messageId));
        } else
            uriBuilder.setPath(getMessagePath(userPrincipalName, userId, messageId, folderId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getAttachmentsUrl(@NotNull final String userPrincipalName,
                                            @NotNull final String userId,
                                            @NotNull final String messageId,
                                            @NotNull final String attachmentId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getAttachmentsPath(userPrincipalName, userId, messageId, attachmentId));

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
    private static String addAttachmentUrl(
            @NotNull final String userPrincipalName,
            @NotNull final String userId,
            @NotNull final String messageId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder().setPath(addAttachmentPath(userPrincipalName, userId, messageId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String listMessagesUrl(@NotNull final String userPrincipalName,
                                          @NotNull final String userId,
                                          @NotNull final String folderId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();

        if (isEmpty(folderId)) {
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId));
        } else
            uriBuilder.setPath(getMessagesPath(userPrincipalName, userId, folderId));

        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static Map<String, String> createMessage(@NotNull final CreateMessageInputs createMessageInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = createMessageInputs.getCommonInputs();
        httpClientInputs.url(createMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                createMessageInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.contentType(APPLICATION_JSON);
        httpClientInputs.body(PopulateMessageBody.populateMessageBody(commonInputs, createMessageInputs, DELIMITER));

        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    private static String createMessageUrl(@NotNull final String userPrincipalName,
                                           @NotNull final String userId,
                                           @NotNull final String folderId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        if (isEmpty(folderId)) {
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
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = deleteMessageInputs.getCommonInputs();
        httpClientInputs.url(getMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                deleteMessageInputs.getMessageId(),
                deleteMessageInputs.getFolderId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(DELETE);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> moveMessage(@NotNull final MoveMessageInputs moveMessageInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = moveMessageInputs.getCommonInputs();
        httpClientInputs.url(moveMessageUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                moveMessageInputs.getMessageId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.contentType(APPLICATION_JSON);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.body(PopulateMoveMessageBody.populateMoveMessageBody(moveMessageInputs.getDestinationId()));

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> addAttachment(@NotNull final AddAttachmentInputs addAttachmentInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = addAttachmentInputs.getCommonInputs();
        httpClientInputs.url(addAttachmentUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId(),
                addAttachmentInputs.getMessageId()));

        setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.contentType(APPLICATION_JSON);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.body(populateAddAttachmentBody(
                addAttachmentInputs.getFilePath(),
                addAttachmentInputs.getContentName(),
                addAttachmentInputs.getContentBytes()));

        return HttpCommons.execute(httpClientInputs);
    }

    public static String addBigAttachment(@NotNull final AddAttachmentInputs addAttachmentInputs) throws Exception {
        return UploadBigFileImpl.createUploadSession(addAttachmentInputs);
    }

    @NotNull
    public static Map<String, String> getAttachments(@NotNull final GetAttachmentsInputs getAttachmentsInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = getAttachmentsInputs.getCommonInputs();
        httpClientInputs.url(getAttachmentsUrl(commonInputs.getUserPrincipalName(),
                commonInputs.getUserId(),
                getAttachmentsInputs.getMessageId(),
                getAttachmentsInputs.getAttachmentId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));

        return HttpCommons.execute(httpClientInputs);
    }

    private static void downloadAttachment(String filePath, String contentBytes, String contentName) throws IOException {
        byte[] data = Base64.decodeBase64(contentBytes);
        final String finalPath = filePath + File.separator + contentName;
        try (OutputStream stream = Files.newOutputStream(Paths.get(finalPath))) {
            stream.write(data);
        }
    }

    public static String addAditionalOutputs(Map<String, String> results, Map<String, String> result, String returnMessage, String filePath) {
        final int statusCode = Integer.parseInt(result.get(STATUS_CODE));

        try {
            if (statusCode >= 200 && statusCode < 300) {
                final JsonParser parser = new JsonParser();
                final JsonObject responseJson = parser.parse(returnMessage).getAsJsonObject();
                addOutput(results, responseJson, NAME, CONTENT_NAME);
                addOutput(results, responseJson, CONTENT_TYPE, CONTENT_TYPE);
                addOutput(results, responseJson, CONTENT_BYTES, CONTENT_BYTES);
                addOutput(results, responseJson, SIZE, CONTENT_SIZE);

                try {
                    if (!isEmpty(filePath))
                        downloadAttachment(filePath, responseJson.get(CONTENT_BYTES).getAsString(), responseJson.get(NAME).getAsString());
                } catch (NullPointerException e) {
                    throw new RuntimeException("Unsupported file format.");
                }
            }
        } catch (NullPointerException | IOException e) {
            return String.valueOf(e);
        }
        return null;
    }

    public static void addOutput(Map<String, String> results, JsonObject responseJson, String key, String keyToAdd) {
        if (responseJson.has(key))
            results.put(keyToAdd, responseJson.get(key).getAsString());
        else
            results.put(keyToAdd, EMPTY);
    }

    @NotNull
    public static Map<String, String> listAttachment(@NotNull final ListAttachmentsInputs getListAttachmentInputs) throws Exception {
        final HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final Office365CommonInputs commonInputs = getListAttachmentInputs.getCommonInputs();
        httpClientInputs.url(listAttachmentsUrl(commonInputs.getUserPrincipalName(), commonInputs.getUserId(), getListAttachmentInputs.getMessageId()));

        HttpCommons.setCommonHttpInputs(httpClientInputs, commonInputs);

        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.keystore(DEFAULT_JAVA_KEYSTORE);
        httpClientInputs.keystorePassword(CHANGEIT);
        httpClientInputs.contentType(APPLICATION_JSON);

        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));

        return HttpCommons.execute(httpClientInputs);
    }

    @NotNull
    private static String listAttachmentsUrl(String userPrincipalName, String userId, String messageId) {
        String finalUrl;
        if (!StringUtils.isEmpty(userPrincipalName))
            finalUrl = GET_USER_REQUEST_URL + userPrincipalName;
        else
            finalUrl = GET_USER_REQUEST_URL + userId;
        if (!StringUtils.isEmpty(messageId))
            finalUrl = finalUrl + MESSAGES_PATH + PATH_SEPARATOR + messageId;
        finalUrl = finalUrl + ATTACHMENTS;
        return finalUrl;
    }

    public static String retrieveAttachmentIdList(String returnMessage) {
        final List<JsonElement> attachmentIdList = new ArrayList<>();
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
