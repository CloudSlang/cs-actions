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




package io.cloudslang.content.office365.actions.email;

import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.AddAttachmentInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.utils.StringUtilities;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.office365.services.EmailServiceImpl.*;
import static io.cloudslang.content.office365.utils.Constants.FILE_PATH;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Descriptions.AddAttachment.FAILURE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.AddAttachment.SUCCESS_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.AddAttachment.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetEmail.*;
import static io.cloudslang.content.office365.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.office365.utils.Inputs.AddAttachment.CONTENT_BYTES;
import static io.cloudslang.content.office365.utils.Inputs.AddAttachment.CONTENT_NAME;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.*;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyAddAttachmentInputs;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class AddAttachment {
    @Action(name = "Add an attachment to a message in Office 365",
            outputs = {
                    @Output(value = RETURN_RESULT, description = ADD_ATTACHMENT_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = DOCUMENT, description = DOCUMENT_DESC),
                    @Output(value = EXCEPTION, description = ADD_ATTACHMENT_EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = ATTACHMENT_ID, description = ATTACHMENT_ID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,

                                       @Param(value = USER_PRINCIPAL_NAME, description = USER_PRINCIPAL_NAME_DESC) String userPrincipalName,
                                       @Param(value = USER_ID, description = USER_ID_DESC) String userId,
                                       @Param(value = MESSAGE_ID, required = true, description = MESSAGE_ID_DESC) String messageId,
                                       @Param(value = FILE_PATH, description = FILE_PATH_DESC) String filePath,
                                       @Param(value = CONTENT_NAME, description = CONTENT_NAME_DESC) String contentName,
                                       @Param(value = CONTENT_BYTES, description = CONTENT_BYTES_DESC) String contentBytes,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,

                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {
        userPrincipalName = defaultIfEmpty(userPrincipalName, EMPTY);
        userId = defaultIfEmpty(userId, EMPTY);
        messageId = defaultIfEmpty(messageId, EMPTY);
        filePath = defaultIfEmpty(filePath, EMPTY);
        contentName = defaultIfEmpty(contentName, EMPTY);
        contentBytes = defaultIfEmpty(contentBytes, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, ZERO);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_FALSE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final List<String> exceptionMessages = verifyAddAttachmentInputs(filePath,
                contentName, contentBytes, userPrincipalName, userId,
                messageId, proxyPort, trustAllRoots, connectTimeout, socketTimeout, keepAlive,
                connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            if (Files.size(Paths.get(filePath)) < ATTACHMENT_SIZE_THRESHOLD) {

                final Map<String, String> result = addAttachment(AddAttachmentInputs.builder()
                        .messageId(messageId)
                        .filePath(filePath)
                        .contentName(contentName)
                        .contentBytes(contentBytes)
                        .commonInputs(Office365CommonInputs.builder()
                                .authToken(authToken)
                                .userPrincipalName(userPrincipalName)
                                .userId(userId)
                                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                                .connectionsMaxTotal(connectionsMaxTotal)
                                .proxyHost(proxyHost)
                                .proxyPort(proxyPort)
                                .proxyUsername(proxyUsername)
                                .proxyPassword(proxyPassword)
                                .keepAlive(keepAlive)
                                .responseCharacterSet(responseCharacterSet)
                                .connectTimeout(connectTimeout)
                                .trustAllRoots(trustAllRoots)
                                .x509HostnameVerifier(x509HostnameVerifier)
                                .trustKeystore(trustKeystore)
                                .trustPassword(trustPassword)
                                .build())
                        .build());

                final String returnMessage = result.get(RETURN_RESULT);
                final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
                final Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));

                if (statusCode >= 200 && statusCode < 300) {
                    addOutput(results, new JsonParser().parse(returnMessage).getAsJsonObject(), ID, ATTACHMENT_ID);
                }
                return results;
            } else {
                String response = addBigAttachment(AddAttachmentInputs.builder()
                        .messageId(messageId)
                        .filePath(filePath)
                        .contentName(contentName)
                        .contentBytes(contentBytes)
                        .commonInputs(Office365CommonInputs.builder()
                                .authToken(authToken)
                                .userPrincipalName(userPrincipalName)
                                .userId(userId)
                                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                                .connectionsMaxTotal(connectionsMaxTotal)
                                .proxyHost(proxyHost)
                                .proxyPort(proxyPort)
                                .proxyUsername(proxyUsername)
                                .proxyPassword(proxyPassword)
                                .keepAlive(keepAlive)
                                .responseCharacterSet(responseCharacterSet)
                                .connectTimeout(connectTimeout)
                                .trustAllRoots(trustAllRoots)
                                .x509HostnameVerifier(x509HostnameVerifier)
                                .trustKeystore(trustKeystore)
                                .trustPassword(trustPassword)
                                .build())
                        .build());

                final Map<String, String> results = new HashMap<>();

                String attachmentId =  response.split(ATTACHMENTS_SPLIT)[1];
                results.put(ATTACHMENT_ID, attachmentId.substring(2, attachmentId.length() - 2));
                results.put(STATUS_CODE, STATUS_CODE_201);
                results.put(RETURN_RESULT, response);
                results.put(DOCUMENT, response);
                results.put(RETURN_CODE, ZERO);
                return results;
            }
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
