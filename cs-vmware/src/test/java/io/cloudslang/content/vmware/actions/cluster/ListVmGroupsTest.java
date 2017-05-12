/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.cluster;

import com.vmware.vim25.ClusterVmGroup;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/12/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ListVmGroups.class)
public class ListVmGroupsTest {
    private ListVmGroups listVmGroups;

    @Before
    public void init() {
        listVmGroups = new ListVmGroups();
    }

    @After
    public void tearDown() {
        listVmGroups = null;
    }

    @Mock
    private ClusterComputeResourceService clusterComputeResourceServiceMock;

    @Test
    public void testListGroupVms() throws Exception {
        String expectedReturnResult = new String();

        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(clusterComputeResourceServiceMock);
        when(clusterComputeResourceServiceMock.listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterVmGroup.class))).thenReturn(expectedReturnResult);

        Map<String, String> actualResultMap = listVmGroups.listVmGroups("", "", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, times(1)).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterVmGroup.class));

        assertNotNull(actualResultMap);
        assertEquals(expectedReturnResult, actualResultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testListGroupVmsProtocolException() throws Exception {
        Map<String, String> resultMap = listVmGroups.listVmGroups("", "", "myProtocol", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterVmGroup.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get(OutputNames.RETURN_RESULT));
    }

    @Test
    public void testListGroupVmsPortException() throws Exception {
        Map<String, String> resultMap = listVmGroups.listVmGroups("", "myPort", "", "", "", "", "", "", "", null);

        verify(clusterComputeResourceServiceMock, never()).listGroups(any(HttpInputs.class), any(String.class), any(String.class), eq(ClusterVmGroup.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(OutputNames.RETURN_CODE)));
        assertEquals(NOT_ZERO_OR_POSITIVE_NUMBER, resultMap.get(OutputNames.RETURN_RESULT));
    }

}
