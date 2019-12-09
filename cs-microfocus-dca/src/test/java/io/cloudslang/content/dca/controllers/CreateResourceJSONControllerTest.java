/*
 * (c) Copyright 2019 Micro Focus, L.P.
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


package io.cloudslang.content.dca.controllers;

import io.cloudslang.content.dca.models.DcaBaseResourceModel;
import io.cloudslang.content.dca.models.DcaDeploymentParameterModel;
import org.junit.Test;

import java.util.List;

import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaBaseResourceModels;
import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaDeploymentParameterModels;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class CreateResourceJSONControllerTest {

    private static final String N_1 = "n1";
    private static final String N_2 = "n2";
    private static final String V_1 = "v1";
    private static final String V_2 = "v2";
    private static final String BR_UUID_1 = "br_uuid1";
    private static final String BR_UUID_2 = "br_uuid2";
    private static final String CI_TYPE_1 = "ci_type_1";
    private static final String CI_TYPE_2 = "ci_type_2";
    private static final String BRT_UUID_1 = "brt_uuid1";
    private static final String BRT_UUID_2 = "brt_uuid2";

    @Test
    public void testGetDcaBaseResourceModels() {
        final List<DcaBaseResourceModel> models = getDcaBaseResourceModels(asList(BR_UUID_1, BR_UUID_2),
                asList(CI_TYPE_1, CI_TYPE_2), asList(BRT_UUID_1, BRT_UUID_2));

        final List<DcaBaseResourceModel> expected = asList(new DcaBaseResourceModel(BR_UUID_1, CI_TYPE_1, BRT_UUID_1),
                new DcaBaseResourceModel(BR_UUID_2, CI_TYPE_2, BRT_UUID_2));

        assertEquals(expected, models);
    }

    @Test
    public void testGetDcaDeploymentParameterModels() {
        final List<DcaDeploymentParameterModel> models = getDcaDeploymentParameterModels(asList(N_1, N_2),
                asList(V_1, V_2));

        final List<DcaDeploymentParameterModel> expected = asList(new DcaDeploymentParameterModel(N_1, V_1),
                new DcaDeploymentParameterModel(N_2, V_2));

        assertEquals(expected, models);
    }
}
