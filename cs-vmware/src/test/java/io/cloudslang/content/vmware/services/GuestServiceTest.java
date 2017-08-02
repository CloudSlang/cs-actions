/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.services;

import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.GuestConfigSpecs;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static java.lang.Integer.parseInt;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 3/28/2016.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(PowerMockRunner.class)
@PrepareForTest({MorObjectHandler.class, GuestConfigSpecs.class, GuestService.class})
public class GuestServiceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private ConnectionResources connectionResourcesMock;

    @Mock
    private VimPortType vimPortMock;

    @Mock
    private ManagedObjectReference serviceInstanceMock;

    @Mock
    private ManagedObjectReference taskMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private MorObjectHandler morObjectHandlerMock;

    @Mock
    private ManagedObjectReference vmMorMock;

    @Mock
    private GuestConfigSpecs guestConfigSpecsMock;

    @Mock
    private CustomizationSpec customizationSpecMock;

    @Mock
    private HttpInputs httpInputsMock;

    @Mock
    private VimPortType vimPortTypeMock;

    private GuestService guestService;

    @Before
    public void init() throws Exception {
        whenNew(ConnectionResources.class).withArguments(any(HttpInputs.class), any(VmInputs.class)).thenReturn(connectionResourcesMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);

        guestService = new GuestService();
    }

    @After
    public void tearDown() {
        guestService = null;
    }

    @Test
    public void customizeWinVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortTypeMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getWinCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortTypeMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortTypeMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testWinVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, true);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testWinVMName"));
        verify(guestConfigSpecsMock, times(1)).getWinCustomizationSpec(eq(guestInputs));
        verify(vimPortTypeMock, times(1)).checkCustomizationSpec(eq(vmMorMock), eq(customizationSpecMock));
        verify(vimPortTypeMock, times(1)).customizeVMTask(eq(vmMorMock), eq(customizationSpecMock));
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The [testWinVMName] VM was successfully customized. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void customizeWinVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortTypeMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getWinCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortTypeMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortTypeMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testWinVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, true);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testWinVMName"));
        verify(guestConfigSpecsMock, times(1)).getWinCustomizationSpec(eq(guestInputs));
        verify(vimPortTypeMock, times(1)).checkCustomizationSpec(eq(vmMorMock), eq(customizationSpecMock));
        verify(vimPortTypeMock, times(1)).customizeVMTask(eq(vmMorMock), eq(customizationSpecMock));
        verify(taskMock, times(1)).getValue();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: The [testWinVMName] VM could not be customized.", results.get("returnResult"));
    }

    @Test
    public void customizeWinVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortTypeMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getWinCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testWinVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, true);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(guestConfigSpecsMock, never()).getWinCustomizationSpec(any(GuestInputs.class));
        verify(vimPortTypeMock, never()).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(vimPortTypeMock, never()).customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(taskMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [testWinVMName] VM.", results.get("returnResult"));
    }

    @Test
    public void customizeLinuxVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortTypeMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getLinuxCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortTypeMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortTypeMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testLinuxVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, false);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testLinuxVMName"));
        verify(guestConfigSpecsMock, times(1)).getLinuxCustomizationSpec(guestInputs);
        verify(vimPortTypeMock, times(1)).checkCustomizationSpec(eq(vmMorMock), eq(customizationSpecMock));
        verify(vimPortTypeMock, times(1)).customizeVMTask(eq(vmMorMock), eq(customizationSpecMock));
        verify(taskMock, times(1)).getValue();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The [testLinuxVMName] VM was successfully customized. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void customizeLinuxVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getLinuxCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testLinuxVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, false);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testLinuxVMName"));
        verify(guestConfigSpecsMock, times(1)).getLinuxCustomizationSpec(any(GuestInputs.class));
        verify(vimPortMock, times(1)).checkCustomizationSpec(eq(vmMorMock), eq(customizationSpecMock));
        verify(vimPortMock, times(1)).customizeVMTask(eq(vmMorMock), eq(customizationSpecMock));
        verify(taskMock, times(1)).getValue();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: The [testLinuxVMName] VM could not be customized.", results.get("returnResult"));
    }

    @Test
    public void customizeLinuxVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getLinuxCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testLinuxVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, false);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionMock, times(1)).disconnect();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testLinuxVMName"));
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(guestConfigSpecsMock, never()).getLinuxCustomizationSpec(any(GuestInputs.class));
        verify(vimPortMock, never()).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(vimPortMock, never()).customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(taskMock, never()).getValue();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [testLinuxVMName] VM.", results.get("returnResult"));
    }

    @Test
    public void customizeLinuxVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getLinuxCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testLinuxVMName").build();
        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder().build();

        Map<String, String> results = guestService.customizeVM(httpInputsMock, vmInputs, guestInputs, false);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testLinuxVMName"));
        verify(guestConfigSpecsMock, times(1)).getLinuxCustomizationSpec(eq(guestInputs));
        verify(vimPortMock, times(1)).checkCustomizationSpec(eq(vmMorMock), eq(customizationSpecMock));
        verify(vimPortMock, times(1)).customizeVMTask(eq(vmMorMock), eq(customizationSpecMock));
        verify(taskMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void mountToolsSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        doNothing().when(vimPortMock).mountToolsInstaller(any(ManagedObjectReference.class));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("whateverName").build();

        Map<String, String> results = guestService.mountTools(httpInputsMock, vmInputs);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("whateverName"));
        verify(vimPortMock, times(1)).mountToolsInstaller(eq(vmMorMock));
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Initiated VMware Tools Installer Mount on: whateverName.", results.get("returnResult"));
    }

    @Test
    public void mountToolsNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        PowerMockito.doNothing().when(vimPortMock).mountToolsInstaller(any(ManagedObjectReference.class));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("whateverName").build();

        Map<String, String> results = guestService.mountTools(httpInputsMock, vmInputs);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("whateverName"));
        verify(vimPortMock, never()).mountToolsInstaller(any(ManagedObjectReference.class));
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [whateverName] VM.", results.get("returnResult"));
    }

    @Test
    public void mountToolsException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        doNothing().when(vimPortMock).mountToolsInstaller(any(ManagedObjectReference.class));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("whateverName").build();

        Map<String, String> results = guestService.mountTools(httpInputsMock, vmInputs);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).mountToolsInstaller(any(ManagedObjectReference.class));
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    private ResponseHelper getResponseHelper(final ConnectionResources connectionResources, final ManagedObjectReference task, final boolean isDone) {
        return new ResponseHelper(connectionResources, task) {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void verifyConnection() {
        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(taskMock, times(1)).getValue();
        verify(connectionMock, times(1)).disconnect();
    }
}
