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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetVmOverridesTest {

    private static final String OPERATION_FAILED = "Operation failed!";
    private static final String PROVIDE_ONE_OR_NONE_EXCEPTION = "Virtual Machine identification inputs are mutually exclusive! Provide only one or none.";

    @Test
    public void testSuccessModifyVmOverrides() throws Exception {
        String expectedReturnResult = "expectMe";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("returnResult", expectedReturnResult);
        expectedResult.put("returnCode", "0");
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> when(mock.getVmOverride(any(HttpInputs.class), any(VmInputs.class))).thenReturn(expectedReturnResult))) {
            Map<String, String> actual = new GetVmOverrides().getVmOverrides("", "", "", "", "", "", "", "", "vmName", "", "", null);
            verify(construction.constructed().get(0)).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
            assertEquals(expectedResult, actual);
        }
    }

    @Test
    public void testSuccessModifyVmOverridesWithNoVmDetails() throws Exception {
        String expectedReturnResult = "expectMe2";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("returnResult", expectedReturnResult);
        expectedResult.put("returnCode", "0");
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> when(mock.getVmOverride(any(HttpInputs.class), any(VmInputs.class))).thenReturn(expectedReturnResult))) {
            Map<String, String> actual = new GetVmOverrides().getVmOverrides("", "", "", "", "", "", "", "", "", "", "", null);
            verify(construction.constructed().get(0)).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
            assertEquals(expectedResult, actual);
        }
    }

    @Test
    public void testFailureModifyVmOverrides() throws Exception {
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> doThrow(new Exception(OPERATION_FAILED)).when(mock).getVmOverride(any(HttpInputs.class), any(VmInputs.class)))) {
            Map<String, String> result = new GetVmOverrides().getVmOverrides("", "", "", "", "", "", "", "", "", "vm-123", "", null);
            verify(construction.constructed().get(0)).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
            assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
            assertTrue(StringUtilities.contains(result.get(Outputs.EXCEPTION), OPERATION_FAILED));
        }
    }

    @Test
    public void testValidateMutualExclusiveInputs() throws Exception {
        verifyFailureResultMap(new GetVmOverrides().getVmOverrides("", "", "", "", "", "", "", "", "vmName", "vm-123", "", null));
    }

    private void verifyFailureResultMap(Map<String, String> result) {
        assertEquals(PROVIDE_ONE_OR_NONE_EXCEPTION, result.get(RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(EXCEPTION), PROVIDE_ONE_OR_NONE_EXCEPTION));
    }
}
