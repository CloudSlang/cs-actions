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
package io.cloudslang.content.azure.actions.streamanalytics.inputs;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.entities.AzureCommonInputs;
import io.cloudslang.content.azure.entities.CreateStreamingInputJobInputs;
import io.cloudslang.content.azure.entities.CreateStreamingOutputJobInputs;
import io.cloudslang.content.azure.services.StreamingJobImpl;
import io.cloudslang.content.azure.utils.Constants;
import io.cloudslang.content.azure.utils.Descriptions;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PASSWORD;
import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.CreateStreamingJobConstants.CREATE_STREAMING_INPUT_JOB_OPERATION_NAME;
import static io.cloudslang.content.azure.utils.Constants.CreateStreamingJobConstants.DEFAULT_SOURCE_TYPE;
import static io.cloudslang.content.azure.utils.Descriptions.Common.*;
import static io.cloudslang.content.azure.utils.Descriptions.Common.TRUST_PASSWORD_DESC;
import static io.cloudslang.content.azure.utils.Descriptions.CreateStreamingJob.*;
import static io.cloudslang.content.azure.utils.Descriptions.CreateStreamingJob.API_VERSION_DESC;
import static io.cloudslang.content.azure.utils.HttpUtils.getFailureResults;
import static io.cloudslang.content.azure.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.ACCOUNT_KEY;
import static io.cloudslang.content.azure.utils.Inputs.CommonInputs.API_VERSION;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_HOST;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_PORT;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PROXY_USERNAME;
import static io.cloudslang.content.azure.utils.Inputs.CreateStreamingJobInputs.SOURCE_TYPE;
import static io.cloudslang.content.azure.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static io.cloudslang.content.azure.utils.Inputs.CreateStreamingJobInputs.INPUT_NAME;

public class GetInput {
    @Action(name = CREATE_STREAMING_INPUT_JOB_OPERATION_NAME,
            description = CREATE_STREAMING_INPUT_JOB_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = JOB_NAME, required = true, description = JOB_NAME_DESC) String jobName,
                                       @Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC, encrypted = true) String authToken,
                                       @Param(value = INPUT_NAME, required = true, description = INPUT_NAME_DESC) String inputName,
                                       @Param(value = RESOURCE_GROUP_NAME, required = true, description = RESOURCE_GROUP_NAME_DESC) String resourceGroupName,
                                       @Param(value = SUBSCRIPTION_ID, required = true, description = Descriptions.Common.SUBSCRIPTION_ID_DESC) String subscriptionId,
                                       @Param(value = ACCOUNT_NAME, required = true, description = ACCOUNT_NAME_DESC) String accountName,
                                       @Param(value = ACCOUNT_KEY, required = true, description = ACCOUNT_KEY_DESC) String accountKey,
                                       @Param(value = SOURCE_TYPE,  description =SOURCE_TYPE_DESC) String sourceType,
                                       @Param(value = API_VERSION, description = API_VERSION_DESC) String apiVersion,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword) {
        apiVersion = defaultIfEmpty(apiVersion, DEFAULT_API_VERSION);
        sourceType = defaultIfEmpty(sourceType, DEFAULT_SOURCE_TYPE);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, Constants.DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);

        final List<String> exceptionMessage = verifyCommonInputs(proxyPort, trustAllRoots);
        if (!exceptionMessage.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessage, NEW_LINE));
        }

        try {
            final Map<String, String> result = StreamingJobImpl.CreateInputJob(CreateStreamingInputJobInputs.builder()
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
                            .jobName(jobName)
                            .inputName(inputName)
                            .accountName(accountName)
                            .accountKey(accountKey)
                            .sourceType(sourceType)
                            .build());
            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode == 200) {

            } else {
                return getFailureResults(subscriptionId, statusCode, returnMessage);
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }


    }
}
