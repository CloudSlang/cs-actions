/*
 * Copyright 2026-2027 Open Text
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
package io.cloudslang.content.nutanix.prism.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixCreateSnapshotInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixDeleteSnapshotInputs;
import io.cloudslang.content.nutanix.prism.entities.NutanixHttpClientInputs;
import io.cloudslang.content.nutanix.prism.services.models.snapshots.CreateSnapshotRequestBody;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.SnapshotConstants.*;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SnapshotImpl {

    @NotNull
    public static Map<String, String> createSnapshot(@NotNull final NutanixCreateSnapshotInputs inputs)
            throws Exception {
        final NutanixHttpClientInputs httpClientInputs = new NutanixHttpClientInputs();
        httpClientInputs.setUrl(createSnapshotURL(inputs));
        setCommonHttpInputs(httpClientInputs, inputs.getCommonInputs());
        httpClientInputs.setBody(createSnapshotBody(inputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setUsername(inputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(inputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> deleteSnapshot(@NotNull final NutanixDeleteSnapshotInputs inputs)
            throws Exception {
        final NutanixHttpClientInputs httpClientInputs = new NutanixHttpClientInputs();
        httpClientInputs.setUrl(deleteSnapshotURL(inputs));
        if (!isEmpty(inputs.getLogicalTimestamp())) {
            httpClientInputs.setQueryParams(getDeleteSnapshotQueryParams(inputs.getLogicalTimestamp()));
        }
        setCommonHttpInputs(httpClientInputs, inputs.getCommonInputs());
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setUsername(inputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(inputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static String createSnapshotURL(@NotNull final NutanixCreateSnapshotInputs inputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder(inputs.getCommonInputs());
        uriBuilder.setPath(API + inputs.getCommonInputs().getAPIVersion() + SNAPSHOTS_PATH);
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String deleteSnapshotURL(@NotNull final NutanixDeleteSnapshotInputs inputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder(inputs.getCommonInputs());
        uriBuilder.setPath(API + inputs.getCommonInputs().getAPIVersion() + SNAPSHOTS_PATH +
                PATH_SEPARATOR + inputs.getSnapshotUUID());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createSnapshotBody(@NotNull final NutanixCreateSnapshotInputs inputs)
            throws JsonProcessingException {
        final Long vmLogicalTimestamp = isEmpty(inputs.getVmLogicalTimestamp())
                ? null : Long.valueOf(inputs.getVmLogicalTimestamp());
        final CreateSnapshotRequestBody.SnapshotSpec snapshotSpec = new CreateSnapshotRequestBody.SnapshotSpec(
                inputs.getSnapshotName(),
                isEmpty(inputs.getSnapshotUUID()) ? null : inputs.getSnapshotUUID(),
                vmLogicalTimestamp,
                inputs.getVmUUID());
        return new ObjectMapper().writeValueAsString(new CreateSnapshotRequestBody(snapshotSpec));
    }

    @NotNull
    public static String getDeleteSnapshotQueryParams(@NotNull final String logicalTimestamp) {
        return LOGICAL_TIMESTAMP_QUERY_PARAM + logicalTimestamp;
    }
}
