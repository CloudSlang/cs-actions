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
 * 1/11/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CreateVM.class)
public class CreateVMTest {
    private CreateVM createVM = new CreateVM();

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testCreatesVM() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.createVM(any(HttpInputs.class), any(VmInputs.class))).thenReturn(resultMap);

        resultMap = createVM.createVM("", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock).createVM(any(HttpInputs.class), any(VmInputs.class));
    }

    @Test
    public void testCreatesVMProtocolException() throws Exception {
        Map<String, String> resultMap = createVM.createVM("", "", "myProtocol", "", "", "", "", "", "", "", "", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock, never()).createVM(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }

    @Test
    public void testCreatesVMIntException() throws Exception {
        Map<String, String> resultMap = createVM.createVM("", "", "", "", "", "", "", "", "", "", "", "", "2147483648", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock, never()).createVM(any(HttpInputs.class), any(VmInputs.class));
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("The input value must be a int number.", resultMap.get("returnResult"));
    }
}