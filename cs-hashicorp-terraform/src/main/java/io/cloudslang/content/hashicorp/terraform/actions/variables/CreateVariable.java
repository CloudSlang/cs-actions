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

package io.cloudslang.content.hashicorp.terraform.actions.variables;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformVariableInputs;
import io.cloudslang.content.utils.StringUtilities;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.hashicorp.terraform.services.VariableImpl.createVariable;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.CREATE_VARIABLES_OPERATION_NAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.VARIABLE_ID_JSON_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateVariable.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.WORKSPACE_ID_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.CreateVariableInputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.InputsValidation.verifyCreateVariableInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateVariableOutputs.VARIABLE_ID;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateWorkspaceOutputs.WORKSPACE_ID;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class CreateVariable {

    @Action(name = CREATE_VARIABLES_OPERATION_NAME,
            description = CREATE_VARIABLE_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = VARIABLE_ID, description = VARIABLE_ID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, encrypted = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = VARIABLE_NAME, description = VARIABLE_NAME_DESC) String variableName,
                                       @Param(value = VARIABLE_VALUE, description = VARIABLE_VALUE_DESC) String variableValue,
                                       @Param(value = SENSITIVE_VARIABLE_NAME, description = SENSITIVE_VARIABLE_NAME_DESC) String sensitiveVariableName,
                                       @Param(value = SENSITIVE_VARIABLE_VALUE, encrypted = true, description = SENSITIVE_VARIABLE_VALUE_DESC) String sensitiveVariableValue,
                                       @Param(value = VARIABLE_CATEGORY, description = VARIABLE_CATEGORY_DESC) String variableCategory,
                                       @Param(value = SENSITIVE, description = SENSITIVE_DESC) String sensitive,
                                       @Param(value = HCL, description = HCL_DESC) String hcl,
                                       @Param(value = WORKSPACE_ID, description = WORKSPACE_ID_DESC) String workspaceId,
                                       @Param(value = REQUEST_BODY, description = VARIABLE_REQUEST_BODY_DESC) String requestBody,
                                       @Param(value = SENSITIVE_REQUEST_BODY,encrypted = true, description = VARIABLE_SENSITIVE_REQUEST_BODY_DESC) String sensitiveVariableRequestBody,
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
        authToken = defaultIfEmpty(authToken, EMPTY);
        variableName = defaultIfEmpty(variableName, EMPTY);
        variableValue = defaultIfEmpty(variableValue, EMPTY);
        sensitiveVariableName = defaultIfEmpty(sensitiveVariableName, EMPTY);
        sensitiveVariableValue = defaultIfEmpty(sensitiveVariableValue, EMPTY);
        variableCategory = defaultIfEmpty(variableCategory, EMPTY);
        hcl = defaultIfEmpty(hcl, BOOLEAN_FALSE);
        sensitive = defaultIfEmpty(sensitive, BOOLEAN_FALSE);
        workspaceId = defaultIfEmpty(workspaceId, BOOLEAN_TRUE);
        requestBody = defaultIfEmpty(requestBody, EMPTY);
        sensitiveVariableRequestBody = defaultIfEmpty(sensitiveVariableRequestBody, EMPTY);
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
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);

        final List<String> exceptionMessage = verifyCommonInputs(proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }
        if(!requestBody.isEmpty() & !sensitiveVariableRequestBody.isEmpty()){
            return getFailureResultsMap("Please provide either requestBody or sensitiveVariableRequestBody");
        }
        final List<String> exceptionMessages = verifyCreateVariableInputs(workspaceId, variableCategory, variableName, variableValue, sensitiveVariableName, sensitiveVariableValue, requestBody);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }


        try {
            final Map<String, String> result = createVariable(TerraformVariableInputs.builder()
                    .variableName(variableName)
                    .variableValue(variableValue)
                    .sensitiveVariableName(sensitiveVariableName)
                    .sensitiveVariableValue(sensitiveVariableValue)
                    .variableCategory(variableCategory)
                    .sensitive(sensitive)
                    .hcl(hcl)
                    .workspaceId(workspaceId)
                    .sensitiveVariableRequestBody(sensitiveVariableRequestBody)
                    .commonInputs(TerraformCommonInputs.builder()
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
                    final String variableId = JsonPath.read(returnMessage, VARIABLE_ID_JSON_PATH);
                    if (!variableId.isEmpty()) {
                        results.put(VARIABLE_ID, variableId);
                    } else {
                        results.put(VARIABLE_ID, EMPTY);
                    }
                }

                return results;

        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
