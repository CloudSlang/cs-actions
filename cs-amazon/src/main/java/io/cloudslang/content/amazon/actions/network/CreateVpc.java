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
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.OutputNames;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.CREATE_VPC;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.TENANCY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.AMAZON_PROVIDED_IPV6_CIDR_BLOCK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.CIDR_BLOCK;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

/**
 * Created by TusaM
 * 3/13/2017.
 */
public class CreateVpc {
    /**
     * Creates a VPC with the specified IPv4 CIDR block.
     * Note: The smallest VPC you can create uses a /28 netmask (16 IPv4 addresses), and the largest uses a /16 netmask
     * (65,536 IPv4 addresses). To help you decide how big to make your VPC, see Your VPC and Subnets in the Amazon Virtual
     * Private Cloud User Guide: http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_Subnets.html
     * You can optionally request an Amazon-provided IPv6 CIDR block for the VPC. The IPv6 CIDR block uses a /56 prefix
     * length, and is allocated from Amazon's pool of IPv6 addresses. You cannot choose the IPv6 range for your VPC.
     * By default, each instance you launch in the VPC has the default DHCP options, which includes only a default DNS
     * server that we provide (AmazonProvidedDNS). For more information about DHCP options, see DHCP Options Sets:
     * http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/VPC_DHCP_Options.html
     * You can specify the instance tenancy value for the VPC when you create it. You can't change this value for the VPC
     * after you create it. For more information, see Dedicated Instances: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/dedicated-instance.html
     * <p>
     * Important: This is an idempotent operation. If you perform the operation more than once, Amazon EC2 doesn't return
     * an error.
     *
     * @param endpoint                    Optional - Endpoint to which request will be sent.
     *                                    Default: "https://ec2.amazonaws.com"
     * @param identity                    ID of the secret access key associated with your Amazon AWS or IAM account.
     *                                    Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                  Secret access key associated with your Amazon AWS or IAM account.
     *                                    Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                   Optional - proxy server used to connect to Amazon API. If empty no proxy will
     *                                    be used.
     *                                    Default: ""
     * @param proxyPort                   Optional - proxy server port. You must either specify values for both proxyHost
     *                                    and proxyPort inputs or leave them both empty.
     *                                    Default: ""
     * @param proxyUsername               Optional - proxy server user name.
     *                                    Default: ""
     * @param proxyPassword               Optional - proxy server password associated with the proxyUsername input value.
     *                                    Default: ""
     * @param headers                     Optional - string containing the headers to use for the request separated by
     *                                    new line (CRLF). The header name-value pair will be separated by ":".
     *                                    Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                    Examples: "Accept:text/plain"
     *                                    Default: ""
     * @param queryParams                 Optional - string containing query parameters that will be appended to the URL.
     *                                    The names and the values must not be URL encoded because if they are encoded
     *                                    then a double encoded will occur. The separator between name-value pairs is "&"
     *                                    symbol. The query name will be separated from query value by "=".
     *                                    Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                    Default: ""
     * @param version                     Optional - Version of the web service to made the call against it.
     *                                    Example: "2016-11-15"
     *                                    Default: "2016-11-15"
     * @param amazonProvidedIpv6CidrBlock Optional - Requests an Amazon-provided IPv6 CIDR block with a /56 prefix length
     *                                    for the VPC. You cannot specify the range of IP addresses, or the size of the
     *                                    CIDR block.
     *                                    Valid values: "true", "false"
     *                                    Default: "false"
     * @param cidrBlock                   IPv4 network range for the VPC, in CIDR notation.
     *                                    Example: "10.0.0.0/16"
     *                                    Default: "10.0.0.0/28"
     * @param tenancy                     Optional - tenancy options for instances launched into the VPC. For default,
     *                                    instances are launched with shared tenancy by default. You can launch instances
     *                                    with any tenancy into a shared tenancy VPC. For dedicated, instances are launched
     *                                    as dedicated tenancy instances by default. You can only launch instances with
     *                                    a tenancy of dedicated or host into a dedicated tenancy VPC.
     *                                    Important: The "host" value cannot be used with this parameter. Use the "default"
     *                                    or "dedicated" values only.
     *                                    Valid values: "default", "dedicated"
     *                                    Default: "default"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Vpc",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
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
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = VERSION) String version,
                                       @Param(value = AMAZON_PROVIDED_IPV6_CIDR_BLOCK) String amazonProvidedIpv6CidrBlock,
                                       @Param(value = CIDR_BLOCK) String cidrBlock,
                                       @Param(value = TENANCY) String tenancy) {
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
                    .withAction(CREATE_VPC)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withTenancy(tenancy)
                    .build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withAmazonProvidedIpv6CidrBlock(amazonProvidedIpv6CidrBlock)
                    .withCidrBlock(cidrBlock)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, instanceInputs, networkInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}