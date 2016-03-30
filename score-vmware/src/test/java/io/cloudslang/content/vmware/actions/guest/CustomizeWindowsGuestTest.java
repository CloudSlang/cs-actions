package io.cloudslang.content.vmware.actions.guest;

import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;
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
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 3/29/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomizeWindowsGuest.class)
public class CustomizeWindowsGuestTest {
    private CustomizeWindowsGuest windowsGuest = new CustomizeWindowsGuest();

    @Mock
    private GuestService guestServiceMock;

    @Test
    public void customizeWindowsGuestSuccess() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(GuestService.class).withNoArguments().thenReturn(guestServiceMock);
        when(guestServiceMock.customizeWinVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class)))
                .thenReturn(resultMap);

        resultMap = windowsGuest.customizeWindowsGuest("", "", "", "", "", "", "", "noreboot", "", "", "", "", "", "",
                "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "");

        verify(guestServiceMock).customizeWinVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class));

        assertNotNull(resultMap);
    }

    @Test
    public void customizeWindowsGuestProtocolFailure() throws Exception {
        Map<String, String> resultMap = windowsGuest.customizeWindowsGuest("", "", "myProtocol", "", "", "", "", "noreboot",
                "", "", "", "", "", "", "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "");

        verify(guestServiceMock, never()).customizeWinVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }

    @Test
    public void customizeWindowsGuestRebootException() throws Exception {
        Map<String, String> resultMap = windowsGuest.customizeWindowsGuest("", "", "", "", "", "", "", "myIncorrectOption",
                "", "", "", "", "", "", "", "", "", "perServer", "", "", "", "", "", "", "", "", "", "", "");

        verify(guestServiceMock, never()).customizeWinVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("No enum constant io.cloudslang.content.vmware.entities.RebootOption.MYINCORRECTOPTION", resultMap.get("returnResult"));
    }

    @Test
    public void customizeWindowsGuestLicenseException() throws Exception {
        Map<String, String> resultMap = windowsGuest.customizeWindowsGuest("", "", "", "", "", "", "", "reboot",
                "", "", "", "", "", "", "", "", "", "myIncorrectOption", "", "", "", "", "", "", "", "", "", "", "");

        verify(guestServiceMock, never()).customizeWinVM(any(HttpInputs.class), any(VmInputs.class), any(GuestInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("No enum constant io.cloudslang.content.vmware.entities.LicenseDataMode.MYINCORRECTOPTION", resultMap.get("returnResult"));
    }
}