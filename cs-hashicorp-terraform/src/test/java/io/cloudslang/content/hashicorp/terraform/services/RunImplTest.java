package io.cloudslang.content.hashicorp.terraform.services;

import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RunImplTest.class)
public class RunImplTest {
    private final String EXPECTED_CREATE_RUN_BODY="{\"data\":{\"attributes\":{\"message\":\"test\",\"is-Destroy\":false},\"type\":\"runs\",\"relationships\":{\"workspace\":{\"data\":{\"type\":\"test\",\"id\":\"test-123\"}}}}}";
    private final CreateRunInputs invalidCreateRunInputs=CreateRunInputs.builder()
            .workspaceId("")
            .workspaceName("")
            .runMessage("")
            .isDestroy("")
            .commonInputs(Inputs.builder()
                    .authToken("")
                    .requestBody("")
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
                    .executionTimeout("")
                    .async("")
                    .pollingInterval("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
                    .build();

        private final CreateRunInputs createRunBody = CreateRunInputs.builder()
                .workspaceId("test-123")
                .workspaceName("test")
                .runMessage("test")
                .isDestroy("false")
                .build();

        @Test(expected = IllegalArgumentException.class)
        public void getRunInputsThrows() throws Exception{
            RunImpl.createRunClient(invalidCreateRunInputs);
        }

    @Test
    public void getCreateRunBody() {
        String createRunRequestBody=RunImpl.createRunBody(createRunBody);
        assertEquals(EXPECTED_CREATE_RUN_BODY,createRunRequestBody);
    }



}
