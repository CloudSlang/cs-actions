/*
 * (c) Copyright 2023 EntIT Software LLC, a Micro Focus company, L.P.
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

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.azure.entities.CreateStreamingOutputJobInputs;
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingOutputJobRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.ANONYMOUS;
import static io.cloudslang.content.azure.utils.Constants.Common.CONTENT_TYPE;
import static io.cloudslang.content.azure.utils.Constants.Common.DEFAULT_DATE_FORMAT;
import static io.cloudslang.content.azure.utils.Constants.Common.DEFAULT_TIME_FORMAT;
import static io.cloudslang.content.azure.utils.Constants.Common.ENCODING;
import static io.cloudslang.content.azure.utils.Constants.Common.FIELD_DELIMETER;
import static io.cloudslang.content.azure.utils.Constants.Common.PATH_PATTERN;
import static io.cloudslang.content.azure.utils.Constants.Common.PUT;
import static io.cloudslang.content.azure.utils.Constants.Common.SET_TYPE;
import static io.cloudslang.content.azure.utils.Constants.Common.TYPE;
import static io.cloudslang.content.azure.utils.Constants.DEFAULT_RESOURCE;
import static io.cloudslang.content.azure.utils.Constants.OUTPUTS_JOBS_PATH;
import static io.cloudslang.content.azure.utils.Constants.RESOURCE_GROUPS_PATH;
import static io.cloudslang.content.azure.utils.Constants.STREAMING_JOBS_PATH;
import static io.cloudslang.content.azure.utils.Constants.STREAM_ANALYTICS_PATH;
import static io.cloudslang.content.azure.utils.Constants.SUBSCRIPTION_PATH;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;

public class StreamingOutputJobImpl {

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
        httpClientInputs.setQueryParams(setAPIVersion(createStreamingOutputJobInputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(createStreamingOutputJobRequestBody(createStreamingOutputJobInputs));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getCreateOutputStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName, String outputName, String apiVersion) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(createStreamingOutputJobPath(subscriptionId, resourceGroupName, jobName, outputName, apiVersion));
        return uriBuilder.build().toURL().toString();
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
        subproperties.setContainer(inputs.getContainerName());
        subproperties.setPathPattern(PATH_PATTERN);
        subproperties.setDateFormat(DEFAULT_DATE_FORMAT);
        subproperties.setTimeFormat(DEFAULT_TIME_FORMAT);
        serializationprop.setFieldDelimiter(FIELD_DELIMETER);
        serializationprop.setEncoding(ENCODING);
        serialization.setType(TYPE);
        serialization.setProperties(serializationprop);
        properties.setSerialization(serialization);
        createStreamingOutputJobRequestBody.setProperties(properties);

        ObjectMapper createInstanceMapper = new ObjectMapper();
        createInstanceMapper.disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);

        return createInstanceMapper.writeValueAsString(createStreamingOutputJobRequestBody);

    }


}
