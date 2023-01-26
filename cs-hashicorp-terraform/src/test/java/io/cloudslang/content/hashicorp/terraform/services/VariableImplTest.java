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

import io.cloudslang.content.hashicorp.terraform.entities.TerraformVariableInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.VariableImpl.getVariablePath;


import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(io.cloudslang.content.hashicorp.terraform.services.VariableImpl.class)
public class VariableImplTest {
    private final String EXPECTED_CREATE_VARIABLE_BODY = "{\"data\":{\"attributes\":{\"key\":\"test\",\"value\":\"test-123\",\"category\":\"env\",\"hcl\":\"false\",\"sensitive\":\"false\"},\"relationships\":{\"workspace\":{\"data\":{\"id\":\"ws-test123\",\"type\":\"workspaces\"}}},\"type\":\"vars\"}}";
    private static final String EXPECTED_DELETE_VAR_PATH = "/api/v2/vars/var-test";
    private static final String EXPECTED_UPDATE_VAR_PATH = "/api/v2/vars/var-test1";
    private final String EXPECTED_UPDATE_VARIABLE_BODY = "{\"data\": { \"id\":\"var-test1\", \"attributes\": { \"key\":\"dummyname\", \"value\":\"mars\", \"category\":\"terraform\" }, \"type\":\"vars\" }}";
    private final TerraformVariableInputs getTerraformVariableInputs = TerraformVariableInputs.builder()
            .variableName("test")
            .variableValue("test")
            .sensitiveVariableValue("test")
            .variableCategory("")
            .workspaceId("")
            .hcl("false")
            .sensitive("false")
            .sensitiveVariableRequestBody("")
            .variableJson("[]")
            .sensitiveVariableJson("[]")
            .commonInputs(TerraformCommonInputs.builder()
                    .organizationName("")
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
    private final TerraformWorkspaceInputs listVariableInputs=TerraformWorkspaceInputs.builder()
            .workspaceName("")
            .commonInputs(TerraformCommonInputs.builder()
            .authToken("")
            .proxyHost("")
            .proxyPort("")
            .proxyUsername("")
            .proxyPassword("")
            .trustAllRoots("")
            .trustPassword("")
            .trustKeystore("")
            .connectTimeout("")
            .connectionsMaxTotal("")
            .connectionsMaxPerRoot("")
            .responseCharacterSet("")
            .build())
            .build();


    private final TerraformVariableInputs terraformVariableDeleteInputs = TerraformVariableInputs.builder()
            .variableId("var-test")
            .build();

    private final TerraformVariableInputs terraformVariableDeleteInputs1 = TerraformVariableInputs.builder()
            .variableId("var-test1")
            .commonInputs(TerraformCommonInputs.builder()
            .requestBody(EXPECTED_UPDATE_VARIABLE_BODY).build())
            .build();

    @Test
    public void getDeleteVariablePath() {
        String path = getVariablePath(terraformVariableDeleteInputs.getVariableId());
        assertEquals(EXPECTED_DELETE_VAR_PATH, path);

    }

    @Test
    public void getUpdateVariablePath() {
        String path = getVariablePath(terraformVariableDeleteInputs1.getVariableId());
        assertEquals(EXPECTED_UPDATE_VAR_PATH, path);

    }

    @Test(expected = IllegalArgumentException.class)
    public void createVariable() throws Exception {
        VariableImpl.createVariable(getTerraformVariableInputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void listVariables()throws Exception{
        VariableImpl.listVariables(listVariableInputs);
    }
    @Test
    public void createVariables() throws  Exception{
        Map<String,Map<String,String>> createVariablesResult= VariableImpl.createVariables(getTerraformVariableInputs);
        assertEquals(0,createVariablesResult.size());
    }

    @Test
    public void getCreateVariableBody() {
        String createVariableBody = VariableImpl.createVariableRequestBody(terraformVariableInputs);

        assertEquals(EXPECTED_CREATE_VARIABLE_BODY, createVariableBody);
    }


}

