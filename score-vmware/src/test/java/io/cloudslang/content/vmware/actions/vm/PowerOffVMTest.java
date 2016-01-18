package io.cloudslang.content.vmware.actions.vm;

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;
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
 * 1/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerOffVM.class)
public class PowerOffVMTest {
    private PowerOffVM powerOffVM = new PowerOffVM();

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testPowersOffVM() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.powerOffVM(any(HttpInputs.class), any(VmInputs.class))).thenReturn(resultMap);

        resultMap = powerOffVM.powerOffVM("", "", "", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock).powerOffVM(any(HttpInputs.class), any(VmInputs.class));
    }

    @Test
    public void testPowersOffVMProtocolException() throws Exception {
        Map<String, String> resultMap = powerOffVM.powerOffVM("", "", "myProtocol", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock, never()).powerOffVM(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}