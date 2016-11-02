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
import io.cloudslang.content.amazon.entities.inputs.IamInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.queries.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.QueryApiActions.CREATE_NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.SUBNET_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SECONDARY_PRIVATE_IP_ADDRESS_COUNT;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class CreateNetworkInterfaceAction {
    /**
     * Creates a network interface in the specified subnet.
     * Note: For more information about network interfaces, see Elastic Network Interfaces in the Amazon Elastic Compute
     * Cloud User Guide.
     *
     * @param endpoint                       Optional - Endpoint to which request will be sent.
     *                                       Default: "https://ec2.amazonaws.com"
     * @param identity                       ID of the secret access key associated with your Amazon AWS or IAM account.
     *                                       Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                     Secret access key associated with your Amazon AWS or IAM account.
     *                                       Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                      Optional - proxy server used to connect to Amazon API. If empty no proxy will
     *                                       be used.
     * @param proxyPort                      Optional - proxy server port. You must either specify values for both <proxyHost>
     *                                       and <proxyPort> inputs or leave them both empty.
     * @param proxyUsername                  Optional - proxy server user name.
     * @param proxyPassword                  Optional - proxy server password associated with the <proxyUsername> input
     *                                       value.
     * @param headers                        Optional - string containing the headers to use for the request separated
     *                                       by new line (CRLF). The header name-value pair will be separated by ":".
     *                                       Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                       Examples: "Accept:text/plain"
     * @param queryParams                    Optional - string containing query parameters that will be appended to the
     *                                       URL. The names and the values must not be URL encoded because if they are
     *                                       encoded then a double encoded will occur. The separator between name-value
     *                                       pairs is "&" symbol. The query name will be separated from query value by "=".
     *                                       Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version                        Optional - Version of the web service to made the call against it.
     *                                       Example: "2016-04-01"
     *                                       Default: "2016-04-01"
     * @param delimiter                      Optional - Delimiter that will be used.
     *                                       Default: ","
     * @param subnetId                       ID of the subnet to associate with the network interface.
     * @param privateIpAddress               Optional - The primary private IP address of the network interface. If you
     *                                       don't specify an IP address, Amazon EC2 selects one for you from the subnet
     *                                       range. If you specify an IP address, you cannot indicate any IP addresses
     *                                       specified in privateIpAddresses as primary (only one IP address can be
     *                                       designated as primary).
     *                                       Default: ""
     * @param privateIpAddressesString       Optional - String that contains one or more private IP addresses separated
     *                                       by <delimiter>
     *                                       Default: ""
     * @param networkInterfaceDescription    Optional - A description for the network interface.
     *                                       Default: ""
     * @param secondaryPrivateIpAddressCount Optional - The number of secondary private IP addresses to assign to a network
     *                                       interface. When you specify a number of secondary IP addresses, Amazon EC2
     *                                       selects these IP addresses within the subnet range. You can't specify this
     *                                       option and specify more than one private IP address using privateIpAddresses.
     *                                       The number of IP addresses you can assign to a network interface varies by
     *                                       instance type. For more information, see Private IP Addresses Per ENI Per
     *                                       Instance Type in the Amazon Elastic Compute Cloud User Guide.
     *                                       http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-eni.html#AvailableIpPerENI
     *                                       Example: "2"
     * @param securityGroupIdsString         Optional - IDs of one or more security groups.
     *                                       Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Network Interface",
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
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = SUBNET_ID, required = true) String subnetId,
                                       @Param(value = PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = PRIVATE_IP_ADDRESSES_STRING) String privateIpAddressesString,
                                       @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                       @Param(value = NETWORK_INTERFACE_DESCRIPTION, required = true) String networkInterfaceDescription,
                                       @Param(value = SECONDARY_PRIVATE_IP_ADDRESS_COUNT) String secondaryPrivateIpAddressCount) {
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
                    .withDelimiter(delimiter)
                    .withAction(CREATE_NETWORK_INTERFACE)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withSubnetId(subnetId).build();

            ElasticIpInputs elasticIpInputs = new ElasticIpInputs.Builder()
                    .withPrivateIpAddress(privateIpAddress)
                    .withPrivateIpAddressesString(privateIpAddressesString)
                    .build();

            IamInputs iamInputs = new IamInputs.Builder().withSecurityGroupIdsString(securityGroupIdsString).build();

            NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfaceDescription(networkInterfaceDescription)
                    .withSecondaryPrivateIpAddressCount(secondaryPrivateIpAddressCount)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, elasticIpInputs, iamInputs, networkInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}