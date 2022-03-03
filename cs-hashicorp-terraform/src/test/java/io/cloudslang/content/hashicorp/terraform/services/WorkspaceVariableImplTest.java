/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceVariableInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceVariableImpl.getWorkspaceVariablePath;


import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(io.cloudslang.content.hashicorp.terraform.services.WorkspaceVariableImpl.class)
public class WorkspaceVariableImplTest {
    private final String EXPECTED_CREATE_WORKSPACE_VARIABLE_BODY = "{\"data\":{\"attributes\":{\"key\":\"test\",\"value\":\"test-123\",\"category\":\"env\",\"hcl\":\"false\",\"sensitive\":\"false\"},\"type\":\"vars\"}}";
    private static final String EXPECTED_DELETE_WORKSPACE_VAR_PATH = "/api/v2/workspaces/test1/vars";
    private static final String EXPECTED_UPDATE_WORKSPACE_VAR_PATH = "/api/v2/workspaces/test1/vars";
    private final String EXPECTED_UPDATE_WORKSPACE_VARIABLE_BODY = "{\"data\": { \"id\":\"var-test1\", \"attributes\": { \"key\":\"dummyname\", \"value\":\"mars\", \"category\":\"terraform\" },\"type\":\"vars\" }}";
    private final TerraformWorkspaceVariableInputs getTerraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
            .workspaceVariableName("test")
            .workspaceVariableValue("test")
            .sensitiveWorkspaceVariableValue("test")
            .workspaceVariableCategory("")
            .workspaceId("test1")
            .hcl("false")
            .sensitive("false")
            .sensitiveWorkspaceVariableRequestBody("")
            .workspaceVariableJson("[]")
            .sensitiveWorkspaceVariableJson("[]")
            .commonInputs(TerraformCommonInputs.builder()
                    .authToken("")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .trustAllRoots("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .connectTimeout("")
                    .socketTimeout("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
            .build();

    private final TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
            .workspaceId("ws-test123")
            .workspaceVariableCategory("test")
            .workspaceVariableValue("test")
            .workspaceVariableName("test")
            .workspaceVariableValue("test-123")
            .sensitiveWorkspaceVariableValue("test-123")
            .workspaceVariableCategory("env")
            .hcl("false")
            .sensitive("false")
            .build();


    private final TerraformWorkspaceVariableInputs terraformWorkspaceVariableDeleteInputs = TerraformWorkspaceVariableInputs.builder()
            .workspaceId("test1")
            .workspaceVariableId("var-test")
            .build();

    private final TerraformWorkspaceVariableInputs terraformWorkspaceVariableDeleteInputs1 = TerraformWorkspaceVariableInputs.builder()
            .workspaceId("test1")
            .workspaceVariableId("var-test1")
            .commonInputs(TerraformCommonInputs.builder()
                    .requestBody(EXPECTED_UPDATE_WORKSPACE_VARIABLE_BODY).build())
            .build();

    @Test
    public void getDeleteWorkspaceVariablePath() {
        String path = getWorkspaceVariablePath(terraformWorkspaceVariableDeleteInputs);
        assertEquals(EXPECTED_DELETE_WORKSPACE_VAR_PATH, path);

    }

    @Test
    public void getUpdateWorkspaceVariablePath() {
        String path = getWorkspaceVariablePath(terraformWorkspaceVariableDeleteInputs1);
        assertEquals(EXPECTED_UPDATE_WORKSPACE_VAR_PATH, path);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createWorkspaceVariable() throws Exception {
        WorkspaceVariableImpl.createWorkspaceVariable(getTerraformWorkspaceVariableInputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void listWorkspaceVariables()throws Exception{
        WorkspaceVariableImpl.listWorkspaceVariables(getTerraformWorkspaceVariableInputs);
    }
    @Test
    public void createWorkspaceVariables() throws  Exception{
        Map<String,Map<String,String>> createWorkspaceVariablesResult= WorkspaceVariableImpl.createWorkspaceVariables(getTerraformWorkspaceVariableInputs);
        assertEquals(0,createWorkspaceVariablesResult.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWorkspaceVariables() throws  Exception{
        Map<String,Map<String,String>> updateWorkspaceVariablesResult= WorkspaceVariableImpl.updateWorkspaceVariables(getTerraformWorkspaceVariableInputs);
        assertEquals(0,updateWorkspaceVariablesResult.size());
    }

    @Test
    public void getCreateWorkspaceVariableBody() {
        String createWorkspaceVariableBody = WorkspaceVariableImpl.createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs);
        assertEquals(EXPECTED_CREATE_WORKSPACE_VARIABLE_BODY, createWorkspaceVariableBody);
    }


}

