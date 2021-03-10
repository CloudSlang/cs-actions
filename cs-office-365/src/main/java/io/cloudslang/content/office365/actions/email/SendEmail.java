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

package io.cloudslang.content.office365.actions.email;

import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.*;
import io.cloudslang.content.office365.services.AuthorizationTokenImpl;
import io.cloudslang.content.office365.utils.Constants;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.RESPONSE_CHARACTER_SET;
import static io.cloudslang.content.office365.services.EmailServiceImpl.*;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Descriptions.AddAttachment.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONN_MAX_TOTAL_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.CreateMessage.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetEmail.*;
import static io.cloudslang.content.office365.utils.Descriptions.SendMail.MESSAGE_ID_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMail.TENANT_NAME_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMessage.RETURN_RESULT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMessage.EXCEPTION_DESC;
import static io.cloudslang.content.office365.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.office365.utils.Inputs.AddAttachment.ATTACHMENT_ID;
import static io.cloudslang.content.office365.utils.Inputs.AddAttachment.FILE_PATH;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.*;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.BODY;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.*;
import static io.cloudslang.content.office365.utils.Inputs.SendMailInputs.FROM_ADDRESS;
import static io.cloudslang.content.office365.utils.Inputs.SendMailInputs.TENANT_NAME;
import static io.cloudslang.content.office365.utils.InputsValidation.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class SendEmail {

    @Action(name = "Send email",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = MESSAGE_ID, description = MESSAGE_ID_DESC),
                    @Output(value = ATTACHMENT_ID, description = ATTACHMENT_ID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = CLIENT_ID, required = true, description = CLIENT_ID_DESC) String clientId,
                                       @Param(value = CLIENT_SECRET, encrypted = true, description = CLIENT_SECRET_DESC) String clientSecret,
                                       @Param(value = TENANT_NAME, required = true, description = TENANT_NAME_DESC) String tenant,
                                       @Param(value = LOGIN_TYPE, description = LOGIN_TYPE_DESC) String loginType,
                                       @Param(value = USERNAME, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = CC_RECIPIENTS, description = CC_RECIPIENTS_DESC) String ccRecipients,
                                       @Param(value = FROM_ADDRESS, required = true, description = FROM_DESC) String fromAddress,
                                       @Param(value = TO_RECIPIENTS, required = true, description = TO_RECIPIENTS_DESC) String toRecipients,
                                       @Param(value = BODY, description = BODY_DESC) String body,
                                       @Param(value = SUBJECT, description = SUBJECT_DESC) String subject,

                                       @Param(value = FILE_PATH, description = FILE_PATH_DESC) String filePath,

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

        clientId = defaultIfEmpty(clientId, EMPTY);
        final String loginAuthority = LOGIN_AUTHORITY_PREFIX + tenant + LOGIN_AUTHORITY_SUFFIX;
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        loginType = defaultIfEmpty(loginType, DEFAULT_LOGIN_TYPE);

        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);

        ccRecipients = defaultIfEmpty(ccRecipients, EMPTY);
        fromAddress = defaultIfEmpty(fromAddress, EMPTY);
        toRecipients = defaultIfEmpty(toRecipients, EMPTY);
        body = defaultIfEmpty(body, EMPTY);
        subject = defaultIfEmpty(subject, EMPTY);
        filePath = defaultIfEmpty(filePath, EMPTY);

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

        List<String> exceptionMessages = verifyAuthorizationInputs(loginType, clientId, clientSecret, username, password, proxyPort);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        exceptionMessages = verifySendEmailInputs(clientId, proxyPort, fromAddress, EMPTY,
                trustAllRoots, connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final String authToken = AuthorizationTokenImpl.getToken(AuthorizationTokenInputs.builder()
                    .loginType(loginType)
                    .username(username)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authority(loginAuthority)
                    .resource(DEFAULT_RESOURCE)
                    .proxyHost(proxyHost)
                    .proxyPort(NumberUtilities.toInteger(proxyPort))
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .build()).getAccessToken();

            final String createdMessage = createMessage(CreateMessageInputs.builder()
                    .ccRecipients(ccRecipients)
                    .from(fromAddress)
                    .importance(SEND_MAIL_DEFAULT_IMPORTANCE)
                    .inferenceClassification(SEND_MAIL_DEFAULT_INFERENCE_CLASSIFICATION)
                    .isRead(BOOLEAN_FALSE)
                    .sender(fromAddress)
                    .toRecipients(toRecipients)
                    .body(body)
                    .isDeliveryReceiptRequested(BOOLEAN_FALSE)
                    .isReadReceiptRequested(BOOLEAN_FALSE)
                    .subject(subject)
                    .commonInputs(Office365CommonInputs.builder()
                            .authToken(authToken)
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
                            .userPrincipalName(fromAddress)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build()).get(RETURN_RESULT);

            final StringBuilder messageId = new StringBuilder();
            try {
                messageId.append(new JsonParser().parse(createdMessage).getAsJsonObject().get(ID).getAsString());
            } catch (Exception e) {
                throw new RuntimeException(new JsonParser().parse(createdMessage).getAsJsonObject().get(Constants.ERROR).getAsJsonObject().get(Constants.MESSAGE).getAsString());
            }

            List<String> attachmentIDs = new ArrayList<>();
            List<String> attachmentErrors = new ArrayList<>();
            if (!isEmpty(filePath)) {
                String[] pathArray = filePath.split(COMMA);
                for (String path : pathArray) {
                    Map<String, String> attachmentResult = addAttachment(AddAttachmentInputs.builder()
                            .messageId(messageId.toString())
                            .filePath(path)
                            .commonInputs(Office365CommonInputs.builder()
                                    .authToken(authToken)
                                    .userPrincipalName(fromAddress)
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
                    if (Integer.parseInt(attachmentResult.get(STATUS_CODE)) >= 200 && Integer.parseInt(attachmentResult.get(STATUS_CODE)) < 300) {
                        attachmentIDs.add(new JsonParser().parse(attachmentResult.get(RETURN_RESULT)).getAsJsonObject().get(ID).getAsString());
                    } else {
                        attachmentErrors.add(path);
                    }
                }

                if (!attachmentErrors.isEmpty()) {
                    return getFailureResultsMap(String.format(SEND_EMAIL_ADD_ATTACHMENT, attachmentErrors.toString().substring(1, attachmentErrors.toString().length() - 1).replaceAll("\\s+", "")));
                }
            }

            final Map<String, String> sendMailResult = sendMessage(SendMessageInputs.builder()
                    .messageId(messageId.toString())
                    .body(EMPTY)
                    .commonInputs(Office365CommonInputs.builder()
                            .authToken(authToken)
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
                            .userPrincipalName(fromAddress)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build());

            final String returnMessage = sendMailResult.get(RETURN_RESULT);
            final Map<String, String> successResultMap = getOperationResults(sendMailResult, SEND_EMAIL, returnMessage, EMPTY);
            successResultMap.put(MESSAGE_ID, messageId.toString());
            if (!isEmpty(filePath))
                successResultMap.put(ATTACHMENT_ID, attachmentIDs.toString().substring(1, attachmentIDs.toString().length() - 1).replaceAll("\\s+", ""));
            return successResultMap;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
