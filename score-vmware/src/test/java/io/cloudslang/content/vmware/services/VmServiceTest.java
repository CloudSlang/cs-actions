package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
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
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@PrepareForTest({GetObjectProperties.class, MorObjectHandler.class, VirtualMachineRelocateSpec.class,
        VmConfigSpecs.class, VmService.class, VmUtils.class})
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
    private Map<String, ManagedObjectReference> inContainerByTypeMock;

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
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class)))
                .thenReturn(hostMock);

        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);

        when(vimPortMock.createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        Map<String, String> results = vmService.createVM(httpInputsMock,
                new VmInputs.VmInputsBuilder()
                        .withDataCenterName("testDatacenter")
                        .withHostname("testHostname")
                        .withVirtualMachineName("nameToBeTested")
                        .withDataStore("testDatastore")
                        .withGuestOsId("testOS")
                        .build());

        verifyConnection();
        verify(utilsMock, times(1)).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(configSpecsMock, times(1)).getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class));
        verify(vimPortMock, times(1)).createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class));
        verify(taskMorMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: Created [nameToBeTested] VM. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void createVMFailure() throws Exception {
        ManagedObjectReference folderMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference resourcePoolMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference hostMock = PowerMockito.mock(ManagedObjectReference.class);
        ManagedObjectReference dataStoreMock = PowerMockito.mock(ManagedObjectReference.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class)))
                .thenReturn(hostMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        when(utilsMock.getMorFolder(anyString(), any(ConnectionResources.class))).thenReturn(folderMock);
        when(utilsMock.getMorResourcePool(anyString(), any(ConnectionResources.class))).thenReturn(resourcePoolMock);
        when(utilsMock.getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class)))
                .thenReturn(hostMock);
        whenNew(VmConfigSpecs.class).withNoArguments().thenReturn(configSpecsMock);
        when(configSpecsMock.getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class)))
                .thenReturn(virtualMachineConfigSpecMock);
        when(vimPortMock.createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(folderMock, resourcePoolMock, hostMock, dataStoreMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
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
        verify(utilsMock, times(1)).getMorFolder(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorResourcePool(anyString(), any(ConnectionResources.class));
        verify(utilsMock, times(1)).getMorHost(anyString(), any(ConnectionResources.class), any(ManagedObjectReference.class));
        verify(configSpecsMock, times(1)).getVmConfigSpec(any(VmInputs.class), any(ConnectionResources.class));
        verify(vimPortMock, times(1)).createVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class),
                any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: Could not create [anotherNameToBeTested] VM", results.get("returnResult"));
    }

    @Test
    public void deleteVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).destroyTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [deletedNameToBeTested] VM was deleted. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void deleteVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).destroyTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [deletedNameToBeTested] VM could not be deleted.", results.get("returnResult"));
    }

    @Test
    public void deleteVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).destroyTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [deletedNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void deleteVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.destroyTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().deleteVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("deletedNameToBeTested").build());

        verify(connectionResourcesMock, times(1)).getConnection();
        verify(morObjectHandlerMock, never()).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).destroyTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void powerOnVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [powerOnNameToBeTested] VM was successfully powered on. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void powerOnVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [powerOnNameToBeTested] VM could not be powered on.", results.get("returnResult"));
    }

    @Test
    public void powerOnVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verify(morObjectHandlerMock, never()).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void powerOnVMtNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOnVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOnNameToBeTested").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOnVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [powerOnNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).powerOffVMTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [powerOffNameToBeTested] VM was successfully powered off. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void powerOffVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).powerOffVMTask(any(ManagedObjectReference.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [powerOffNameToBeTested] VM could not be powered off.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [powerOffNameToBeTested] VM.", results.get("returnResult"));
    }

    @Test
    public void powerOffVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.powerOffVMTask(any(ManagedObjectReference.class))).thenReturn(taskMorMock);

        Map<String, String> results = new VmService().powerOffVM(httpInputsMock,
                new VmInputs.VmInputsBuilder().withVirtualMachineName("powerOffNameToBeTested").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, never()).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).powerOffVMTask(any(ManagedObjectReference.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void getOsDescriptorsSuccess() throws Exception {
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
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
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("firstDescriptorToBeTested,secondDescriptorToBeTested", results.get("returnResult"));
    }

    @Test
    public void getOsDescriptorsException() throws Exception {
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(anyString())).thenReturn(environmentBrowserMorMock);
        when(vimPortMock.queryConfigOption(any(ManagedObjectReference.class), anyString(), any(ManagedObjectReference.class)))
                .thenReturn(null);

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
        verify(configOptionsMock, never()).getGuestOSDescriptor();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
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

        verify(morObjectHandlerMock, times(1)).getSpecificObjectsMap(any(ConnectionResources.class), anyString());

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
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

        verify(morObjectHandlerMock, times(1)).getSpecificObjectsMap(any(ConnectionResources.class), anyString());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
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

        verify(morObjectHandlerMock, never()).getSpecificObjectsMap(any(ConnectionResources.class), anyString());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void getVMDetailsSuccess() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));

        ObjectContent[] objectContents = getObjectContents();
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(objectContents);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").build());

        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());

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
        assertTrue(results.get("returnResult").contains("\"ipAddress\":\"127.0.0.1\""));
    }

    @Test
    public void getVMDetailsEmpty() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(null);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").withVirtualMachineName("Ubuntu64").build());

        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not retrieve the details for: [Ubuntu64] VM.", results.get("returnResult"));
    }

    @Test
    public void getVMDetailsException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(GetObjectProperties.getObjectProperties(any(ConnectionResources.class), any(ManagedObjectReference.class),
                any(String[].class))).thenReturn(null);

        Map<String, String> results = new VmService().getVMDetails(httpInputsMock,
                new VmInputs.VmInputsBuilder().withHostname("hostname").withVirtualMachineName("Ubuntu64").build());

        verify(morObjectHandlerMock, never()).getVmMor(any(ConnectionResources.class), anyString(), anyString());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
    }

    @Test
    public void updateVMAddDisk() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(60000L);

        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualSCSIController scsiController = new VirtualSCSIController();
        virtualDevicesList.add(scsiController);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void updateVMAddCD() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();
        VirtualIDEController virtualIDEController = new VirtualIDEController();
        virtualDevicesList.add(virtualIDEController);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void updateVMAddNic() throws Exception {
        List<VirtualDevice> virtualDevicesList = new ArrayList<>();

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("add").withDevice("nic").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345",
                results.get("returnResult"));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("cd").withUpdateValue("anyCD").build());

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(virtualDevicesMock, times(1)).getVirtualDevice();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(connectionResourcesMock.getMoRefHandler()).thenReturn(moRefHandlerMock);
        when(moRefHandlerMock.entityProps(any(ManagedObjectReference.class), any(String[].class))).thenReturn(entityPropsMock);
        when(entityPropsMock.get(eq("config.hardware.device"))).thenReturn(virtualDevicesMock);
        when(virtualDevicesMock.getVirtualDevice()).thenReturn(virtualDevicesList);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("remove").withDevice("nic").withUpdateValue("eth2").build());

        verify(connectionResourcesMock, times(1)).getMoRefHandler();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(moRefHandlerMock, times(1)).entityProps(any(ManagedObjectReference.class), any(String[].class));
        verify(entityPropsMock, times(1)).get(anyString());
        verify(virtualDevicesMock, times(1)).getVirtualDevice();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: No nic named: [eth2] can be found.", results.get("returnResult"));
    }

    @Test
    public void updateVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, false));
        PowerMockito.doNothing().when(virtualMachineConfigSpecMock).setMemoryAllocation(any(ResourceAllocationInfo.class));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("memory").withUpdateValue("low").build());

        verify(connectionResourcesMock).getConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [testVM] VM.", results.get("returnResult"));
    }

    @Test
    public void updateVMAddDiskNoDataStore() throws Exception {
        DatastoreSummary datastoreSummary = new DatastoreSummary();
        datastoreSummary.setFreeSpace(20000L);

        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(entityPropsMock, times(2)).get(anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.RuntimeException: Cannot find any dataStore with: [30000] minimum amount of space available.", results.get("returnResult"));
    }

    @Test
    public void updateVMCpu() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("cpu").withUpdateValue("normal").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void updateVMMemory() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVM").withOperation("update").withDevice("memory").withUpdateValue("100").build());

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345", results.get("returnResult"));
    }

    @Test
    public void updateVMNotSupported() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        when(vimPortMock.reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class)))
                .thenReturn(taskMorMock);

        Map<String, String> results = new VmService().updateVM(httpInputsMock, new VmInputs.VmInputsBuilder().withOperation("add").withDevice("memory").build());

        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).reconfigVMTask(any(ManagedObjectReference.class), any(VirtualMachineConfigSpec.class));

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);

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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock));
        verify(taskMorMock, times(1)).getValue();

        assertNotNull(results);
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
        VirtualMachineCloneSpec cloneSpecMock = PowerMockito.mock(VirtualMachineCloneSpec.class);

        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);

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
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, times(1)).cloneVMTask(eq(vmMorMock), eq(folderMock), eq("cloneVM"), eq(cloneSpecMock));
        verify(taskMorMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [toCloneVM] VM could not be cloned.", results.get("returnResult"));
    }

    @Test
    public void cloneVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(null, null, null, null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM").build();

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verify(connectionResourcesMock).getConnection();
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).cloneVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class),
                anyString(), any(VirtualMachineCloneSpec.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [toCloneVM] VM.", results.get("returnResult"));
    }

    @Test
    public void cloneVMException() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(null);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(VmUtils.class).withNoArguments().thenReturn(utilsMock);

        VmService vmService = createResponseHelperForCreateAndCloneVM(null, null, null, null);
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMorMock, true));
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("toCloneVM").withCloneName("cloneVM").build();

        Map<String, String> results = vmService.cloneVM(httpInputsMock, vmInputs);

        verify(connectionResourcesMock).getConnection();
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(morObjectHandlerMock, never()).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(vimPortMock, never()).cloneVMTask(any(ManagedObjectReference.class), any(ManagedObjectReference.class),
                anyString(), any(VirtualMachineCloneSpec.class));
        verify(taskMorMock, never()).getValue();
        verify(connectionMock).disconnect();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("java.lang.NullPointerException", results.get("returnResult"));
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

    private VmService createResponseHelperForCreateAndCloneVM(final ManagedObjectReference folderMock,
                                                              final ManagedObjectReference resourcePoolMock,
                                                              final ManagedObjectReference hostMock,
                                                              final ManagedObjectReference dataStoreMock) {
        return new VmService() {
            ManagedObjectReference getMorFolder(String name, ConnectionResources connectionResources) {
                return folderMock;
            }

            ManagedObjectReference getMorResourcePool(String name, ConnectionResources connectionResources) {
                return resourcePoolMock;
            }

            ManagedObjectReference getMorHost(String name, ConnectionResources connectionResources, ManagedObjectReference vmMor)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
                return hostMock;
            }

            ManagedObjectReference getMorDataStore(String name, ConnectionResources connectionResources, ManagedObjectReference vmMor)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
                return dataStoreMock;
            }
        };
    }

    private ResponseHelper getResponseHelper(final ConnectionResources connectionResources,
                                             final ManagedObjectReference task,
                                             final boolean isDone) {
        return new ResponseHelper(connectionResources, task) {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources,
                                                  ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }
}