/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.MoRefHandler;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VmServiceTest {
    @Mock private HttpInputs httpInputs;
    @Mock private Connection connection;
    @Mock private VimPortType vimPort;
    @Mock private MoRefHandler moRefHandler;

    private final ManagedObjectReference root = new ManagedObjectReference();
    private final ManagedObjectReference vm = new ManagedObjectReference();
    private final ManagedObjectReference task = new ManagedObjectReference();
    private final ManagedObjectReference host = new ManagedObjectReference();
    private final ManagedObjectReference folder = new ManagedObjectReference();
    private final ManagedObjectReference pool = new ManagedObjectReference();
    private final ManagedObjectReference datastore = new ManagedObjectReference();
    private final VmService service = new VmService();
    private MockedConstruction<ConnectionResources> resources;
    private MockedConstruction<WaitForValues> taskWait;
    private VmInputs expectedInputs;
    private boolean taskSucceeded;

    @BeforeEach
    void setUp() {
        task.setValue("task-12345");
        taskSucceeded = true;
        when(httpInputs.isCloseSession()).thenReturn(true);
        resources = mockConstruction(ConnectionResources.class, (mock, context) -> {
            assertSame(httpInputs, context.arguments().get(0));
            assertSame(expectedInputs, context.arguments().get(1));
            lenient().when(mock.getMorRootFolder()).thenReturn(root);
            lenient().when(mock.getMoRefHandler()).thenReturn(moRefHandler);
            lenient().when(mock.getVimPortType()).thenReturn(vimPort);
            lenient().when(mock.getHostMor()).thenReturn(host);
            when(mock.getConnection()).thenReturn(connection);
        });
        taskWait = mockConstruction(WaitForValues.class, (mock, context) -> {
            assertSame(connection, context.arguments().get(0));
            when(mock.wait(eq(task), any(String[].class), any(String[].class), any(Object[][].class)))
                    .thenAnswer(invocation -> new Object[]{taskSucceeded ? TaskInfoState.SUCCESS : TaskInfoState.ERROR, null});
        });
    }

    @AfterEach
    void tearDown() {
        taskWait.close();
        resources.close();
    }

    @Test void createVMSuccess() throws Exception { createVm(true); }
    @Test void createVMFailure() throws Exception { createVm(false); }

    private void createVm(boolean success) throws Exception {
        taskSucceeded = success;
        expectedInputs = inputs().withDataCenterName("testDatacenter").withHostname("testHostname")
                .withDataStore("testDatastore").withGuestOsId("testOS").build();
        VirtualMachineConfigSpec config = new VirtualMachineConfigSpec();
        when(vimPort.createVMTask(folder, config, pool, host)).thenReturn(task);
        try (MockedConstruction<VmUtils> utils = mockConstruction(VmUtils.class, (mock, context) -> {
            when(mock.getMorFolder(eq(expectedInputs.getFolderName()), any(ConnectionResources.class))).thenReturn(folder);
            when(mock.getMorResourcePool(eq(expectedInputs.getResourcePool()), any(ConnectionResources.class))).thenReturn(pool);
            when(mock.getMorHost(eq("testHostname"), any(ConnectionResources.class), isNull())).thenReturn(host);
        }); MockedConstruction<VmConfigSpecs> specs = mockConstruction(VmConfigSpecs.class, (mock, context) ->
                when(mock.getVmConfigSpec(eq(expectedInputs), any(ConnectionResources.class))).thenReturn(config))) {
            Map<String, String> result = service.createVM(httpInputs, expectedInputs);
            assertResult(result, success ? "0" : "-1", success ?
                    "Success: Created [testVM] VM. The taskId is: task-12345" : "Failure: Could not create [testVM] VM");
            ConnectionResources resource = resource();
            assertEquals(1, utils.constructed().size());
            verify(utils.constructed().get(0)).getMorFolder(expectedInputs.getFolderName(), resource);
            verify(utils.constructed().get(0)).getMorResourcePool(expectedInputs.getResourcePool(), resource);
            verify(utils.constructed().get(0)).getMorHost("testHostname", resource, null);
            assertEquals(1, specs.constructed().size());
            verify(specs.constructed().get(0)).getVmConfigSpec(expectedInputs, resource);
            verify(vimPort).createVMTask(folder, config, pool, host);
            verifyTaskAndDisconnect();
        }
    }

    @Test void deleteVMSuccess() throws Exception { taskOperation("delete", true, true, false); }
    @Test void deleteVMFailure() throws Exception { taskOperation("delete", false, true, false); }
    @Test void deleteVMNotFound() throws Exception { taskOperation("delete", true, false, false); }
    @Test void deleteVMException() throws Exception { taskOperation("delete", true, true, true); }
    @Test void powerOnVMSuccess() throws Exception { taskOperation("on", true, true, false); }
    @Test void powerOnVMFailure() throws Exception { taskOperation("on", false, true, false); }
    @Test void powerOnVMException() throws Exception { taskOperation("on", true, true, true); }
    @Test void powerOnVMtNotFound() throws Exception { taskOperation("on", true, false, false); }
    @Test void powerOffVMSuccess() throws Exception { taskOperation("off", true, true, false); }
    @Test void powerOffVMFailure() throws Exception { taskOperation("off", false, true, false); }
    @Test void powerOffVMNotFound() throws Exception { taskOperation("off", true, false, false); }
    @Test void powerOffVMException() throws Exception { taskOperation("off", true, true, true); }

    private void taskOperation(String operation, boolean success, boolean found, boolean lookupFailure) throws Exception {
        taskSucceeded = success;
        expectedInputs = inputs().build();
        if (lookupFailure) {
            stubLookupFailure();
        } else {
            stubVm(found);
        }
        if (found && !lookupFailure) {
            switch (operation) {
                case "delete": when(vimPort.destroyTask(vm)).thenReturn(task); break;
                case "on": when(vimPort.powerOnVMTask(vm, null)).thenReturn(task); break;
                case "off": when(vimPort.powerOffVMTask(vm)).thenReturn(task); break;
                default: fail("Unknown task operation");
            }
        }
        Map<String, String> result;
        String successVerb;
        String failureVerb;
        switch (operation) {
            case "delete":
                result = service.deleteVM(httpInputs, expectedInputs);
                successVerb = "deleted";
                failureVerb = "deleted";
                break;
            case "on":
                result = service.powerOnVM(httpInputs, expectedInputs);
                successVerb = "successfully powered on";
                failureVerb = "powered on";
                break;
            default:
                result = service.powerOffVM(httpInputs, expectedInputs);
                successVerb = "successfully powered off";
                failureVerb = "powered off";
        }
        assertResult(result, found && success && !lookupFailure ? "0" : "-1",
                lookupFailure ? "java.lang.RuntimeException: VM lookup failed" :
                !found ? "Could not find the [testVM] VM." :
                success ? "Success: The [testVM] VM was " + successVerb + ". The taskId is: task-12345" :
                        "Failure: The [testVM] VM could not be " + failureVerb + ".");
        verifyLookup();
        if (found && !lookupFailure) {
            switch (operation) {
                case "delete": verify(vimPort).destroyTask(vm); break;
                case "on": verify(vimPort).powerOnVMTask(vm, null); break;
                case "off": verify(vimPort).powerOffVMTask(vm); break;
                default: fail("Unknown task operation");
            }
            verifyTaskAndDisconnect();
        } else {
            verifyNoInteractions(vimPort);
            assertTrue(taskWait.constructed().isEmpty());
            verify(connection).disconnect();
        }
    }

    @Test void getOsDescriptorsSuccess() throws Exception { osDescriptors(false); }
    @Test void getOsDescriptorsException() throws Exception { osDescriptors(true); }

    private void osDescriptors(boolean failure) throws Exception {
        expectedInputs = inputs().withDataCenterName("datacenter").withHostname("hostname").build();
        ManagedObjectReference browser = new ManagedObjectReference();
        when(moRefHandler.entityProps(isNull(), any(String[].class)))
                .thenReturn(Collections.singletonMap("environmentBrowser", browser));
        if (failure) {
            when(vimPort.queryConfigOption(browser, null, host)).thenThrow(new RuntimeException("Config query failed"));
        } else {
            VirtualMachineConfigOption option = new VirtualMachineConfigOption();
            GuestOsDescriptor first = new GuestOsDescriptor();
            first.setId("firstDescriptorToBeTested");
            GuestOsDescriptor second = new GuestOsDescriptor();
            second.setId("secondDescriptorToBeTested");
            option.getGuestOSDescriptor().add(first);
            option.getGuestOSDescriptor().add(second);
            when(vimPort.queryConfigOption(browser, null, host)).thenReturn(option);
        }
        Map<String, String> result = service.getOsDescriptors(httpInputs, expectedInputs, "");
        assertResult(result, failure ? "-1" : "0", failure ? "java.lang.RuntimeException: Config query failed" :
                "firstDescriptorToBeTested,secondDescriptorToBeTested");
        verify(moRefHandler).entityProps(isNull(), aryEq(new String[]{"environmentBrowser"}));
        verify(vimPort).queryConfigOption(browser, null, host);
        verify(connection).disconnect();
    }

    @Test void listVMsAndTemplatesSuccess() throws Exception { listVms(false, false); }
    @Test void listVMsAndTemplatesEmptyMap() throws Exception { listVms(true, false); }
    @Test void listVMsAndTemplatesException() throws Exception { listVms(false, true); }

    private void listVms(boolean empty, boolean failure) throws Exception {
        expectedInputs = inputs().build();
        Map<String, ManagedObjectReference> vms = new LinkedHashMap<>();
        if (!empty) {
            vms.put("firstVM", vm);
            vms.put("secondVM", vm);
        }
        if (failure) {
            when(moRefHandler.inContainerByType(root, "VirtualMachine")).thenThrow(new RuntimeException("Listing failed"));
        } else {
            when(moRefHandler.inContainerByType(root, "VirtualMachine")).thenReturn(vms);
        }
        Map<String, String> result = service.listVMsAndTemplates(httpInputs, expectedInputs, "");
        assertResult(result, empty || failure ? "-1" : "0", failure ? "java.lang.RuntimeException: Listing failed" :
                empty ? "No VM found in datacenter." : "firstVM,secondVM");
        verify(moRefHandler).inContainerByType(root, "VirtualMachine");
        verify(connection).disconnect();
    }

    @Test
    void getVMDetailsSuccess() throws Exception {
        expectedInputs = inputs().withHostname("hostname").build();
        stubVm(true);
        ObjectContent[] contents = objectContents();
        try (MockedStatic<GetObjectProperties> properties = mockStatic(GetObjectProperties.class)) {
            properties.when(() -> GetObjectProperties.getObjectProperties(any(ConnectionResources.class), eq(vm),
                    any(String[].class))).thenReturn(contents);
            Map<String, String> result = service.getVMDetails(httpInputs, expectedInputs);
            assertEquals("0", result.get("returnCode"));
            Map<String, String> details = new LinkedHashMap<>();
            details.put("vmId", "vm-123");
            details.put("numCPUs", "3");
            details.put("numEths", "4");
            details.put("numDisks", "2");
            details.put("vmUuid", "a3e76177-5020-41a3-ac2a-59c6303c8415");
            details.put("isTemplate", "true");
            details.put("virtualMachineFullName", "Ubuntu Linux (64-bit)");
            details.put("dataStore", "AbCdEf123-vc6-2");
            details.put("vmMemorySize", "8192");
            details.put("vmPathName", "[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx");
            details.put("ipAddress", "127.0.0.1");
            details.forEach((key, value) ->
                    assertTrue(result.get("returnResult").contains("\"" + key + "\":\"" + value + "\""), key));
            properties.verify(() -> GetObjectProperties.getObjectProperties(eq(resource()), eq(vm),
                    aryEq(new String[]{"summary"})));
            verifyLookup();
            verify(connection).disconnect();
        }
    }

    @Test
    void getVMDetailsEmpty() throws Exception {
        expectedInputs = inputs().build();
        stubVm(true);
        try (MockedStatic<GetObjectProperties> properties = mockStatic(GetObjectProperties.class)) {
            properties.when(() -> GetObjectProperties.getObjectProperties(any(ConnectionResources.class), eq(vm),
                    any(String[].class))).thenReturn(null);
            assertResult(service.getVMDetails(httpInputs, expectedInputs), "-1",
                    "Could not retrieve the details for: [testVM] VM.");
            properties.verify(() -> GetObjectProperties.getObjectProperties(eq(resource()), eq(vm),
                    aryEq(new String[]{"summary"})));
            verifyLookup();
            verify(connection).disconnect();
        }
    }

    @Test
    void getVMDetailsException() throws Exception {
        expectedInputs = inputs().build();
        stubLookupFailure();
        try (MockedStatic<GetObjectProperties> properties = mockStatic(GetObjectProperties.class)) {
            assertResult(service.getVMDetails(httpInputs, expectedInputs), "-1", "java.lang.RuntimeException: VM lookup failed");
            properties.verifyNoInteractions();
            verifyLookup();
            verify(connection).disconnect();
        }
    }

    @Test void updateVMAddDisk() throws Exception { updateDevice("disk", true); }
    @Test void updateVMAddCD() throws Exception { updateDevice("cd", true); }
    @Test void updateVMAddNic() throws Exception { updateDevice("nic", true); }
    @Test void updateVMADeleteDisk() throws Exception { updateDevice("disk", false); }
    @Test void updateVMDeleteCD() throws Exception { updateDevice("cd", false); }
    @Test void updateVMDeleteNic() throws Exception { updateDevice("nic", false); }

    private void updateDevice(String device, boolean add) throws Exception {
        expectedInputs = inputs().withOperation(add ? "add" : "remove").withDevice(device)
                .withUpdateValue("testDevice").withLongVmDiskSize("40000").withDiskMode("persistent").build();
        stubVm(true);
        VirtualDevice existing = device("testDevice", device);
        if (!"nic".equals(device) || !add) {
            stubDevices(device, existing);
        }
        if ("disk".equals(device) && add) {
            stubDatastore(60000L);
        }
        when(vimPort.reconfigVMTask(eq(vm), any(VirtualMachineConfigSpec.class))).thenReturn(task);
        Map<String, String> result = service.updateVM(httpInputs, expectedInputs);
        assertResult(result, "0", "Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345");
        ArgumentCaptor<VirtualMachineConfigSpec> config = ArgumentCaptor.forClass(VirtualMachineConfigSpec.class);
        verify(vimPort).reconfigVMTask(eq(vm), config.capture());
        assertEquals(1, config.getValue().getDeviceChange().size());
        VirtualDeviceConfigSpec change = config.getValue().getDeviceChange().get(0);
        assertEquals(add ? VirtualDeviceConfigSpecOperation.ADD : VirtualDeviceConfigSpecOperation.REMOVE, change.getOperation());
        if (add) {
            assertEquals(existing.getClass(), change.getDevice().getClass());
            if ("disk".equals(device)) {
                assertEquals(40000L * 1024, ((VirtualDisk) change.getDevice()).getCapacityInKB());
                assertEquals(VirtualDeviceConfigSpecFileOperation.CREATE, change.getFileOperation());
            }
        } else {
            assertSame(existing, change.getDevice());
            if ("disk".equals(device)) {
                assertEquals(VirtualDeviceConfigSpecFileOperation.DESTROY, change.getFileOperation());
            }
        }
        verifyLookup();
        verifyTaskAndDisconnect();
    }

    @Test void updateVMDiskNotFound() throws Exception { missingDevice("disk", "anotherDisk", "disk"); }
    @Test void updateVMCDNotFound() throws Exception { missingDevice("cd", "anyCD", "optical"); }
    @Test void updateVMNicNotFound() throws Exception { missingDevice("nic", "eth2", "nic"); }

    private void missingDevice(String device, String requested, String description) throws Exception {
        expectedInputs = inputs().withOperation("remove").withDevice(device).withUpdateValue(requested).build();
        stubVm(true);
        stubDevices(device, device("existingDevice", device));
        assertResult(service.updateVM(httpInputs, expectedInputs), "-1",
                "java.lang.RuntimeException: No " + description + ("nic".equals(device) ? "" : " device") +
                        " named: [" + requested + "] can be found.");
        verify(vimPort, never()).reconfigVMTask(any(), any());
        assertTrue(taskWait.constructed().isEmpty());
        verifyLookup();
        verify(connection).disconnect();
    }

    @Test
    void updateVMNotFound() throws Exception {
        expectedInputs = inputs().withOperation("update").withDevice("memory").withUpdateValue("low").build();
        stubVm(false);
        assertResult(service.updateVM(httpInputs, expectedInputs), "-1", "Could not find the [testVM] VM.");
        verifyNoInteractions(vimPort);
        verifyLookup();
        verify(connection).disconnect();
    }

    @Test
    void updateVMAddDiskNoDataStore() throws Exception {
        expectedInputs = inputs().withOperation("add").withDevice("disk").withUpdateValue("someDisk")
                .withLongVmDiskSize("30000").withDiskMode("persistent").build();
        stubVm(true);
        stubDatastore(20000L);
        assertResult(service.updateVM(httpInputs, expectedInputs), "-1",
                "java.lang.RuntimeException: Cannot find any dataStore with: [30000] minimum amount of space available.");
        verifyNoInteractions(vimPort);
        verifyLookup();
        verify(connection).disconnect();
    }

    @Test void updateVMCpu() throws Exception { updateAllocation("cpu", "normal"); }
    @Test void updateVMMemory() throws Exception { updateAllocation("memory", "100"); }

    private void updateAllocation(String device, String value) throws Exception {
        expectedInputs = inputs().withOperation("update").withDevice(device).withUpdateValue(value).build();
        stubVm(true);
        when(vimPort.reconfigVMTask(eq(vm), any(VirtualMachineConfigSpec.class))).thenReturn(task);
        assertResult(service.updateVM(httpInputs, expectedInputs), "0",
                "Success: The [testVM] VM was successfully reconfigured. The taskId is: task-12345");
        ArgumentCaptor<VirtualMachineConfigSpec> config = ArgumentCaptor.forClass(VirtualMachineConfigSpec.class);
        verify(vimPort).reconfigVMTask(eq(vm), config.capture());
        ResourceAllocationInfo allocation = "cpu".equals(device) ? config.getValue().getCpuAllocation() :
                config.getValue().getMemoryAllocation();
        assertNotNull(allocation);
        assertEquals("cpu".equals(device) ? SharesLevel.NORMAL : SharesLevel.CUSTOM, allocation.getShares().getLevel());
        if ("memory".equals(device)) {
            assertEquals(100, allocation.getShares().getShares());
        }
        verifyLookup();
        verifyTaskAndDisconnect();
    }

    @Test
    void updateVMNotSupported() throws Exception {
        expectedInputs = inputs().withOperation("add").withDevice("memory").build();
        stubVm(true);
        assertResult(service.updateVM(httpInputs, expectedInputs), "-1",
                "java.lang.RuntimeException: Unsupported operation specified for CPU or memory device. The CPU or memory can only be updated.");
        verifyNoInteractions(vimPort);
        verifyLookup();
        verify(connection).disconnect();
    }

    @Test void cloneVMSuccess() throws Exception { cloneVm(true); }
    @Test void cloneVMFailure() throws Exception { cloneVm(false); }

    private void cloneVm(boolean success) throws Exception {
        taskSucceeded = success;
        expectedInputs = inputs().withCloneName("cloneVM").withFolderName("testFolder").withCloneHost("testHost")
                .withCloneResourcePool("testResourcePool").withCloneDataStore("testDataStore").build();
        stubVm(true);
        VirtualMachineRelocateSpec relocate = new VirtualMachineRelocateSpec();
        VirtualMachineCloneSpec clone = new VirtualMachineCloneSpec();
        when(vimPort.cloneVMTask(vm, folder, "cloneVM", clone)).thenReturn(task);
        try (MockedConstruction<VmUtils> utils = mockConstruction(VmUtils.class, (mock, context) -> {
            when(mock.getMorFolder(eq("testFolder"), any(ConnectionResources.class))).thenReturn(folder);
            when(mock.getMorResourcePool(eq("testResourcePool"), any(ConnectionResources.class))).thenReturn(pool);
            when(mock.getMorHost(eq("testHost"), any(ConnectionResources.class), eq(vm))).thenReturn(host);
            when(mock.getMorDataStore(eq("testDataStore"), any(ConnectionResources.class), eq(vm), eq(expectedInputs)))
                    .thenReturn(datastore);
            when(mock.getVirtualMachineRelocateSpec(pool, host, datastore, expectedInputs)).thenReturn(relocate);
        }); MockedConstruction<VmConfigSpecs> specs = mockConstruction(VmConfigSpecs.class, (mock, context) ->
                when(mock.getCloneSpec(expectedInputs, relocate)).thenReturn(clone))) {
            assertResult(service.cloneVM(httpInputs, expectedInputs), success ? "0" : "-1", success ?
                    "Success: The [testVM] VM was successfully cloned. The taskId is: task-12345" :
                    "Failure: The [testVM] VM could not be cloned.");
            assertEquals(1, utils.constructed().size());
            VmUtils utility = utils.constructed().get(0);
            verify(utility).getMorFolder("testFolder", resource());
            verify(utility).getMorResourcePool("testResourcePool", resource());
            verify(utility).getMorHost("testHost", resource(), vm);
            verify(utility).getMorDataStore("testDataStore", resource(), vm, expectedInputs);
            verify(utility).getVirtualMachineRelocateSpec(pool, host, datastore, expectedInputs);
            assertEquals(1, specs.constructed().size());
            verify(specs.constructed().get(0)).getCloneSpec(expectedInputs, relocate);
            verify(vimPort).cloneVMTask(vm, folder, "cloneVM", clone);
            verifyLookup();
            verifyTaskAndDisconnect();
        }
    }

    @Test void cloneVMNotFound() throws Exception { cloneLookup(false); }
    @Test void cloneVMException() throws Exception { cloneLookup(true); }

    private void cloneLookup(boolean failure) throws Exception {
        expectedInputs = inputs().withCloneName("cloneVM").build();
        if (failure) {
            stubLookupFailure();
        } else {
            stubVm(false);
        }
        try (MockedConstruction<VmUtils> utils = mockConstruction(VmUtils.class);
             MockedConstruction<VmConfigSpecs> specs = mockConstruction(VmConfigSpecs.class)) {
            assertResult(service.cloneVM(httpInputs, expectedInputs), "-1", failure ?
                    "java.lang.RuntimeException: VM lookup failed" : "Could not find the [testVM] VM.");
            assertTrue(utils.constructed().isEmpty());
            assertTrue(specs.constructed().isEmpty());
            verifyNoInteractions(vimPort);
            verifyLookup();
            verify(connection).disconnect();
        }
    }

    @Test
    void disconnectExceptionPropagates() throws Exception {
        expectedInputs = inputs().build();
        stubVm(false);
        when(connection.disconnect()).thenThrow(new RuntimeException("Disconnect failed"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteVM(httpInputs, expectedInputs));
        assertEquals("Disconnect failed", exception.getMessage());
        verify(connection).disconnect();
    }

    private VmInputs.VmInputsBuilder inputs() {
        return new VmInputs.VmInputsBuilder().withVirtualMachineName("testVM");
    }

    private void stubVm(boolean found) throws Exception {
        when(moRefHandler.inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class)))
                .thenReturn(found ? Collections.singletonMap("testVM", vm) : Collections.emptyMap());
    }

    private void stubLookupFailure() throws Exception {
        when(moRefHandler.inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class)))
                .thenThrow(new RuntimeException("VM lookup failed"));
    }

    private void stubDevices(String kind, VirtualDevice existing) throws Exception {
        ArrayOfVirtualDevice devices = new ArrayOfVirtualDevice();
        if ("disk".equals(kind)) {
            VirtualSCSIController controller = new VirtualSCSIController();
            controller.setKey(1000);
            devices.getVirtualDevice().add(controller);
        } else if ("cd".equals(kind)) {
            VirtualIDEController controller = new VirtualIDEController();
            controller.setKey(2000);
            devices.getVirtualDevice().add(controller);
        }
        devices.getVirtualDevice().add(existing);
        when(moRefHandler.entityProps(eq(vm), aryEq(new String[]{"config.hardware.device"})))
                .thenReturn(Collections.singletonMap("config.hardware.device", devices));
    }

    private VirtualDevice device(String name, String kind) {
        VirtualDevice device = "disk".equals(kind) ? new VirtualDisk() :
                "cd".equals(kind) ? new VirtualCdrom() : new VirtualPCNet32();
        Description description = new Description();
        description.setLabel(name);
        device.setDeviceInfo(description);
        device.setKey(3000);
        return device;
    }

    private void stubDatastore(long freeSpace) throws Exception {
        ArrayOfManagedObjectReference stores = new ArrayOfManagedObjectReference();
        stores.getManagedObjectReference().add(datastore);
        DatastoreSummary summary = new DatastoreSummary();
        summary.setName("testDatastore");
        summary.setFreeSpace(freeSpace);
        when(moRefHandler.entityProps(eq(vm), aryEq(new String[]{"datastore"})))
                .thenReturn(Collections.singletonMap("datastore", stores));
        when(moRefHandler.entityProps(eq(datastore), aryEq(new String[]{"summary"})))
                .thenReturn(Collections.singletonMap("summary", summary));
    }

    private ObjectContent[] objectContents() {
        VirtualMachineConfigSummary config = new VirtualMachineConfigSummary();
        config.setGuestId("Ubuntu64");
        config.setGuestFullName("Ubuntu Linux (64-bit)");
        config.setUuid("a3e76177-5020-41a3-ac2a-59c6303c8415");
        config.setNumCpu(3);
        config.setMemorySizeMB(8192);
        config.setNumEthernetCards(4);
        config.setNumVirtualDisks(2);
        config.setVmPathName("[AbCdEf123-vc6-2] Ubuntu64/Ubuntu64.vmx");
        config.setTemplate(true);
        VirtualMachineSummary summary = new VirtualMachineSummary();
        summary.setConfig(config);
        VirtualMachineGuestSummary guest = new VirtualMachineGuestSummary();
        guest.setIpAddress("127.0.0.1");
        summary.setGuest(guest);
        ManagedObjectReference reference = new ManagedObjectReference();
        reference.setValue("vm-123");
        summary.setVm(reference);
        DynamicProperty property = new DynamicProperty();
        property.setVal(summary);
        ObjectContent content = new ObjectContent();
        content.getPropSet().add(property);
        return new ObjectContent[]{new ObjectContent(), content};
    }

    private ConnectionResources resource() {
        assertEquals(1, resources.constructed().size());
        return resources.constructed().get(0);
    }

    private void verifyLookup() throws Exception {
        resource();
        verify(moRefHandler).inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class));
    }

    private void verifyTaskAndDisconnect() throws Exception {
        resource();
        assertEquals(1, taskWait.constructed().size());
        verify(taskWait.constructed().get(0)).wait(eq(task), aryEq(new String[]{"info.state", "info.error"}),
                aryEq(new String[]{"state"}), any(Object[][].class));
        verify(connection).disconnect();
    }

    private void assertResult(Map<String, String> result, String code, String message) {
        assertNotNull(result);
        assertEquals(code, result.get("returnCode"));
        assertEquals(message, result.get("returnResult"));
    }
}
