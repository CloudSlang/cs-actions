/*
 * Copyright 2024 Open Text
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



package io.cloudslang.content.azure.actions.compute.virtualmachines;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.entities.AzureCommonInputs;
import io.cloudslang.content.azure.entities.AzureComputeCommonInputs;
import io.cloudslang.content.azure.services.AzureComputeImpl;
import io.cloudslang.content.azure.utils.Constants;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_HOST;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PASSWORD;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_USERNAME;
import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.DeleteVMConstants.DELETE_VM;
import static io.cloudslang.content.azure.utils.Descriptions.Common.*;
import static io.cloudslang.content.azure.utils.Descriptions.ComputeCommonDescriptions.*;
import static io.cloudslang.content.azure.utils.Descriptions.CreateStreamingJob.RESOURCE_GROUP_NAME_DESC;
import static io.cloudslang.content.azure.utils.Descriptions.CreateVMDescriptions.VM_NAME_DESC;
import static io.cloudslang.content.azure.utils.Descriptions.DeleteVMDescriptions.DELETE_VM_DESC;
import static io.cloudslang.content.azure.utils.HttpUtils.getFailureResults;
import static io.cloudslang.content.azure.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.azure.utils.Inputs.ComputeCommonInputs.AZURE_HOST;
import static io.cloudslang.content.azure.utils.Inputs.ComputeCommonInputs.AZURE_PROTOCOL;
import static io.cloudslang.content.azure.utils.Inputs.CreateVMInputs.VM_NAME;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DeleteVM {

    @Action(name = DELETE_VM,
            description = DELETE_VM_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })

    public Map<String, String> execute(@Param(value = SUBSCRIPTION_ID, required = true, description = SUBSCRIPTION_ID_DESC) String subscriptionId,
                                       @Param(value = AZURE_PROTOCOL, required = true, description = AZURE_PROTOCOL_DESC) String azureProtocol,
                                       @Param(value = AZURE_HOST, required = true, description = AZURE_HOST_DESC) String azureHost,
                                       @Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC, encrypted = true) String authToken,
                                       @Param(value = API_VERSION, description = COMPUTE_API_VERSION_DESC) String apiVersion,
                                       @Param(value = RESOURCE_GROUP_NAME, required = true, description = RESOURCE_GROUP_NAME_DESC) String resourceGroupName,
                                       @Param(value = VM_NAME, required = true, description = VM_NAME_DESC) String vmName,

                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword) {

        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_COMPUTE_API_VERSION);
        azureProtocol = defaultIfEmpty(azureProtocol, HTTPS);
        azureHost = defaultIfEmpty(azureHost, AZURE_HOST_CONST);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, Constants.DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        List<String> exceptionMessage = verifyCommonInputs(proxyPort, trustAllRoots);

        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            final Map<String, String> result = AzureComputeImpl.deleteVM(
                    AzureComputeCommonInputs.builder().azureHost(azureHost)
                            .azureProtocol(azureProtocol)
                            .vmName(vmName)
                            .azureCommonInputs(AzureCommonInputs.builder()
                                    .apiVersion(apiVersion)
                                    .authToken(authToken)
                                    .subscriptionId(subscriptionId)
                                    .resourceGroupName(resourceGroupName)
                                    .proxyPort(proxyPort)
                                    .proxyHost(proxyHost)
                                    .proxyUsername(proxyUsername)
                                    .proxyPassword(proxyPassword)
                                    .trustAllRoots(trustAllRoots)
                                    .x509HostnameVerifier(x509HostnameVerifier)
                                    .trustKeystore(trustKeystore)
                                    .trustPassword(trustPassword).build())
                            .build());

            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));
            if (!(statusCode >= 200 && statusCode < 300)) {
                return getFailureResults(subscriptionId, statusCode, returnMessage);
            }

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}


