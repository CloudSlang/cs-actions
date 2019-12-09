

package io.cloudslang.content.vmware.actions.guest;

import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 3/29/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomizeWindowsGuest.class)
public class CustomizeWindowsGuestTest {
    private CustomizeWindowsGuest windowsGuest;

    @Before
    public void init() {
        windowsGuest = new CustomizeWindowsGuest();
    }

    @After
    public void tearDown() {
        windowsGuest = null;
    }

    @Mock
    private GuestService guestServiceMock;

    @Test
    public void customizeWindowsGuestSuccess() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(GuestService.class).withNoArguments().thenReturn(guestServiceMock);
        when(guestServiceMock.customizeVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class), anyBoolean())).thenReturn(resultMap);

        resultMap = windowsGuest.customizeWindowsGuest("", "", "", "", "", "", "", "", "noreboot", "", "", "", "", "", "",
                "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "", null);

        verify(guestServiceMock, times(1)).customizeVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class), anyBoolean());

        assertNotNull(resultMap);
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
