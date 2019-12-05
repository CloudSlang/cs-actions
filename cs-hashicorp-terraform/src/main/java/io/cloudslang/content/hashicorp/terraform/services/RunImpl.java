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
import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.runs.CreateRunBody;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.CREATE_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RunImpl {
    @NotNull
    public static Map<String, String> createRunClient(@NotNull final CreateRunInputs createRunInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = createRunInputs.getCommonInputs();
        httpClientInputs.setUrl(createRunClientUrl());
        if(commonInputs.getRequestBody().isEmpty()){
            try{
            httpClientInputs.setBody(createRunBody(createRunInputs));
            }catch(JsonProcessingException e){
                return getFailureResultsMap(e);
            }
        }else{
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
    public static String createRunClientUrl() throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(CREATE_RUN_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createRunBody(CreateRunInputs createRunInputs )throws JsonProcessingException{
        ObjectMapper mapper=new ObjectMapper();
        CreateRunBody createBody=new CreateRunBody();
        CreateRunBody.CreateRunData createRundata=createBody.new CreateRunData();
        CreateRunBody.Attributes attributes=createBody.new Attributes();
        CreateRunBody.Relationships relationships=createBody.new Relationships();
        CreateRunBody.Workspace workspace=createBody.new Workspace();
        CreateRunBody.WorkspaceData workspaceData=createBody.new WorkspaceData();

        String requestBody= EMPTY;

        workspaceData.setId(createRunInputs.getWorkspaceId());
        workspaceData.setType(WORKSPACE_TYPE);

        attributes.setDestroy(createRunInputs.getIsDestroy());
        attributes.setRunMessage(createRunInputs.getRunMessage());
        relationships.setWorkspace(workspace);

        workspace.setData(workspaceData);

        createRundata.setRelationships(relationships);
        createRundata.setAttributes(attributes);
        createRundata.setType(RUN_TYPE);

        createBody.setData(createRundata);

        requestBody=mapper.writeValueAsString(createBody);

        System.out.println(requestBody);

        return requestBody;
    }
}
