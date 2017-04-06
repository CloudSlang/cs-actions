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

import com.google.gson.JsonArray;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.ClusterDasVmSettings;
import com.vmware.vim25.ClusterGroupInfo;
import com.vmware.vim25.ClusterGroupSpec;
import com.vmware.vim25.ClusterHostGroup;
import com.vmware.vim25.ClusterRuleInfo;
import com.vmware.vim25.ClusterRuleSpec;
import com.vmware.vim25.ClusterVmGroup;
import com.vmware.vim25.ClusterVmHostRuleInfo;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.CLUSTER_RULE_COULD_NOT_BE_FOUND;
import static io.cloudslang.content.vmware.constants.ErrorMessages.RULE_ALREADY_EXISTS;
import static io.cloudslang.content.vmware.constants.ErrorMessages.VM_GROUP_DOES_NOT_EXIST;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/4/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClusterComputeResourceService.class)
public class ClusterComputeResourceServiceTest {
    private static final String CLUSTER_CONFIGURATION_FAILED = "Cluster configuration failed!";
    private static final String GET_CLUSTER_CONFIGURATION = "getClusterConfiguration";
    private static final String GET_DAS_VM_CONFIG = "getDasVmConfig";
    private static final String RULE_EXISTS = "ruleExists";
    private static final String GET_CLUSTER_VM_HOST_RULE_INFO = "getClusterVmHostRuleInfo";
    private static final String GET_RULE = "getRule";
    private static final String GET_CLUSTER_RULE_INFO = "getClusterRuleInfo";
    private static final String ADD_AFFINE_GROUP_TO_RULE = "addAffineGroupToRule";
    private static final String ADD_ANTI_AFFINE_GROUP_TO_RULE = "addAntiAffineGroupToRule";
    private static final String EXISTS_GROUP = "existsGroup";
    private static final String SUCCESS_MESSAGE = "Success: The [Cluster1] cluster was successfully reconfigured. The taskId is: task-12345";
    private static final String FAILURE_MESSAGE = "Failure: The [Cluster1] cluster could not be reconfigured.";
    private static final String VM_ID_VALUE = "vm-911";
    private static final String DAS_RESTART_PRIORITY = "dasFcPriority";
    @Mock
    private HttpInputs httpInputsMock;

    @Mock
    private ConnectionResources connectionResourcesMock;

    @Mock
    private ManagedObjectReference clusterMorMock;

    @Mock
    private ManagedObjectReference vmMorMock;

    @Mock
    private ManagedObjectReference hostMorMock;

    @Mock
    private ManagedObjectReference taskMock;

    @Mock
    private ClusterGroupInfo clusterGroupInfoMock;

    @Mock
    private ClusterRuleInfo clusterRuleInfoMock;

    @Mock
    private ClusterGroupSpec clusterGroupSpecMock;

    @Mock
    private ClusterRuleSpec clusterRuleSpecMock;

    @Mock
    private ClusterVmGroup clusterVmGroupMock;

    @Mock
    private ClusterHostGroup clusterHostGroupMock;

    @Mock
    private ClusterConfigInfoEx clusterConfigInfoExMock;

    @Mock
    private ClusterConfigSpecEx clusterConfigSpecExMock;

    @Mock
    private VimPortType vimPortMock;

    @Mock
    private ManagedObjectReference serviceInstanceMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private MorObjectHandler morObjectHandlerMock;

    @Spy
    private ArrayList<String> vmGroupNameListSpy = new ArrayList<>();

    @Spy
    private ArrayList<String> hostGroupNameListSpy = new ArrayList<>();

    @Mock
    private List<ClusterGroupInfo> clusterGroupInfoListMock;

    @Mock
    private List<ClusterRuleInfo> clusterRuleInfoListMock;

    @Mock
    private ClusterVmHostRuleInfo clusterVmHostRuleInfoMock;

    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    @Spy
    private ClusterComputeResourceService clusterComputeResourceServiceSpy = new ClusterComputeResourceService();

    @Spy
    private ClusterConfigInfoEx clusterConfigInfoExSpy = new ClusterConfigInfoEx();

    private ClusterComputeResourceService clusterComputeResourceService;

    @Before
    public void setUp() throws Exception {
        whenNew(ConnectionResources.class).withArguments(any(HttpInputs.class)).thenReturn(connectionResourcesMock);
        whenNew(ConnectionResources.class).withArguments(any(HttpInputs.class), any(VmInputs.class)).thenReturn(connectionResourcesMock);
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        when(morObjectHandlerMock.getSpecificMor(any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class), any(String.class)))
                .thenReturn(clusterMorMock);

