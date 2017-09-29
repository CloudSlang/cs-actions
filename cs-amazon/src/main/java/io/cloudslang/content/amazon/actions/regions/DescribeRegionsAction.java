/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.actions.regions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.REGIONS_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_REGIONS;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_FILTERS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.REGIONS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_FILTERS_STRING;

/**
 * Created by persdana
 * 6/23/2015.
 */
public class DescribeRegionsAction {
    /**
     * Describes one or more regions that are currently available to you.
     * Note: For a list of the regions supported by Amazon EC2, see Regions and Endpoints:
     * http://docs.aws.amazon.com/general/latest/gr/rande.html#ec2_region
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
     * @param headers            Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                           The header name-value pair will be separated by ":".
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: "Accept:text/plain"
     *                           Default: ""
     * @param queryParams        Optional - string containing query parameters that will be appended to the URL. The names
     *                           and the values must not be URL encoded because if they are encoded then a double encoded
     *                           will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                           separated from query value by "=".
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                           Default: ""
     * @param version            Optional - Version of the web service to made the call against it.
     *                           Example: "2016-11-15"
     *                           Default: "2016-11-15"
     * @param delimiter          Optional - delimiter that will be used
     *                           Default: ","
     * @param keyFiltersString   Optional - String that contains one or more filter keys separated by delimiter.
     *                           Example: "endpoint", "region-name"
     *                           Default: ""
     * @param valueFiltersString Optional - String that contains one or more filter values separated by delimiter.
     *                           Example: "ec2.eu-central-1.amazonaws.com", "us-east-1"
     *                           Default: ""
     * @param regionsString      String that contains one or more regions.
     *                           Example: "us-east-1,eu-central-1"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *         operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Regions",
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
                                       @Param(value = KEY_FILTERS_STRING, required = true) String keyFiltersString,
                                       @Param(value = VALUE_FILTERS_STRING, required = true) String valueFiltersString,
                                       @Param(value = REGIONS_STRING, required = true) String regionsString) {
        try {
            version = getDefaultStringInput(version, REGIONS_DEFAULT_API_VERSION);

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
                    .withAction(DESCRIBE_REGIONS)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withKeyFiltersString(keyFiltersString)
                    .withValueFiltersString(valueFiltersString)
                    .withRegionsString(regionsString)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
