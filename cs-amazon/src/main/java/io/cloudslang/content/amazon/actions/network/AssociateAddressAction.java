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
import io.cloudslang.content.amazon.entities.inputs.ElasticIpInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.QueryApiActions.ASSOCIATE_ADDRESS;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.ALLOW_REASSOCIATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ID;

/**
 * Created by Mihai Tusa.
 * 9/12/2016.
 */
public class AssociateAddressAction {
    /**
     * Associates an Elastic IP address with an instance or a network interface.
     * Note: An Elastic IP address is for use in either the EC2-Classic platform or in a VPC. For more information, see
     * Elastic IP Addresses in the Amazon Elastic Compute Cloud User Guide.
     * [EC2-Classic, VPC in an EC2-VPC-only account] If the Elastic IP address is already associated with a different
     * instance, it is disassociated from that instance and associated with the specified instance.
     * [VPC in an EC2-Classic account] If you don't specify a private IP address, the Elastic IP address is associated
     * with the primary IP address. If the Elastic IP address is already associated with a different instance or a network
     * interface, you get an error unless you allow re-association.
     * <p>
     * Important: This is an idempotent operation. If you perform the operation more than once, Amazon EC2 doesn't return
     * an error, and you may be charged for each time the Elastic IP address is remapped to the same instance. For more
     * information, see the Elastic IP Addresses section of Amazon EC2 Pricing.
     *
     * @param endpoint           Optional - Endpoint to which request will be sent.
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
     * @param version            Optional - Version of the web service to made the call against it.
     *                           Example: "2016-04-01"
     *                           Default: "2016-04-01"
     * @param headers            Optional - string containing the headers to use for the request separated by new line
     *                           (CRLF). The header name-value pair will be separated by ":".
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: Accept:text/plain
     * @param queryParams        Optional - string containing query parameters that will be appended to the URL. The names
     *                           and the values must not be URL encoded because if they are encoded then a double encoded
     *                           will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                           separated from query value by "=".
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param allocationId       Optional - [EC2-VPC] Allocation ID. This is required for EC2-VPC.
     *                           Example: "eipalloc-abcdef12"
     *                           Default: ""
     * @param instanceId         Optional - ID of the instance. This is required for EC2-Classic. For EC2-VPC, you can
     *                           specify either the instance ID or the network interface ID, but not both. The operation
     *                           fails if you specify an instance ID unless exactly one network interface is attached.
     *                           Example: "i-12345678"
     *                           Default: ""
     * @param allowReassociation Optional - [EC2-VPC] For a VPC in an EC2-Classic account, specify "true" to allow an
     *                           Elastic IP address that is already associated with an instance or network interface to
     *                           be re-associated with the specified instance or network interface. Otherwise, the operation
     *                           fails. In a VPC in an EC2-VPC-only account, re-association is automatic, therefore you
     *                           can specify "false" to ensure the operation fails if the Elastic IP address is already
     *                           associated with another resource. Any other values except valid values will be ignored.
     *                           Valid values: "true", "false"
     *                           Default: "false"
     * @param privateIpAddress   Optional - [EC2-VPC] The primary or secondary private IP address to associate with the
     *                           Elastic IP address. If no private IP address is specified, the Elastic IP address is
     *                           associated with the primary private IP address.
     *                           Default: ""
     * @param publicIp           Optional - Elastic IP address. This is required for EC2-Classic.
     * @param networkInterfaceId Optional - [EC2-VPC] ID of the network interface. If the instance has more than one network
     *                           interface, you must specify a network interface ID.
     *                           Example: "eni-12345678"
     *                           Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Associate Address",
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
                                       @Param(value = ALLOCATION_ID) String allocationId,
                                       @Param(value = INSTANCE_ID) String instanceId,
                                       @Param(value = ALLOW_REASSOCIATION) String allowReassociation,
                                       @Param(value = PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = PUBLIC_IP) String publicIp,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2016-04-01");
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
                    .withAction(ASSOCIATE_ADDRESS)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withAllocationId(allocationId).withInstanceId(instanceId).build();

            ElasticIpInputs elasticIpInputs = new ElasticIpInputs.Builder()
                    .withAllowReassociation(allowReassociation)
                    .withPrivateIpAddress(privateIpAddress)
                    .withPublicIp(publicIp)
                    .build();

            NetworkInputs networkInputs = new NetworkInputs.Builder().withNetworkInterfaceId(networkInterfaceId).build();

            Map<String, String> queryMapResult = new QueryApiExecutor().execute(commonInputs, customInputs, elasticIpInputs, networkInputs);
            return queryMapResult;
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}