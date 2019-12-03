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
import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceImpl.getWorkspaceDetailsPath;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WorkspaceImpl.class)
public class WorkspaceImplTest {

    private final Inputs getOrganizationName=Inputs.builder()
            .organizationName("test")
            .build();
    private static final String WORKSPACE_NAME="test";
    private static final String EXPECTED_GET_WORKSPACE_PATH = "/api/v2/organizations/test/workspaces/test";

   @Test
    public void getWorkspaceDetailsPathTest() {
       final String path=getWorkspaceDetailsPath(getOrganizationName.getOrganizationName(),WORKSPACE_NAME);
        assertEquals(EXPECTED_GET_WORKSPACE_PATH,path);
    }



}