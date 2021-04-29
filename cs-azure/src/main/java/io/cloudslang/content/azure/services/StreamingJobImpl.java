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
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingJobRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.*;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class StreamingJobImpl {

    @NotNull
    public static Map<String, String> createStreamingJob(@NotNull final CreateStreamingJobInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateStreamingJobUrl(inputs.getAzureCommonInputs().getSubscriptionId(), inputs.getAzureCommonInputs().getResourceGroupName(),
                inputs.getJobName(), inputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion());
        httpClientInputs.setBody(createStreamingJobRequestBody(inputs));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getCreateStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName, String apiVersion) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(createStreamingJobPath(subscriptionId, resourceGroupName, jobName, apiVersion));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String createStreamingJobPath(String subscriptionId, String resourceGroupName, String jobName, String apiVersion) {
        StringBuilder pathString = new StringBuilder()
                .append(DEFAULT_RESOURCE)
                .append(SUBSCRIPTION_PATH)
                .append(subscriptionId)
                .append(RESOURCE_GROUPS_PATH)
                .append(resourceGroupName)
                .append(STREAM_ANALYTICS_PATH)
                .append(STREAMING_JOBS_PATH)
                .append(jobName);
        return pathString.toString();
    }

    private static String createStreamingJobRequestBody(@NotNull final CreateStreamingJobInputs inputs) throws Exception {
        CreateStreamingJobRequestBody createStreamingJobRequestBody = new CreateStreamingJobRequestBody();
        createStreamingJobRequestBody.setLocation(inputs.getLocation());
        JSONParser jsonParser = new JSONParser();
        CreateStreamingJobRequestBody.Properties properties = new CreateStreamingJobRequestBody.Properties();
        CreateStreamingJobRequestBody.SKU sku = new CreateStreamingJobRequestBody.SKU();
        sku.setName(inputs.getSkuName());
        properties.setCompatibilityLevel(inputs.getCompatibilityLevel());
        properties.setDataLocale(inputs.getDataLocale());
        properties.setEventsLateArrivalMaxDelayInSeconds(Integer.parseInt(inputs.getEventsOutOfOrderMaxDelayInSeconds()));
        properties.setEventsOutOfOrderMaxDelayInSeconds(Integer.parseInt(inputs.getEventsOutOfOrderMaxDelayInSeconds()));
        properties.setEventsOutOfOrderPolicy(inputs.getEventsOutOfOrderPolicy());
        properties.setOutputErrorPolicy(inputs.getOutputErrorPolicy());
        properties.setSku(sku);
        createStreamingJobRequestBody.setTags(stringToJSON(jsonParser, inputs.getTags()));
        createStreamingJobRequestBody.setProperties(properties);
        ObjectMapper createInstanceMapper = new ObjectMapper();
        return createInstanceMapper.writeValueAsString(createStreamingJobRequestBody);

    }

    private static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!isEmpty(property))
            return (JSONObject) jsonParser.parse(property);
        return new JSONObject();
    }

}
