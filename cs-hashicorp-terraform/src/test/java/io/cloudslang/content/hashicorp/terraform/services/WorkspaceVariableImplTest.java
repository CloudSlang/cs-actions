

package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceVariableInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceVariableImpl.getWorkspaceVariablePath;


import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void createWorkspaceVariableReturnsHttpClientResponse() throws Exception {
        assertEquals(0, TerraformTestUtils.executeWithMockedHttpClient(
                () -> WorkspaceVariableImpl.createWorkspaceVariable(getTerraformWorkspaceVariableInputs)).size());
    }

    @Test
    public void listWorkspaceVariablesReturnsHttpClientResponse() throws Exception {
        assertEquals(0, TerraformTestUtils.executeWithMockedHttpClient(
                () -> WorkspaceVariableImpl.listWorkspaceVariables(getTerraformWorkspaceVariableInputs)).size());
    }
    @Test
    public void createWorkspaceVariables() throws  Exception{
        Map<String,Map<String,String>> createWorkspaceVariablesResult= WorkspaceVariableImpl.createWorkspaceVariables(getTerraformWorkspaceVariableInputs);
        assertEquals(0,createWorkspaceVariablesResult.size());
    }

    @Test
    public void updateWorkspaceVariablesReturnsHttpClientResponse() throws Exception {
        assertEquals(0, TerraformTestUtils.executeWithMockedHttpClient(() ->
                WorkspaceVariableImpl.updateWorkspaceVariables(getTerraformWorkspaceVariableInputs)).size());
    }

    @Test
    public void getCreateWorkspaceVariableBody() {
        String createWorkspaceVariableBody = WorkspaceVariableImpl.createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs);
        assertEquals(EXPECTED_CREATE_WORKSPACE_VARIABLE_BODY, createWorkspaceVariableBody);
    }


}
