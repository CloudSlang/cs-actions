package io.cloudslang.content.vmware.services;

import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.CLUSTER_RULE_COULD_NOT_BE_FOUND;
import static io.cloudslang.content.vmware.constants.ErrorMessages.RULE_ALREADY_EXISTS;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by pinteae on 10/4/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClusterComputeResourceService.class)
public class ClusterComputeResourceServiceTest {
    private static final String CLUSTER_CONFIGURATION_FAILED = "Cluster configuration failed!";
    private static final String GET_CLUSTER_CONFIGURATION = "getClusterConfiguration";
    private static final String RULE_EXISTS = "ruleExists";
    private static final String GET_CLUSTER_VM_HOST_RULE_INFO = "getClusterVmHostRuleInfo";
    private static final String GET_RULE = "getRule";
    private static final String GET_CLUSTER_RULE_INFO = "getClusterRuleInfo";
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
        whenNew(MorObjectHandler.class).withNoArguments().thenReturn(morObjectHandlerMock);
        whenNew(ClusterConfigSpecEx.class).withNoArguments().thenReturn(clusterConfigSpecExMock);
        whenNew(ClusterGroupSpec.class).withNoArguments().thenReturn(clusterGroupSpecMock);
        whenNew(ClusterRuleSpec.class).withNoArguments().thenReturn(clusterRuleSpecMock);
        whenNew(ClusterConfigSpecEx.class).withNoArguments().thenReturn(clusterConfigSpecExMock);

        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(connectionResourcesMock.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(connectionResourcesMock.getConnection()).thenReturn(connectionMock);
        when(taskMock.getValue()).thenReturn("task-12345");
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenReturn(taskMock);
        when(morObjectHandlerMock.getSpecificMor(any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class), any(String.class)))
                .thenReturn(clusterMorMock);

