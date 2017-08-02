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

import com.vmware.vim25.ArrayOfManagedObjectReference;
import com.vmware.vim25.ArrayOfVirtualDevice;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.Description;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ResourceAllocationInfo;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualCdrom;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualIDEController;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigOption;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineGuestSummary;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.VirtualSCSIController;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.MoRefHandler;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 1/08/2016.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetObjectProperties.class, MorObjectHandler.class, VirtualMachineRelocateSpec.class, VmConfigSpecs.class, VmService.class, VmUtils.class})
public class VmServiceTest {
    @Mock
    private HttpInputs httpInputsMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private ConnectionResources connectionResourcesMock;

    @Mock
    private ManagedObjectReference serviceInstanceMock;

    @Mock
    private ServiceContent serviceContentMock;

    @Mock
    private VimPortType vimPortMock;

    @Mock
    private MoRefHandler moRefHandlerMock;

    @Mock
    private Map<String, Object> entityPropsMock;

    @Mock
    private ManagedObjectReference environmentBrowserMorMock;

    @Mock
    private ManagedObjectReference taskMorMock;

    @Mock
    private ManagedObjectReference vmMorMock;

    @Mock
    private VmConfigSpecs configSpecsMock;

    @Mock
    private VirtualMachineConfigSpec virtualMachineConfigSpecMock;

    @Mock
    private VirtualMachineConfigOption configOptionsMock;

    @Mock
    private ObjectContent objectItemMock;

    @Mock
    private ArrayOfManagedObjectReference dataStoresMock;

    @Mock
    private ArrayOfVirtualDevice virtualDevicesMock;

    @Mock
    private VmUtils utilsMock;

    @Mock
    private MorObjectHandler morObjectHandlerMock;

    private List<ManagedObjectReference> dataStoresVictim;
    private ManagedObjectReference morVictim;

