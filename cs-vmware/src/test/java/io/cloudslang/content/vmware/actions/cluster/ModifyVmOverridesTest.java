/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.cluster;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.vmware.constants.ErrorMessages.PROVIDE_VM_NAME_OR_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModifyVmOverrides.class)
public class ModifyVmOverridesTest {

    private static final String OPERATION_FAILED = "Operation failed!";
    private static final String CLUSTER_RESTART_PRIORITY = "clusterRestartPriority";
    private static final String DISABLED = "disabled";
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MEDIUM = "medium";
    private static final String INVALID_RESTART_PRIORITY_MSG = "The 'restartPriority' input value is not valid! Valid values are ";
    private static final String VALIDATE_RESTART_PRIORITY_METHOD = "validateRestartPriority";
    private static final String WRONG_RESTART_PRIORITY = "wrong_restart_priority";

    private ModifyVmOverrides action;

    @Mock
    private ClusterComputeResourceService service;

    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    @Before
    public void setUp() {
        action = new ModifyVmOverrides();
    }

    @After
    public void tearDown() {
        action = null;
        service = null;
    }

    @Test
    public void testSuccessModifyVmOverrides() throws Exception {
        Map<String, String> map = new HashMap<>();
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(service);
        doReturn(map).when(service).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), anyString());

        Map<String, String> result = action.modifyVmOverrides("", "", "", "", "", "", "", "vmName", "", "", CLUSTER_RESTART_PRIORITY);

        verifyNew(ClusterComputeResourceService.class).withNoArguments();
        verify(service).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), anyString());
        assertEquals(map, result);
    }

    @Test
    public void testFailureModifyVmOverrides() throws Exception {
        whenNew(ClusterComputeResourceService.class).withNoArguments().thenReturn(service);
        doThrow(new Exception(OPERATION_FAILED)).when(service).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), anyString());

        Map<String, String> result = action.modifyVmOverrides("", "", "", "", "", "", "", "", "vm-123", "", CLUSTER_RESTART_PRIORITY); // TODO

        verifyNew(ClusterComputeResourceService.class).withNoArguments();
        verify(service).updateOrAddVmOverride(any(HttpInputs.class), any(VmInputs.class), anyString());
        assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(Outputs.EXCEPTION), OPERATION_FAILED));
    }

    @Test
    public void testValidateRestartPriority() throws Exception {
        Method method = ModifyVmOverrides.class.getDeclaredMethod(VALIDATE_RESTART_PRIORITY_METHOD, String.class);
        method.setAccessible(true);
        assertEquals(CLUSTER_RESTART_PRIORITY, method.invoke(action, CLUSTER_RESTART_PRIORITY));
        assertEquals(DISABLED, method.invoke(action, DISABLED));
        assertEquals(HIGH, method.invoke(action, HIGH));
        assertEquals(MEDIUM, method.invoke(action, MEDIUM));
        assertEquals(LOW, method.invoke(action, LOW));

        thrownException.expectMessage(INVALID_RESTART_PRIORITY_MSG);
        method.invoke(action, WRONG_RESTART_PRIORITY);
    }

    @Test
    public void testValidateMutualExclusiveInputs() throws Exception {
        verifyFailureResultMap(action.modifyVmOverrides("", "", "", "", "", "", "", "", "", "", CLUSTER_RESTART_PRIORITY));
        verifyFailureResultMap(action.modifyVmOverrides("", "", "", "", "", "", "", "vmName", "vm-123", "", CLUSTER_RESTART_PRIORITY));
    }

    private void verifyFailureResultMap(Map<String, String> result) {
        assertEquals(PROVIDE_VM_NAME_OR_ID, result.get(RETURN_RESULT));
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(StringUtilities.contains(result.get(EXCEPTION), PROVIDE_VM_NAME_OR_ID));
    }
}
