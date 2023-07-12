/*
 * Copyright 2023 Open Text
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



package io.cloudslang.content.azure.services;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.azure.entities.AzureCommonInputs;
import io.cloudslang.content.azure.entities.CreateStreamingJobInputs;
import io.cloudslang.content.azure.entities.GetStreamingJobInputs;
import io.cloudslang.content.azure.entities.StartStreamingJobInputs;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingJobRequestBody;
import io.cloudslang.content.azure.entities.models.streamanalytics.StartStreamingJobRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.ANONYMOUS;
import static io.cloudslang.content.azure.utils.Constants.Common.CONTENT_TYPE;
import static io.cloudslang.content.azure.utils.Constants.Common.EMPTY_JSON;
import static io.cloudslang.content.azure.utils.Constants.Common.GET;
import static io.cloudslang.content.azure.utils.Constants.Common.POST;
import static io.cloudslang.content.azure.utils.Constants.Common.PUT;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_RESOURCE;
import static io.cloudslang.content.azure.utils.Constants.RESOURCE_GROUPS_PATH;
import static io.cloudslang.content.azure.utils.Constants.STREAMING_JOBS_PATH;
import static io.cloudslang.content.azure.utils.Constants.STREAM_ANALYTICS_PATH;
import static io.cloudslang.content.azure.utils.Constants.SUBSCRIPTION_PATH;
import static io.cloudslang.content.azure.utils.Constants.StartStreamingJobConstants.START_STREAMING_JOB_PATH;
import static io.cloudslang.content.azure.utils.Constants.StopStreamingJobConstants.STOP_STREAMING_JOB_PATH;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class StreamingJobImpl {

    @NotNull
    public static Map<String, String> createStreamingJob(@NotNull final CreateStreamingJobInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateStreamingJobUrl(inputs.getAzureCommonInputs().getSubscriptionId(), inputs.getAzureCommonInputs().getResourceGroupName(),
                inputs.getAzureCommonInputs().getJobName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(inputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(createStreamingJobRequestBody(inputs));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> startStreamingJob(@NotNull final StartStreamingJobInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getStartStreamingJobUrl(inputs.getAzureCommonInputs().getSubscriptionId(), inputs.getAzureCommonInputs().getResourceGroupName(),
                inputs.getAzureCommonInputs().getJobName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion(inputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(StartStreamingJobRequestBody(inputs));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> stopStreamingJob(@NotNull final AzureCommonInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getStopStreamingJobUrl(inputs.getSubscriptionId(), inputs.getResourceGroupName(),
                inputs.getJobName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs);
        httpClientInputs.setBody(EMPTY_JSON);
        httpClientInputs.setQueryParams(setAPIVersion(inputs.getApiVersion()));

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getStreamingJob(@NotNull final GetStreamingJobInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateStreamingJobUrl(inputs.getAzureCommonInputs().getSubscriptionId(), inputs.getAzureCommonInputs().getResourceGroupName(),
                inputs.getAzureCommonInputs().getJobName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(inputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        if (!isEmpty(inputs.getExpand())) {
            httpClientInputs.setQueryParams(setAPIVersion(inputs.getAzureCommonInputs().getApiVersion(), inputs.getExpand()));
        } else {
            httpClientInputs.setQueryParams(setAPIVersion(inputs.getAzureCommonInputs().getApiVersion()));
        }


        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getCreateStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(getStreamURLPath(subscriptionId, resourceGroupName, jobName).toString());
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
       // uriBuilder.setPath(getStreamURLPath(subscriptionId, resourceGroupName, jobName).toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getStartStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(startStreamingJobPath(subscriptionId, resourceGroupName, jobName));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
       // uriBuilder.setPath(startStreamingJobPath(subscriptionId, resourceGroupName, jobName));
        return uriBuilder.build().toURL().toString();
    }


    @NotNull
    private static String getStopStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder(stopStreamingJobPath(subscriptionId, resourceGroupName, jobName));
        //from httpclient 4.5.13 the setPath method is adding one extra / at the start of the URI instead it can be given directly to the constructor
       // uriBuilder.setPath(stopStreamingJobPath(subscriptionId, resourceGroupName, jobName));
        return uriBuilder.build().toURL().toString();
    }

    private static StringBuilder getStreamURLPath(String subscriptionId, String resourceGroupName, String jobName) {
        return new StringBuilder()
                .append(DEFAULT_RESOURCE)
                .append(SUBSCRIPTION_PATH)
                .append(subscriptionId)
                .append(RESOURCE_GROUPS_PATH)
                .append(resourceGroupName)
                .append(STREAM_ANALYTICS_PATH)
                .append(STREAMING_JOBS_PATH)
                .append(jobName);
    }

    @NotNull
    private static String startStreamingJobPath(String subscriptionId, String resourceGroupName, String jobName) {
        return getStreamURLPath(subscriptionId, resourceGroupName, jobName).append(START_STREAMING_JOB_PATH).toString();
    }


    @NotNull
    private static String stopStreamingJobPath(String subscriptionId, String resourceGroupName, String jobName) {
        return getStreamURLPath(subscriptionId, resourceGroupName, jobName).append(STOP_STREAMING_JOB_PATH).toString();
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
        properties.setEventsLateArrivalMaxDelayInSeconds(Integer.parseInt(inputs.getEventsLateArrivalMaxDelayInSeconds()));
        properties.setEventsOutOfOrderMaxDelayInSeconds(Integer.parseInt(inputs.getEventsOutOfOrderMaxDelayInSeconds()));
        properties.setEventsOutOfOrderPolicy(inputs.getEventsOutOfOrderPolicy());
        properties.setOutputErrorPolicy(inputs.getOutputErrorPolicy());
        properties.setSku(sku);
        if (!isEmpty(inputs.getTags())) {
            createStreamingJobRequestBody.setTags(stringToJSON(jsonParser, inputs.getTags()));
        }
        createStreamingJobRequestBody.setProperties(properties);

        ObjectMapper createInstanceMapper = new ObjectMapper();
        createInstanceMapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);

        return createInstanceMapper.writeValueAsString(createStreamingJobRequestBody);

    }

    private static String StartStreamingJobRequestBody(@NotNull final StartStreamingJobInputs inputs) throws Exception {
        StartStreamingJobRequestBody startStreamingJobRequestBody = new StartStreamingJobRequestBody();
        startStreamingJobRequestBody.setOutputStartMode(inputs.getOutputStartMode());
        if (!isEmpty(inputs.getOutputStartTime())) {
            startStreamingJobRequestBody.setOutputStartTime(inputs.getOutputStartTime());
        }

        ObjectMapper createInstanceMapper = new ObjectMapper();
        createInstanceMapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);

        return createInstanceMapper.writeValueAsString(startStreamingJobRequestBody);
    }

    private static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!isEmpty(property))
            return (JSONObject) jsonParser.parse(property);
        return new JSONObject();
    }

}
