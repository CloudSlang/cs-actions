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
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.utils.GuestConfigSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {
    @Mock private HttpInputs httpInputs;
    @Mock private Connection connection;
    @Mock private VimPortType vimPort;
    @Mock private MoRefHandler moRefHandler;

    private final ManagedObjectReference root = new ManagedObjectReference();
    private final ManagedObjectReference vm = new ManagedObjectReference();
    private final ManagedObjectReference task = new ManagedObjectReference();
    private final CustomizationSpec customization = new CustomizationSpec();
    private final VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineName("testVM").build();
    private final GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
            .withRebootOption("noreboot").withLicenseDataMode("perServer").build();
    private MockedConstruction<ConnectionResources> resources;
    private MockedConstruction<WaitForValues> taskWait;
    private boolean taskSucceeded;

    @BeforeEach
    void setUp() {
        task.setValue("task-12345");
        taskSucceeded = true;
        when(httpInputs.isCloseSession()).thenReturn(true);
        resources = mockConstruction(ConnectionResources.class, (mock, context) -> {
            assertSame(httpInputs, context.arguments().get(0));
            assertSame(vmInputs, context.arguments().get(1));
            when(mock.getMorRootFolder()).thenReturn(root);
            when(mock.getMoRefHandler()).thenReturn(moRefHandler);
            lenient().when(mock.getVimPortType()).thenReturn(vimPort);
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

    @Test void customizeWinVMSuccess() throws Exception { customize(true, true, true); }
    @Test void customizeWinVMFailure() throws Exception { customize(true, false, true); }
    @Test void customizeWinVMNotFound() throws Exception { customize(true, true, false); }
    @Test void customizeLinuxVMSuccess() throws Exception { customize(false, true, true); }
    @Test void customizeLinuxVMFailure() throws Exception { customize(false, false, true); }
    @Test void customizeLinuxVMNotFound() throws Exception { customize(false, true, false); }

    private void customize(boolean windows, boolean success, boolean found) throws Exception {
        taskSucceeded = success;
        stubVm(found);
        if (found) {
            when(vimPort.customizeVMTask(vm, customization)).thenReturn(task);
        }
        try (MockedConstruction<GuestConfigSpecs> specs = customizationSpecs(windows)) {
            Map<String, String> result = new GuestService().customizeVM(httpInputs, vmInputs, guestInputs, windows);
            assertResult(result, found && success ? "0" : "-1", !found ? "Could not find the [testVM] VM." :
                    success ? "Success: The [testVM] VM was successfully customized. The taskId is: task-12345" :
                            "Failure: The [testVM] VM could not be customized.");
            verifyLookupAndDisconnect();
            if (found) {
                assertEquals(1, specs.constructed().size());
                GuestConfigSpecs spec = specs.constructed().get(0);
                if (windows) {
                    verify(spec).getWinCustomizationSpec(guestInputs);
                    verify(spec, never()).getLinuxCustomizationSpec(any());
                } else {
                    verify(spec).getLinuxCustomizationSpec(guestInputs);
                    verify(spec, never()).getWinCustomizationSpec(any());
                }
                verify(vimPort).checkCustomizationSpec(vm, customization);
                verify(vimPort).customizeVMTask(vm, customization);
                assertEquals(1, taskWait.constructed().size());
            } else {
                assertTrue(specs.constructed().isEmpty());
                assertTrue(taskWait.constructed().isEmpty());
                verifyNoInteractions(vimPort);
            }
        }
    }

    @Test
    void customizeLinuxVMException() throws Exception {
        stubVm(true);
        when(vimPort.customizeVMTask(vm, customization)).thenThrow(new RuntimeException("Customization failed"));
        try (MockedConstruction<GuestConfigSpecs> specs = customizationSpecs(false)) {
            Map<String, String> result = new GuestService().customizeVM(httpInputs, vmInputs, guestInputs, false);
            assertResult(result, "-1", "java.lang.RuntimeException: Customization failed");
            verify(vimPort).checkCustomizationSpec(vm, customization);
            verify(vimPort).customizeVMTask(vm, customization);
            assertEquals(1, specs.constructed().size());
            assertTrue(taskWait.constructed().isEmpty());
            verifyLookupAndDisconnect();
        }
    }

    @Test
    void mountToolsSuccess() throws Exception {
        stubVm(true);
        Map<String, String> result = new GuestService().mountTools(httpInputs, vmInputs);
        assertResult(result, "0", "Initiated VMware Tools Installer Mount on: testVM");
        verify(vimPort).mountToolsInstaller(vm);
        verifyLookupAndDisconnect();
    }

    @Test
    void mountToolsNotFound() throws Exception {
        stubVm(false);
        Map<String, String> result = new GuestService().mountTools(httpInputs, vmInputs);
        assertResult(result, "-1", "Could not find the [testVM] VM.");
        verifyNoInteractions(vimPort);
        verifyLookupAndDisconnect();
    }

    @Test
    void mountToolsException() throws Exception {
        when(moRefHandler.inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class)))
                .thenThrow(new RuntimeException("VM lookup failed"));
        Map<String, String> result = new GuestService().mountTools(httpInputs, vmInputs);
        assertResult(result, "-1", "java.lang.RuntimeException: VM lookup failed");
        verifyNoInteractions(vimPort);
        verifyLookupAndDisconnect();
    }

    @Test
    void disconnectExceptionPropagates() throws Exception {
        stubVm(false);
        when(connection.disconnect()).thenThrow(new RuntimeException("Disconnect failed"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> new GuestService().mountTools(httpInputs, vmInputs));
        assertEquals("Disconnect failed", exception.getMessage());
        verifyLookupAndDisconnect();
    }

    private MockedConstruction<GuestConfigSpecs> customizationSpecs(boolean windows) {
        return mockConstruction(GuestConfigSpecs.class, (mock, context) -> {
            if (windows) {
                when(mock.getWinCustomizationSpec(guestInputs)).thenReturn(customization);
            } else {
                when(mock.getLinuxCustomizationSpec(guestInputs)).thenReturn(customization);
            }
        });
    }

    private void stubVm(boolean found) throws Exception {
        when(moRefHandler.inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class)))
                .thenReturn(found ? Collections.singletonMap("testVM", vm) : Collections.emptyMap());
    }

    private void verifyLookupAndDisconnect() throws Exception {
        assertEquals(1, resources.constructed().size());
        verify(moRefHandler).inContainerByType(eq(root), eq("VirtualMachine"), any(RetrieveOptions.class));
        verify(connection).disconnect();
    }

    private void assertResult(Map<String, String> result, String code, String message) {
        assertNotNull(result);
        assertEquals(code, result.get("returnCode"));
        assertEquals(message, result.get("returnResult"));
    }
}
