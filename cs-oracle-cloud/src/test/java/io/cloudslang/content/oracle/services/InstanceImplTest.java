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

package io.cloudslang.content.oracle.services;

import io.cloudslang.content.oracle.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.oracle.services.models.instances.InstanceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InstanceImpl.class)
public class InstanceImplTest {

    private final OCIInstanceInputs invalidListInstanceInputs = OCIInstanceInputs.builder()
            .compartmentOcid("ocid1.tenancy.oc1..aaaaaaaaehpjcmxxi5cafq7j6vagweraa35jhssss6ngtzmzdgmjhfhj6u2q")
            .commonInputs(OCICommonInputs.builder()
                    .tenancyOcId("ocid1.tenancy.oc1..aaaaaaaaehpjcmxxi5cafq7j6vagweraa35jhssss6ngtzmzdgmjhfhj6u2q")
                    .userOcid("ocid1.user.oc1..aaaaaaaankmhjx5iug7xom4lei52d5p3sljcltuk7xoho24ys4ur2gkoedga")
                    .fingerPrint("b3:0f:a2:07:fd:b2:78:e1:fa:8e:04:be:1c:9d:81:1b")
                    .privateKeyFilename("C:\\Users\\dwiveame.CORPDOM\\.oci\\oci_api_key.pem")
                    .build())
            .build();


    @Test (expected = IllegalArgumentException.class)
    public void listInstancesThrows() throws Exception{
        InstanceImpl.listInstances(invalidListInstanceInputs);
    }


}
