/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.microsoftAD.actions.userManagement;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.CommonUserInputs;
import io.cloudslang.content.microsoftAD.entities.UpdateUserInputs;
import io.cloudslang.content.microsoftAD.utils.Descriptions;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.microsoftAD.services.UpdateUserService.updateUser;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.CreateUser.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.GetAuthorizationToken.AUTH_TOKEN_DESC;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.UpdateUser.*;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.parseApiExceptionMessage;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.ACCOUNT_ENABLED;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.BODY;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.USER_ID;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.UpdateUserInputs.UPDATED_USER_PRINCIPAL_NAME;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyUpdateUserInputs;
import static io.cloudslang.content.microsoftAD.utils.Outputs.OutputNames.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateUser {

    @Action(name = UPDATE_USER_NAME,
            description = UPDATE_USER_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = UPDATE_USER_RETURN_RESULT),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = UPDATE_USER_RETURN_RESULT),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = UPDATE_USER_FAILURE_RETURN_RESULT)
            })

    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = USER_PRINCIPAL_NAME, description = Descriptions.Common.USER_PRINCIPAL_NAME_DESC) String userPrincipalName,
                                       @Param(value = USER_ID, description = Descriptions.Common.USER_ID_DESC) String userId,
                                       @Param(value = ACCOUNT_ENABLED, description = UPDATED_ACCOUNT_ENABLED) String accountEnabled,
                                       @Param(value = DISPLAY_NAME, description = UPDATED_DISPLAY_NAME) String displayName,
                                       @Param(value = ON_PREMISES_IMMUTABLE_ID, description = UPDATED_ON_PREMISES_IMMUTABLE_ID_DESC) String onPremisesImmutableId,
                                       @Param(value = MAIL_NICKNAME, description = UPDATED_MAIL_NICKNAME_DESC) String mailNickname,
                                       @Param(value = FORCE_CHANGE_PASSWORD, description = FORCE_CHANGE_PASSWORD_DESC) String forceChangePassword,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,
                                       @Param(value = UPDATED_USER_PRINCIPAL_NAME, description = UPDATED_USER_PRINCIPAL_NAME_DESC) String updatedUserPrincipalName,
                                       @Param(value = BODY, description = BODY_DESC) String body,
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
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal) {

        displayName = defaultIfEmpty(displayName, EMPTY);
        onPremisesImmutableId = defaultIfEmpty(onPremisesImmutableId, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        updatedUserPrincipalName = defaultIfEmpty(updatedUserPrincipalName, EMPTY);
        body = defaultIfEmpty(body, EMPTY);
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

        final List<String> exceptionMessages = verifyUpdateUserInputs(accountEnabled, userId, userPrincipalName, forceChangePassword,
                proxyPort, trustAllRoots, x509HostnameVerifier, connectTimeout, socketTimeout, keepAlive,
                connectionsMaxPerRoute, connectionsMaxTotal);

        if (!exceptionMessages.isEmpty())
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        try {
            Map<String, String> result = updateUser(UpdateUserInputs.builder()
                    .updatedUserPrincipalName(updatedUserPrincipalName)
                    .commonUserInputs(CommonUserInputs.builder()
                            .accountEnabled(accountEnabled)
                            .displayName(displayName)
                            .onPremisesImmutableId(onPremisesImmutableId)
                            .mailNickname(mailNickname)
                            .forceChangePassword(forceChangePassword)
                            .password(password)
                            .body(body)
                            .commonInputs(AzureActiveDirectoryCommonInputs.builder()
                                    .authToken(authToken)
                                    .userId(userId)
                                    .userPrincipalName(userPrincipalName)
                                    .proxyHost(proxyHost)
                                    .proxyPort(proxyPort)
                                    .proxyUsername(proxyUsername)
                                    .proxyPassword(proxyPassword)
                                    .connectionsMaxTotal(connectionsMaxTotal)
                                    .connectionsMaxPerRoute(connectionsMaxPerRoute)
                                    .keepAlive(keepAlive)
                                    .connectTimeout(connectTimeout)
                                    .socketTimeout(socketTimeout)
                                    .trustAllRoots(trustAllRoots)
                                    .x509HostnameVerifier(x509HostnameVerifier)
                                    .trustKeystore(trustKeystore)
                                    .trustPassword(trustPassword)
                                    .build())
                            .build())
                    .build());

            Map<String, String> finalResult = getOperationResults(result, result.get(RETURN_RESULT), result.get(RETURN_RESULT));
            if (!result.get(STATUS_CODE).isEmpty() && Integer.parseInt(result.get(STATUS_CODE)) >= 200 && Integer.parseInt(result.get(STATUS_CODE)) < 300) {
                finalResult.put(RETURN_RESULT, UPDATE_USER_RETURN_RESULT);
            }
            parseApiExceptionMessage(finalResult);
            return finalResult;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
