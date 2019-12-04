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
import io.cloudslang.content.hashicorp.terraform.entities.ApplyRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RunImplTest.class)
public class RunImplTest {

    private final String EXPECTED_APPLY_RUN_REQUEST_BODY = "{\"comment\":\"test apply run comment\"}";
    private final String EXPECTED_APPLY_RUN_URL = "https://app.terraform.io/api/v2/runs/run-456test/actions/apply";

    private final ApplyRunInputs applyRunInputs = ApplyRunInputs.builder()
            .runComment("test apply run comment")
            .runId("test-123")
            .build();
    private final ApplyRunInputs applyRunInputsForURL = ApplyRunInputs.builder()
            .runId("run-456test")
            .build();
    private final ApplyRunInputs getApplyRun = ApplyRunInputs.builder()
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
                    .executionTimeout("")
                    .async("")
                    .pollingInterval("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
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


}
