/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
import io.cloudslang.content.azure.entities.models.streamanalytics.CreateStreamingInputJobRequestBody;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.*;
import static io.cloudslang.content.azure.utils.Constants.CreateStreamingInputJobConstants.DEFAULT_SOURCE_TYPE;
import static io.cloudslang.content.azure.utils.Constants.*;
import static io.cloudslang.content.azure.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.azure.utils.HttpUtils.setAPIVersion;

public class StreamingInputJobImpl {

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
        httpClientInputs.setQueryParams(setAPIVersion(createStreamingInputJobInputs.getAzureCommonInputs().getApiVersion()));
        httpClientInputs.setBody(createStreamingInputJobRequestBody(createStreamingInputJobInputs));
        return new HttpClientService().execute(httpClientInputs);

    }

    @NotNull
    private static String getCreateInputStreamingJobUrl(String subscriptionId, String resourceGroupName, String jobName, String inputName, String apiVersion) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(createStreamingInputJobPath(subscriptionId, resourceGroupName, jobName, inputName, apiVersion));
        return uriBuilder.build().toURL().toString();
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

    private static String createStreamingInputJobRequestBody(@NotNull final CreateStreamingInputJobInputs inputs) throws Exception {
        CreateStreamingInputJobRequestBody createStreamingOutputJobRequestBody = new CreateStreamingInputJobRequestBody();
        CreateStreamingInputJobRequestBody.Properties properties = new CreateStreamingInputJobRequestBody.Properties();
        CreateStreamingInputJobRequestBody.Datasource datasource = new CreateStreamingInputJobRequestBody.Datasource();
        CreateStreamingInputJobRequestBody.Serialization serialization = new CreateStreamingInputJobRequestBody.Serialization();
        CreateStreamingInputJobRequestBody.SerializationProperties serializationprop = new CreateStreamingInputJobRequestBody.SerializationProperties();
        CreateStreamingInputJobRequestBody.Datasource.SubProperties.StorageAccounts storage = new CreateStreamingInputJobRequestBody.Datasource.SubProperties.StorageAccounts();
        CreateStreamingInputJobRequestBody.Datasource.SubProperties subproperties = new CreateStreamingInputJobRequestBody.Datasource.SubProperties();
        properties.setSourceType(DEFAULT_SOURCE_TYPE);
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
        return createInstanceMapper.writeValueAsString(createStreamingOutputJobRequestBody);

    }
}
