package io.cloudslang.content.jclouds.actions.instances;

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
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by persdana on 6/18/2015.
 */
public class StopInstancesAction {
    /**
     * Stops an Amazon EBS-backed instance.
     * Note: We don't charge hourly usage for a stopped instance, or data transfer fees; however, your root partition
     * Amazon EBS volume remains, continues to persist your data, and you are charged for Amazon EBS volume usage.
     * Each time you transition an instance from stopped to started, Amazon EC2 charges a full instance hour, even
     * if transitions happen multiple times within a single hour.
     * You can't start or stop Spot instances, and you can't stop instance store-backed instances.
     * When you stop an instance, we shut it down. You can restart your instance at any time. Before stopping an
     * instance, make sure it is in a state from which it can be restarted. Stopping an instance does not preserve
     * data stored in RAM.
     * Stopping an instance is different to rebooting or terminating it. For example, when you stop an instance,
     * the root device and any other devices attached to the instance persist. When you terminate an instance, the
     * root device and any other devices attached during the instance launch are automatically deleted. For more
     * information about the differences between rebooting, stopping, and terminating instances, see Instance
     * Lifecycle in the Amazon Elastic Compute Cloud User Guide.
     * When you stop an instance, we attempt to shut it down forcibly after a short while. If your instance appears
     * stuck in the stopping state after a period of time, there may be an issue with the underlying host computer.
     * For more information, see Troubleshooting Stopping Your Instance in the Amazon Elastic Compute Cloud User
     * Guide.
     *
     * @param endpoint      Endpoint to which request will be sent.
     *                      Default: "https://ec2.amazonaws.com"
     * @param identity      ID of the secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential    Secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost     Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                      Default: ""
     * @param proxyPort     Optional - proxy server port. You must either specify values for both <proxyHost> and <proxyPort>
     *                      inputs or leave them both empty.
     *                      Default: ""
     * @param proxyUsername Optional - proxy server user name.
     *                      Default: ""
     * @param proxyPassword Optional - proxy server password associated with the <proxyUsername> input value.
     *                      Default: ""
     * @param headers       Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                      The header name-value pair will be separated by ":".
     *                      Format: Conforming with HTTP standard for headers (RFC 2616)
     *                      Examples: "Accept:text/plain"
     *                      Default: ""
     * @param queryParams   Optional - string containing query parameters that will be appended to the URL. The names and
     *                      the values must not be URL encoded because if they are encoded then a double encoded will occur.
     *                      The separator between name-value pairs is "&" symbol. The query name will be separated from
     *                      query value by "=".
     *                      Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                      Default: ""
     * @param version       Version of the web service to made the call against it.
     *                      Example: "2016-04-01"
     * @param delimiter     Optional - delimiter that will be used.
     *                      Default: ","
     * @param instanceId    String that contains one or more values that represents instance IDs.
     *                      Example: "i-12345678,i-abcdef12,i-12ab34cd"
     * @param forceStop     Optional - Forces the instances to stop. The instances do not have an opportunity to flush
     *                      file system caches or file system metadata. If you use this option, you must perform file
     *                      system check and repair procedures. This option is not recommended for Windows instances.
     *                      Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request.
     */
    @Action(name = "Stop Instances",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> stopInstances(@Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                             @Param(value = Inputs.CommonInputs.IDENTITY, required = true) String identity,
                                             @Param(value = Inputs.CommonInputs.CREDENTIAL, required = true, encrypted = true) String credential,
                                             @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                             @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                             @Param(value = Inputs.CommonInputs.PROXY_USERNAME) String proxyUsername,
                                             @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                             @Param(value = Inputs.CommonInputs.HEADERS) String headers,
                                             @Param(value = Inputs.CommonInputs.QUERY_PARAMS) String queryParams,
                                             @Param(value = Inputs.CommonInputs.VERSION, required = true) String version,
                                             @Param(value = Inputs.CommonInputs.DELIMITER, required = true) String delimiter,

                                             @Param(value = Inputs.CustomInputs.INSTANCE_ID, required = true) String instanceId,

<<<<<<< HEAD
                                             @Param(value = Inputs.InstanceInputs.FORCE_STOP, required = true) String forceStop) {
        try {
            CommonInputs commonInputs = new CommonInputs.Builder()
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
                    .withDelimiter(delimiter)
                    .withAction(Constants.QueryApiActions.STOP_INSTANCES)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpClientMethod(Constants.AwsParams.HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withInstanceId(instanceId).build();
            InstanceInputs instanceInputs = new InstanceInputs.Builder().withForceStop(forceStop).build();
=======
        CommonInputs inputs = new CommonInputs.Builder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.Builder()
                .withRegion(region)
                .withInstanceId(instanceId)
                .build();
>>>>>>> master

            return new QueryApiExecutor().execute(commonInputs, customInputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}