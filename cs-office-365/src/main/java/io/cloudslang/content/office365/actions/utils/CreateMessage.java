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
package io.cloudslang.content.office365.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.CreateMessageInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.utils.StringUtilities;
import java.util.List;
import java.util.Map;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.office365.services.EmailServiceImpl.createMessage;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.*;
import static io.cloudslang.content.office365.utils.Descriptions.CreateMessage.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.FAILURE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.SUCCESS_DESC;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.*;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.BODY;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.*;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyCreateMessageInputs;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateMessage {
    @Action(name = "Create a draft of a new message for Office 365",
            outputs = {
                    @Output(value = RETURN_RESULT, description = CREATE_MESSAGE_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = DOCUMENT, description = DOCUMENT_DESC),
                    @Output(value = EXCEPTION, description = CREATE_MESSAGE_EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = USER_PRINCIPAL_NAME, description = USER_PRINCIPAL_NAME_DESC) String userPrincipalName,
                                       @Param(value = USER_ID, description = USER_ID_DESC) String userId,
                                       @Param(value = FOLDER_ID, description = FOLDER_ID_DESC) String folderId,
                                       @Param(value = BCC_RECIPIENTS, description = BCCC_RECIPIENTS_DESC) String bccRecipients,
                                       @Param(value = CATEGORIES, description = CATEGORIES_DESC) String categories,
                                       @Param(value = CC_RECIPIENTS, description = CC_RECIPIENTS_DESC) String ccRecipients,
                                       @Param(value = FROM, required = true, description = FROM_DESC) String from,
                                       @Param(value = IMPORTANCE, description = IMPORTANCE_DESC) String importance,
                                       @Param(value = INFERENCE_CLASSIFICATION, description = INFERENCE_CLASSIFICATION_DESC) String inferenceClassification,
                                       @Param(value = INTERNET_MESSAGE_ID, description = INTERNET_MESSAGE_ID_DESC) String internetMessageId,
                                       @Param(value = IS_READ, description = IS_READ_DESC) String isRead,
                                       @Param(value = REPLY_TO, description = REPLY_TO_DESC) String replyTo,
                                       @Param(value = SENDER, required = true, description = SENDER_DESC) String sender,
                                       @Param(value = TO_RECIPIENTS, required = true, description = TO_RECIPIENTS_DESC) String toRecipients,
                                       @Param(value = BODY, description = BODY_DESC) String body,
                                       @Param(value = IS_DELIVERY_RECEIPT_REQUESTED, description = IS_DELIVERY_RECEIPT_REQUESTED_DESC) String isDeliveryReceiptRequested,
                                       @Param(value = IS_READ_RECEIPT_REQUESTED, description = IS_READ_RECEIPT_REQUESTED_DESC) String isReadReceiptRequested,
                                       @Param(value = SUBJECT, description = SUBJECT_DESC) String subject,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = CONN_MAX_TOTAL_DESC) String responseCharacterSet) {
        userPrincipalName = defaultIfEmpty(userPrincipalName, EMPTY);
        userId = defaultIfEmpty(userId, EMPTY);
        folderId = defaultIfEmpty(folderId, EMPTY);
        bccRecipients = defaultIfEmpty(bccRecipients, EMPTY);
        categories = defaultIfEmpty(categories, EMPTY);
        ccRecipients = defaultIfEmpty(ccRecipients, EMPTY);
        from = defaultIfEmpty(from, EMPTY);
        importance = defaultIfEmpty(importance, EMPTY);
        inferenceClassification = defaultIfEmpty(inferenceClassification, EMPTY);
        internetMessageId = defaultIfEmpty(internetMessageId, EMPTY);
        isRead = defaultIfEmpty(isRead, BOOLEAN_TRUE);
        replyTo = defaultIfEmpty(replyTo, EMPTY);
        sender = defaultIfEmpty(sender, EMPTY);
        toRecipients = defaultIfEmpty(toRecipients, EMPTY);
        body = defaultIfEmpty(body, EMPTY);
        isDeliveryReceiptRequested = defaultIfEmpty(isDeliveryReceiptRequested, BOOLEAN_FALSE);
        isReadReceiptRequested = defaultIfEmpty(isReadReceiptRequested, BOOLEAN_FALSE);
        subject = defaultIfEmpty(subject, EMPTY);
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
        importance = defaultIfEmpty(importance, DEFAULT_IMPORTANCE);
        inferenceClassification = defaultIfEmpty(inferenceClassification, DEFAULT_INFERENCE_CLASSIFICATION);

        final List<String> exceptionMessages = verifyCreateMessageInputs(sender, proxyPort, trustAllRoots, userPrincipalName, userId, connectTimeout,
                toRecipients, isRead, isDeliveryReceiptRequested, isReadReceiptRequested, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = getSuccessResultsMap(createMessage(CreateMessageInputs.builder()
                    .folderId(folderId)
                    .bccRecipients(bccRecipients)
                    .categories(categories)
                    .ccRecipients(ccRecipients)
                    .from(from)
                    .importance(importance)
                    .inferenceClassification(inferenceClassification)
                    .internetMessageId(internetMessageId)
                    .isRead(isRead)
                    .replyTo(replyTo)
                    .sender(sender)
                    .toRecipients(toRecipients)
                    .body(body)
                    .isDeliveryReceiptRequested(isDeliveryReceiptRequested)
                    .isReadReceiptRequested(isReadReceiptRequested)
                    .subject(subject)
                    .commonInputs(Office365CommonInputs.builder()
                            .authToken(authToken)
                            .connectionsMaxPerRoute(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .connectionsMaxPerRoute(connectionsMaxPerRoute)
                            .keepAlive(keepAlive)
                            .responseCharacterSet(responseCharacterSet)
                            .connectTimeout(connectTimeout)
                            .trustAllRoots(trustAllRoots)
                            .userId(userId)
                            .userPrincipalName(userPrincipalName)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build()));
            result.put(DOCUMENT, result.get(RETURN_RESULT));
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
