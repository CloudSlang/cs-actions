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
import io.cloudslang.content.vmware.services.utils.VmUtils;
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

import java.util.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mihai Tusa.
 * 1/08/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VmService.class, FindObjects.class, GetObjectProperties.class, VmConfigSpecs.class, VirtualMachineRelocateSpec.class, VmUtils.class})
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

    @Mock
    private VmUtils utilsMock;

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
    public void createVMSuccess() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.createVM(httpInputsMock,
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
    public void createVMFailure() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class),
                any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class),
                any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.createVM(httpInputsMock,
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
    public void deleteVMSuccess() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVM(httpInputsMock,
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
    public void deleteVMFailure() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(false);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(vmMorMock);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVM(httpInputsMock,
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
    public void deleteVMNotFound() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        when(FindObjects.findObject(any(VimPortType.class),
                any(ServiceContent.class),
                anyString(),
                anyString())).thenReturn(null);
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = vmService.deleteVM(httpInputsMock,
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
    public void powerOnVMSuccess() throws Exception {
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
    public void powerOnVMtFailure() throws Exception {
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
    public void powerOnVMtNotFound() throws Exception {
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
    public void powerOffVMSuccess() throws Exception {
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
    public void powerOffVMFailure() throws Exception {
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
    public void powerOffVMtNotFound() throws Exception {
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
    public void getOsDescriptorsSuccess() throws Exception {
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
    public void listVMsAndTemplatesSuccess() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);
        Set<String> virtualMachineNamesList = new HashSet<>();
        virtualMachineNamesList.add("firstVM");
        virtualMachineNamesList.add("secondVM");

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(any(ManagedObjectReference.class), anyString())).thenReturn(inContainerByTypeMock);
        when(inContainerByTypeMock.keySet()).thenReturn(virtualMachineNamesList);

        Map<String, String> results = vmService.listVMsAndTemplates(httpInputsMock,
                new VmInputs.VmInputsBuilder().build(), "");

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("firstVM,secondVM", results.get("returnResult"));
    }

    @Test
    public void listVMsAndTemplatesEmptyList() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);
        Set<String> virtualMachineNamesList = new HashSet<>();

        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(any(ManagedObjectReference.class), anyString())).thenReturn(inContainerByTypeMock);
        when(inContainerByTypeMock.keySet()).thenReturn(virtualMachineNamesList);

        Map<String, String> results = vmService.listVMsAndTemplates(httpInputsMock,
                new VmInputs.VmInputsBuilder().build(), "");

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("No VM found in datacenter.", results.get("returnResult"));
    }

    @Test
    public void getVMDetailsSuccess() throws Exception {
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
        assertTrue(results.get("returnResult").contains("\"numEths\":\"4\""));
        assertTrue(results.get("returnResult").contains("\"numDisks\":\"2\""));
        assertTrue(results.get("returnResult").contains("\"vmUuid\":\"a3e76177-5020-41a3-ac2a-59c6303c8415\""));
        assertTrue(results.get("returnResult").contains("\"isTemplate\":\"true\""));
        assertTrue(results.get("returnResult").contains("\"virtualMachineFullName\":\"Ubuntu Linux (64-bit)\""));
        assertTrue(results.get("returnResult").contains("\"dataStore\":\"AbCdEf123-vc6-2\""));
        assertTrue(results.get("returnResult").contains("\"vmMemorySize\":\"8192\""));
        assertTrue(results.get("returnResult").contains("\"vmPathName\":\"[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx\""));
    }

    @Test
    public void getVMDetailsEmpty() throws Exception {
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

        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
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
    public void updateVMCpu() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setCpuAllocation(any(ResourceAllocationInfo.class));
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
    public void updateVMMemory() throws Exception {
        VmService vmService = getVmServiceWithTaskResult(true);

        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setCpuAllocation(any(ResourceAllocationInfo.class));
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
    public void updateVMNotSupported() throws Exception {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Unsupported operation specified for CPU or memory device. " +
                "The CPU or memory can only be updated.");

        VmService vmService = getVmServiceWithTaskResult(true);

        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        vmService.updateVM(httpInputsMock, new VmInputs.VmInputsBuilder().withOperation("add").withDevice("memory").build());
    }

    @Test
    public void cloneVMSuccess() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);
        VirtualMachineRelocateSpec vmRelocateSpecMock = PowerMockito.mock(VirtualMachineRelocateSpec.class);
        VirtualMachineCloneSpec cloneSpeckMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        Map<String, ManagedObjectReference> testMap = new HashMap<>();

        VmService vmService = createVmServiceForCloneVM(true, folderMock, resourcePoolMock, hostMock, dataStoreMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(eq(folderMock), eq("Folder")))
                .thenReturn(getPopulatedMor(testMap, "folderKey", "Folder", "testFolder"));
        when(getMOREFMock.inContainerByType(eq(resourcePoolMock), eq("ResourcePool")))
                .thenReturn(getPopulatedMor(testMap, "resourcePoolKey", "ResourcePool", "testResourcePool"));
        when(getMOREFMock.inContainerByType(eq(hostMock), eq("HostSystem")))
                .thenReturn(getPopulatedMor(testMap, "hostKey", "HostSystem", "testHost"));
        when(getMOREFMock.inContainerByType(eq(dataStoreMock), eq("datastore")))
                .thenReturn(getPopulatedMor(testMap, "dataStoreKey", "datastore", "testDataStore"));
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(VirtualMachineRelocateSpec.class).withNoArguments().thenReturn(vmRelocateSpecMock);

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("toCloneVM")
                .withCloneName("cloneVM")
                .withFolderName("testFolder")
                .withCloneHost("testHost")
                .withCloneResourcePool("testResourcePool")
                .withCloneDataStore("testDataStore")
                .build();

        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(configSpecsMock.getCloneSpec(vmInputs, vmRelocateSpecMock)).thenReturn(cloneSpeckMock);
        when(utilsMock.getMorObject(eq(testMap), eq("Folder"))).thenReturn(testMap.get("folderKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("ResourcePool"))).thenReturn(testMap.get("resourcePoolKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("HostSystem"))).thenReturn(testMap.get("hostKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("datastore"))).thenReturn(testMap.get("dataStoreKey"));
        when(vimPortMock.cloneVMTask(vmMorMock, folderMock, "cloneVM", cloneSpeckMock)).thenReturn(taskMorMock);

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        assertNotNull(results);
        verifyConnection();
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [toCloneVM] VM was successfully cloned. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void cloneVMFailure() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);
        VirtualMachineRelocateSpec vmRelocateSpecMock = PowerMockito.mock(VirtualMachineRelocateSpec.class);
        VirtualMachineCloneSpec cloneSpeckMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        Map<String, ManagedObjectReference> testMap = new HashMap<>();

        VmService vmService = createVmServiceForCloneVM(false, folderMock, resourcePoolMock, hostMock, dataStoreMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(vmMorMock);
        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(eq(folderMock), eq("Folder")))
                .thenReturn(getPopulatedMor(testMap, "folderKey", "Folder", "testFolder"));
        when(getMOREFMock.inContainerByType(eq(resourcePoolMock), eq("ResourcePool")))
                .thenReturn(getPopulatedMor(testMap, "resourcePoolKey", "ResourcePool", "testResourcePool"));
        when(getMOREFMock.inContainerByType(eq(hostMock), eq("HostSystem")))
                .thenReturn(getPopulatedMor(testMap, "hostKey", "HostSystem", "testHost"));
        when(getMOREFMock.inContainerByType(eq(dataStoreMock), eq("datastore")))
                .thenReturn(getPopulatedMor(testMap, "dataStoreKey", "datastore", "testDataStore"));
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(VirtualMachineRelocateSpec.class).withNoArguments().thenReturn(vmRelocateSpecMock);

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("toCloneVM")
                .withCloneName("cloneVM")
                .withFolderName("testFolder")
                .withCloneHost("testHost")
                .withCloneResourcePool("testResourcePool")
                .withCloneDataStore("testDataStore")
                .build();

        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(configSpecsMock.getCloneSpec(vmInputs, vmRelocateSpecMock)).thenReturn(cloneSpeckMock);
        when(utilsMock.getMorObject(eq(testMap), eq("Folder"))).thenReturn(testMap.get("folderKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("ResourcePool"))).thenReturn(testMap.get("resourcePoolKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("HostSystem"))).thenReturn(testMap.get("hostKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("datastore"))).thenReturn(testMap.get("dataStoreKey"));
        when(vimPortMock.cloneVMTask(vmMorMock, folderMock, "cloneVM", cloneSpeckMock)).thenReturn(taskMorMock);

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        assertNotNull(results);
        verify(connectionResourcesMock, atMost(3)).getVimPortType();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [toCloneVM] VM could not be cloned.", results.get("returnResult"));
    }

    @Test
    public void cloneVMNotFound() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);
        VirtualMachineRelocateSpec vmRelocateSpecMock = PowerMockito.mock(VirtualMachineRelocateSpec.class);
        VirtualMachineCloneSpec cloneSpeckMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        Map<String, ManagedObjectReference> testMap = new HashMap<>();

        VmService vmService = createVmServiceForCloneVM(true, folderMock, resourcePoolMock, hostMock, dataStoreMock);
        when(FindObjects.findObject(any(VimPortType.class), any(ServiceContent.class), anyString(), anyString()))
                .thenReturn(null);
        when(connectionResourcesMock.getGetMOREF()).thenReturn(getMOREFMock);
        when(getMOREFMock.inContainerByType(eq(folderMock), eq("Folder")))
                .thenReturn(getPopulatedMor(testMap, "folderKey", "Folder", "testFolder"));
        when(getMOREFMock.inContainerByType(eq(resourcePoolMock), eq("ResourcePool")))
                .thenReturn(getPopulatedMor(testMap, "resourcePoolKey", "ResourcePool", "testResourcePool"));
        when(getMOREFMock.inContainerByType(eq(hostMock), eq("HostSystem")))
                .thenReturn(getPopulatedMor(testMap, "hostKey", "HostSystem", "testHost"));
        when(getMOREFMock.inContainerByType(eq(dataStoreMock), eq("datastore")))
                .thenReturn(getPopulatedMor(testMap, "dataStoreKey", "datastore", "testDataStore"));
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(VirtualMachineRelocateSpec.class).withNoArguments().thenReturn(vmRelocateSpecMock);

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("toCloneVM")
                .withCloneName("cloneVM")
                .withFolderName("testFolder")
                .withCloneHost("testHost")
                .withCloneResourcePool("testResourcePool")
                .withCloneDataStore("testDataStore")
                .build();

        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(configSpecsMock.getCloneSpec(vmInputs, vmRelocateSpecMock)).thenReturn(cloneSpeckMock);
        when(utilsMock.getMorObject(eq(testMap), eq("Folder"))).thenReturn(testMap.get("folderKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("ResourcePool"))).thenReturn(testMap.get("resourcePoolKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("HostSystem"))).thenReturn(testMap.get("hostKey"));
        when(utilsMock.getMorObject(eq(testMap), eq("datastore"))).thenReturn(testMap.get("dataStoreKey"));
        when(vimPortMock.cloneVMTask(vmMorMock, folderMock, "cloneVM", cloneSpeckMock)).thenReturn(taskMorMock);

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        assertNotNull(results);
        verify(connectionResourcesMock, atMost(2)).getVimPortType();
        verify(connectionResourcesMock).getConnection();
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [toCloneVM] VM.", results.get("returnResult"));
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

    private VmService createVmServiceForCloneVM(final boolean isDone,
                                                final ManagedObjectReference folderMock,
                                                final ManagedObjectReference resourcePoolMock,
                                                final ManagedObjectReference hostMock,
                                                final ManagedObjectReference dataStoreMock) {
        return new VmService() {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources,
                                                  ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }

            ManagedObjectReference getMorFolder(VmInputs vmInputs, ConnectionResources connectionResources, VmUtils utils) {
                return folderMock;
            }

            ManagedObjectReference getMorResourcePool(VmInputs vmInputs, ConnectionResources connectionResources, VmUtils utils) {
                return resourcePoolMock;
            }

            ManagedObjectReference getMorHost(VmInputs vmInputs, ConnectionResources connectionResources,
                                              ManagedObjectReference vmMor, VmUtils utils) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
                return hostMock;
            }

            ManagedObjectReference getMorDataStore(VmInputs vmInputs, ConnectionResources connectionResources,
                                                   ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
                return dataStoreMock;
            }
        };
    }

    private Map<String, ManagedObjectReference> getPopulatedMor(Map<String, ManagedObjectReference> map,
                                                                String key, String type, String value) {
        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setType(type);
        mor.setValue(value);
        map.put(key, mor);

        return map;
    }
}