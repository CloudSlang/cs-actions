package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.CreateVariableInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(io.cloudslang.content.hashicorp.terraform.services.VariableImpl.class)
public class CreateVariableImplTest {

    private final CreateVariableInputs getCreateVariableInputs = CreateVariableInputs.builder()
            .variableName("")
            .variableValue("")
            .variableCategory("")
            .workspaceId("")
            .hcl(false)
            .sensitive(false)
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

    @Test(expected = IllegalArgumentException.class)
    public void createVariable() throws Exception {
        VariableImpl.createVariable(getCreateVariableInputs);
    }
}
