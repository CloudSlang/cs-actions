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

import io.cloudslang.content.hashicorp.terraform.entities.GetRunDetailsInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.GetRunDetailsConstants.GET_RUN_DETAILS_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class RunImpl {

    @NotNull
    public static Map<String, String> getRunDetails(@NotNull final GetRunDetailsInputs getRunDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = getRunDetailsInputs.getCommonInputs();
        httpClientInputs.setUrl(getRunDetailsUrl(getRunDetailsInputs.getRunId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String getRunDetailsUrl(@NotNull final String runId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(GET_RUN_DETAILS_PATH)
                .append(runId);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }
}