        clusterComputeResourceService = new ClusterComputeResourceService();
    }

    @After
    public void tearDown() throws Exception {
        connectionResourcesMock = null;
        morObjectHandlerMock = null;
        clusterMorMock = null;
        vmMorMock = null;

        clusterComputeResourceService = null;
    }

    @Test
    public void createVmGroupSuccess() throws Exception {
        commonVmGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        List<String> vmList = getList();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.createVmGroup(httpInputsMock, vmInputs, vmList);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void createVmGroupFailure() throws Exception {
        commonVmGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        List<String> vmList = getList();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.createVmGroup(httpInputsMock, vmInputs, vmList);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void createVmGroupThrowsException() throws Exception {
        vmGroupMockInitializationOnExceptionThrown();
        VmInputs vmInputs = getVmInputs();
        List<String> vmList = getList();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);

        clusterComputeResourceService.createVmGroup(httpInputsMock, vmInputs, vmList);
    }

    @Test
    public void deleteVmGroupSuccess() throws Exception {
        commonVmGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteVmGroupFailure() throws Exception {
        commonVmGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteVmGroupThrowsException() throws Exception {
        vmGroupMockInitializationOnExceptionThrown();
        VmInputs vmInputs = getVmInputs();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);

        clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);
    }

    @Test
    public void listVmGroupsSuccess() throws Exception {
        commonMockInitializations();
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        ClusterVmGroup clusterVmGroup1 = new ClusterVmGroup();
        ClusterVmGroup clusterVmGroup2 = new ClusterVmGroup();
        clusterVmGroup1.setName("abc");
        clusterVmGroup2.setName("def");
        clusterGroupInfoList.add(clusterVmGroup1);
        clusterGroupInfoList.add(clusterVmGroup2);
        clusterGroupInfoList.add(new ClusterHostGroup());
        clusterGroupInfoList.add(new ClusterHostGroup());
        clusterGroupInfoList.add(new ClusterHostGroup());
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        whenNew(ArrayList.class).withNoArguments().thenReturn(vmGroupNameListSpy);

        String result = clusterComputeResourceServiceSpy.listGroups(httpInputsMock, "Cluster1", ",", ClusterVmGroup.class);

        verify(vmGroupNameListSpy, times(2)).add(any(String.class));
        assertNotNull(result);
        assertEquals("abc,def", result);
    }

    @Test
    public void listVmGroupsThrowsException() throws Exception {
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        doThrow(new RuntimeFaultFaultMsg(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"), new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                        any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        thrownException.expectMessage(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"));

        clusterComputeResourceServiceSpy.listGroups(httpInputsMock, "Cluster1", "", ClusterVmGroup.class);
    }

    @Test
    public void createHostGroupSuccess() throws Exception {
        commonHostGroupMockInitializations();

        VmInputs vmInputs = getVmInputs();
        List<String> hostList = getList();

        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.createHostGroup(httpInputsMock, vmInputs, hostList);

        commonVerifications();
        verify(taskMock, times(1)).getValue();
        assertionsSuccess(resultMap);
    }

    @Test
    public void createHostGroupFailure() throws Exception {
        commonHostGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        List<String> hostList = getList();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.createHostGroup(httpInputsMock, vmInputs, hostList);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void createHostGroupThrowsException() throws Exception {
        hostGroupMockInitializationOnExceptionThrown();
        VmInputs vmInputs = getVmInputs();
        List<String> hostList = getList();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);

        clusterComputeResourceService.createHostGroup(httpInputsMock, vmInputs, hostList);
    }

    @Test
    public void deleteHostGroupSuccess() throws Exception {
        commonHostGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteHostGroupFailure() throws Exception {
        commonHostGroupMockInitializations();
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteHostGroupThrowsException() throws Exception {
        hostGroupMockInitializationOnExceptionThrown();
        VmInputs vmInputs = getVmInputs();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));

        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);
        clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);
    }

    @Test
    public void getVmOverrideWithNoVmInformationAndNoConfigurationsSuccess() throws Exception {
        commonMockInitializations();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));

        String result = clusterComputeResourceServiceSpy.getVmOverride(httpInputsMock, getVmInputs());

        assertNotNull(result);
        assertEquals(new JsonArray().toString(), result);
    }

    @Test
    public void getVmOverrideWithVmInformationAndNoConfigurationsSuccess() throws Exception {
        commonVmMockInitializations();
        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineId(VM_ID_VALUE)
                .build();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));

        String result = clusterComputeResourceServiceSpy.getVmOverride(httpInputsMock, vmInputs);

        assertNotNull(result);
        assertEquals("unknown configuration", result);
    }

    @Test
    public void getVmOverrideWithVmInformationAndConfigurationsSuccess() throws Exception {
        commonVmMockInitializations();
        VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                .withVirtualMachineId(VM_ID_VALUE)
                .build();

        String result = clusterComputeResourceServiceSpy.getVmOverride(httpInputsMock, vmInputs);

        assertNotNull(result);
        assertEquals(DAS_RESTART_PRIORITY, result);
    }

    @Test
    public void getVmOverrideWithNoVmInformationAndConfigurationsSuccess() throws Exception {
        String expectedResponse = String.format("[{\"vmId\":\"%s\",\"restartPriority\":\"%s\"}]", VM_ID_VALUE, DAS_RESTART_PRIORITY);
        commonVmMockInitializations();

        String result = clusterComputeResourceServiceSpy.getVmOverride(httpInputsMock, getVmInputs());

        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    public void listHostGroupsSuccess() throws Exception {
        commonMockInitializations();
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        clusterGroupInfoList.add(new ClusterVmGroup());
        clusterGroupInfoList.add(new ClusterVmGroup());
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        whenNew(ArrayList.class).withNoArguments().thenReturn(hostGroupNameListSpy);

        String result = clusterComputeResourceServiceSpy.listGroups(httpInputsMock, "Cluster1", ",", ClusterHostGroup.class);

        verify(hostGroupNameListSpy, never()).add(any(String.class));
        assertNotNull(result);
        assertTrue(StringUtilities.isEmpty(result));
    }

    @Test
    public void listHostGroupsThrowsException() throws Exception {
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        doThrow(new RuntimeFaultFaultMsg(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"), new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                        any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        thrownException.expectMessage(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"));

        clusterComputeResourceServiceSpy.listGroups(httpInputsMock, "Cluster1", "", ClusterHostGroup.class);
    }

    @Test
    public void createAffinityRuleSuccess() throws Exception {
        commonMockInitializations();
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        doReturn(clusterVmHostRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_VM_HOST_RULE_INFO,
                any(ClusterConfigInfoEx.class), any(VmInputs.class), any(String.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void createAffinityRuleFailure() throws Exception {
        commonMockInitializations();
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        doReturn(clusterVmHostRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_VM_HOST_RULE_INFO,
                any(ClusterConfigInfoEx.class), any(VmInputs.class), any(String.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void createAffinityRuleThrowsRuleAlreadyExistsException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doThrow(new RuntimeFaultFaultMsg(String.format(RULE_ALREADY_EXISTS, vmInputs.getRuleName()), new RuntimeFault())).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        thrownException.expectMessage(String.format(RULE_ALREADY_EXISTS, vmInputs.getRuleName()));

        clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");
    }

    @Test
    public void createAffinityRuleThrowsAffineHostGroupNotFoundException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        doThrow(new RuntimeFaultFaultMsg(AFFINE_HOST_GROUP_DOES_NOT_EXIST, new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, ADD_AFFINE_GROUP_TO_RULE,
                        any(ClusterVmHostRuleInfo.class), any(ClusterConfigInfoEx.class), any(String.class));
        thrownException.expectMessage(AFFINE_HOST_GROUP_DOES_NOT_EXIST);

        clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");
    }

    @Test
    public void createAffinityRuleThrowsAntiAffineHostGroupNotFoundException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        doThrow(new RuntimeFaultFaultMsg(ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST, new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, ADD_ANTI_AFFINE_GROUP_TO_RULE,
                        any(ClusterVmHostRuleInfo.class), any(ClusterConfigInfoEx.class), any(String.class));
        thrownException.expectMessage(ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST);

        clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "", "antiAffineHostGroup");
    }

    @Test
    public void createAffinityRuleVmGroupNotFoundException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        whenNew(ClusterVmHostRuleInfo.class).withNoArguments().thenReturn(clusterVmHostRuleInfoMock);
        doReturn(clusterVmHostRuleInfoMock).when(clusterComputeResourceServiceSpy, ADD_AFFINE_GROUP_TO_RULE,
                any(ClusterVmHostRuleInfo.class), any(ClusterConfigInfoEx.class), any(String.class));
        doThrow(new RuntimeFaultFaultMsg(VM_GROUP_DOES_NOT_EXIST, new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, EXISTS_GROUP,
                        any(ClusterConfigInfoEx.class), any(String.class), any(Class.class));
        thrownException.expectMessage(VM_GROUP_DOES_NOT_EXIST);

        clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");
    }

    @Test
    public void deleteClusterRuleSuccess() throws Exception {
        commonMockInitializations();
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        doReturn(clusterRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_RULE_INFO,
                any(List.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteClusterRuleFailure() throws Exception {
        commonMockInitializations();
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        doReturn(clusterRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_RULE_INFO,
                any(List.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteClusterRuleThrowsException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        doThrow(new RuntimeFaultFaultMsg(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()), new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, GET_CLUSTER_RULE_INFO,
                        any(List.class), any(String.class));

        thrownException.expectMessage(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()));
        clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);
    }

    private ResponseHelper getResponseHelper(final ConnectionResources connectionResources,
                                             final ManagedObjectReference task,
                                             final boolean isDone) {
        return new ResponseHelper(connectionResources, task) {
            public boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
                    throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
                return isDone;
            }
        };
    }

    @NotNull
    private List<String> getList() {
        List<String> list = new ArrayList();
        list.add("asd");
        return list;
    }

    private VmInputs getVmInputs() {
        return new VmInputs.VmInputsBuilder()
                .withClusterName("Cluster1")
                .withVmGroupName("DemoVmGroup")
                .withHostGroupName("DemoHostGroup")
                .withRuleName("DemoRule")
                .build();
    }

    private void commonMockInitializations() throws Exception {
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenReturn(taskMock);
    }

    private void commonVmMockInitializations() throws Exception {
        commonMockInitializations();
        when(morObjectHandlerMock.getMorById(eq(connectionResourcesMock), eq("VirtualMachine"), anyString())).thenReturn(vmMorMock);
        when(vmMorMock.getValue()).thenReturn(VM_ID_VALUE);
        when(vmMorMock.getType()).thenReturn("VirtualMachine");
        List<ClusterDasVmConfigInfo> dasVmConfig = new ArrayList<>();
        ClusterDasVmConfigInfo clusterDasVmConfigInfo = new ClusterDasVmConfigInfo();
        ManagedObjectReference vmMor = new ManagedObjectReference();
        vmMor.setValue(VM_ID_VALUE);
        clusterDasVmConfigInfo.setKey(vmMor);
        ClusterDasVmSettings clusterDasVmSettings = new ClusterDasVmSettings();
        clusterDasVmSettings.setRestartPriority(DAS_RESTART_PRIORITY);
        clusterDasVmConfigInfo.setDasSettings(clusterDasVmSettings);
        dasVmConfig.add(clusterDasVmConfigInfo);
        doReturn(dasVmConfig).when(clusterConfigInfoExSpy, GET_DAS_VM_CONFIG);
        doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
    }

    private void commonVmGroupMockInitializations() throws Exception {
        commonMockInitializations();
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
    }

    private void commonHostGroupMockInitializations() throws Exception {
        commonMockInitializations();
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
    }

    private void vmGroupMockInitializationOnExceptionThrown() throws Exception {
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
    }

    private void hostGroupMockInitializationOnExceptionThrown() throws Exception {
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
    }

    private void commonVerifications() throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        verify(connectionResourcesMock, times(1)).getConnection();
        verify(connectionResourcesMock, times(1)).getVimPortType();
        verify(morObjectHandlerMock, times(1)).getSpecificMor(any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class), any(String.class));
        verify(vimPortMock, times(1)).reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class));
        verify(taskMock, times(1)).getValue();
        verify(connectionMock, times(1)).disconnect();
    }

    private void assertionsSuccess(Map<String, String> resultMap) {
        assertNotNull(resultMap);
        assertEquals(Integer.parseInt(Outputs.RETURN_CODE_SUCCESS), Integer.parseInt(resultMap.get(Outputs.RETURN_CODE)));
        assertEquals(resultMap.get(Outputs.RETURN_RESULT), SUCCESS_MESSAGE);
    }

    private void assertionsFailure(Map<String, String> resultMap) {
        assertNotNull(resultMap);
        assertEquals(Integer.parseInt(Outputs.RETURN_CODE_FAILURE), Integer.parseInt(resultMap.get(Outputs.RETURN_CODE)));
        assertEquals(resultMap.get(Outputs.RETURN_RESULT), FAILURE_MESSAGE);
    }
}
