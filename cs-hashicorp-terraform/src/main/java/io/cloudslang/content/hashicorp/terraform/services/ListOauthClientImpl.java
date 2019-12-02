/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

import java.util.Map;

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.apache.http.client.utils.URIBuilder;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.jetbrains.annotations.NotNull;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ListOAuthClientConstants.OAUTH_CLIENT_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class ListOauthClientImpl {
    @NotNull
    public static Map<String, String> listOAuthClient(@NotNull final ListOAuthClientInputs listOAuthInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final Inputs commonInputs = listOAuthInputs.getCommonInputs();
        httpClientInputs.setUrl(listOAuthClientUrl(commonInputs.getOrganizationName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String listOAuthClientUrl(@NotNull final String organizationName) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getListOAuthClientPath(organizationName));

        return uriBuilder.build().toURL().toString();
    }
    @NotNull
    public static String getListOAuthClientPath(@NotNull String organizationName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName)
                .append(OAUTH_CLIENT_PATH);
        return pathString.toString();
    }
}
