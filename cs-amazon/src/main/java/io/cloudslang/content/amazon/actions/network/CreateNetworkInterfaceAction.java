/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.ElasticIpInputs;
import io.cloudslang.content.amazon.entities.inputs.IamInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.CREATE_NETWORK_INTERFACE;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SECONDARY_PRIVATE_IP_ADDRESS_COUNT;
import static io.cloudslang.content.amazon.entities.constants.Outputs.NETWORK_INTERFACE_ID_RESULT;
import static io.cloudslang.content.amazon.utils.OutputsUtil.putResponseIn;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class CreateNetworkInterfaceAction {
    private static final String NETWORK_INTERFACE_ID_X_PATH_QUERY = "/CreateNetworkInterfaceResponse/networkInterface/networkInterfaceId";
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
     * @param proxyPort                      Optional - proxy server port. You must either specify values for both proxyHost
     *                                       and proxyPort inputs or leave them both empty.
     * @param proxyUsername                  Optional - proxy server user name.
     * @param proxyPassword                  Optional - proxy server password associated with the proxyUsername input
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
     *                                       Example: "2016-11-15"
     *                                       Default: "2016-11-15"
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
     *                                       by delimiter
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
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Network Interface",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(NETWORK_INTERFACE_ID_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
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
                                       @Param(value = NETWORK_INTERFACE_ID_RESULT, required = true) String subnetId,
                                       @Param(value = PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = PRIVATE_IP_ADDRESSES_STRING) String privateIpAddressesString,
                                       @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                       @Param(value = NETWORK_INTERFACE_DESCRIPTION, required = true) String networkInterfaceDescription,
                                       @Param(value = SECONDARY_PRIVATE_IP_ADDRESS_COUNT) String secondaryPrivateIpAddressCount) {
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
                    .withDelimiter(delimiter)
                    .withAction(CREATE_NETWORK_INTERFACE)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder().withSubnetId(subnetId).build();

            final ElasticIpInputs elasticIpInputs = new ElasticIpInputs.Builder()
                    .withPrivateIpAddress(privateIpAddress)
                    .withPrivateIpAddressesString(privateIpAddressesString)
                    .build();

            final IamInputs iamInputs = new IamInputs.Builder().withSecurityGroupIdsString(securityGroupIdsString).build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfaceDescription(networkInterfaceDescription)
                    .withSecondaryPrivateIpAddressCount(secondaryPrivateIpAddressCount)
                    .build();

            Map<String, String> queryMapResult = new QueryApiExecutor().execute(commonInputs, customInputs, elasticIpInputs,
                    iamInputs, networkInputs);

            if ((ReturnCodes.SUCCESS).equals(queryMapResult.get(RETURN_CODE))) {
                putResponseIn(queryMapResult, NETWORK_INTERFACE_ID_RESULT, NETWORK_INTERFACE_ID_X_PATH_QUERY);
            }

            return queryMapResult;
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
