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

import io.cloudslang.content.azure.entities.AuthorizationTokenUsingWebAPIInputs;
import io.cloudslang.content.azure.utils.HttpUtils;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.azure.utils.Constants.Common.*;

public class AuthenticationTokenUsingWebAPIImpl {

    @NotNull
    public static Map<String, String> getAuthToken(@NotNull final AuthorizationTokenUsingWebAPIInputs inputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getAuthTokenUrl(inputs.getTenantId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(AUTHENTICATION_TOKEN_CONTENT_TYPE);
        HttpUtils.setCommonHttpInputs(httpClientInputs, inputs.getAzureCommonInputs());
        httpClientInputs.setBody(HttpUtils.setAuthenticationBody(inputs.getClientId(), inputs.getClientSecret(), inputs.getResource()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getAuthTokenUrl(String tenantId) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(getAuthTokenPath(tenantId));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getAuthTokenPath(String tenantId) {
        StringBuilder pathString = new StringBuilder()
                .append(AUTHENTICATION_TOKEN_URL)
                .append(tenantId)
                .append(AUTHENTICATION_TOKEN_PATH);
        return pathString.toString();
    }
}
