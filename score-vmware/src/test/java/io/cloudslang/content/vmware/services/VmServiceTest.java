package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.GetMOREF;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.VmConfigSpecs;
import io.cloudslang.content.vmware.utils.FindObjects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 1/08/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VmService.class, FindObjects.class})
public class VmServiceTest {

    @Mock
    private HttpInputs httpInputsMock;

    @Mock
    private ConnectionResources connectionResourcesMock;

    @Mock
    private VimPortType vimPortMock;

    @Mock
    private VmConfigSpecs configSpecsMock;

    @Mock
    private VirtualMachineConfigSpec virtualMachineConfigSpecMock;

    @Mock
    private ManagedObjectReference taskMorMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private ManagedObjectReference serviceInstanceMock;

    @Mock
    private ServiceContent serviceContentMock;

    @Mock
    private ManagedObjectReference vmMorMock;

    @Mock
    private GetMOREF getMOREFMock;

    @Mock
    private Map<String, Object> entityPropsMock;

    @Mock
    private ManagedObjectReference environmentBrowserMorMock;

    @Mock
    private VirtualMachineConfigOption configOptionsMock;

    @Before
    public void init() throws Exception {
        mockStatic(FindObjects.class);

        whenNew(ConnectionResources.class).withArguments(anyObject(), anyObject()).thenReturn(connectionResourcesMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMorMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(vimPortMock.retrieveServiceContent(any(ManagedObjectReference.class))).thenReturn(serviceContentMock);
    }

    @Test
    public void testSuccessfullyCreatesVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.createVirtualMachine(httpInputsMock, new VmInputs("nameToBeTested"));

        verifyConnection();
        verify(connectionResourcesMock).getVmFolderMor();
        verify(connectionResourcesMock).getResourcePoolMor();
        verify(connectionResourcesMock).getHostMor();
        verify(configSpecsMock).getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class));
        verify(vimPortMock).createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: Created [nameToBeTested] VM. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void testFailureToCreateVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.createVirtualMachine(httpInputsMock, new VmInputs("anotherNameToBeTested"));

        verifyConnection();
        verify(connectionResourcesMock).getVmFolderMor();
        verify(connectionResourcesMock).getResourcePoolMor();
        verify(connectionResourcesMock).getHostMor();
        verify(configSpecsMock).getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class));
        verify(vimPortMock).createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: Creating [anotherNameToBeTested] VM", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyDeletesVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock, new VmInputs("deletedNameToBeTested"));

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).destroyTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [deletedNameToBeTested] VM was deleted. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void testFailureToDeleteVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock, new VmInputs("deletedNameToBeTested"));

        verifyConnection();
        verify(connectionResourcesMock).getServiceInstance();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).destroyTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [deletedNameToBeTested] VM could not be deleted.", results.get("returnResult"));
    }

    @Test
    public void testNotFoundVirtualMachineToDelete() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(null);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock, new VmInputs("deletedNameToBeTested"));

        verify(connectionResourcesMock).getServiceInstance();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock, never()).destroyTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [deletedNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyPowersOnVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOnVM(httpInputsMock, new VmInputs("powerOnNameToBeTested"));

        verifyConnection();
        verify(connectionResourcesMock).getServiceInstance();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [powerOnNameToBeTested] VM was successfully powered on. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void testFailureToPowerOnVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOnVM(httpInputsMock, new VmInputs("powerOnNameToBeTested"));

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [powerOnNameToBeTested] VM could not be powered on.", results.get("returnResult"));
    }

    @Test
    public void testNotFoundVirtualMachineToPowerOn() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(null);
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOnVM(httpInputsMock, new VmInputs("powerOnNameToBeTested"));

        verify(connectionResourcesMock).getServiceInstance();
        verify(connectionResourcesMock).getVimPortType();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock, never()).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [powerOnNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyPowersOffVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOffVM(httpInputsMock, new VmInputs("powerOffNameToBeTested"));

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).powerOffVMTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [powerOffNameToBeTested] VM was successfully powered off. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void testFailureToPowerOffVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOffVM(httpInputsMock, new VmInputs("powerOffNameToBeTested"));

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).powerOffVMTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [powerOffNameToBeTested] VM could not be powered off.", results.get("returnResult"));
    }

    @Test
    public void testNotFoundVirtualMachineToPowerOff() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(null);
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.powerOffVM(httpInputsMock, new VmInputs("powerOffNameToBeTested"));

        verify(connectionResourcesMock).getServiceInstance();
        verify(connectionResourcesMock).getVimPortType();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [powerOffNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyGetsOSDescriptors() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(anyString())).thenReturn(environmentBrowserMorMock);
        when(vimPortMock
                .queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class)))
                .thenReturn(configOptionsMock);

        List<GuestOsDescriptor> guestOSDescriptors = new ArrayList<>();
        populateOsDescriptorsList(guestOSDescriptors, "firstDescriptorToBeTested");
        populateOsDescriptorsList(guestOSDescriptors, "secondDescriptorToBeTested");
        when(configOptionsMock.getGuestOSDescriptor()).thenReturn(guestOSDescriptors);

        Map<String, String> results = vmService.getOsDescriptors(httpInputsMock, new VmInputs("datacenter", "hostname"), "");

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("firstDescriptorToBeTested,secondDescriptorToBeTested", results.get("returnResult"));
    }

    private VmService getVmServiceWithTaskResult(final boolean isDone) {
        return new VmService() {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources,
                                                  ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }

    private void verifyConnection() {
        verify(connectionResourcesMock).getVimPortType();
        verify(taskMorMock).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();
    }

    private void populateOsDescriptorsList(List<GuestOsDescriptor> guestOSDescriptorsList, String guestOsId) {
        GuestOsDescriptor guestOsDescriptor = new GuestOsDescriptor();
        guestOsDescriptor.setId(guestOsId);
        guestOSDescriptorsList.add(guestOsDescriptor);
    }
}