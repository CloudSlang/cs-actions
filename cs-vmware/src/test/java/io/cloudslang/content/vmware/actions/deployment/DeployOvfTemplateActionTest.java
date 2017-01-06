/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.actions.deployment;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.DeployOvfTemplateService;
import io.cloudslang.content.vmware.utils.OvfUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DeployOvfTemplateAction.class, OvfUtils.class})
public class DeployOvfTemplateActionTest {

    private static final String OVF_NETWORK_JS_VALUES = "[\"Network 1\",\"Network 2\"]";
    private static final String NET_PORT_GROUP_JS_VALUES = "[\"VM Network\", \"dvPortGroup\"]";
    private static final String OVF_PROP_KEY_JS_VALUES = "[\"vami.ip0.vmName\",\"vami.ip1.vmName\"]";
    private static final String OVF_PROP_VALUE_JS_VALUES = "[\"10.10.10.10\",\"10.20.30.40\"]";
    private static final String SUCCESSFULLY_DEPLOYED = "Template was deployed successfully!";
    private static final String OPERATION_FAILED = "Operation failed!";

    private DeployOvfTemplateAction action;

    @Mock
    private DeployOvfTemplateService service;

    @Before
    public void setUp() {
        action = new DeployOvfTemplateAction();
    }

    @After
    public void tearDown() {
        action = null;
        service = null;
    }

    @Test
    public void testSuccessDeployTemplate() throws Exception {
        prepareForTests();
        Mockito.doNothing().when(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());

        Map<String, String> result = action.deployTemplate("", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES, OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES, "");

        Mockito.verify(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());
        assertEquals(ReturnCodes.SUCCESS, result.get(Outputs.RETURN_CODE));
        assertEquals(SUCCESSFULLY_DEPLOYED, result.get(Outputs.RETURN_RESULT));
        verifyStatic();
        OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES);
        OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES);
        PowerMockito.verifyNew(DeployOvfTemplateService.class);
    }

    @Test
    public void testFailureDeployTemplate() throws Exception {
        prepareForTests();
        Mockito.doThrow(new Exception(OPERATION_FAILED)).when(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());

        Map<String, String> result = action.deployTemplate("", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES, OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES, "");

        Mockito.verify(service).deployOvfTemplate(any(HttpInputs.class), any(VmInputs.class), anyString(), anyMap(), anyMap());
        assertEquals(ReturnCodes.FAILURE, result.get(Outputs.RETURN_CODE));
        assertEquals(OPERATION_FAILED, result.get(Outputs.RETURN_RESULT));
        assertTrue(StringUtilities.contains(result.get(Outputs.RETURN_RESULT), OPERATION_FAILED));
        verifyStatic();
        OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES);
        OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES);
        PowerMockito.verifyNew(DeployOvfTemplateService.class);
    }

    private void prepareForTests() throws Exception {
        PowerMockito.mockStatic(OvfUtils.class);
        PowerMockito.doReturn(new HashMap<>()).when(OvfUtils.class);
        OvfUtils.getOvfMappings(OVF_NETWORK_JS_VALUES, NET_PORT_GROUP_JS_VALUES);
        PowerMockito.doReturn(new HashMap<>()).when(OvfUtils.class);
        OvfUtils.getOvfMappings(OVF_PROP_KEY_JS_VALUES, OVF_PROP_VALUE_JS_VALUES);
        whenNew(DeployOvfTemplateService.class).withArguments(true).thenReturn(service);
    }
}
