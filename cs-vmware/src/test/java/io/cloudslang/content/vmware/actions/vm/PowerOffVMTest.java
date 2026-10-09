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


package io.cloudslang.content.vmware.actions.vm;

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockConstruction;

/**
 * Created by Mihai Tusa.
 * 1/12/2016.
 */
@ExtendWith(MockitoExtension.class)
public class PowerOffVMTest {
    private PowerOffVM powerOffVM;

    @BeforeEach
    public void init() {
        powerOffVM = new PowerOffVM();
    }

    @AfterEach
    public void tearDown() {
        powerOffVM = null;
    }

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testPowersOffVM() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();
        try (MockedConstruction<VmService> construction = mockConstruction(VmService.class,
                (mock, context) -> when(mock.powerOffVM(any(HttpInputs.class), any(VmInputs.class))).thenReturn(expectedResultMap))) {
            Map<String, String> resultMap = powerOffVM.powerOffVM("", "", "", "", "", "", "", "", null);
            assertEquals(1, construction.constructed().size());
            verify(construction.constructed().get(0), times(1)).powerOffVM(any(HttpInputs.class), any(VmInputs.class));
            assertEquals(expectedResultMap, resultMap);
        }
    }

    @Test
    public void testPowersOffVMProtocolException() throws Exception {
        Map<String, String> resultMap = powerOffVM.powerOffVM("", "", "myProtocol", "", "", "", "", "", null);

        verify(vmServiceMock, never()).powerOffVM(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
