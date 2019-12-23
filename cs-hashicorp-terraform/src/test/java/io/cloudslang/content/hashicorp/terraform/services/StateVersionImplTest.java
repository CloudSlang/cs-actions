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

import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformStateVersionInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.StateVersionImpl.getCurrentStateVersionPath;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StateVersionImpl.class)
public class StateVersionImplTest {
    private static final String WORKSPACE_ID = "test";
    private static final String EXPECTED_GET_CURRENT_STATE_VERSION_PATH = "/api/v2/workspaces/test/current-state-version";

    private final TerraformStateVersionInputs invalidGetCurrentStateVersionInputs = TerraformStateVersionInputs.builder()
            .workspaceId(WORKSPACE_ID)
            .commonInputs(TerraformCommonInputs.builder()
                    .authToken("test")
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
    public void getCurrentStateVersionThrows() throws Exception {
        StateVersionImpl.getCurrentStateVersion(invalidGetCurrentStateVersionInputs);
    }

    @Test
    public void getCurrentStateVersionPathTest() {
        final String path = getCurrentStateVersionPath(invalidGetCurrentStateVersionInputs.getWorkspaceId());
        assertEquals(EXPECTED_GET_CURRENT_STATE_VERSION_PATH, path);
    }

}
