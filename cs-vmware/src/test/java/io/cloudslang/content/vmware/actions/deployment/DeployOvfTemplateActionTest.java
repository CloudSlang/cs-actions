/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.vmware.actions.deployment;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.DeployOvfTemplateService;
import io.cloudslang.content.vmware.utils.OvfUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class DeployOvfTemplateActionTest {

    private static final String OVF_NETWORK_JS_VALUES = "[\"Network 1\",\"Network 2\"]";
    private static final String NET_PORT_GROUP_JS_VALUES = "[\"VM Network\", \"dvPortGroup\"]";
    private static final String OVF_PROP_KEY_JS_VALUES = "[\"vami.ip0.vmName\",\"vami.ip1.vmName\"]";
    private static final String OVF_PROP_VALUE_JS_VALUES = "[\"10.10.10.10\",\"10.20.30.40\"]";
    private static final String SUCCESSFULLY_DEPLOYED = "Template was deployed successfully!";
    private static final String OPERATION_FAILED = "Operation failed!";

    @Test
    public void testSuccessDeployTemplate() throws Exception {
        try (MockedStatic<OvfUtils> ovfUtils = mockStatic(OvfUtils.class);
             MockedConstruction<DeployOvfTemplateService> construction = mockConstruction(DeployOvfTemplateService.class)) {
            stubMappings(ovfUtils);
            Map<String, String> result = new DeployOvfTemplateAction().deployTemplate("", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES, OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES, "", null);
            DeployOvfTemplateService service = construction.constructed().get(0);
            org.mockito.Mockito.verify(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());
            assertEquals(ReturnCodes.SUCCESS, result.get(Outputs.RETURN_CODE));
            assertEquals(SUCCESSFULLY_DEPLOYED, result.get(Outputs.RETURN_RESULT));
            assertEquals(1, construction.constructed().size());
            ovfUtils.verify(() -> OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES));
            ovfUtils.verify(() -> OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES));
        }
    }

    @Test
    public void testFailureDeployTemplate() throws Exception {
        try (MockedStatic<OvfUtils> ovfUtils = mockStatic(OvfUtils.class);
             MockedConstruction<DeployOvfTemplateService> construction = mockConstruction(DeployOvfTemplateService.class,
                     (mock, context) -> doThrow(new Exception(OPERATION_FAILED)).when(mock)
                             .deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap()))) {
            stubMappings(ovfUtils);
            Map<String, String> result = new DeployOvfTemplateAction().deployTemplate("", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", "", "", "", OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES, OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES, "", null);
            DeployOvfTemplateService service = construction.constructed().get(0);
            org.mockito.Mockito.verify(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());
            assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
            assertEquals(OPERATION_FAILED, result.get(Outputs.RETURN_RESULT));
            assertTrue(StringUtilities.contains(result.get(Outputs.RETURN_RESULT), OPERATION_FAILED));
            assertEquals(1, construction.constructed().size());
            ovfUtils.verify(() -> OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES));
            ovfUtils.verify(() -> OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES));
        }
    }

    private void stubMappings(MockedStatic<OvfUtils> ovfUtils) throws Exception {
        ovfUtils.when(() -> OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES)).thenReturn(new HashMap<>());
        ovfUtils.when(() -> OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES)).thenReturn(new HashMap<>());
    }
}
