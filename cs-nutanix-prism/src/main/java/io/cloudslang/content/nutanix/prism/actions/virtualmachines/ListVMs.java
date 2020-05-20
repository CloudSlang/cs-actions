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
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.nutanix.prism.entities.NutanixCommonInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixListVMdetailsInputs;

import io.cloudslang.content.nutanix.prism.utils.Constants;
import io.cloudslang.content.nutanix.prism.utils.Outputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.nutanix.prism.service.VMImpl.listVMs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Outputs.ListVMOutputs.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.ListVMsConstants.LIST_VMS_OPERATION_NAME;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.GetVMDetails.*;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.LISTVMInputs.*;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.ListVMs.LIST_VMS_OPERATION_DESC;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getFailureResults;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PASSWORD;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.USERNAME;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.nutanix.prism.utils.Inputs.GetVMDetailsInputs.*;
import static io.cloudslang.content.nutanix.prism.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;


public class ListVMs {
    @Action(name = LIST_VMS_OPERATION_NAME,
            description = LIST_VMS_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = VM_LIST, description = VM_LIST_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = PROTOCOL, description = PROTOCOL_DESC) String protocol,
                                       @Param(value = HOSTNAME, required = true, description = HOSTNAME_DESC) String hostname,
                                       @Param(value = PORT, description = PORT_DESC) String port,
                                       @Param(value = USERNAME, required = true, description = USERNAME_DESC) String username,
                                       @Param(value = PASSWORD, encrypted = true, required = true, description = PASSWORD_DESC) String password,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
                                       @Param(value = FILTER, description = FILTER_DESC) String filter,
                                       @Param(value = OFFSET, description = OFFSET_DESC) String offset,
                                       @Param(value = LENGTH, description = LENGTH_DESC) String length,
                                       @Param(value = SORT_ORDER, description = SORT_ORDER_DESC) String sortorder,
                                       @Param(value = SORT_ATTRIBUTE, description = SORT_ATTRIBUTE_DESC) String sortattribute,
                                       @Param(value = INCLUDE_VM_DISK_CONFIG_INFO, description = INCLUDE_VM_DISK_CONFIG_INFO_DESC) String includeVMDiskConfigInfo,
                                       @Param(value = INCLUDE_VM_NIC_CONFIG_INFO, description = INCLUDE_VM_NIC_CONFIG_INFO_DESC) String includeVMNicConfigInfo,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true, description = KEYSTORE_PASSWORD_DESC) String keystorePassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {
        protocol = defaultIfEmpty(protocol, HTTPS);
        port = defaultIfEmpty(port, DEFAULT_NUTANIX_PORT);
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        filter = defaultIfEmpty(filter, EMPTY);
        offset = defaultIfEmpty(offset, ZERO);
        length = defaultIfEmpty(length, ZERO);
        sortorder = defaultIfEmpty(sortorder, EMPTY);
        sortattribute = defaultIfEmpty(sortattribute, EMPTY);
        includeVMDiskConfigInfo = defaultIfEmpty(includeVMDiskConfigInfo, BOOLEAN_FALSE);
        includeVMNicConfigInfo = defaultIfEmpty(includeVMNicConfigInfo, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        keystore = defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE);
        keystorePassword = defaultIfEmpty(keystorePassword, CHANGEIT);
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

        try {
            final Map<String, String> result = listVMs(NutanixListVMdetailsInputs.builder()
                    .filter(filter)
                    .offset(offset)
                    .length(length)
                    .sortorder(sortorder)
                    .sortattribute(sortattribute)
                    .includeVMDiskConfigInfo(includeVMDiskConfigInfo)
                    .includeVMNicConfigInfo(includeVMNicConfigInfo)
                    .commonInputs(
                            NutanixCommonInputs.builder()
                                    .protocol(protocol)
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
                                    .keystore(keystore)
                                    .keystorePassword(keystorePassword)
                                    .connectTimeout(connectTimeout)
                                    .socketTimeout(socketTimeout)
                                    .keepAlive(keepAlive)
                                    .connectionsMaxPerRoot(connectionsMaxPerRoute)
                                    .connectionsMaxTotal(connectionsMaxTotal)
                                    .responseCharacterSet(responseCharacterSet)
                                    .build()).build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));
            if (statusCode >= 200 && statusCode < 300) {
                final List<String> Listvm = JsonPath.read(returnMessage, Constants.GetListVMConstants.LIST_VM_JSON_PATH);
                if (!Listvm.isEmpty()) {
                    final String ListVMAsString = join(Listvm.toArray(), DELIMITER);
                    results.put(VM_LIST, ListVMAsString);
                   // System.out.println( results.put(VM_LIST, ListVMAsString));
                }else{
                    results.put(VM_LIST, EMPTY);
                }

            } else {
                return getFailureResults(hostname, statusCode, returnMessage);
            }
            //return getOperationResults(result, returnMessage, returnMessage, returnMessage);
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
