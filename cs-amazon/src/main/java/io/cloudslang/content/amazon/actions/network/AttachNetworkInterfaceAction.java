/*
 * Copyright 2019-2024 Open Text
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
/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.amazon.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.OutputsUtil;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.ATTACH_NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Outputs.ATTACHMENT_ID_RESULT;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by Mihai Tusa.
 * 8/11/2016.
 */
public class AttachNetworkInterfaceAction {
    private static final String ATTACHMENT_ID_X_PATH_QUERY = "/AttachNetworkInterfaceResponse/attachmentId";
    /**
     * Attaches a network interface to an instance.
     * Note: The set of: instanceId, networkInterfaceId, deviceIndex are mutually exclusive with queryParams input.
     * Please provide values EITHER FOR ALL: instanceId, networkInterfaceId, deviceIndex inputs OR FOR queryParams input.
     * Note: As with all Amazon EC2 operations, the results might not appear immediately.
     * Note: For Region-Endpoint correspondence information, check all the service endpoints available at:
     * http://docs.amazonwebservices.com/general/latest/gr/rande.html#ec2_region
     *
     * @param endpoint           Optional - Endpoint to which request will be sent.
     *                           Default: "https://ec2.amazonaws.com"
     * @param identity           ID of the secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential         Secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost          Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                           Default: ""
     * @param proxyPort          Optional - proxy server port. You must either specify values for both proxyHost and
     *                           proxyPort inputs or leave them both empty.
     *                           Default: ""
     * @param proxyUsername      Optional - proxy server user name.
     *                           Default: ""
     * @param proxyPassword      Optional - proxy server password associated with the proxyUsername input value.
     *                           Default: ""
     * @param instanceId         Optional - ID of the instance that will be attached to the network interface. The instance
     *                           should be running (hot attach) or stopped (warm attach).
     *                           Example: "i-abcdef12"
     * @param headers            Optional - string containing the headers to use for the request separated by new line
     *                           (CRLF). The header name-value pair will be separated by ":"
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: "Accept:text/plain"
     *                           Default: ""
     * @param queryParams        Optional - string containing query parameters that will be appended to the URL. The names
     *                           and the values must not be URL encoded because if they are encoded then a double encoded
     *                           will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                           separated from query value by "=".
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                           Default: ""
     * @param networkInterfaceId Optional - ID of the network interface to attach.
     *                           Example: "eni-12345678"
     * @param deviceIndex        Optional - ID of the device for the network interface attachment on the instance.
     *                           Example: "1"
     * @param version            Optional - Version of the web service to made the call against it.
     *                           Example: "2016-11-15"
     *                           Default: "2016-11-15"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Attach Network Interface",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(ATTACHMENT_ID_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = Outputs.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = Outputs.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = INSTANCE_ID) String instanceId,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId,
                                       @Param(value = DEVICE_INDEX) String deviceIndex) {
        try {
            version = getDefaultStringInput(version, NETWORK_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, EC2_API, EMPTY)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withAction(ATTACH_NETWORK_INTERFACE)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder().withInstanceId(instanceId).build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfaceId(networkInterfaceId)
                    .withDeviceIndex(deviceIndex)
                    .build();

            Map<String, String> queryMapResult = new QueryApiExecutor().execute(commonInputs, customInputs, networkInputs);

            if ((ReturnCodes.SUCCESS).equals(queryMapResult.get(RETURN_CODE))) {
                OutputsUtil.putResponseIn(queryMapResult, ATTACHMENT_ID_RESULT, ATTACHMENT_ID_X_PATH_QUERY);
            }

            return queryMapResult;
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
