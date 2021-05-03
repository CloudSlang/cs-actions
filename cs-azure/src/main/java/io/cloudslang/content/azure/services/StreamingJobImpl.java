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
import io.cloudslang.content.azure.entities.CreateStreamingInputJobInputs;
import io.cloudslang.content.azure.entities.CreateStreamingJobInputs;
import io.cloudslang.content.azure.entities.CreateStreamingOutputJobInputs;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingInputJobRequestBody;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingJobRequestBody;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingOutputJobRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
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
    private static String getCreateOutputStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName, String outputName, String apiVersion) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(createStreamingOutputJobPath(subscriptionId, resourceGroupName, jobName, outputName, apiVersion));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getCreateInputStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName, String inputName, String apiVersion) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(createStreamingInputJobPath(subscriptionId, resourceGroupName, jobName, inputName, apiVersion));
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

    @NotNull
    private static String createStreamingOutputJobPath(String subscriptionId, String resourceGroupName, String jobName, String outputName, String apiVersion) {
        StringBuilder pathString = new StringBuilder()
                .append(DEFAULT_RESOURCE)
                .append(SUBSCRIPTION_PATH)
                .append(subscriptionId)
                .append(RESOURCE_GROUPS_PATH)
                .append(resourceGroupName)
                .append(STREAM_ANALYTICS_PATH)
                .append(STREAMING_JOBS_PATH)
                .append(jobName)
                .append(OUTPUTS_JOBS_PATH)
                .append(outputName);
        return pathString.toString();

    }

    @NotNull
    private static String createStreamingInputJobPath(String subscriptionId, String resourceGroupName, String jobName, String inputName, String apiVersion) {
        StringBuilder pathString = new StringBuilder()
                .append(DEFAULT_RESOURCE)
                .append(SUBSCRIPTION_PATH)
                .append(subscriptionId)
                .append(RESOURCE_GROUPS_PATH)
                .append(resourceGroupName)
                .append(STREAM_ANALYTICS_PATH)
                .append(STREAMING_JOBS_PATH)
                .append(jobName)
                .append(INPUTS_JOBS_PATH)
                .append(inputName);
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

    private static String createStreamingOutputJobRequestBody(@NotNull final CreateStreamingOutputJobInputs inputs) throws Exception {
        CreateStreamingOutputJobRequestBody createStreamingOutputJobRequestBody = new CreateStreamingOutputJobRequestBody();
        CreateStreamingOutputJobRequestBody.Properties properties = new CreateStreamingOutputJobRequestBody.Properties();
        CreateStreamingOutputJobRequestBody.Datasource datasource = new CreateStreamingOutputJobRequestBody.Datasource();
        CreateStreamingOutputJobRequestBody.Serialization serialization = new CreateStreamingOutputJobRequestBody.Serialization();
        CreateStreamingOutputJobRequestBody.SerializationProperties serializationprop = new CreateStreamingOutputJobRequestBody.SerializationProperties();
        CreateStreamingOutputJobRequestBody.Datasource.SubProperties.StorageAccounts storage = new CreateStreamingOutputJobRequestBody.Datasource.SubProperties.StorageAccounts();
        CreateStreamingOutputJobRequestBody.Datasource.SubProperties subproperties = new CreateStreamingOutputJobRequestBody.Datasource.SubProperties();
        datasource.setType(SET_TYPE);
        properties.setDatasource(datasource);
        storage.setAccountName(inputs.getAccountName());
        storage.setAccountKey(inputs.getAccountKey());
        JSONArray json = new JSONArray();
        json.add(storage);
        subproperties.setStrogeaccounts(json);
        datasource.setProperties(subproperties);
        subproperties.setContainer(CONTAINER);
        subproperties.setPathPattern(PATH_PATTERN);
        serializationprop.setFieldDelimiter(FIELD_DELIMETER);
        serializationprop.setEncoding(ENCODING);
        serialization.setType(TYPE);
        serialization.setProperties(serializationprop);
        properties.setSerialization(serialization);
        createStreamingOutputJobRequestBody.setProperties(properties);
        ObjectMapper createInstanceMapper = new ObjectMapper();
        return createInstanceMapper.writeValueAsString(createStreamingOutputJobRequestBody);

    }

    private static String createStreamingInputJobRequestBody(@NotNull final CreateStreamingInputJobInputs inputs) throws Exception {
        CreateStreamingInputJobRequestBody createStreamingOutputJobRequestBody = new CreateStreamingInputJobRequestBody();
        CreateStreamingInputJobRequestBody.Properties properties = new CreateStreamingInputJobRequestBody.Properties();
        CreateStreamingInputJobRequestBody.Datasource datasource = new CreateStreamingInputJobRequestBody.Datasource();
        CreateStreamingInputJobRequestBody.Serialization serialization = new CreateStreamingInputJobRequestBody.Serialization();
        CreateStreamingInputJobRequestBody.SerializationProperties serializationprop = new CreateStreamingInputJobRequestBody.SerializationProperties();
        CreateStreamingInputJobRequestBody.Datasource.SubProperties.StorageAccounts storage = new CreateStreamingInputJobRequestBody.Datasource.SubProperties.StorageAccounts();
        CreateStreamingInputJobRequestBody.Datasource.SubProperties subproperties = new CreateStreamingInputJobRequestBody.Datasource.SubProperties();
        properties.setSourceType(inputs.getSourceType());
        datasource.setType(SET_TYPE);
        properties.setDatasource(datasource);
        storage.setAccountName(inputs.getAccountName());
        storage.setAccountKey(inputs.getAccountKey());
        JSONArray json = new JSONArray();
        json.add(storage);
        subproperties.setStrogeaccounts(json);
        datasource.setProperties(subproperties);
        subproperties.setContainer(CONTAINER);
        subproperties.setPathPattern(PATH_PATTERN);
        serializationprop.setFieldDelimiter(FIELD_DELIMETER);
        serializationprop.setEncoding(ENCODING);
        serialization.setType(TYPE);
        serialization.setProperties(serializationprop);
        properties.setSerialization(serialization);
        createStreamingOutputJobRequestBody.setProperties(properties);
        ObjectMapper createInstanceMapper = new ObjectMapper();
        return createInstanceMapper.writeValueAsString(createStreamingOutputJobRequestBody);

    }


    private static JSONObject stringToJSON(JSONParser jsonParser, String property) throws ParseException {
        if (!isEmpty(property))
            return (JSONObject) jsonParser.parse(property);
        return new JSONObject();
    }

    @NotNull
    public static Map<String, String> CreateOutputJob(@NotNull final CreateStreamingOutputJobInputs createStreamingOutputJobInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateOutputStreamingJobUrl(createStreamingOutputJobInputs.getAzureCommonInputs().getSubscriptionId(), createStreamingOutputJobInputs.getAzureCommonInputs().getResourceGroupName(),
                createStreamingOutputJobInputs.getJobName(), createStreamingOutputJobInputs.getOutputName(), createStreamingOutputJobInputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(createStreamingOutputJobInputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, createStreamingOutputJobInputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion());
        httpClientInputs.setBody(createStreamingOutputJobRequestBody(createStreamingOutputJobInputs));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> CreateInputJob(@NotNull final CreateStreamingInputJobInputs createStreamingInputJobInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getCreateInputStreamingJobUrl(createStreamingInputJobInputs.getAzureCommonInputs().getSubscriptionId(), createStreamingInputJobInputs.getAzureCommonInputs().getResourceGroupName(),
                createStreamingInputJobInputs.getJobName(), createStreamingInputJobInputs.getInputName(), createStreamingInputJobInputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PUT);
        httpClientInputs.setContentType(CONTENT_TYPE);
        httpClientInputs.setHeaders(getAuthHeaders(createStreamingInputJobInputs.getAzureCommonInputs().getAuthToken()));
        HttpUtils.setCommonHttpInputs(httpClientInputs, createStreamingInputJobInputs.getAzureCommonInputs());
        httpClientInputs.setQueryParams(setAPIVersion());
        httpClientInputs.setBody(createStreamingInputJobRequestBody(createStreamingInputJobInputs));
        return new HttpClientService().execute(httpClientInputs);
    }

}
