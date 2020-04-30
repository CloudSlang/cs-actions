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

import io.cloudslang.content.hashicorp.terraform.entities.TerraformVariableInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceVariableImpl.getVariablePath;


import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(io.cloudslang.content.hashicorp.terraform.services.WorkspaceVariableImpl.class)
public class WorkspaceVariableImplTest {
    private final String EXPECTED_CREATE_VARIABLE_BODY = "{\"data\":{\"attributes\":{\"key\":\"test\",\"value\":\"test-123\",\"category\":\"env\",\"hcl\":\"false\",\"sensitive\":\"false\"},\"relationships\":null,\"type\":\"vars\"}}";
    private static final String EXPECTED_DELETE_VAR_PATH = "/api/v2/workspaces/test1/vars";
    private static final String EXPECTED_UPDATE_VAR_PATH = "/api/v2/workspaces/test1/vars";
    private final String EXPECTED_UPDATE_VARIABLE_BODY = "{\"data\": { \"id\":\"var-test1\", \"attributes\": { \"key\":\"dummyname\", \"value\":\"mars\", \"category\":\"terraform\" },\"type\":\"vars\" }}";
    private final TerraformVariableInputs getTerraformVariableInputs = TerraformVariableInputs.builder()
            .variableName("test")
            .variableValue("test")
            .sensitiveVariableValue("test")
            .variableCategory("")
            .workspaceId("test1")
            .hcl("false")
            .sensitive("false")
            .sensitiveVariableRequestBody("")
            .variableJson("[]")
            .sensitiveVariableJson("[]")
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

    private final TerraformVariableInputs terraformVariableInputs = TerraformVariableInputs.builder()
            .workspaceId("ws-test123")
            .variableCategory("test")
            .variableValue("test")
            .variableName("test")
            .variableValue("test-123")
            .sensitiveVariableValue("test-123")
            .variableCategory("env")
            .hcl("false")
            .sensitive("false")
            .build();


    private final TerraformVariableInputs terraformVariableDeleteInputs = TerraformVariableInputs.builder()
            .workspaceId("test1")
            .variableId("var-test")
            .build();

    private final TerraformVariableInputs terraformVariableDeleteInputs1 = TerraformVariableInputs.builder()
            .workspaceId("test1")
            .variableId("var-test1")
            .commonInputs(TerraformCommonInputs.builder()
                    .requestBody(EXPECTED_UPDATE_VARIABLE_BODY).build())
            .build();

    @Test
    public void getDeleteVariablePath() {
        String path = getVariablePath(terraformVariableDeleteInputs);
        assertEquals(EXPECTED_DELETE_VAR_PATH, path);

    }

    @Test
    public void getUpdateVariablePath() {
        String path = getVariablePath(terraformVariableDeleteInputs1);
        assertEquals(EXPECTED_UPDATE_VAR_PATH, path);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createVariable() throws Exception {
        WorkspaceVariableImpl.createVariable(getTerraformVariableInputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void listVariables()throws Exception{
        WorkspaceVariableImpl.listVariables(getTerraformVariableInputs);
    }
    @Test
    public void createVariables() throws  Exception{
        Map<String,Map<String,String>> createVariablesResult= WorkspaceVariableImpl.createVariables(getTerraformVariableInputs);
        assertEquals(0,createVariablesResult.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateVariables() throws  Exception{
        Map<String,Map<String,String>> updateVariablesResult= WorkspaceVariableImpl.updateVariables(getTerraformVariableInputs);
        assertEquals(0,updateVariablesResult.size());
    }

    @Test
    public void getCreateVariableBody() {
        String createVariableBody = WorkspaceVariableImpl.createVariableRequestBody(terraformVariableInputs);
        assertEquals(EXPECTED_CREATE_VARIABLE_BODY, createVariableBody);
    }


}

