/*
 * Copyright 2019-2023 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.amazon.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.ImageInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.*;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.IMAGES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_IMAGES;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PLATFORM;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ROOT_DEVICE_TYPE;

/**
 * Created by Mihai Tusa.
 * 5/6/2016.
 */
public class DescribeImagesAction {
    /**
     * Describes one or more of the images (AMIs, AKIs, and ARIs) available to you. Images available to you include
     * public images, private images that you own, and private images owned by other AWS accounts but for which you have
     * explicit launch permissions.
     * Note:
     * De-registered images are included in the returned results for an unspecified interval after de-registration.
     *
     * @param endpoint                     Optional - Endpoint to which request will be sent.- Example: "https://ec2.amazonaws.com"
     * @param identity                     Username of your account or the Access Key ID.
     * @param credential                   Password of the user or the Secret Access Key that correspond to
     *                                     the identity input.
     * @param owners                       Scopes the results to images with the specified owners. You can specify a combination of AWS
     *                                     account IDs, self, amazon, and aws-marketplace. If you omit this parameter, the results
     *                                     include all images for which you have launch permissions, regardless of ownership.
     * @param executableBy                 Scopes the images by users with explicit launch permissions. Specify an AWS account ID,
     *                                     self (the sender of the request), or all (public AMIs).
     * @param proxyHost                    Optional - Proxy server used to access the web site. If empty no proxy will be
     *                                     used.
     * @param proxyPort                    Optional - Proxy server port - Default: "8080"
     * @param proxyUsername                Optional - proxy server user name.
     * @param proxyPassword                Optional - proxy server password associated with the proxyUsername input value.
     * @param headers                      Optional - string containing the headers to use for the request separated by
     *                                     new line (CRLF).
     *                                     The header name-value pair will be separated by ":".
     *                                     Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                     Examples: "Accept:text/plain"
     * @param queryParams                  Optional - string containing query parameters that will be appended to the URL.
     *                                     The names and the values must not be URL encoded because if they are encoded
     *                                     then a double encoded will occur. The separator between name-value pairs is
     *                                     "&" symbol. The query name will be separated from query value by "=".
     *                                     Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version                      Optional - Version of the web service to made the call against it.
     *                                     Example: "2016-04-01"
     *                                     Default: "2016-04-01"
     * @param delimiter                    Optional - Delimiter that will be used - Default: ","
     * @param platform                     Optional - platform used. Use "windows" if you have Windows instances; otherwise,
     *                                     use "others". Valid values: "", "windows".
     * @param rootDeviceType               Optional - type of root device that the instance uses.
     *                                     Valid values: "ebs", "instance-store".
     * @param type                         Optional - Image type - Valid values: "machine", "kernel", "ramdisk".
     * @param state                        Optional - State of the image - Valid values: "available", "pending", "failed".
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *         operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Images in Region",
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
                                       @Param(value = OWNERS) String owners,
                                       @Param(value = EXECUTABLE_BY) String executableBy,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = PLATFORM) String platform,
                                       @Param(value = ROOT_DEVICE_TYPE) String rootDeviceType,
                                       @Param(value = TYPE) String type,
                                       @Param(value = STATE) String state) {
        try {
            version = getDefaultStringInput(version, IMAGES_DEFAULT_API_VERSION);
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
                    .withAction(DESCRIBE_IMAGES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withPlatform(platform)
                    .withRootDeviceType(rootDeviceType)
                    .build();

            final ImageInputs imageInputs = new ImageInputs.Builder()
                    .withOwners(owners)
                    .withExecutableBy(executableBy)
                    .withType(type)
                    .withState(state)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
