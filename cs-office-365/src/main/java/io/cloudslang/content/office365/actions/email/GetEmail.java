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

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.AuthorizationTokenInputs;
import io.cloudslang.content.office365.entities.GetMessageInputs;
import io.cloudslang.content.office365.entities.ListMessagesInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import io.cloudslang.content.office365.services.AuthorizationTokenImpl;
import io.cloudslang.content.utils.NumberUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECTIONS_MAX_TOTAL;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.KEEP_ALIVE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.RESPONSE_CHARACTER_SET;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.SOCKET_TIMEOUT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_KEYSTORE;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.office365.services.EmailServiceImpl.getMessage;
import static io.cloudslang.content.office365.services.EmailServiceImpl.listMessages;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONNECT_TIMEOUT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONN_MAX_ROUTE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.CONN_MAX_TOTAL_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.KEEP_ALIVE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_HOST_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_PASSWORD_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_PORT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.PROXY_USERNAME_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.SOCKET_TIMEOUT_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_ALL_ROOTS_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_KEYSTORE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.TRUST_PASSWORD_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.Common.X509_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.CLIENT_ID_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.CLIENT_SECRET_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetEmail.*;
import static io.cloudslang.content.office365.utils.Descriptions.ListMessages.MESSAGE_ID_LIST_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMail.MESSAGE_ID_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMail.TENANT_NAME_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMessage.EXCEPTION_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.SendMessage.RETURN_RESULT_DESC;
import static io.cloudslang.content.office365.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.CLIENT_ID;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.CLIENT_SECRET;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.FOLDER_ID;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.MESSAGE_ID;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.MESSAGE_ID_LIST;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.O_DATA_QUERY;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.SELECT_QUERY;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.TOP_QUERY_INPUT;
import static io.cloudslang.content.office365.utils.Inputs.GetEmailInputs.EMAIL_ADDRESS;
import static io.cloudslang.content.office365.utils.Inputs.SendMailInputs.TENANT_NAME;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class GetEmail {

    @Action(name = "Get email",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = MESSAGE_ID_LIST, description = MESSAGE_ID_LIST_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = CLIENT_ID, required = true, description = CLIENT_ID_DESC) String clientId,
                                       @Param(value = EMAIL_ADDRESS, description = EMAIL_ADDRESS_DESC) String emailAddress,

                                       @Param(value = CLIENT_SECRET, encrypted = true, description = CLIENT_SECRET_DESC) String clientSecret,
                                       @Param(value = TENANT_NAME, required = true, description = TENANT_NAME_DESC) String tenant,
                                       @Param(value = MESSAGE_ID, description = MESSAGE_ID_DESC) String messageId,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,

                                       @Param(value = FOLDER_ID, description = FOLDER_ID_DESC) String folderId,
                                       @Param(value = TOP_QUERY_INPUT, description = TOP_QUERY_DESC) String topQuery,
                                       @Param(value = SELECT_QUERY, description = SELECT_QUERY_DESC) String selectQuery,
                                       @Param(value = O_DATA_QUERY, description = O_DATA_QUERY_DESC) String oDataQuery,

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
        clientSecret = defaultIfEmpty(clientSecret, EMPTY);
        final String loginAuthority = LOGIN_AUTHORITY_PREFIX + tenant + LOGIN_AUTHORITY_SUFFIX;
        folderId = defaultIfEmpty(folderId, EMPTY);

        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);

        messageId = defaultIfEmpty(messageId, EMPTY);
        topQuery = defaultIfEmpty(topQuery, TOP_QUERY_CONST);
        selectQuery = defaultIfEmpty(selectQuery, EMPTY);
        oDataQuery = defaultIfEmpty(oDataQuery, EMPTY);

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

        try {
            final String authToken = AuthorizationTokenImpl.getToken(AuthorizationTokenInputs.builder()
                    .loginType(DEFAULT_LOGIN_TYPE)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authority(loginAuthority)
                    .resource(DEFAULT_RESOURCE)
                    .proxyHost(proxyHost)
                    .proxyPort(NumberUtilities.toInteger(proxyPort))
                    .proxyUsername(proxyUsername)
                    .proxyPassword(proxyPassword)
                    .build()).getAccessToken();

            if (messageId.isEmpty()) {
                final Map<String, String> ListMessagesResult = listMessages(ListMessagesInputs.builder()
                        .folderId(folderId)
                        .topQuery(topQuery)
                        .selectQuery(selectQuery)
                        .oDataQuery(oDataQuery)
                        .commonInputs(Office365CommonInputs.builder()
                                .authToken(authToken)
                                .proxyHost(proxyHost)
                                .proxyPort(proxyPort)
                                .proxyUsername(proxyUsername)
                                .proxyPassword(proxyPassword)
                                .connectionsMaxTotal(connectionsMaxTotal)
                                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                                .keepAlive(keepAlive)
                                .socketTimeout(socketTimeout)
                                .responseCharacterSet(responseCharacterSet)
                                .connectTimeout(connectTimeout)
                                .trustAllRoots(trustAllRoots)
                                .userPrincipalName(emailAddress)
                                .x509HostnameVerifier(x509HostnameVerifier)
                                .trustKeystore(trustKeystore)
                                .trustPassword(trustPassword)
                                .build())
                        .build());

                final String returnMessage = ListMessagesResult.get(RETURN_RESULT);
                final Map<String, String> successResultMap = getOperationResults(ListMessagesResult, returnMessage, returnMessage, returnMessage);
                final int statusCode = Integer.parseInt(ListMessagesResult.get(STATUS_CODE));

                if (statusCode >= 200 && statusCode < 300) {
                    final List<String> messageIdList = JsonPath.read(returnMessage, MESSAGE_ID_LIST_JSON_PATH);

                    if (!messageIdList.isEmpty()) {
                        final String messageIdListAsString = join(messageIdList.toArray(), DELIMITER);
                        successResultMap.put(MESSAGE_ID_LIST, messageIdListAsString);
                    } else {
                        successResultMap.put(MESSAGE_ID_LIST, EMPTY);
                    }
                }
                return successResultMap;

            } else {

                final Map<String, String> result = getMessage(GetMessageInputs.builder()
                        .messageId(messageId)
                        .folderId(folderId)
                        .selectQuery(selectQuery)
                        .oDataQuery(oDataQuery)
                        .commonInputs(Office365CommonInputs.builder()
                                .authToken(authToken)
                                .proxyHost(proxyHost)
                                .proxyPort(proxyPort)
                                .proxyUsername(proxyUsername)
                                .proxyPassword(proxyPassword)
                                .connectionsMaxTotal(connectionsMaxTotal)
                                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                                .keepAlive(keepAlive)
                                .socketTimeout(socketTimeout)
                                .responseCharacterSet(responseCharacterSet)
                                .connectTimeout(connectTimeout)
                                .trustAllRoots(trustAllRoots)
                                .userPrincipalName(emailAddress)
                                .x509HostnameVerifier(x509HostnameVerifier)
                                .trustKeystore(trustKeystore)
                                .trustPassword(trustPassword)
                                .build())
                        .build());
                final String returnMessage = result.get(RETURN_RESULT);
                return getOperationResults(result, returnMessage, returnMessage, returnMessage);
            }

        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}
