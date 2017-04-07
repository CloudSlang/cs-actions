/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.vm.utils;

/**
 * Created by Mihai Tusa.
 * 1/14/2016.
 */

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.GuestService;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GetOSDescriptors.class)
public class GetOSDescriptorsTest {
    private GetOSDescriptors getOSDescriptors;

    @Before
    public void init() {
        getOSDescriptors = new GetOSDescriptors();
    }

    @After
    public void tearDown() {
        getOSDescriptors = null;
    }

    @Mock
    private GuestService guestServiceMock;

    @Mock
    private VmService vmServiceMock;

    @Test
    public void testSuccessfullyGetsOSDescriptors() throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        whenNew(VmService.class).withNoArguments().thenReturn(vmServiceMock);
        when(vmServiceMock.getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString())).thenReturn(resultMap);

        resultMap = getOSDescriptors.getOsDescriptors("", "", "", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, times(1)).getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString());

        assertNotNull(resultMap);
    }

    @Test
    public void testGetOSDescriptorsProtocolException() throws Exception {
        Map<String, String> resultMap = getOSDescriptors.getOsDescriptors("", "", "myProtocol", "", "", "", "", "", "", "", null);

        verify(vmServiceMock, never()).getOsDescriptors(any(HttpInputs.class), any(VmInputs.class), anyString());

        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get("returnCode")));
        assertEquals("Unsupported protocol value: [myProtocol]. Valid values are: https, http.", resultMap.get("returnResult"));
    }
}
