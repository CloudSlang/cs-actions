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

package io.cloudslang.content.nutanix.prism.actions.virtualmachines;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixSetPowerStateInputs;
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
import static io.cloudslang.content.nutanix.prism.service.TaskImpl.getTaskDetails;
import static io.cloudslang.content.nutanix.prism.service.VMImpl.setPowerState;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.CreateVMConstants.TASK_UUID_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetTaskDetailsConstants.SUCCEEDED;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetTaskDetailsConstants.TASK_STATUS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.Constants.SetPowerStateConstants.SET_POWER_STATE_OF_VM_OPERATION_NAME;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.SetPowerState.*;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getFailureResults;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PASSWORD;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.USERNAME;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.SetPowerStateInputs.*;
import static io.cloudslang.content.nutanix.prism.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.CommonOutputs.TASK_UUID;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.GetTaskDetailsOutputs.TASK_STATUS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class SetPowerStateOfVM {
    @Action(name = SET_POWER_STATE_OF_VM_OPERATION_NAME,
            description = SET_POWER_STATE_OF_VM_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = TASK_UUID, description = TASK_UUID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL,
                            responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL,
                            responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = HOSTNAME, required = true, description = HOSTNAME_DESC) String hostname,
                                       @Param(value = PORT, description = PORT_DESC) String port,
                                       @Param(value = USERNAME, required = true, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, required = true, description = PASSWORD_DESC) String password,
                                       @Param(value = VM_UUID, required = true, description = VM_UUID_DESC) String vmUUID,
                                       @Param(value = POWER_STATE, required = true, description = POWER_STATE_DESC) String powerState,
                                       @Param(value = HOST_UUID, description = HOST_UUID_DESC) String hostUUID,
                                       @Param(value = VM_LOGICAL_TIMESTAMP, description = VM_LOGICAL_TIMESTAMP_DESC) String vmLogicalTimestamp,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
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
        port = defaultIfEmpty(port, DEFAULT_NUTANIX_PORT);
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
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
        final List<String> exceptionMessage = verifyCommonInputs(proxyPort, trustAllRoots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            NutanixCommonInputs commonInputs = NutanixCommonInputs.builder()
                    .hostname(hostname)
                    .port(port)
                    .username(username)
                    .password(password)
                    .apiVersion(apiVersion)
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
                    .build();

            final Map<String, String> result = setPowerState(NutanixSetPowerStateInputs.builder()
                    .vmUUID(vmUUID)
                    .transition(powerState)
                    .hostUUID(hostUUID)
                    .vmLogicalTimestamp(vmLogicalTimestamp)
                    .commonInputs(commonInputs).build());

            Map<String, String> results;
            int statusCode;
            String returnMessage;
            returnMessage = result.get(RETURN_RESULT);
            results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                final String taskUUID = JsonPath.read(returnMessage, TASK_UUID_PATH);
                results.put(TASK_UUID, taskUUID);
                final Map<String, String> taskResult = getTaskDetails(NutanixGetTaskDetailsInputs.builder()
                        .taskUUID(taskUUID)
                        .commonInputs(commonInputs).build());
                returnMessage = taskResult.get(RETURN_RESULT);
                results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
                statusCode = Integer.parseInt(result.get(STATUS_CODE));
                String taskStatus = JsonPath.read(returnMessage, TASK_STATUS_PATH);
                if (statusCode >= 200 && statusCode < 300 && taskStatus.equals(SUCCEEDED)) {
                    results.put(TASK_STATUS, taskStatus);
                    String powerStateSuccessMessage = ("Successfully transitioned the Power State of Virtual Machine " + vmUUID + " to " + powerState);
                    results.put(RETURN_RESULT, powerStateSuccessMessage);
                } else {
                    return getFailureResults(hostname, statusCode, taskStatus, returnMessage, returnMessage);
                }
            } else {
                return getFailureResults(hostname, statusCode, returnMessage, returnMessage, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}

