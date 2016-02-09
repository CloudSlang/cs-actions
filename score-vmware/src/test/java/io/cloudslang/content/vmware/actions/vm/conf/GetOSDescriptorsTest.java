package io.cloudslang.content.vmware.actions.vm.conf;

/**
 * Created by Mihai Tusa.
 * 1/14/2016.
 */

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GetOSDescriptors.class)
public class GetOSDescriptorsTest {
    private GetOSDescriptors getOSDescriptors = new GetOSDescriptors();

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testSuccessfullyGetsOSDescriptors() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString())).thenReturn(resultMap);

        resultMap = getOSDescriptors.getOsDescriptors("", "", "", "", "", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock).getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString());
    }

    @Test
    public void testGetOSDescriptorsProtocolException() throws Exception {
        Map<String, String> resultMap = getOSDescriptors.getOsDescriptors("", "", "myProtocol", "", "", "", "", "", "");

        assertNotNull(resultMap);
        verify(vmServiceMock, never()).getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString());
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