        clusterComputeResourceService = new ClusterComputeResourceService();
    }

    @After
    public void tearDown() throws Exception {
        clusterComputeResourceService = null;
    }

    @Test
    public void createVmGroupSuccess() throws Exception {
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
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
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
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
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
        VmInputs vmInputs = getVmInputs();
        List<String> vmList = getList();
        when(connectionResourcesMock.getVimPortType()).thenReturn(vimPortMock);
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);

        clusterComputeResourceService.createVmGroup(httpInputsMock, vmInputs, vmList);

        commonVerifications();
    }

    @Test
    public void deleteVmGroupSuccess() throws Exception {
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteVmGroupFailure() throws Exception {
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteVmGroupThrowsException() throws Exception {
        whenNew(ClusterVmGroup.class).withNoArguments().thenReturn(clusterVmGroupMock);

        VmInputs vmInputs = getVmInputs();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);

        clusterComputeResourceService.deleteVmGroup(httpInputsMock, vmInputs);

        commonVerifications();
    }

    @Test
    public void listVmGroupsSuccess() throws Exception {
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
        PowerMockito.doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        whenNew(ArrayList.class).withNoArguments().thenReturn(vmGroupNameListSpy);

        String result = clusterComputeResourceServiceSpy.listVmGroups(httpInputsMock, "Cluster1", ",");

        verify(vmGroupNameListSpy, times(2)).add(any(String.class));
        assertNotNull(result);
        assertEquals("abc,def", result);
    }

    @Test
    public void listVmGroupsThrowsException() throws Exception {
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        PowerMockito.doThrow(new RuntimeFaultFaultMsg(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"), new RuntimeFault())).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        thrownException.expectMessage(String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1"));

        clusterComputeResourceServiceSpy.listVmGroups(httpInputsMock, "Cluster1", "");

        commonVerifications();
    }

    @Test
    public void createHostGroupSuccess() throws Exception {
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);

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
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
        VmInputs vmInputs = getVmInputs();
        List<String> hostList = getList();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.createHostGroup(httpInputsMock, vmInputs, hostList);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteHostGroupSuccess() throws Exception {
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteHostGroupFailure() throws Exception {
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
        VmInputs vmInputs = getVmInputs();
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void deleteHostGroupThrowsException() throws Exception {
        whenNew(ClusterHostGroup.class).withNoArguments().thenReturn(clusterHostGroupMock);
        VmInputs vmInputs = getVmInputs();
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class), any(ClusterConfigSpecEx.class), any(Boolean.class)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));

        thrownException.expectMessage(CLUSTER_CONFIGURATION_FAILED);
        clusterComputeResourceService.deleteHostGroup(httpInputsMock, vmInputs);

        commonVerifications();
    }

    @Test
    public void listHostGroupsSuccess() throws Exception {
        List<ClusterGroupInfo> clusterGroupInfoList = new ArrayList<>();
        clusterGroupInfoList.add(new ClusterVmGroup());
        clusterGroupInfoList.add(new ClusterVmGroup());
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        clusterConfigInfoEx.getGroup().addAll(clusterGroupInfoList);
        PowerMockito.doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        whenNew(ArrayList.class).withNoArguments().thenReturn(hostGroupNameListSpy);

        String result = clusterComputeResourceServiceSpy.listHostGroups(httpInputsMock, "Cluster1", ",");

        verify(hostGroupNameListSpy, never()).add(any(String.class));
        assertNotNull(result);
        assertTrue(StringUtilities.isEmpty(result));
    }

    @Test
    public void createAffinityRuleSuccess() throws Exception {
        VmInputs vmInputs = getVmInputs();
        PowerMockito.doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        PowerMockito.doReturn(clusterVmHostRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_VM_HOST_RULE_INFO,
                any(ClusterConfigInfoEx.class), any(VmInputs.class), any(String.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void createAffinityRuleFailure() throws Exception {
        VmInputs vmInputs = getVmInputs();
        PowerMockito.doReturn(clusterConfigInfoExMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(false).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        PowerMockito.doReturn(clusterVmHostRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_VM_HOST_RULE_INFO,
                any(ClusterConfigInfoEx.class), any(VmInputs.class), any(String.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, false));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");

        commonVerifications();
        assertionsFailure(resultMap);
    }

    @Test
    public void createAffinityRuleThrowsException() throws Exception {
        VmInputs vmInputs = getVmInputs();
        ClusterConfigInfoEx clusterConfigInfoEx = new ClusterConfigInfoEx();
        PowerMockito.doReturn(clusterConfigInfoEx).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doThrow(new RuntimeFaultFaultMsg(String.format(RULE_ALREADY_EXISTS, vmInputs.getRuleName()), new RuntimeFault())).when(clusterComputeResourceServiceSpy, RULE_EXISTS,
                any(ClusterConfigInfoEx.class), any(String.class));
        thrownException.expectMessage(String.format(RULE_ALREADY_EXISTS, vmInputs.getRuleName()));

        clusterComputeResourceServiceSpy.createAffinityRule(httpInputsMock, vmInputs, "affineHostGroupName", "");
    }

    @Test
    public void deleteClusterRuleSuccess() throws Exception {
        VmInputs vmInputs = getVmInputs();
        PowerMockito.doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        PowerMockito.doReturn(clusterRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_RULE_INFO,
                any(List.class), any(String.class));
        whenNew(ResponseHelper.class).withArguments(any(ConnectionResources.class), any(ManagedObjectReference.class))
                .thenReturn(getResponseHelper(connectionResourcesMock, taskMock, true));

        Map<String, String> resultMap = clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);

        commonVerifications();
        assertionsSuccess(resultMap);
    }

    @Test
    public void deleteClusterRuleFailure() throws Exception {
        VmInputs vmInputs = getVmInputs();
        PowerMockito.doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        PowerMockito.doReturn(clusterRuleInfoMock).when(clusterComputeResourceServiceSpy, GET_CLUSTER_RULE_INFO,
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
        PowerMockito.doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        PowerMockito.doThrow(new RuntimeFaultFaultMsg(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()), new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, "getClusterRuleInfo",
                        any(List.class), any(String.class));

        thrownException.expectMessage(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()));
        clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);

        commonVerifications();
    }

    @Test
    public void deleteClusterRuleThrowsExceptionVmGroupDoesNotExist() throws Exception {
        VmInputs vmInputs = getVmInputs();
        PowerMockito.doReturn(clusterConfigInfoExSpy).when(clusterComputeResourceServiceSpy, GET_CLUSTER_CONFIGURATION,
                any(ConnectionResources.class), any(ManagedObjectReference.class), any(String.class));
        PowerMockito.doReturn(clusterRuleInfoListMock).when(clusterConfigInfoExSpy, GET_RULE);
        PowerMockito.doThrow(new RuntimeFaultFaultMsg(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()), new RuntimeFault()))
                .when(clusterComputeResourceServiceSpy, "getClusterRuleInfo",
                        any(List.class), any(String.class));

        thrownException.expectMessage(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, vmInputs.getRuleName()));
        clusterComputeResourceServiceSpy.deleteClusterRule(httpInputsMock, vmInputs);

        commonVerifications();
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
        list.add("asdf");
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
        assertEquals(resultMap.get(Outputs.RETURN_RESULT), "Success: The [Cluster1] cluster was successfully reconfigured. The taskId is: task-12345");
    }

    private void assertionsFailure(Map<String, String> resultMap) {
        assertNotNull(resultMap);
        assertEquals(-1, Integer.parseInt(resultMap.get(Outputs.RETURN_CODE)));
        assertEquals(resultMap.get(Outputs.RETURN_RESULT), "Failure: The [Cluster1] cluster could not be reconfigured.");
    }
}
