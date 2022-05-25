/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.amazon.actions.keypairs;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.DESCRIBE_KEYPAIR_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_KEYPAIRS;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.DESCRIBE_KEYPAIRS_FILTER_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.DESCRIBE_KEYPAIRS_FILTER_VALUES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_RESULTS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.NEXT_TOKEN;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * 20/05/2022 (DD/MM/YYYY).
 */

public class DescribeKeyPairsAction {
    /**
     * Describes the specified key pairs or all of your key pairs.
     *
     * @param endpoint           Endpoint to which request will be sent.
     *                           Default: "https://ec2.amazonaws.com"
     * @param identity           ID of the secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential         Secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     *                           all the subnets are described.
     * @param proxyHost          Optional - proxy server used to connect to Amazon API. If empty no proxy
     *                           will be used.
     *                           Default: ""
     * @param proxyPort          Optional - proxy server port. You must either specify values for both
     *                           proxyHost and proxyPort inputs or leave them both empty.
     *                           Default: ""
     * @param proxyUsername      Optional - proxy server username.
     *                           Default: ""
     * @param proxyPassword      Optional - proxy server password associated with the proxyUsername
     *                           input value.
     *                           Default: ""
     * @param headers            Optional - string containing the headers to use for the request separated
     *                           by new line (CRLF). The header name-value pair will be separated by ":".
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: "Accept:text/plain"
     *                           Default: ""
     * @param queryParams        Optional - string containing query parameters that will be appended to
     *                           the URL. The names and the values must not be URL encoded because if
     *                           they are encoded then a double encoded will occur. The separator between
     *                           name-value pairs is "&" symbol. The query name will be separated from
     *                           query value by "=".
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                           Default: ""
     * @param version            Optional - Version of the web service to made the call against it.
     *                           Example: "2016-11-15"
     *                           Default: ""
     * @param delimiter          Optional - Delimiter that will be used.
     * @param filterNamesString  Optional - String that contains one or more values that represents filters for
     *                           the search.
     *                           Example: "key-pair-id,key-name,fingerprint ",
     *                           Default: ""
     * @param filterValuesString Optional - String that contains one or more values that represents filters values.
     *                           Default: ""
     * @param maxResults         Optional - The maximum number of results to return in a single call. To retrieve the
     *                           remaining results, make another call with the returned NextToken value. This value can
     *                           be between 5 and 1000. You cannot specify this parameter and the instance IDs parameter
     *                           or tag filters in the same call.
     *                           Default: ""
     * @param nextToken          Optional - The token to use to retrieve the next page of results. This value is null when
     *                           there are no more results to return.
     *                           Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe KeyPairs",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
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
                                       @Param(value = DESCRIBE_KEYPAIRS_FILTER_NAMES_STRING) String filterNamesString,
                                       @Param(value = DESCRIBE_KEYPAIRS_FILTER_VALUES_STRING) String filterValuesString,
                                       @Param(value = MAX_RESULTS) String maxResults,
                                       @Param(value = NEXT_TOKEN) String nextToken) {

        try {
            version = getDefaultStringInput(version, DESCRIBE_KEYPAIR_DEFAULT_API_VERSION);

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
                    .withAction(DESCRIBE_KEYPAIRS)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withKeyFiltersString(filterNamesString)
                    .withValueFiltersString(filterValuesString)
                    .build();

            final FilterInputs filterInputs = new FilterInputs.Builder()
                    .withMaxResults(maxResults)
                    .withNextToken(nextToken)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, filterInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
