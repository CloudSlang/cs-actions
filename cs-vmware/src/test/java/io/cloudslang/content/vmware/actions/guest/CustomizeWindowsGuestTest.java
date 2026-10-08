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


package io.cloudslang.content.vmware.actions.guest;

import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;
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
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockConstruction;

/**
 * Created by Mihai Tusa.
 * 3/29/2016.
 */
@ExtendWith(MockitoExtension.class)
public class CustomizeWindowsGuestTest {
    private CustomizeWindowsGuest windowsGuest;

    @BeforeEach
    public void init() {
        windowsGuest = new CustomizeWindowsGuest();
    }

    @AfterEach
    public void tearDown() {
        windowsGuest = null;
    }

    @Mock
    private GuestService guestServiceMock;

    @Test
    public void customizeWindowsGuestSuccess() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();
        try (MockedConstruction<GuestService> construction = mockConstruction(GuestService.class,
                (mock, context) -> when(mock.customizeVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class), anyBoolean())).thenReturn(expectedResultMap))) {
            Map<String, String> resultMap = windowsGuest.customizeWindowsGuest("", "", "", "", "", "", "", "", "noreboot", "", "", "", "", "", "",
                    "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "", null);
            assertEquals(1, construction.constructed().size());
            verify(construction.constructed().get(0), times(1)).customizeVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class), anyBoolean());
            assertEquals(expectedResultMap, resultMap);
        }
    }

    @Test
    public void customizeWindowsGuestProtocolFailure() throws Exception {
        Map<String, String> resultMap = windowsGuest.customizeWindowsGuest("", "", "myProtocol", "", "", "", "", "", "noreboot",
                "", "", "", "", "", "", "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(guestServiceMock, never()).customizeVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class), anyBoolean());

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
