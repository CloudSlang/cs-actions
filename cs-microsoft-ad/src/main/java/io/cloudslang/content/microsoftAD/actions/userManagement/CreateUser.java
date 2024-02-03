/*
 * Copyright 2021-2024 Open Text
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
package io.cloudslang.content.microsoftAD.actions.userManagement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.CommonUserInputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.microsoftAD.services.CreateUserService.createUser;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.CreateUser.USER_ID_DESC;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.CreateUser.USER_PRINCIPAL_NAME_DESC;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.CreateUser.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.GetAuthorizationToken.AUTH_TOKEN_DESC;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.parseApiExceptionMessage;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.ACCOUNT_ENABLED;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.BODY;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyCommonUserInputs;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyCreateUserInputs;
import static io.cloudslang.content.microsoftAD.utils.Outputs.OutputNames.STATUS_CODE;
import static io.cloudslang.content.microsoftAD.utils.Outputs.OutputNames.USER_ID;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateUser {

    @Action(name = NAME,
            description = DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = USER_ID, description = USER_ID_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })

    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,

                                       @Param(value = BODY, description = BODY_DESC) String body,

                                       @Param(value = ACCOUNT_ENABLED, description = ACCOUNT_ENABLED_DESC) String accountEnabled,
                                       @Param(value = DISPLAY_NAME, description = DISPLAY_NAME_DESC) String displayName,
                                       @Param(value = ON_PREMISES_IMMUTABLE_ID, description = ON_PREMISES_IMMUTABLE_ID_DESC) String onPremisesImmutableId,
                                       @Param(value = MAIL_NICKNAME, description = MAIL_NICKNAME_DESC) String mailNickname,
                                       @Param(value = FORCE_CHANGE_PASSWORD, description = FORCE_CHANGE_PASSWORD_DESC) String forceChangePassword,
                                       @Param(value = PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,
                                       @Param(value = USER_PRINCIPAL_NAME, description = USER_PRINCIPAL_NAME_DESC) String userPrincipalName,

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

        accountEnabled = defaultIfEmpty(accountEnabled, BOOLEAN_TRUE);
        displayName = defaultIfEmpty(displayName, EMPTY);
        onPremisesImmutableId = defaultIfEmpty(onPremisesImmutableId, EMPTY);
        mailNickname = defaultIfEmpty(mailNickname, EMPTY);
        userPrincipalName = defaultIfEmpty(userPrincipalName, EMPTY);
        forceChangePassword = defaultIfEmpty(forceChangePassword, BOOLEAN_FALSE);
        password = defaultIfEmpty(password, EMPTY);
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

        final List<String> exceptionMessages;

        if (body.equals(EMPTY))
            exceptionMessages = verifyCreateUserInputs(accountEnabled, displayName, mailNickname,
                    userPrincipalName, forceChangePassword, password, proxyPort, trustAllRoots, x509HostnameVerifier,
                    connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        else
            exceptionMessages = verifyCommonUserInputs(proxyPort, trustAllRoots, x509HostnameVerifier,
                    connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);

        if (!exceptionMessages.isEmpty())
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        try {
            Map<String, String> result = createUser(CommonUserInputs.builder()
                    .accountEnabled(accountEnabled)
                    .displayName(displayName)
                    .onPremisesImmutableId(onPremisesImmutableId)
                    .mailNickname(mailNickname)
                    .forceChangePassword(forceChangePassword)
                    .password(password)
                    .body(body)
                    .commonInputs(AzureActiveDirectoryCommonInputs.builder()
                            .authToken(authToken)
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
                    .build());

            Map<String, String> finalResult = getOperationResults(result, result.get(RETURN_RESULT), result.get(RETURN_RESULT));

            finalResult.put(USER_ID, EMPTY);

            if (!result.get(STATUS_CODE).isEmpty() && Integer.parseInt(result.get(STATUS_CODE)) >= 200 && Integer.parseInt(result.get(STATUS_CODE)) < 300) {

                JsonObject json = ((new JsonParser()).parse(result.get(RETURN_RESULT))).getAsJsonObject();

                if (json.has(ID))
                    finalResult.put(USER_ID, json.get(ID).getAsString());
            }

            parseApiExceptionMessage(finalResult);

            return finalResult;

        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
