/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.nutanix.prism.service;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.prism.entities.NutanixGetTaskDetailsInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.prism.service.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.*;
import static io.cloudslang.content.nutanix.prism.utils.Constants.GetTaskDetailsConstants.GET_TASK_DETAILS_PATH;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getTaskDetailsQueryParams;
import static io.cloudslang.content.nutanix.prism.utils.HttpUtils.getUriBuilder;

public class TaskImpl {

    @NotNull
    public static Map<String, String> getTaskDetails(@NotNull final NutanixGetTaskDetailsInputs nutanixGetTaskDetailsInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getTaskDetailsURL(nutanixGetTaskDetailsInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixGetTaskDetailsInputs.getCommonInputs().getUsername());
        httpClientInputs.setPassword(nutanixGetTaskDetailsInputs.getCommonInputs().getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        httpClientInputs.setQueryParams(getTaskDetailsQueryParams(nutanixGetTaskDetailsInputs.getIncludeSubtasksInfo()));
        setCommonHttpInputs(httpClientInputs, nutanixGetTaskDetailsInputs.getCommonInputs());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String getTaskDetailsURL(NutanixGetTaskDetailsInputs nutanixGetTaskDetailsInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixGetTaskDetailsInputs.getCommonInputs());
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(nutanixGetTaskDetailsInputs.getCommonInputs().getAPIVersion())
                .append(GET_TASK_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(nutanixGetTaskDetailsInputs.getTaskUUID());
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }
}
