package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.CreateVariableInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(io.cloudslang.content.hashicorp.terraform.services.VariableImpl.class)
public class VariableImplTest {
    private final String EXPECTED_CREATE_VARIABLE_BODY="{\"data\":{\"attributes\":{\"key\":\"test\",\"value\":\"test-123\",\"category\":\"env\",\"hcl\":\"false\",\"sensitive\":\"false\"},\"relationships\":{\"workspace\":{\"data\":{\"id\":\"ws-test123\",\"type\":\"workspaces\"}}},\"type\":\"vars\"}}";

    private final CreateVariableInputs getCreateVariableInputs = CreateVariableInputs.builder()
            .variableName("")
            .variableValue("")
            .variableCategory("")
            .workspaceId("")
            .hcl("false")
            .sensitive("false")
            .commonInputs(Inputs.builder()
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
    private final CreateVariableInputs createVariableInputs = CreateVariableInputs.builder()
            .workspaceId("ws-test123")
            .variableName("test")
            .variableValue("test-123")
            .variableCategory("env")
            .hcl("false")
            .sensitive("false")
            .build();

    @Test(expected = IllegalArgumentException.class)
    public void createVariable() throws Exception {
        VariableImpl.createVariable(getCreateVariableInputs);
    }
    @Test
    public void getCreateVariableBody() throws Exception {
        String createVariableBody= VariableImpl.createVariableRequestBody(createVariableInputs);

        assertEquals(EXPECTED_CREATE_VARIABLE_BODY,createVariableBody);
    }
}
