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

package io.cloudslang.content.office365.actions.userManagement;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.office365.entities.CreateUserInputs;
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
import static io.cloudslang.content.office365.services.UserServiceImpl.updateUser;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Descriptions.Common.*;
import static io.cloudslang.content.office365.utils.Descriptions.CreateMessage.AUTH_TOKEN_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.CreateUser.*;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.FAILURE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetAuthorizationToken.SUCCESS_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.GetEmail.STATUS_CODE_DESC;
import static io.cloudslang.content.office365.utils.Descriptions.UpdateUser.*;
import static io.cloudslang.content.office365.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.CreateUser.*;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.AUTH_TOKEN;
import static io.cloudslang.content.office365.utils.Inputs.UpdateUser.*;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyUpdateUserInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateUser {
    @Action(name = "Update an existing user in Office 365",
            outputs = {
                    @Output(value = RETURN_RESULT, description = UPDATE_USER_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = UPDATE_USER_EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = USER_ID_TO_UPDATE, description = UPDATE_USER_ID_TO_UPDATE_DESC) String userIdToUpdate,
                                       @Param(value = USER_PRINCIPAL_NAME_TO_UPDATE, description = UPDATE_USER_PRINCIPAL_NAME_TO_UPDATE_DESC) String userPrincipalNameToUpdate,

                                       @Param(value = ACCOUNT_ENABLED, description = ACCOUNT_ENABLED_DESC) String accountEnabled,
                                       @Param(value = DISPLAY_NAME, required = true, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = ON_PREMISES_IMMUTABLE_ID, description = UPDATE_USER_ON_PREMISES_IMMUTABLE_ID_DESC) String onPremisesImmutableId,
                                       @Param(value = MAIL_NICKNAME, required = true, description = MAIL_NICKNAME_DESC) String mailNickname,
                                       @Param(value = USER_PRINCIPAL_NAME_TO_UPDATE_WITH, required = true, description = CREATE_USER_PRINCIPAL_NAME_TO_UPDATE_WITH_DESC) String userPrincipalNameToUpdateWith,
                                       @Param(value = FORCE_CHANGE_PASSWORD, description = FORCE_CHANGE_PASSWORD_DESC) String forceChangePassword,
                                       @Param(value = PASSWORD, required = true, description = UPDATE_USER_PASSWORD_DESC) String password,

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

        accountEnabled = defaultIfEmpty(accountEnabled, BOOLEAN_TRUE);
        displayName = defaultIfEmpty(displayName, EMPTY);
        onPremisesImmutableId = defaultIfEmpty(onPremisesImmutableId, EMPTY);
        mailNickname = defaultIfEmpty(mailNickname, EMPTY);
        userIdToUpdate = defaultIfEmpty(userIdToUpdate, EMPTY);
        userPrincipalNameToUpdate = defaultIfEmpty(userPrincipalNameToUpdate, EMPTY);
        userPrincipalNameToUpdateWith = defaultIfEmpty(userPrincipalNameToUpdateWith, EMPTY);
        forceChangePassword = defaultIfEmpty(forceChangePassword, BOOLEAN_TRUE);
        password = defaultIfEmpty(password, EMPTY);
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

        final List<String> exceptionMessages = verifyUpdateUserInputs(accountEnabled, userIdToUpdate,
                userPrincipalNameToUpdate, forceChangePassword, proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = updateUser(CreateUserInputs.builder()
                    .accountEnabled(accountEnabled)
                    .displayName(displayName)
                    .onPremisesImmutableId(onPremisesImmutableId)
                    .mailNickname(mailNickname)
                    .userPrincipalName(userPrincipalNameToUpdateWith)
                    .forceChangePassword(forceChangePassword)
                    .password(password)
                    .commonInputs(Office365CommonInputs.builder()
                            .authToken(authToken)
                            .userPrincipalName(userPrincipalNameToUpdate)
                            .userId(userIdToUpdate)
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
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);
            return getOperationResults(result, UPDATE_USER, returnMessage, EMPTY);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}

