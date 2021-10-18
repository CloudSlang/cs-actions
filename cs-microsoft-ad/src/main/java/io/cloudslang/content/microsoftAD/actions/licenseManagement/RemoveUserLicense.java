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
package io.cloudslang.content.microsoftAD.actions.licenseManagement;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.RemoveUserLicenseInputs;
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
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.microsoftAD.services.AssignRemoveUserLicenseService.removeUserLicense;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.RemoveUserLicense.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.RemoveUserLicense.REMOVE_USER_LICENSE_DESC;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.microsoftAD.utils.HttpUtils.parseApiExceptionMessage;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.AUTH_TOKEN;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.USER_PRINCIPAL_NAME;
import static io.cloudslang.content.microsoftAD.utils.Inputs.RemoveUserLicenseInputs.REMOVED_LICENSES;
import static io.cloudslang.content.microsoftAD.utils.InputsValidation.verifyLicenseInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class RemoveUserLicense {

    @Action(name = REMOVE_USER_LICENSE_NAME,
            description = REMOVE_USER_LICENSE_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = REMOVE_USER_LICENSE_RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = REMOVE_USER_LICENSE_SUCCESS_RETURN_RESULT_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = REMOVE_USER_LICENSE_FAILURE_DESC)
            })

    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = Descriptions.Common.AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = USER_PRINCIPAL_NAME, description = USER_PRINCIPAL_NAME_DESC) String userPrincipalName,
                                       @Param(value = USER_ID, description = USER_ID_DESC) String userId,
                                       @Param(value = REMOVED_LICENSES, required = true, description = REMOVED_LICENSES_DESC) String removedLicenses,

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


        userPrincipalName = defaultIfEmpty(userPrincipalName, EMPTY);
        userId = defaultIfEmpty(userId, EMPTY);
        removedLicenses = defaultIfEmpty(removedLicenses,EMPTY);

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

        final List<String> exceptionMessages = verifyLicenseInputs(userPrincipalName, userId, removedLicenses, proxyPort, trustAllRoots, x509HostnameVerifier,
                    connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);

        if (!exceptionMessages.isEmpty())
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));

        try {
            Map<String, String> result = removeUserLicense(RemoveUserLicenseInputs.builder()
                    .removedLicenses(removedLicenses)
                    .commonInputs(AzureActiveDirectoryCommonInputs.builder()
                            .authToken(authToken)
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
                            .userPrincipalName(userPrincipalName)
                            .userId(userId)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .build())
                    .build());

            Map<String, String> finalResult = getOperationResults(result, result.get(RETURN_RESULT), result.get(RETURN_RESULT));
            parseApiExceptionMessage(finalResult);

            return finalResult;

        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
