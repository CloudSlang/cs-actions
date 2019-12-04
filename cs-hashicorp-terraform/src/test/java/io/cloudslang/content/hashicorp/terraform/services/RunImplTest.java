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


import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.hashicorp.terraform.services.RunImpl.getRunDetailsUrl;
import static org.junit.Assert.assertEquals;


@RunWith(PowerMockRunner.class)
@PrepareForTest(WorkspaceImpl.class)
public class RunImplTest {

    private static final String RUN_ID="test123";
    private final String EXPECTED_GET_RUN_DETAILS_PATH="https://app.terraform.io/api/v2/runs/test123";

    @Test
    public void getRunDetailsPathTest() throws Exception{
        final String path=getRunDetailsUrl(RUN_ID);
        assertEquals(EXPECTED_GET_RUN_DETAILS_PATH,path);
    }


}

