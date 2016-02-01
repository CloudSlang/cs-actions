package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.GetMOREF;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.FindObjects;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 1/08/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VmService.class, FindObjects.class, GetObjectProperties.class})
public class VmServiceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    private GetMOREF getMOREFMock;

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
    private Map<String, ManagedObjectReference> inContainerByTypeMock;

    @Mock
    private ObjectContent objectItemMock;

    @Mock
    private ArrayOfManagedObjectReference dataStoresMock;

    @Mock
    private ArrayOfVirtualDevice virtualDevicesMock;

    private List<ManagedObjectReference> dataStoresVictim;
    private ManagedObjectReference morVictim;

    @Before
    public void init() throws Exception {
        mockStatic(FindObjects.class);
        mockStatic(GetObjectProperties.class);

        whenNew(ConnectionResources.class).withArguments(anyObject(), anyObject()).thenReturn(connectionResourcesMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMorMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(vimPortMock.retrieveServiceContent(any(ManagedObjectReference.class))).thenReturn(serviceContentMock);

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
    public void testSuccessfullyCreatesVirtualMachine() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.createVirtualMachine(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("nameToBeTested").build());

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

        Map<String, String> results = vmService.createVirtualMachine(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("anotherNameToBeTested").build());

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

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

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

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

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

        Map<String, String> results = vmService.deleteVirtualMachine(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

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

        Map<String, String> results = vmService.powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

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

        Map<String, String> results = vmService.powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

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

        Map<String, String> results = vmService.powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verify(connectionResourcesMock).getServiceInstance();
        verify(connectionResourcesMock, atMost(2)).getVimPortType();
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

        Map<String, String> results = vmService.powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

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

        Map<String, String> results = vmService.powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

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

        Map<String, String> results = vmService.powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verify(connectionResourcesMock).getServiceInstance();
        verify(connectionResourcesMock, atMost(2)).getVimPortType();
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

        Map<String, String> results = vmService.getOsDescriptors(httpInputsMock,
                new VmInputs.VmInputsBuilder().withDataCenterName("datacenter").withHostname("hostname").build(), "");

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("firstDescriptorToBeTested,secondDescriptorToBeTested", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyListVMsAndTemplates() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);
        Set<String> virtualMachineNamesList = new HashSet<>();
        virtualMachineNamesList.add("firstVM");
        virtualMachineNamesList.add("secondVM");

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(any(ManagedObjectReference.class), anyString())).thenReturn(inContainerByTypeMock);
        when(inContainerByTypeMock.keySet()).thenReturn(virtualMachineNamesList);

        Map<String, String> results = vmService.listVirtualMachinesAndTemplates(httpInputsMock,
                new VmInputs.VmInputsBuilder().build(), "");

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("firstVM,secondVM", results.get("returnResult"));
    }

    @Test
    public void testEmptyListVMsAndTemplates() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);
        Set<String> virtualMachineNamesList = new HashSet<>();

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(any(ManagedObjectReference.class), anyString())).thenReturn(inContainerByTypeMock);
        when(inContainerByTypeMock.keySet()).thenReturn(virtualMachineNamesList);

        Map<String, String> results = vmService.listVirtualMachinesAndTemplates(httpInputsMock,
                new VmInputs.VmInputsBuilder().build(), "");

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("No VM found in datacenter.", results.get("returnResult"));
    }

    @Test
    public void testSuccessfullyGetsVMDetails() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);
        ObjectContent[] objectContents = getObjectContents();

        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(objectContents);

        Map<String, String> results = vmService.getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").build());

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertTrue(results.get("returnResult").contains("\"vmId\":\"vm-123\""));
        assertTrue(results.get("returnResult").contains("\"numCPUs\":\"3\""));
        assertTrue(results.get("returnResult").contains("\"numEths\":\"2\""));
        assertTrue(results.get("returnResult").contains("\"vmUuid\":\"a3e76177-5020-41a3-ac2a-59c6303c8415\""));
        assertTrue(results.get("returnResult").contains("\"isTemplate\":\"true\""));
        assertTrue(results.get("returnResult").contains("\"virtualMachineFullName\":\"Ubuntu Linux (64-bit)\""));
        assertTrue(results.get("returnResult").contains("\"dataStore\":\"AbCdEf123-vc6-2\""));
        assertTrue(results.get("returnResult").contains("\"vmMemorySize\":\"8192\""));
        assertTrue(results.get("returnResult").contains("\"vmPathName\":\"[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx\""));
    }

    @Test
    public void testGetVMDetailsEmpty() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(null);

        Map<String, String> results = vmService.getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").withVirtualMachineName("Ubuntu64").build());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not retrieve the details for: [Ubuntu64] VM.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddDisk() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(60000L);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualSCSIController scsiController = new VirtualSCSIController();
        virtualDevicesList.add(scsiController);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("disk")
                .withUpdateValue("someDisk")
                .withLongVmDiskSize("40000")
                .withDiskMode("persistent")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMAddCD() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("cd")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMAddNic() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualEthernetCard ethernetCard = new VirtualEthernetCard();
        virtualDevicesList.add(ethernetCard);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("nic")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMADeleteDisk() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

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

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("disk")
                .withUpdateValue("toRemoveDisk")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMDeleteCD() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);
        VirtualCdrom cdrom = new VirtualCdrom();
        Description description = new Description();
        description.setLabel("toRemoveCD");
        cdrom.setDeviceInfo(description);
        virtualDevicesList.add(cdrom);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("cd")
                .withUpdateValue("toRemoveCD")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMDeleteNic() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualEthernetCard ethernetCard = new VirtualEthernetCard();
        Description description = new Description();
        description.setLabel("eth1");
        ethernetCard.setDeviceInfo(description);
        virtualDevicesList.add(ethernetCard);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("nic")
                .withUpdateValue("eth1")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateVMDiskNotFound() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("No disk device named: [anotherDisk] can be found.");

        VmService vmService = getVmServiceWithTaskResult(true);

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

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("disk")
                .withUpdateValue("anotherDisk")
                .build());
    }

    @Test
    public void updateVMCDNotFound() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("No optical device named: [anyCD] can be found.");

        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);
        VirtualCdrom cdrom = new VirtualCdrom();
        Description description = new Description();
        description.setLabel("toRemoveCD");
        cdrom.setDeviceInfo(description);
        virtualDevicesList.add(cdrom);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("cd")
                .withUpdateValue("anyCD")
                .build());
    }

    @Test
    public void updateVMNicNotFound() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("No nic named: [eth2] can be found.");

        VmService vmService = getVmServiceWithTaskResult(true);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualEthernetCard ethernetCard = new VirtualEthernetCard();
        Description description = new Description();
        description.setLabel("eth1");
        ethernetCard.setDeviceInfo(description);
        virtualDevicesList.add(ethernetCard);

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("remove")
                .withDevice("nic")
                .withUpdateValue("eth2")
                .build());

    }

    @Test
    public void updateVMNotFound() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(null);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("update")
                .withDevice("memory")
                .withUpdateValue("low")
                .build());

        verify(connectionResourcesMock, atMost(2)).getVimPortType();
        verify(connectionResourcesMock).getConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [testVM] VM.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddDiskNoDataStore() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Can find any dataStore with: [30000] minimum amount of space available.");

        VmService vmService = getVmServiceWithTaskResult(true);

        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(20000L);

        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.entityProps(any(ManagedObjectReference.class), any(String[].class)))
                .thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("datastore"))).thenReturn(dataStoresMock);
        when(dataStoresMock.getManagedObjectReference()).thenReturn(dataStoresVictim);
        when(entityPropsMock.get(eq("summary"))).thenReturn(datastoreSummary);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("add")
                .withDevice("disk")
                .withUpdateValue("someDisk")
                .withLongVmDiskSize("30000")
                .withDiskMode("persistent")
                .build());
    }

    @Test
    public void updateCpu() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        doNothing().when(virtualMachineConfigSpecMock).setCpuAllocation(any(ResourceAllocationInfo.class));
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("update")
                .withDevice("cpu")
                .withUpdateValue("normal")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateMemory() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        doNothing().when(virtualMachineConfigSpecMock).setCpuAllocation(any(ResourceAllocationInfo.class));
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM")
                .withOperation("update")
                .withDevice("memory")
                .withUpdateValue("100")
                .build());

        verifyConnection();
        verify(vimPortMock).retrieveServiceContent(any(ManagedObjectReference.class));
        verify(vimPortMock).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void updateNotSupported() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Unsupported operation specified for CPU or memory device. " +
                "The CPU or memory can only be updated.");

        VmService vmService = getVmServiceWithTaskResult(true);

        doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder().withOperation("add").withDevice("memory").build());
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
        verify(connectionResourcesMock, atMost(3)).getVimPortType();
        verify(taskMorMock).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();
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

        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setValue("vm-123");
        virtualMachineSummary.setVm(mor);

        dynamicProperty.setVal(virtualMachineSummary);

        when(objectItemMock.getPropSet()).thenReturn(vmProperties);

        return objectContents;
    }
}