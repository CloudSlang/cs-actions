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


package io.cloudslang.content.vmware.actions.cluster;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_VM_NAME_OR_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModifyVmOverridesTest {

    private static final String OPERATION_FAILED = "Operation failed!";
    private static final String CLUSTER_RESTART_PRIORITY = "clusterRestartPriority";
    private static final String DISABLED = "disabled";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MEDIUM = "medium";
    private static final String INVALID_RESTART_PRIORITY_MSG = "The 'restartPriority' input value is not valid! Valid values are ";
    private static final String WRONG_RESTART_PRIORITY = "wrong_restart_priority";
    private static final String INVALID_RESTART_PRIORITY_MESSAGE = INVALID_RESTART_PRIORITY_MSG
            + "clusterRestartPriority,disabled,high,medium,low";

    @Test
    public void testSuccessModifyVmOverrides() throws Exception {
        Map<String, String> map = new HashMap<>();
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> when(mock.updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), eq(CLUSTER_RESTART_PRIORITY))).thenReturn(map))) {
            Map<String, String> result = new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "vmName", "", "", CLUSTER_RESTART_PRIORITY, null);
            verify(construction.constructed().get(0)).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), eq(CLUSTER_RESTART_PRIORITY));
            assertEquals(map, result);
        }
    }

    @Test
    public void testFailureModifyVmOverrides() throws Exception {
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> doThrow(new Exception(OPERATION_FAILED)).when(mock).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), eq(CLUSTER_RESTART_PRIORITY)))) {
            Map<String, String> result = new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "", "vm-123", "", CLUSTER_RESTART_PRIORITY, null);
            verify(construction.constructed().get(0)).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), eq(CLUSTER_RESTART_PRIORITY));
            assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
            assertTrue(StringUtilities.contains(result.get(Outputs.EXCEPTION), OPERATION_FAILED));
        }
    }

    @Test
    public void testValidateRestartPriority() throws Exception {
        for (String priority : new String[]{CLUSTER_RESTART_PRIORITY, DISABLED, HIGH, MEDIUM, LOW}) {
            try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class)) {
                new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "vmName", "", "", priority, null);
                assertEquals(1, construction.constructed().size());
                verify(construction.constructed().get(0)).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), eq(priority));
            }
        }

        Map<String, String> result = new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "vmName", "", "", WRONG_RESTART_PRIORITY, null);
        assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(Outputs.EXCEPTION), INVALID_RESTART_PRIORITY_MESSAGE));
    }

    @Test
    public void testValidateMutualExclusiveInputs() throws Exception {
        verifyFailureResultMap(new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "", "", "", CLUSTER_RESTART_PRIORITY, null));
        verifyFailureResultMap(new ModifyVmOverrides().modifyVmOverrides("", "", "", "", "", "", "", "", "vmName", "vm-123", "", CLUSTER_RESTART_PRIORITY, null));
    }

    private void verifyFailureResultMap(Map<String, String> result) {
        assertEquals(PROVIDE_VM_NAME_OR_ID, result.get(RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(EXCEPTION), PROVIDE_VM_NAME_OR_ID));
    }
}
