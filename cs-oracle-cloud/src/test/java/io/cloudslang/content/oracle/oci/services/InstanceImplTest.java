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

package io.cloudslang.content.oracle.oci.services;


import io.cloudslang.content.oracle.oci.entities.inputs.OCICommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.oracle.oci.services.InstanceImpl.*;
import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InstanceImpl.class)
public class InstanceImplTest {

    private static final String EXPECTED_LIST_INSTANCES_PATH = "/20160918/instances/";
    private static final String EXPECTED_GET_INSTANCE_PATH = "/20160918/instances/myinstance";
    private static final String EXPECTED_GET_INSTANCE_DEFAULT_CREDENTIALS_PATH = "/20160918/instances/myinstance/defaultCredentials";
    private final String REGION = "ap-hyderabad-1";
    private final String INSTANCE_ID = "myinstance";
    private final OCICommonInputs ociCommonInputs = OCICommonInputs.builder()
            .region(REGION)
            .instanceId(INSTANCE_ID)
            .build();

    @Test
    public void listInstancesPathTest() {
        String path = listInstancesPath();
        assertEquals(EXPECTED_LIST_INSTANCES_PATH, path);
    }

    @Test
    public void getInstanceDetailsPathTest() {
        String path = getInstanceDetailsPath(INSTANCE_ID);
        assertEquals(EXPECTED_GET_INSTANCE_PATH, path);

    }

    @Test
    public void getDefaultCredentialsTest() {
        String path = getInstanceDefaultCredentialsPath(INSTANCE_ID);
        assertEquals(EXPECTED_GET_INSTANCE_DEFAULT_CREDENTIALS_PATH, path);

    }
}
