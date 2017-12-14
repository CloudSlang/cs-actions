/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.amazon.actions.tags;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.FilterInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.aws.TagFilter.KEY;
import static io.cloudslang.content.amazon.entities.aws.TagFilter.RESOURCE_ID;
import static io.cloudslang.content.amazon.entities.aws.TagFilter.RESOURCE_TYPE;
import static io.cloudslang.content.amazon.entities.aws.TagFilter.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.TAGS_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_TAGS;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_RESULTS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.NEXT_TOKEN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.TagsInputs.FILTER_KEY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.TagsInputs.FILTER_RESOURCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.TagsInputs.FILTER_RESOURCE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.TagsInputs.FILTER_VALUE;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.tuple.ImmutablePair.of;

/**
 * Created by Tirla Alin.
 * 9/5/2016.
 */
public class DescribeTagsAction {

    /**
     * Describes one or more of the tags for your EC2 resources.
     *
     * @param endpoint                              Optional - Endpoint to which request will be sent.
     *                                              Default: "https://ec2.amazonaws.com"
     * @param identity                              ID of the secret access key associated with your Amazon AWS or
     *                                              IAM account.
     *                                              Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                            Secret access key associated with your Amazon AWS or IAM account.
     *                                              Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                             Optional - proxy server used to connect to Amazon API. If empty no
     *                                              proxy will be used.
     * @param proxyPort                             Optional - proxy server port. You must either specify values for both
     *                                              proxyHost and proxyPort inputs or leave them both empty.
     * @param proxyUsername                         Optional - proxy server user name.
     *                                              Default: ""
     * @param proxyPassword                         Optional - proxy server password associated with the proxyUsername
     *                                              input value.
     * @param version                               Optional - Version of the web service to made the call against it.
     *                                              Example: "2016-11-15"
     *                                              Default: "2016-11-15"
     * @param headers                               Optional - string containing the headers to use for the request
     *                                              separated by new line (CRLF). The header name-value pair will be
     *                                              separated by ":"
     *                                              Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                              Examples: "Accept:text/plain"
     *                                              Default: ""
     * @param queryParams                           Optional - string containing query parameters that will be appended
     *                                              to the URL. The names and the values must not be URL encoded because
     *                                              if they are encoded then a double encoded will occur. The separator
     *                                              between name-value pairs is "&" symbol. The query name will be
     *                                              separated from query value by "="
     *                                              Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                              Default: ""
     * @param delimiter                             Optional - Delimiter that will be used between filter values.
     *                                              Default: ","
     * @param filterKey                             Optional - The tag key.
     *                                              Default: ""
     * @param filterResourceId                      Optional - The resource ID.
     *                                              Default: ""
     * @param filterResourceType                    Optional - The resource type.
     *                                              Valid values: customer-gateway, dhcp-options, image, instance,
     *                                              internet-gateway, network-acl, network-interface, reserved-instances,
     *                                              route-table, security-group, snapshot, spot-instances-request,
     *                                              subnet, volume, vpc, vpn-connection, vpn-gateway.
     *                                              Default: ""
     * @param filterValue                           Optional - The tag value.
     *                                              Default: ""
     * @param maxResults                            Optional - The maximum number of results to return in a single call.
     *                                              This value can be between 5 and 1000. To retrieve the remaining
     *                                              results, make another call with the returned NextToken value.
     *                                              Default: ""
     * @param nextToken                             Optional - The token to retrieve the next page of results.
     *                                              Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Describe Tags",
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
                                       @Param(value = FILTER_KEY) String filterKey,
                                       @Param(value = FILTER_RESOURCE_ID) String filterResourceId,
                                       @Param(value = FILTER_RESOURCE_TYPE) String filterResourceType,
                                       @Param(value = FILTER_VALUE) String filterValue,
                                       @Param(value = MAX_RESULTS) String maxResults,
                                       @Param(value = NEXT_TOKEN) String nextToken) {
        try {
            version = getDefaultStringInput(version, TAGS_DEFAULT_API_VERSION);

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
                    .withAction(DESCRIBE_TAGS)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final List<ImmutablePair<String, String>> filterPairs = Arrays.asList(
                    of(KEY, filterKey),
                    of(RESOURCE_ID, filterResourceId),
                    of(RESOURCE_TYPE, filterResourceType),
                    of(VALUE, filterValue)
            );

            final FilterInputs.Builder filterInputsBuilder = new FilterInputs.Builder()
                    .withDelimiter(commonInputs.getDelimiter())
                    .withMaxResults(maxResults)
                    .withNextToken(nextToken);

            for (ImmutablePair<String, String> filterPair : filterPairs) {
                if (isNotEmpty(filterPair.getRight())) {
                    filterInputsBuilder.withNewFilter(filterPair.getLeft(), filterPair.getRight());
                }
            }

            final FilterInputs filterInputs = filterInputsBuilder.build();

            return new QueryApiExecutor().execute(commonInputs, filterInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
