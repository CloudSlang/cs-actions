package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Mihai Tusa.
 * 3/28/2016.
 */
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
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);


        whenNew(ResponseHelper.class).withNoArguments().thenReturn(getResponseHelper(true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVMName")
                .build();

        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
                .withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeWinVM(httpInputsMock, vmInputs, guestInputs);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(guestConfigSpecsMock, times(1)).getCustomizationSpec(any(GuestInputs.class));
        verify(vimPortMock, times(1)).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(vimPortMock, times(1)).customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(taskMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(0, Integer.parseInt(results.get("returnCode")));
        assertEquals("Success: The [testVMName] VM was successfully customized. The taskId is: task-12345",
                results.get("returnResult"));
    }

    @Test
    public void customizeWinVMFailure() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);


        whenNew(ResponseHelper.class).withNoArguments().thenReturn(getResponseHelper(false));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVMName")
                .build();

        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
                .withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeWinVM(httpInputsMock, vmInputs, guestInputs);

        verifyConnection();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(guestConfigSpecsMock, times(1)).getCustomizationSpec(any(GuestInputs.class));
        verify(vimPortMock, times(1)).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(vimPortMock, times(1)).customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(taskMock, times(1)).getValue();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Failure: The [testVMName] VM could not be customized.", results.get("returnResult"));
    }

    @Test
    public void customizeWinVMNotFound() throws Exception {
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getVmMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(null);
        whenNew(GuestConfigSpecs.class).withNoArguments().thenReturn(guestConfigSpecsMock);
        when(guestConfigSpecsMock.getCustomizationSpec(any(GuestInputs.class))).thenReturn(customizationSpecMock);
        doNothing().when(vimPortMock).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        when(vimPortMock.customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class))).thenReturn(taskMock);


        whenNew(ResponseHelper.class).withNoArguments().thenReturn(getResponseHelper(true));

        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineName("testVMName")
                .build();

        GuestInputs guestInputs = new GuestInputs.GuestInputsBuilder()
                .withRebootOption("noreboot").withLicenseDataMode("perServer").build();

        Map<String, String> results = guestService.customizeWinVM(httpInputsMock, vmInputs, guestInputs);

        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();
        verify(morObjectHandlerMock, times(1)).getVmMor(any(ConnectionResources.class), anyString(), anyString());
        verify(connectionResourcesMock, never()).getVimPortType();
        verify(guestConfigSpecsMock, never()).getCustomizationSpec(any(GuestInputs.class));
        verify(vimPortMock, never()).checkCustomizationSpec(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(vimPortMock, never()).customizeVMTask(any(ManagedObjectReference.class), any(CustomizationSpec.class));
        verify(taskMock, never()).getValue();

        assertNotNull(results);
        assertEquals(-1, Integer.parseInt(results.get("returnCode")));
        assertEquals("Could not find the [testVMName] VM.", results.get("returnResult"));
    }

    private ResponseHelper getResponseHelper(final boolean isDone) {
        return new ResponseHelper() {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }

    private void verifyConnection() {
        verify(connectionResourcesMock, atMost(2)).getVimPortType();
        verify(taskMock).getValue();
        verify(connectionResourcesMock).getConnection();
        verify(connectionMock).disconnect();
    }
}
