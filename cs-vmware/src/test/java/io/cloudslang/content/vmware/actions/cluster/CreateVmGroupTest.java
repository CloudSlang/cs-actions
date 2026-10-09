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

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockConstruction;

/**
 * Created by pinteae on 10/12/2016.
 */
@ExtendWith(MockitoExtension.class)
public class CreateVmGroupTest {
    private CreateVmGroup createVmGroup;

    @BeforeEach
    public void init() {
        createVmGroup = new CreateVmGroup();
    }

    @AfterEach
    public void tearDown() {
        createVmGroup = null;
    }

    @Mock
    private ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testCreateVmGroup() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();
        try (MockedConstruction<ClusterComputeResourceService> construction = mockConstruction(ClusterComputeResourceService.class,
                (mock, context) -> when(mock.createVmGroup(any(HttpInputs.class), any(VmInputs.class), any(List.class))).thenReturn(expectedResultMap))) {
            Map<String, String> actualResultMap = createVmGroup.createVmGroup("", "", "", "", "", "", "", "", "", "", "", null);
            assertEquals(1, construction.constructed().size());
            verify(construction.constructed().get(0), times(1)).createVmGroup(any(HttpInputs.class), any(VmInputs.class), any(List.class));
            assertEquals(expectedResultMap, actualResultMap);
        }
    }

    @Test
    public void testCreateVmGroupProtocolException() throws Exception {
        Map<String, String> resultMap = createVmGroup.createVmGroup("", "", "myProtocol", "", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).createVmGroup(any(HttpInputs.class), any(VmInputs.class), any(List.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testCreateVmGroupPortException() throws Exception {
        Map<String, String> resultMap = createVmGroup.createVmGroup("", "myPort", "", "", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).createVmGroup(any(HttpInputs.class), any(VmInputs.class), any(List.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }
}
