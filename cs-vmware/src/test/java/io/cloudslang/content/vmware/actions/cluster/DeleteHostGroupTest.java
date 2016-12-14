/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
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
@PrepareForTest(DeleteHostGroup.class)
public class DeleteHostGroupTest {
    private DeleteHostGroup deleteHostGroup;

    @Before
    public void init() {
        deleteHostGroup = new DeleteHostGroup();
    }

    @After
    public void tearDown() {
        deleteHostGroup = null;
    }

    @Mock
    ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testDeleteHostGroup() throws Exception {
        Map<String, String> expectedResultMap = new HashMap<>();
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(clusterComputeResourceServiceMock);
        when(clusterComputeResourceServiceMock.deleteHostGroup(any(HttpInputs.class), any(VmInputs.class))).thenReturn(expectedResultMap);

        Map<String, String> actualResultMap = deleteHostGroup.deleteHostGroup("", "", "", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, times(1)).deleteHostGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(actualResultMap);
        assertEquals(actualResultMap, expectedResultMap);
    }

    @Test
    public void testDeleteHostGroupProtocolException() throws Exception {
        Map<String, String> resultMap = deleteHostGroup.deleteHostGroup("", "", "myProtocol", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, never()).deleteHostGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testDeleteHostGroupPortException() throws Exception {
        Map<String, String> resultMap = deleteHostGroup.deleteHostGroup("", "myPort", "", "", "", "", "", "");

        verify(clusterComputeResourceServiceMock, never()).deleteHostGroup(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }
}
