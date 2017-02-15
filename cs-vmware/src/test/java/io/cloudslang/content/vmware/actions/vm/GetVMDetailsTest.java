/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.vm;

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 1/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GetVMDetails.class)
public class GetVMDetailsTest {
    private GetVMDetails getVMDetails;

    @Before
    public void init() {
        getVMDetails = new GetVMDetails();
    }

    @After
    public void tearDown() {
        getVMDetails = null;
    }

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testSuccessfullyGetsVMDetails() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);

        when(vmServiceMock.getVMDetails(any(HttpInputs.class), any(VmInputs.class))).thenReturn(resultMap);

        resultMap = getVMDetails.getVMDetails("", "", "", "", "", "", "", "");

        verify(vmServiceMock, times(1)).getVMDetails(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
    }

    @Test
    public void testGetVMDetailsProtocolException() throws Exception {
        Map<String, String> resultMap = getVMDetails.getVMDetails("", "", "myProtocol", "", "", "", "", "");

        verify(vmServiceMock, never()).getVMDetails(any(HttpInputs.class), any(VmInputs.class));

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
