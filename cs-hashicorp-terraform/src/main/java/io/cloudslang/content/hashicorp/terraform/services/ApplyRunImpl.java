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

package io.cloudslang.content.hashicorp.terraform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.hashicorp.terraform.entities.ApplyRunInputs;
import io.cloudslang.content.hashicorp.terraform.services.createmodels.runs.ApplyRunRequestBody;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ApplyRunConstants.APPLY_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ApplyRunConstants.RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ApplyRunImpl {
    @NotNull
    public static Map<String, String> applyRunClient(@NotNull final ApplyRunInputs applyRunInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Inputs commonInputs = applyRunInputs.getCommonInputs();
        System.out.println(applyRunInputs.getRunId());
        System.out.println(applyRunClientUrl(applyRunInputs.getRunId()));
        httpClientInputs.setUrl(applyRunClientUrl(applyRunInputs.getRunId()));
        if (commonInputs.getRequestBody().isEmpty()) {
            httpClientInputs.setBody(applyRunBody(applyRunInputs));
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String applyRunClientUrl(@NotNull String runId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(RUN_PATH)
                .append(runId)
                .append(APPLY_RUN_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }


    @NotNull
    private static String applyRunBody(ApplyRunInputs applyRunInputs) {
        ObjectMapper mapper = new ObjectMapper();
        ApplyRunRequestBody applyRunBody = new ApplyRunRequestBody();
        applyRunBody.setRunComment(applyRunInputs.getRunComment());

        String requestBody = EMPTY;


        try {
            requestBody = mapper.writeValueAsString(applyRunBody);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return requestBody;
    }
}
