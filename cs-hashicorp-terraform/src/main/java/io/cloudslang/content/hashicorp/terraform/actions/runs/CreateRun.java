/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.actions.runs;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs.IS_DESTROY;
import static io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs.RUN_MESSAGE;
import static io.cloudslang.content.hashicorp.terraform.services.RunImpl.createRunClient;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.UTF8;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.CREATE_RUN_OPERATION_NAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_ID_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateRun.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.WORKSPACE_ID_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.WORKSPACE_NAME_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.*;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_HOST;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PASSWORD;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PORT;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_USERNAME;
import static io.cloudslang.content.hashicorp.terraform.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.InputsValidation.verifyCreateRunInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateRunOutputs.RUN_ID;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateWorkspaceOutputs.WORKSPACE_ID;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class CreateRun {

    @Action(name = CREATE_RUN_OPERATION_NAME,
            description = CREATE_RUN_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STATUS_CODE,description = STATUS_CODE_DESC),
                    @Output(value = RUN_ID,description = RUN_ID_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true,encrypted = true,description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = WORKSPACE_ID,description = WORKSPACE_ID_DESC) String workspaceId,
                                       @Param(value = RUN_MESSAGE,description = RUN_MESSAGE_DESC) String runMessage,
                                       @Param(value = IS_DESTROY,description =IS_DESTROY_DESC ) String isDestroy,
                                       @Param( value = REQUEST_BODY) String requestBody,
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
                                       @Param(value = EXECUTION_TIMEOUT,description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
                                       @Param(value = ASYNC,description = ASYN_DESC) String asyn,
                                       @Param(value = POLLING_INTERVAL,description = POLLING_INTERVAL_DESC) String pollingInterval,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {

        workspaceId = defaultIfEmpty(workspaceId, EMPTY);
        runMessage = defaultIfEmpty(runMessage, EMPTY);
        requestBody=defaultIfEmpty(requestBody,EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, CONNECT_TIMEOUT_CONST);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        executionTimeout = defaultIfEmpty(executionTimeout,EXEC_TIMEOUT);
        asyn = defaultString(asyn,BOOLEAN_FALSE);
        pollingInterval = defaultString(pollingInterval,POLLING_INTERVAL_DEFAULT);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final List<String> exceptionMessage = verifyCommonInputs(proxyPort,trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        final List<String> exceptionMessages = verifyCreateRunInputs(workspaceId,requestBody);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = createRunClient(CreateRunInputs.builder()
                    .workspaceId(workspaceId)
                    .runMessage(runMessage)
                    .isDestroy(isDestroy)
                    .commonInputs(Inputs.builder()
                            .authToken(authToken)
                            .requestBody(requestBody)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .trustAllRoots(trustAllRoots)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .connectTimeout(connectTimeout)
                            .socketTimeout(socketTimeout)
                            .executionTimeout(executionTimeout)
                            .async(asyn)
                            .pollingInterval(pollingInterval)
                            .keepAlive(keepAlive)
                            .connectionsMaxPerRoot(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .responseCharacterSet(responseCharacterSet)
                            .build())
                    .build());



            final String returnMessage = result.get(RETURN_RESULT);

            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                final List<String> runEventIdList = JsonPath.read(returnMessage, RUN_ID_PATH);
                if (!runEventIdList.isEmpty()) {
                    final String runEventIdListAsString = join(runEventIdList.toArray(), DELIMITER);
                    results.put(RUN_ID, runEventIdListAsString);
                } else {
                    results.put(RUN_ID, EMPTY);
                }
            }

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
