/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.vmware.actions.cluster;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GetVmOverrides.class)
public class GetVmOverridesTest {

    private static final String OPERATION_FAILED = "Operation failed!";
    private static final String PROVIDE_ONE_OR_NONE_EXCEPTION = "Virtual Machine identification inputs are mutually exclusive! Provide only one or none.";

    private GetVmOverrides action;

    @Mock
    private ClusterComputeResourceService service;

    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    @Before
    public void setUp() {
        action = new GetVmOverrides();
    }

    @After
    public void tearDown() {
        action = null;
        service = null;
    }

    @Test
    public void testSuccessModifyVmOverrides() throws Exception {
        String expectedReturnResult = "expectMe";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("returnResult", expectedReturnResult);
        expectedResult.put("returnCode", "0");
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(service);
        doReturn(expectedReturnResult).when(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));

        Map<String, String> actual = action.getVmOverrides("", "", "", "", "", "", "", "", "vmName", "", "", null);

        verifyNew(ClusterComputeResourceService.class).withNoArguments();
        verify(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(expectedResult, actual);
    }

    @Test
    public void testSuccessModifyVmOverridesWithNoVmDetails() throws Exception {
        String expectedReturnResult = "expectMe2";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("returnResult", expectedReturnResult);
        expectedResult.put("returnCode", "0");
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(service);
        doReturn(expectedReturnResult).when(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));

        Map<String, String> actual = action.getVmOverrides("", "", "", "", "", "", "", "", "", "", "", null);

        verifyNew(ClusterComputeResourceService.class).withNoArguments();
        verify(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(expectedResult, actual);
    }

    @Test
    public void testFailureModifyVmOverrides() throws Exception {
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(service);
        doThrow(new Exception(OPERATION_FAILED)).when(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));

        Map<String, String> result = action.getVmOverrides("", "", "", "", "", "", "", "", "", "vm-123", "", null);

        verifyNew(ClusterComputeResourceService.class).withNoArguments();
        verify(service).getVmOverride(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(Outputs.EXCEPTION), OPERATION_FAILED));
    }

    @Test
    public void testValidateMutualExclusiveInputs() throws Exception {
        verifyFailureResultMap(action.getVmOverrides("", "", "", "", "", "", "", "", "vmName", "vm-123", "", null));
    }

    private void verifyFailureResultMap(Map<String, String> result) {
        assertEquals(PROVIDE_ONE_OR_NONE_EXCEPTION, result.get(RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(EXCEPTION), PROVIDE_ONE_OR_NONE_EXCEPTION));
    }
}
