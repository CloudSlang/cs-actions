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
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformRunInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.RunImpl.*;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RunImplTest.class)
public class RunImplTest {

    private final String EXPECTED_APPLY_RUN_REQUEST_BODY = "{\"comment\":\"test apply run comment\"}";
    private final String EXPECTED_APPLY_RUN_URL = "https://app.terraform.io/api/v2/runs/run-456test/actions/apply";
    private final String EXPECTED_CREATE_RUN_BODY = "{\"data\":{\"attributes\":{\"message\":\"test\",\"is-Destroy\":false},\"type\":\"runs\",\"relationships\":{\"workspace\":{\"data\":{\"type\":\"workspaces\",\"id\":\"test-123\"}}}}}";
    private static final String RUN_ID = "test123";
    private static final String PLAN_ID = "test123";
    private static final String APPLY_ID = "test321";
    private static final String WORKSPACE_ID = "ws-test123";
    private final String EXPECTED_GET_RUN_DETAILS_PATH = "https://app.terraform.io/api/v2/runs/test123";
    private final String EXPECTED_PLAN_DETAILS_PATH = "https://app.terraform.io/api/v2/plans/test123";
    private final String EXPECTED_GET_APPLY_DETAILS_PATH = "https://app.terraform.io/api/v2/applies/test321";
    private final String EXPECTED_LIST_RUNS_IN_WORKSPACE_PATH = "https://app.terraform.io/api/v2/workspaces/ws-test123/runs";

    private final TerraformRunInputs applyRunInputs = TerraformRunInputs.builder()
            .runComment("test apply run comment")
            .runId("test-123")
            .build();
    private final TerraformRunInputs applyRunInputsForURL = TerraformRunInputs.builder()
            .runId("run-456test")
            .build();
    private final TerraformRunInputs getApplyRun = TerraformRunInputs.builder()
            .runId("")
            .runComment("")
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
    private final TerraformRunInputs listRunsInWorkspaceInputs = TerraformRunInputs.builder()
            .workspaceId("")
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


    private final TerraformRunInputs invalidCreateRunInputs = TerraformRunInputs.builder()
            .workspaceId("")
            .runMessage("")
            .isDestroy("")
            .commonInputs(TerraformCommonInputs.builder()
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
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
            .build();

    private final TerraformRunInputs createRunBody = TerraformRunInputs.builder()
            .workspaceId("test-123")
            .runMessage("test")
            .isDestroy("false")
            .build();


    @Test
    public void getApplyRunURL() throws Exception {
        String applyRunClientUrl = RunImpl.applyRunClientUrl(applyRunInputsForURL.getRunId());
        assertEquals(EXPECTED_APPLY_RUN_URL, applyRunClientUrl);
    }


    @Test
    public void getApplyRunBody() throws JsonProcessingException {
        String applyRunRequestBody = RunImpl.applyRunBody(applyRunInputs);
        assertEquals(EXPECTED_APPLY_RUN_REQUEST_BODY, applyRunRequestBody);
    }


    @Test(expected = IllegalArgumentException.class)
    public void applyRunInputThrows() throws Exception {
        RunImpl.applyRunClient(getApplyRun);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getRunInputsThrows() throws Exception {
        RunImpl.createRunClient(invalidCreateRunInputs);
    }

    @Test
    public void getCreateRunBody() throws JsonProcessingException {
        String createRunRequestBody = RunImpl.createRunBody(createRunBody);
        assertEquals(EXPECTED_CREATE_RUN_BODY, createRunRequestBody);
    }

    @Test
    public void getRunDetailsPathTest() throws Exception {
        final String path = getRunDetailsUrl(RUN_ID);
        assertEquals(EXPECTED_GET_RUN_DETAILS_PATH, path);
    }

    @Test
    public void planDetailsPathTest() throws Exception {
        final String path = planDetailsUrl(PLAN_ID);
        assertEquals(EXPECTED_PLAN_DETAILS_PATH, path);
    }

    @Test
    public void getApplyDetailsPathTest() throws Exception {
        final String path = getApplyDetailsUrl(APPLY_ID);
        assertEquals(EXPECTED_GET_APPLY_DETAILS_PATH, path);
    }

    @Test(expected = IllegalArgumentException.class)
    public void listRunsInWorkspaceInputsThrows() throws Exception {
        RunImpl.listRunsInWorkspaceClient(listRunsInWorkspaceInputs);
    }

    @Test
    public void listRunsInWorkspacePathTest() throws Exception {
        final String path = listRunsInWorkspaceClientUrl(WORKSPACE_ID);
        assertEquals(EXPECTED_LIST_RUNS_IN_WORKSPACE_PATH, path);
    }


}
