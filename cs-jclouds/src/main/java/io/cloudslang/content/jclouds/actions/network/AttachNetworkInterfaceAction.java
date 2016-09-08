package io.cloudslang.content.jclouds.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.NetworkInputs;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/11/2016.
 */
public class AttachNetworkInterfaceAction {
    /**
     * Attaches a network interface to an instance.
     * Note: The set of: <instanceId>, <networkInterfaceId>, <deviceIndex> are mutually exclusive with <queryParams> input.
     * Please provide values EITHER FOR ALL: <instanceId>, <networkInterfaceId>, <deviceIndex> inputs OR FOR <queryParams> input.
     * Note: As with all Amazon EC2 operations, the results might not appear immediately.
     * Note: For Region-Endpoint correspondence information, check all the service endpoints available at:
     * http://docs.amazonwebservices.com/general/latest/gr/rande.html#ec2_region
     *
     * @param endpoint           Endpoint to which request will be sent.
     *                           Default: "https://ec2.amazonaws.com"
     * @param identity           ID of the secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential         Secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost          Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort          Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                           <proxyPort> inputs or leave them both empty.
     * @param proxyUsername      Optional - proxy server user name.
     * @param proxyPassword      Optional - proxy server password associated with the <proxyUsername> input value.
     * @param instanceId         Optional - ID of the instance that will be attached to the network interface. The instance
     *                           should be running (hot attach) or stopped (warm attach).
     *                           Example: "i-abcdef12"
     * @param headers            Optional - string containing the headers to use for the request separated by new line
     *                           (CRLF). The header name-value pair will be separated by ":"
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: Accept:text/plain
     * @param queryParams        Optional - string containing query parameters that will be appended to the URL. The names
     *                           and the values must not be URL encoded because if they are encoded then a double encoded
     *                           will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                           separated from query value by "="
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param networkInterfaceId Optional - ID of the network interface to attach.
     *                           Example: "eni-12345678"
     * @param deviceIndex        Optional - ID of the device for the network interface attachment on the instance.
     *                           Example: "1"
     * @param version            Version of the web service to made the call against it.
     *                           Example: "2014-06-15"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Attach Network Interface",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> attachNetworkInterface(@Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                                      @Param(value = Inputs.CommonInputs.IDENTITY, required = true) String identity,
                                                      @Param(value = Inputs.CommonInputs.CREDENTIAL, required = true, encrypted = true) String credential,
                                                      @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                                      @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                                      @Param(value = Inputs.CommonInputs.PROXY_USERNAME) String proxyUsername,
                                                      @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                                      @Param(value = Inputs.CommonInputs.HEADERS) String headers,
                                                      @Param(value = Inputs.CommonInputs.QUERY_PARAMS) String queryParams,
                                                      @Param(value = Inputs.CommonInputs.VERSION, required = true) String version,

                                                      @Param(value = Inputs.CustomInputs.INSTANCE_ID) String instanceId,

                                                      @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ID) String networkInterfaceId,
                                                      @Param(value = Inputs.NetworkInputs.DEVICE_INDEX) String deviceIndex) {

        try {
            CommonInputs commonInputs = new CommonInputs.CommonInputsBuilder()
                    .withEndpoint(endpoint)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withAction(Constants.QueryApiActions.ATTACH_NETWORK_INTERFACE)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpClientMethod(Constants.QueryApiActions.HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.CustomInputsBuilder().withInstanceId(instanceId).build();

            NetworkInputs networkInputs = new NetworkInputs.NetworkInputsBuilder()
                    .withNetworkInterfaceId(networkInterfaceId)
                    .withDeviceIndex(deviceIndex)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, networkInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}