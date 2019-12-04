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

import io.cloudslang.content.hashicorp.terraform.entities.GetWorkspaceDetailsInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.ANONYMOUS;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.GetWorkspaceDetails.GET_WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class WorkspaceImpl {

    @NotNull
    public static Map<String, String> getWorkspaceDetails(@NotNull final GetWorkspaceDetailsInputs getWorkspaceDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = getWorkspaceDetailsInputs.getCommonInputs();
        httpClientInputs.setUrl(getWorkspaceDetailsUrl(getWorkspaceDetailsInputs.getCommonInputs().getOrganizationName(),getWorkspaceDetailsInputs.getWorkspaceName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String getWorkspaceDetailsUrl(@NotNull final String organizationName, @NotNull final String workspaceName) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspaceDetailsPath(organizationName,workspaceName));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getWorkspaceDetailsPath(@NotNull final String organizationName,  @NotNull final String workspaceName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName)
                .append(GET_WORKSPACE_PATH)
                .append(workspaceName);
        return pathString.toString();
    }

}
