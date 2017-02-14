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
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_NETWORK_INTERFACES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_VALUES_STRING;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.ResponseNames.FAILURE;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class DescribeNetworkInterfacesAction {
    /**
     * Describes one or more of your network interfaces.
     *
     * @param endpoint            Optional - Endpoint to which request will be sent.
     *                            Default: "https://ec2.amazonaws.com"
     * @param identity            ID of the secret access key associated with your Amazon AWS or IAM account.
     *                            Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential          Secret access key associated with your Amazon AWS or IAM account.
     *                            Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost           Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort           Optional - proxy server port. You must either specify values for both proxyHost and
     *                            proxyPort inputs or leave them both empty.
     * @param proxyUsername       Optional - proxy server user name.
     *                            Default: ""
     * @param proxyPassword       Optional - proxy server password associated with the proxyUsername input value.
     * @param version             Optional - Version of the web service to made the call against it.
     *                            Example: "2016-11-15"
     *                            Default: "2016-11-15"
     * @param headers             Optional - string containing the headers to use for the request separated by new line
     *                            (CRLF). The header name-value pair will be separated by ":"
     *                            Format: Conforming with HTTP standard for headers (RFC 2616)
     *                            Examples: "Accept:text/plain"
     *                            Default: ""
     * @param queryParams         Optional - string containing query parameters that will be appended to the URL. The names
     *                            and the values must not be URL encoded because if they are encoded then a double encoded
     *                            will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                            separated from query value by "="
     *                            Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                            Default: ""
     * @param delimiter           Optional - Delimiter that will be used.
     * @param filterNamesString   Optional - String that contains one or more values that represents filters for the search.
     *                            For a complete list of valid filters see: http://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html
     *                            Example: "status,attachment.status"
     *                            Default: ""
     * @param filterValuesString  Optional - String that contains one or more values that represents filters values.
     *                            For a complete list of valid filters see: http://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNetworkInterfaces.html
     *                            Example of filters values for the above filterNamesString input: "in-use|available,attaching|attached|detaching|detached"
     *                            Note that "in-use|available" represents values for "status" and are separated
     *                            by the enforced "|" symbol
     *                            Default (describes all your instances): ""
     * @param networkInterfaceId  Optional - String that contains one or more network interface IDs.
     *                            Example: "eni-12345678,eni-87654321"
     *                            Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Describe Network Interfaces",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
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
                                       @Param(value = FILTER_NAMES_STRING) String filterNamesString,
                                       @Param(value = FILTER_VALUES_STRING) String filterValuesString,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId) {
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
                    .withAction(DESCRIBE_NETWORK_INTERFACES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfaceId(networkInterfaceId)
                    .build();

            final InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withFilterNamesString(filterNamesString)
                    .withFilterValuesString(filterValuesString)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, networkInputs, instanceInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
