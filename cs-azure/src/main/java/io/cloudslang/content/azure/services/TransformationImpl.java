/*
 * (c) Copyright 2021 Micro Focus, L.P.
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
package io.cloudslang.content.azure.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.azure.entities.CreateStreamingJobInputs;
import io.cloudslang.content.azure.entities.CreateTransformationInputs;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingJobRequestBody;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateTransformationRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.*;
import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.CreateTransformationsConstants.TRANSFORMATION_JOBS_PATH;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class TransformationImpl {

    @NotNull
    public static Map<String, String> createTransformation(@NotNull final CreateTransformationInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateTransformationUrl(inputs.getAzureCommonInputs().getSubscriptionId(), inputs.getAzureCommonInputs().getResourceGroupName(),
                inputs.getAzureCommonInputs().getJobName(),inputs.getTransformationName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(inputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(createTransformationRequestBody(inputs));
        return new HttpClientService().execute(httpClientInputs);
    }
    @NotNull
    private static String getCreateTransformationUrl(String subscriptionId, String resourceGroupName, String jobName, String transformationName) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(getTransformationURLPath(subscriptionId, resourceGroupName, jobName,transformationName).toString());
        return uriBuilder.build().toURL().toString();
    }

    private static StringBuilder getTransformationURLPath(String subscriptionId, String resourceGroupName, String jobName, String transformationName) {
        return new StringBuilder()
                .append(DEFAULT_RESOURCE)
                .append(SUBSCRIPTION_PATH)
                .append(subscriptionId)
                .append(RESOURCE_GROUPS_PATH)
                .append(resourceGroupName)
                .append(STREAM_ANALYTICS_PATH)
                .append(STREAMING_JOBS_PATH)
                .append(jobName)
                .append(TRANSFORMATION_JOBS_PATH)
                .append(transformationName);
    }

    private static String createTransformationRequestBody(@NotNull final CreateTransformationInputs inputs) throws Exception {
        CreateTransformationRequestBody createTransformationRequestBody = new CreateTransformationRequestBody();


        CreateTransformationRequestBody.Properties properties = new CreateTransformationRequestBody.Properties();


        properties.setQuery(inputs.getQuery());
        properties.setStreamingUnits(inputs.getStreamingUnits());

        createTransformationRequestBody.setProperties(properties);
        ObjectMapper createInstanceMapper = new ObjectMapper();
        return createInstanceMapper.writeValueAsString(createTransformationRequestBody);

    }
}
