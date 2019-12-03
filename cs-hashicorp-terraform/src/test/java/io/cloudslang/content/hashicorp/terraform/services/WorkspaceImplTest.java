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

import io.cloudslang.content.hashicorp.terraform.entities.CreateWorkspaceInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceImpl.createWorkspaceBody;
import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceImpl.getWorkspacePath;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorkspaceImpl.class)
public class WorkspaceImplTest {

    private static final String ORGANIZATION_NAME = "test";
    private static final String EXPECTED_WORKSPACE_PATH = "/api/v2/organizations/test/workspaces";
    private static final String EXPECTED_WORKSPACE_REQUEST_BODY = "{\"data\":{\"attributes\":{\"name\":\"test\",\"terraform_version\":\"0.12.1\",\"description\":\"test\",\"auto-apply\":true,\"file-triggers-enabled\":true," +
            "\"working-directory\":\"/test\",\"trigger-prefixes\":[\"\"],\"queue-all-runs\":false,\"speculative-enabled\":true," +
            "\"vcs-repo\":{\"identifier\":\"test\",\"branch\":\"test\",\"oauth-token-id\":\"test\",\"ingress-submodules\":true}},\"type\":\"workspaces\"}}";
    private final CreateWorkspaceInputs invalidCreateWorkspaceInputs = CreateWorkspaceInputs.builder()
            .workspaceName("test")
            .workspaceDescription("test")
            .autoApply("true")
            .fileTriggersEnabled("true")
            .workingDirectory("/test")
            .triggerPrefixes("")
            .queueAllRuns("false")
            .speculativeEnabled("true")
            .ingressSubmodules("true")
            .vcsRepoId("test")
            .vcsBranchName("test")
            .oauthTokenId("test")
            .commonInputs(Inputs.builder()
                    .organizationName("test")
                    .authToken("test")
                    .terraformVersion("0.12.1")
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
                    .pollingInterval("")
                    .async("")
                    .keepAlive("")
                    .connectionsMaxPerRoot("")
                    .connectionsMaxTotal("")
                    .responseCharacterSet("")
                    .build())
            .build();

    @Test(expected = IllegalArgumentException.class)
    public void createWorkspaceThrows() throws Exception {
        WorkspaceImpl.createWorkspace(invalidCreateWorkspaceInputs);
    }

    @Test
    public void getWorkspacePathTest() {
        final String path = getWorkspacePath(ORGANIZATION_NAME);
        assertEquals(EXPECTED_WORKSPACE_PATH, path);
    }

    @Test
    public void getWorkspaceRequestBody() {
        final String requestBody = createWorkspaceBody(invalidCreateWorkspaceInputs, ",");
        assertEquals(EXPECTED_WORKSPACE_REQUEST_BODY, requestBody);
    }
}
