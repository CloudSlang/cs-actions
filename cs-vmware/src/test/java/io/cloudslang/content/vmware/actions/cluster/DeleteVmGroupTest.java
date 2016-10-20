package io.cloudslang.content.vmware.actions.cluster;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DeleteVmGroup.class)
public class DeleteVmGroupTest {
    private DeleteVmGroup deleteVmGroup;

    @Before
    public void init() {
        deleteVmGroup = new DeleteVmGroup();
    }

    @After
    public void tearDown() {
        deleteVmGroup = null;
    }

    @Mock
    ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testDeleteVmGroup() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(clusterComputeResourceServiceMock);
        when(clusterComputeResourceServiceMock.deleteVmGroup(any(HttpInputs.class), any(VmInputs.class))).thenReturn(expectedResultMap);

        Map<String, String> actualResultMap = deleteVmGroup.deleteVmGroup("", "", "", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, times(1)).deleteVmGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(actualResultMap);
        assertEquals(expectedResultMap, actualResultMap);
    }

    @Test
    public void testDeleteVmGroupProtocolException() throws Exception {
        Map<String, String> resultMap = deleteVmGroup.deleteVmGroup("", "", "myProtocol", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, never()).deleteVmGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testDeleteVmGroupPortException() throws Exception {
        Map<String, String> resultMap = deleteVmGroup.deleteVmGroup("", "myPort", "", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, never()).deleteVmGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }
}