    @Before
    public void init() throws Exception {
        mockStatic(GetObjectProperties.class);

        whenNew(ConnectionResources.class).withArguments(anyObject(), anyObject()).thenReturn(connectionResourcesMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMorMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(vimPortMock.retrieveServiceContent(any(ManagedObjectReference.class))).thenReturn(serviceContentMock);
        when(httpInputsMock.isCloseSession()).thenReturn(true);

        dataStoresVictim = new ArrayList<>();
        morVictim = new ManagedObjectReference();
        dataStoresVictim.add(morVictim);
    }

    @After
    public void tearDown() {
        dataStoresVictim = null;
        morVictim = null;
    }

    @Test
    public void createVMSuccess() throws Exception {
        ManagedObjectReference folderMock = mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = mock(ManagedObjectReference.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class))).thenReturn(hostMock);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class))).thenReturn(virtualMachineConfigSpecMock);

        when(vimPortMock
                .createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class), any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class)).thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        Map<String, String> results = vmService.createVM(httpInputsMock,
                new VmInputs.VmInputsBuilder()
                        .withDataCenterName("testDatacenter")
                        .withHostname("testHostname")
                        .withVirtualMachineName("nameToBeTested")
                        .withDataStore("testDatastore")
                        .withGuestOsId("testOS")
                        .build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(utilsMock, times(1)).getMorFolder(anyString(), eq(connectionResourcesMock));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), eq(connectionResourcesMock));
        verify(utilsMock, times(1)).getMorHost(eq("testHostname"), eq(connectionResourcesMock), any(ManagedObjectReference.class));
        verify(configSpecsMock, times(1)).getVmConfigSpec(any(VmInputs.class), eq(connectionResourcesMock));
        verify(vimPortMock, times(1)).createVMTask(eq(folderMock), eq(virtualMachineConfigSpecMock), eq(resourcePoolMock), eq(hostMock));
        verify(taskMorMock, times(1)).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: Created [nameToBeTested] VM. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void createVMFailure() throws Exception {
        ManagedObjectReference folderMock = mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = mock(ManagedObjectReference.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class))).thenReturn(hostMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class))).thenReturn(hostMock);
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class))).thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock
                .createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class), any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));

        Map<String, String> results = vmService.createVM(httpInputsMock,
                new VmInputs.VmInputsBuilder()
                        .withDataCenterName("testDatacenter")
                        .withHostname("testHostname")
                        .withVirtualMachineName("anotherNameToBeTested")
                        .withDataStore("testDatastore")
                        .withGuestOsId("testOS")
                        .build());

        verifyConnection();
        verify(utilsMock, times(1)).getMorFolder(anyString(), eq(connectionResourcesMock));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), eq(connectionResourcesMock));
        verify(utilsMock, times(1)).getMorHost(anyString(), eq(connectionResourcesMock), any(ManagedObjectReference.class));
        verify(configSpecsMock, times(1)).getVmConfigSpec(any(VmInputs.class), eq(connectionResourcesMock));
        verify(vimPortMock, times(1)).createVMTask(eq(folderMock), eq(virtualMachineConfigSpecMock), eq(resourcePoolMock), eq(hostMock));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: Could not create [anotherNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void deleteVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("deletedNameToBeTested"));
        verify(vimPortMock, times(1)).destroyTask(eq(vmMorMock));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The [deletedNameToBeTested] VM was deleted. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void deleteVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("deletedNameToBeTested"));
        verify(vimPortMock, times(1)).destroyTask(eq(vmMorMock));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: The [deletedNameToBeTested] VM could not be deleted.", results.get("returnResult"));
    }

    @Test
    public void deleteVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("deletedNameToBeTested"));
        verify(vimPortMock, never()).destroyTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [deletedNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void deleteVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).destroyTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void powerOnVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOnNameToBeTested"));
        verify(vimPortMock, times(1)).powerOnVMTask(eq(vmMorMock), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The [powerOnNameToBeTested] VM was successfully powered on. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void powerOnVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOnNameToBeTested"));
        verify(vimPortMock, times(1)).powerOnVMTask(eq(vmMorMock), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: The [powerOnNameToBeTested] VM could not be powered on.", results.get("returnResult"));
    }

    @Test
    public void powerOnVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void powerOnVMtNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOnNameToBeTested"));
        verify(vimPortMock, never()).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [powerOnNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOffNameToBeTested"));
        verify(vimPortMock, times(1)).powerOffVMTask(eq(vmMorMock));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The [powerOffNameToBeTested] VM was successfully powered off. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOffNameToBeTested"));
        verify(vimPortMock, times(1)).powerOffVMTask(eq(vmMorMock));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Failure: The [powerOffNameToBeTested] VM could not be powered off.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("powerOffNameToBeTested"));
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [powerOffNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock, new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void getOsDescriptorsSuccess() throws Exception {
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(anyString())).thenReturn(environmentBrowserMorMock);
        when(vimPortMock.queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class)))
                .thenReturn(configOptionsMock);

        List<GuestOsDescriptor> guestOSDescriptors = new ArrayList<>();
        populateOsDescriptorsList(guestOSDescriptors, "firstDescriptorToBeTested");
        populateOsDescriptorsList(guestOSDescriptors, "secondDescriptorToBeTested");
        when(configOptionsMock.getGuestOSDescriptor()).thenReturn(guestOSDescriptors);

        Map<String, String> results = new VmService().getOsDescriptors(httpInputsMock,
                new VmInputs.VmInputsBuilder().withDataCenterName("datacenter").withHostname("hostname").build(), "");

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class));
        verify(configOptionsMock, times(1)).getGuestOSDescriptor();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("firstDescriptorToBeTested,secondDescriptorToBeTested", results.get("returnResult"));
    }

    @Test
    public void getOsDescriptorsException() throws Exception {
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(anyString())).thenReturn(environmentBrowserMorMock);
        when(vimPortMock.queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class))).thenReturn(null);

        List<GuestOsDescriptor> guestOSDescriptors = new ArrayList<>();
        populateOsDescriptorsList(guestOSDescriptors, "firstDescriptorToBeTested");
        populateOsDescriptorsList(guestOSDescriptors, "secondDescriptorToBeTested");
        when(configOptionsMock.getGuestOSDescriptor()).thenReturn(guestOSDescriptors);

        Map<String, String> results = new VmService().getOsDescriptors(httpInputsMock,
                new VmInputs.VmInputsBuilder().withDataCenterName("datacenter").withHostname("hostname").build(), "");

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get("environmentBrowser");
        verify(vimPortMock, times(1)).queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class));
        verify(configOptionsMock, never()).getGuestOSDescriptor();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void listVMsAndTemplatesSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);

        Map<String, ManagedObjectReference> vmsMap = new HashMap<>();
        vmsMap.put("firstVM", null);
        vmsMap.put("secondVM", null);

        when(morObjectHandlerMock.getSpecificObjectsMap(any(ConnectionResources.class), anyString())).thenReturn(vmsMap);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        Map<String, String> results = new VmService().listVMsAndTemplates(httpInputsMock, new VmInputs.VmInputsBuilder().build(), "");

        verify(morObjectHandlerMock, times(1)).getSpecificObjectsMap(eq(connectionResourcesMock), eq("VirtualMachine"));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("firstVM,secondVM", results.get("returnResult"));
    }

    @Test
    public void listVMsAndTemplatesEmptyMap() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);

        Map<String, ManagedObjectReference> vmsMap = new HashMap<>();

        when(morObjectHandlerMock.getSpecificObjectsMap(any(ConnectionResources.class), anyString())).thenReturn(vmsMap);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        Map<String, String> results = new VmService().listVMsAndTemplates(httpInputsMock, new VmInputs.VmInputsBuilder().build(), "");

        verify(morObjectHandlerMock, times(1)).getSpecificObjectsMap(eq(connectionResourcesMock), eq("VirtualMachine"));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("No VM found in datacenter.", results.get("returnResult"));
    }

    @Test
    public void listVMsAndTemplatesException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);

        Map<String, ManagedObjectReference> vmsMap = new HashMap<>();

        when(morObjectHandlerMock.getSpecificObjectsMap(any(ConnectionResources.class), anyString())).thenReturn(vmsMap);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        Map<String, String> results = new VmService().listVMsAndTemplates(httpInputsMock, new VmInputs.VmInputsBuilder().build(), "");

        verify(morObjectHandlerMock, never()).getSpecificObjectsMap(eq(connectionResourcesMock), eq("VirtualMachine"));

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void getVMDetailsSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        ObjectContent[] objectContents = getObjectContents();
        when(GetObjectProperties
                .getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(objectContents);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock, new VmInputs.VmInputsBuilder().withHostname("hostname").build());

        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), anyString());

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertTrue(results.get("returnResult").contains("\"vmId\":\"vm-123\""));
        assertTrue(results.get("returnResult").contains("\"numCPUs\":\"3\""));
        assertTrue(results.get("returnResult").contains("\"numEths\":\"4\""));
        assertTrue(results.get("returnResult").contains("\"numDisks\":\"2\""));
        assertTrue(results.get("returnResult").contains("\"vmUuid\":\"a3e76177-5020-41a3-ac2a-59c6303c8415\""));
        assertTrue(results.get("returnResult").contains("\"isTemplate\":\"true\""));
        assertTrue(results.get("returnResult").contains("\"virtualMachineFullName\":\"Ubuntu Linux (64-bit)\""));
        assertTrue(results.get("returnResult").contains("\"dataStore\":\"AbCdEf123-vc6-2\""));
        assertTrue(results.get("returnResult").contains("\"vmMemorySize\":\"8192\""));
        assertTrue(results.get("returnResult").contains("\"vmPathName\":\"[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx\""));
        assertTrue(results.get("returnResult").contains("\"ipAddress\":\"127.0.0.1\""));
    }

    @Test
    public void getVMDetailsEmpty() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(null);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").withVirtualMachineName("Ubuntu64").build());

        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), anyString());

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not retrieve the details for: [Ubuntu64] VM.", results.get("returnResult"));
    }

    @Test
    public void getVMDetailsException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(GetObjectProperties
                .getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(null);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock, new VmInputs.VmInputsBuilder().withHostname("hostname").withVirtualMachineName("Ubuntu64").build());

        verify(morObjectHandlerMock, never()).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), anyString());

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    @Test
    public void updateVMAddDisk() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(60000L);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualSCSIController scsiController = new VirtualSCSIController();
        virtualDevicesList.add(scsiController);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("disk")
                .withUpdateValue("someDisk")
                .withLongVmDiskSize("40000")
                .withDiskMode("persistent")
                .build());

        verifyConnection();
        verify(connectionResourcesMock, times(3)).getMoRefHandler();
        verify(moRefHandlerMock, times(3)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(3)).get(anyString());
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddCD() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("add").withDevice("cd").build());

        verifyConnection();
        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddNic() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("add").withDevice("nic").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMADeleteDisk() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(60000L);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualSCSIController scsiController = new VirtualSCSIController();
        virtualDevicesList.add(scsiController);
        VirtualDevice virtualDevice = new VirtualDisk();
        Description description = new Description();
        description.setLabel("toRemoveDisk");
        virtualDevice.setDeviceInfo(description);
        virtualDevicesList.add(virtualDevice);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("disk").withUpdateValue("toRemoveDisk").build());

        verifyConnection();
        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(virtualDevicesMock, times(1)).getVirtualDevice();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMDeleteCD() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);
        VirtualCdrom cdrom = new VirtualCdrom();
        Description description = new Description();
        description.setLabel("toRemoveCD");
        cdrom.setDeviceInfo(description);
        virtualDevicesList.add(cdrom);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("cd").withUpdateValue("toRemoveCD").build());

        verifyConnection();
        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMDeleteNic() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualEthernetCard ethernetCard = new VirtualEthernetCard();
        Description description = new Description();
        description.setLabel("eth1");
        ethernetCard.setDeviceInfo(description);
        virtualDevicesList.add(ethernetCard);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("nic").withUpdateValue("eth1").build());

        verifyConnection();
        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMDiskNotFound() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(60000L);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualSCSIController scsiController = new VirtualSCSIController();
        virtualDevicesList.add(scsiController);
        VirtualDevice virtualDevice = new VirtualDisk();
        Description description = new Description();
        description.setLabel("toRemoveDisk");
        virtualDevice.setDeviceInfo(description);
        virtualDevicesList.add(virtualDevice);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("disk").withUpdateValue("anotherDisk").build());

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: No disk device named: [anotherDisk] can be found.", results.get("returnResult"));
    }

    @Test
    public void updateVMCDNotFound() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);
        VirtualCdrom cdrom = new VirtualCdrom();
        Description description = new Description();
        description.setLabel("toRemoveCD");
        cdrom.setDeviceInfo(description);
        virtualDevicesList.add(cdrom);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("cd").withUpdateValue("anyCD").build());

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(virtualDevicesMock, times(1)).getVirtualDevice();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: No optical device named: [anyCD] can be found.", results.get("returnResult"));
    }

    @Test
    public void updateVMNicNotFound() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualEthernetCard ethernetCard = new VirtualEthernetCard();
        Description description = new Description();
        description.setLabel("eth1");
        ethernetCard.setDeviceInfo(description);
        virtualDevicesList.add(ethernetCard);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("nic").withUpdateValue("eth2").build());

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(virtualDevicesMock, times(1)).getVirtualDevice();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: No nic named: [eth2] can be found.", results.get("returnResult"));
    }

    @Test
    public void updateVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("memory").withUpdateValue("low").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("Could not find the [testVM] VM.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddDiskNoDataStore() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(20000L);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("add").withDevice("disk").withUpdateValue("someDisk")
                .withLongVmDiskSize("30000").withDiskMode("persistent").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(entityPropsMock, times(2)).get(anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: Cannot find any dataStore with: [30000] minimum amount of space available.", results.get("returnResult"));
    }

    @Test
    public void updateVMCpu() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("cpu").withUpdateValue("normal").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMMemory() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("memory").withUpdateValue("100").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void updateVMNotSupported() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder().withOperation("add").withDevice("memory").build());

        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: Unsupported operation specified for CPU or memory device. The CPU or memory can only be updated.", results.get("returnResult"));
    }

    @Test
    public void cloneVMSuccess() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);
        VirtualMachineRelocateSpec vmRelocateSpecMock = PowerMockito.mock(VirtualMachineRelocateSpec.class);
        VirtualMachineCloneSpec cloneSpecMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class)))
                .thenReturn(hostMock);
        when(utilsMock.getMorDataStore(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class), any(VmInputs.class)))
                .thenReturn(dataStoreMock);

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM")
                .withFolderName("testFolder").withCloneHost("testHost").withCloneResourcePool("testResourcePool")
                .withCloneDataStore("testDataStore").build();

        when(utilsMock.getVirtualMachineRelocateSpec(eq(resourcePoolMock), eq(hostMock), eq(dataStoreMock), eq(vmInputs)))
                .thenReturn(vmRelocateSpecMock);
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getCloneSpec(eq(vmInputs), eq(vmRelocateSpecMock))).thenReturn(cloneSpecMock);
        when(vimPortMock.cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verifyConnection();
        verify(utilsMock, times(1)).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(utilsMock, times(1)).getMorDataStore(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class), any(VmInputs.class));
        verify(utilsMock, times(1)).getVirtualMachineRelocateSpec(eq(resourcePoolMock), eq(hostMock), eq(dataStoreMock), eq(vmInputs));
        verify(configSpecsMock, times(1)).getCloneSpec(eq(vmInputs), eq(vmRelocateSpecMock));
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock));
        verify(taskMorMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(0, parseInt(results.get("returnCode")));
        assertEquals("Success: The [toCloneVM] VM was successfully cloned. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void cloneVMFailure() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);
        VirtualMachineRelocateSpec vmRelocateSpecMock = PowerMockito.mock(VirtualMachineRelocateSpec.class);
        VirtualMachineCloneSpec cloneSpecMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));

        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class)))
                .thenReturn(hostMock);
        when(utilsMock.getMorDataStore(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class), any(VmInputs.class)))
                .thenReturn(dataStoreMock);

        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM").build();

        when(utilsMock.getVirtualMachineRelocateSpec(eq(resourcePoolMock), eq(hostMock), eq(dataStoreMock), eq(vmInputs)))
                .thenReturn(vmRelocateSpecMock);
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getCloneSpec(eq(vmInputs), eq(vmRelocateSpecMock))).thenReturn(cloneSpecMock);
        when(vimPortMock.cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verifyConnection();
        verify(utilsMock, times(1)).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(utilsMock, times(1)).getMorDataStore(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class), any(VmInputs.class));
        verify(utilsMock, times(1)).getVirtualMachineRelocateSpec(eq(resourcePoolMock), eq(hostMock), eq(dataStoreMock), eq(vmInputs));
        verify(configSpecsMock, times(1)).getCloneSpec(eq(vmInputs), eq(vmRelocateSpecMock));
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock));
        verify(taskMorMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("Failure: The [toCloneVM] VM could not be cloned.", results.get("returnResult"));
    }

    @Test
    public void cloneVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(null, null, null, null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM").build();

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verify(connectionResourcesMock).getConnection();
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).cloneVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class),
                anyString(), any(VirtualMachineCloneSpec.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
        assertEquals("Could not find the [toCloneVM] VM.", results.get("returnResult"));
    }

    @Test
    public void cloneVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(null, null, null, null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM").build();

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verify(connectionResourcesMock).getConnection();
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).cloneVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class),
                anyString(), any(VirtualMachineCloneSpec.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, parseInt(results.get("returnCode")));
    }

    @Test
    public void createVMSnapshot() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.createSnapshotTask(any(ManagedObjectReference.class), anyString(), anyString(), anyBoolean(), anyBoolean())).thenReturn(taskMorMock);

        VmInputs inputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testMachine")
                .withSnapshotName("snapshotName")
                .withSnapshotDescription("dummy")
                .build();

        Map<String, String> results = new VmService().createVMSnapshot(httpInputsMock, inputs);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testMachine"));
        verify(vimPortMock, times(1)).createSnapshotTask(eq(vmMorMock), eq("snapshotName"), eq("dummy"), eq(false), eq(false));
        verifyNoMoreInteractions(morObjectHandlerMock);
        verifyNoMoreInteractions(vimPortMock);

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == 0);
        assertEquals("Success: The snapshot of [testMachine] VM was successfully created. The taskId is: task-12345.", results.get("returnResult"));
    }

    @Test
    public void createVMSnapshotVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.createSnapshotTask(any(ManagedObjectReference.class), anyString(), anyString(), anyBoolean(), anyBoolean())).thenReturn(taskMorMock);

        VmInputs inputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testMachine").build();

        Map<String, String> results = new VmService().createVMSnapshot(httpInputsMock, inputs);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getMor(eq(connectionResourcesMock), eq("VirtualMachine"), eq("testMachine"));
        verify(vimPortMock, never()).createSnapshotTask(any(ManagedObjectReference.class), anyString(), anyString(), anyBoolean(), anyBoolean());
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();
        verifyNoMoreInteractions(connectionResourcesMock);
        verifyNoMoreInteractions(morObjectHandlerMock);
        verifyNoMoreInteractions(connectionMock);

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
        assertEquals("Could not find the [testMachine] VM.", results.get("returnResult"));
    }

    @Test
    public void createVMSnapshotException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class)
                .withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.createSnapshotTask(any(ManagedObjectReference.class), anyString(), anyString(), anyBoolean(), anyBoolean())).thenReturn(taskMorMock);

        VmInputs inputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testMachine").build();

        Map<String, String> results = new VmService().createVMSnapshot(httpInputsMock, inputs);

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, never()).getMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock, times(1)).disconnect();

        assertNotNull(results);
        assertTrue(parseInt(results.get("returnCode")) == -1);
    }

    private void verifyConnection() {
        verify(connectionResourcesMock, atMost(3)).getVimPortType();
        verify(taskMorMock, times(1)).getValue();
        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionMock, times(1)).disconnect();
        verifyNoMoreInteractions(connectionMock);
    }

    private void populateOsDescriptorsList(List<GuestOsDescriptor> guestOSDescriptorsList, String guestOsId) {
        GuestOsDescriptor guestOsDescriptor = new GuestOsDescriptor();
        guestOsDescriptor.setId(guestOsId);
        guestOSDescriptorsList.add(guestOsDescriptor);
    }

    private VirtualMachineConfigSummary getVMConfigSummary() {
        VirtualMachineConfigSummary virtualMachineConfigSummary = new VirtualMachineConfigSummary();
        virtualMachineConfigSummary.setGuestId("Ubuntu64");
        virtualMachineConfigSummary.setGuestFullName("Ubuntu Linux (64-bit)");
        virtualMachineConfigSummary.setUuid("a3e76177-5020-41a3-ac2a-59c6303c8415");
        virtualMachineConfigSummary.setNumCpu(3);
        virtualMachineConfigSummary.setMemorySizeMB(8192);
        virtualMachineConfigSummary.setNumEthernetCards(4);
        virtualMachineConfigSummary.setNumVirtualDisks(2);
        virtualMachineConfigSummary.setVmPathName("[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx");
        virtualMachineConfigSummary.setTemplate(true);
        return virtualMachineConfigSummary;
    }

    private ObjectContent[] getObjectContents() {
        ObjectContent[] objectContents = new ObjectContent[2];
        ObjectContent objectContent0 = new ObjectContent();
        objectContents[0] = objectContent0;
        objectContents[1] = objectItemMock;

        List<DynamicProperty> vmProperties = new ArrayList<>();
        DynamicProperty dynamicProperty = new DynamicProperty();
        vmProperties.add(dynamicProperty);

        VirtualMachineConfigSummary virtualMachineConfigSummary = getVMConfigSummary();
        VirtualMachineSummary virtualMachineSummary = new VirtualMachineSummary();
        virtualMachineSummary.setConfig(virtualMachineConfigSummary);

        VirtualMachineGuestSummary guestSummary = new VirtualMachineGuestSummary();
        guestSummary.setIpAddress("127.0.0.1");
        virtualMachineSummary.setGuest(guestSummary);

        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setValue("vm-123");
        virtualMachineSummary.setVm(mor);

        dynamicProperty.setVal(virtualMachineSummary);

        when(objectItemMock.getPropSet()).thenReturn(vmProperties);

        return objectContents;
    }

    private VmService createResponseHelperForCreateAndCloneVM(final ManagedObjectReference folderMock, final ManagedObjectReference resourcePoolMock,
                                                              final ManagedObjectReference hostMock, final ManagedObjectReference dataStoreMock) {
        return new VmService() {};
    }

    private ResponseHelper getResponseHelper(final ConnectionResources connectionResources, final ManagedObjectReference task, final boolean isDone) {
        return new ResponseHelper(connectionResources, task) {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }
}
