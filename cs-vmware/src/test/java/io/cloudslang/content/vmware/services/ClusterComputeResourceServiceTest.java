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

import com.google.gson.JsonArray;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.ClusterDasVmSettings;
import com.vmware.vim25.ClusterGroupInfo;
import com.vmware.vim25.ClusterHostGroup;
import com.vmware.vim25.ClusterRuleInfo;
import com.vmware.vim25.ClusterVmGroup;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.DynamicProperty;
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
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.CLUSTER_RULE_COULD_NOT_BE_FOUND;
import static io.cloudslang.content.vmware.constants.ErrorMessages.RULE_ALREADY_EXISTS;
import static io.cloudslang.content.vmware.constants.ErrorMessages.VM_GROUP_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ClusterComputeResourceServiceTest {
    private static final String CLUSTER_CONFIGURATION_FAILED = "Cluster configuration failed!";
    private static final String SUCCESS_MESSAGE = "Success: The [Cluster1] cluster was successfully reconfigured. The taskId is: task-12345";
    private static final String FAILURE_MESSAGE = "Failure: The [Cluster1] cluster could not be reconfigured.";
    private static final String VM_ID_VALUE = "vm-911";
    private static final String DAS_RESTART_PRIORITY = "dasFcPriority";

    @Mock private HttpInputs httpInputsMock;
    @Mock private ConnectionResources connectionResourcesMock;
    @Mock private ManagedObjectReference clusterMorMock;
    @Mock private ManagedObjectReference vmMorMock;
    @Mock private ManagedObjectReference taskMock;
    @Mock private VimPortType vimPortMock;
    @Mock private Connection connectionMock;
    @Mock private ManagedObjectReference serviceInstanceMock;
    @Mock private ManagedObjectReference rootFolderMock;

    private MockedConstruction<ConnectionResources> connectionResourcesConstruction;
    private MockedConstruction<MorObjectHandler> morObjectHandlerConstruction;
    private MockedConstruction<ResponseHelper> responseHelperConstruction;
    private MockedStatic<GetObjectProperties> getObjectPropertiesMock;
    private ClusterConfigInfoEx clusterConfiguration;
    private boolean taskSucceeded;

    @BeforeEach
    void setUp() throws Exception {
        when(httpInputsMock.isCloseSession()).thenReturn(true);
        when(connectionMock.disconnect()).thenReturn(connectionMock);
        when(taskMock.getValue()).thenReturn("task-12345");
        when(vmMorMock.getValue()).thenReturn(VM_ID_VALUE);

        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class),
                any(ClusterConfigSpecEx.class), eq(true))).thenReturn(taskMock);
        connectionResourcesConstruction = mockConstruction(ConnectionResources.class, (mock, context) ->
                configureConnectionResources(mock));
        configureConnectionResources(connectionResourcesMock);

        morObjectHandlerConstruction = mockConstruction(MorObjectHandler.class, (mock, context) -> {
            when(mock.getSpecificMor(any(ConnectionResources.class), any(ManagedObjectReference.class),
                    anyString(), nullable(String.class))).thenReturn(clusterMorMock);
            when(mock.getMor(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
            when(mock.getMorById(any(ConnectionResources.class), anyString(), anyString())).thenReturn(vmMorMock);
        });

        taskSucceeded = true;
        responseHelperConstruction = mockConstruction(ResponseHelper.class, (mock, context) ->
                doAnswer(invocation -> resultMap(invocation.getArgument(0), invocation.getArgument(1), taskSucceeded))
                        .when(mock).getResultsMap(anyString(), anyString()));

        clusterConfiguration = new ClusterConfigInfoEx();
        getObjectPropertiesMock = mockStatic(GetObjectProperties.class);
        getObjectPropertiesMock.when(() -> GetObjectProperties.getObjectProperties(any(ConnectionResources.class),
                any(ManagedObjectReference.class), any())).thenAnswer(invocation -> {
            ObjectContent content = new ObjectContent();
            DynamicProperty property = new DynamicProperty();
            property.setVal(clusterConfiguration);
            content.getPropSet().add(property);
            return new ObjectContent[]{content};
        });
    }

    @AfterEach
    void tearDown() {
        getObjectPropertiesMock.close();
        responseHelperConstruction.close();
        morObjectHandlerConstruction.close();
        connectionResourcesConstruction.close();
    }

    @Test
    void createVmGroupSuccess() throws Exception {
        Map<String, String> result = service().createVmGroup(httpInputsMock, getVmInputs(), getList());
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void createVmGroupFailure() throws Exception {
        taskSucceeded = false;
        Map<String, String> result = service().createVmGroup(httpInputsMock, getVmInputs(), getList());
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void createVmGroupThrowsException() throws Exception {
        stubReconfigureFailure();
        RuntimeFaultFaultMsg exception = assertThrows(RuntimeFaultFaultMsg.class,
                () -> service().createVmGroup(httpInputsMock, getVmInputs(), getList()));
        assertEquals(CLUSTER_CONFIGURATION_FAILED, exception.getMessage());
    }

    @Test
    void deleteVmGroupSuccess() throws Exception {
        Map<String, String> result = service().deleteVmGroup(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void deleteVmGroupFailure() throws Exception {
        taskSucceeded = false;
        Map<String, String> result = service().deleteVmGroup(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void deleteVmGroupThrowsException() throws Exception {
        stubReconfigureFailure();
        RuntimeFaultFaultMsg exception = assertThrows(RuntimeFaultFaultMsg.class,
                () -> service().deleteVmGroup(httpInputsMock, getVmInputs()));
        assertEquals(CLUSTER_CONFIGURATION_FAILED, exception.getMessage());
    }

    @Test
    void listVmGroupsSuccess() throws Exception {
        ClusterVmGroup first = new ClusterVmGroup();
        first.setName("abc");
        ClusterVmGroup second = new ClusterVmGroup();
        second.setName("def");
        clusterConfiguration.getGroup().addAll(List.of(first, second, new ClusterHostGroup(),
                new ClusterHostGroup(), new ClusterHostGroup()));

        String result = service().listGroups(httpInputsMock, "Cluster1", ",", ClusterVmGroup.class);

        assertNotNull(result);
        assertEquals("abc,def", result);
    }

    @Test
    void listVmGroupsThrowsException() {
        String message = String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1");
        stubClusterConfigurationFailure(message);
        Exception exception = assertThrows(Exception.class,
                () -> service().listGroups(httpInputsMock, "Cluster1", "", ClusterVmGroup.class));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void createHostGroupSuccess() throws Exception {
        Map<String, String> result = service().createHostGroup(httpInputsMock, getVmInputs(), getList());
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void createHostGroupFailure() throws Exception {
        taskSucceeded = false;
        Map<String, String> result = service().createHostGroup(httpInputsMock, getVmInputs(), getList());
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void createHostGroupThrowsException() throws Exception {
        stubReconfigureFailure();
        RuntimeFaultFaultMsg exception = assertThrows(RuntimeFaultFaultMsg.class,
                () -> service().createHostGroup(httpInputsMock, getVmInputs(), getList()));
        assertEquals(CLUSTER_CONFIGURATION_FAILED, exception.getMessage());
    }

    @Test
    void deleteHostGroupSuccess() throws Exception {
        Map<String, String> result = service().deleteHostGroup(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void deleteHostGroupFailure() throws Exception {
        taskSucceeded = false;
        Map<String, String> result = service().deleteHostGroup(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void deleteHostGroupThrowsException() throws Exception {
        stubReconfigureFailure();
        RuntimeFaultFaultMsg exception = assertThrows(RuntimeFaultFaultMsg.class,
                () -> service().deleteHostGroup(httpInputsMock, getVmInputs()));
        assertEquals(CLUSTER_CONFIGURATION_FAILED, exception.getMessage());
    }

    @Test
    void getVmOverrideWithNoVmInformationAndNoConfigurationsSuccess() throws Exception {
        String result = service().getVmOverride(httpInputsMock, getVmInputs());
        assertNotNull(result);
        assertEquals(new JsonArray().toString(), result);
    }

    @Test
    void getVmOverrideWithVmInformationAndNoConfigurationsSuccess() throws Exception {
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineId(VM_ID_VALUE).build();
        String result = service().getVmOverride(httpInputsMock, vmInputs);
        assertNotNull(result);
        assertEquals("unknown configuration", result);
    }

    @Test
    void getVmOverrideWithVmInformationAndConfigurationsSuccess() throws Exception {
        addVmOverride();
        VmInputs vmInputs = new VmInputs.VmInputsBuilder().withVirtualMachineId(VM_ID_VALUE).build();
        String result = service().getVmOverride(httpInputsMock, vmInputs);
        assertNotNull(result);
        assertEquals(DAS_RESTART_PRIORITY, result);
    }

    @Test
    void getVmOverrideWithNoVmInformationAndConfigurationsSuccess() throws Exception {
        addVmOverride();
        String expected = String.format("[{\"vmId\":\"%s\",\"restartPriority\":\"%s\"}]",
                VM_ID_VALUE, DAS_RESTART_PRIORITY);
        String result = service().getVmOverride(httpInputsMock, getVmInputs());
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void listHostGroupsSuccess() throws Exception {
        clusterConfiguration.getGroup().addAll(List.of(new ClusterVmGroup(), new ClusterVmGroup()));
        String result = service().listGroups(httpInputsMock, "Cluster1", ",", ClusterHostGroup.class);
        assertNotNull(result);
        assertTrue(StringUtilities.isEmpty(result));
    }

    @Test
    void listHostGroupsThrowsException() {
        String message = String.format(ErrorMessages.ANOTHER_FAILURE_MSG, "Cluster1");
        stubClusterConfigurationFailure(message);
        Exception exception = assertThrows(Exception.class,
                () -> service().listGroups(httpInputsMock, "Cluster1", "", ClusterHostGroup.class));
        assertEquals(message, exception.getMessage());
    }

    @Test
    void createAffinityRuleSuccess() throws Exception {
        addHostGroup("affineHostGroupName");
        addVmGroup("DemoVmGroup");
        Map<String, String> result = service().createAffinityRule(httpInputsMock, getVmInputs(),
                "affineHostGroupName", "");
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void createAffinityRuleFailure() throws Exception {
        addHostGroup("affineHostGroupName");
        addVmGroup("DemoVmGroup");
        taskSucceeded = false;
        Map<String, String> result = service().createAffinityRule(httpInputsMock, getVmInputs(),
                "affineHostGroupName", "");
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void createAffinityRuleThrowsRuleAlreadyExistsException() {
        ClusterRuleInfo rule = new ClusterRuleInfo();
        rule.setName("DemoRule");
        clusterConfiguration.getRule().add(rule);
        Exception exception = assertThrows(Exception.class, () -> service().createAffinityRule(httpInputsMock,
                getVmInputs(), "affineHostGroupName", ""));
        assertEquals(String.format(RULE_ALREADY_EXISTS, "DemoRule"), exception.getMessage());
    }

    @Test
    void createAffinityRuleThrowsAffineHostGroupNotFoundException() {
        Exception exception = assertThrows(Exception.class, () -> service().createAffinityRule(httpInputsMock,
                getVmInputs(), "affineHostGroupName", ""));
        assertEquals(AFFINE_HOST_GROUP_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void createAffinityRuleThrowsAntiAffineHostGroupNotFoundException() {
        Exception exception = assertThrows(Exception.class, () -> service().createAffinityRule(httpInputsMock,
                getVmInputs(), "", "antiAffineHostGroup"));
        assertEquals(ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void createAffinityRuleVmGroupNotFoundException() {
        addHostGroup("affineHostGroupName");
        Exception exception = assertThrows(Exception.class, () -> service().createAffinityRule(httpInputsMock,
                getVmInputs(), "affineHostGroupName", ""));
        assertEquals(VM_GROUP_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void deleteClusterRuleSuccess() throws Exception {
        addRule("DemoRule");
        Map<String, String> result = service().deleteClusterRule(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertSuccess(result);
    }

    @Test
    void deleteClusterRuleFailure() throws Exception {
        addRule("DemoRule");
        taskSucceeded = false;
        Map<String, String> result = service().deleteClusterRule(httpInputsMock, getVmInputs());
        verifyCommonReconfiguration();
        assertFailure(result);
    }

    @Test
    void deleteClusterRuleThrowsException() {
        Exception exception = assertThrows(Exception.class,
                () -> service().deleteClusterRule(httpInputsMock, getVmInputs()));
        assertEquals(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, "DemoRule"), exception.getMessage());
    }

    private void configureConnectionResources(ConnectionResources resources) throws Exception {
        when(resources.getVimPortType()).thenReturn(vimPortMock);
        when(resources.getConnection()).thenReturn(connectionMock);
        when(resources.getServiceInstance()).thenReturn(serviceInstanceMock);
        when(resources.getMorRootFolder()).thenReturn(rootFolderMock);
    }

    private ClusterComputeResourceService service() {
        return new ClusterComputeResourceService();
    }

    private void stubReconfigureFailure() throws Exception {
        when(vimPortMock.reconfigureComputeResourceTask(any(ManagedObjectReference.class),
                any(ClusterConfigSpecEx.class), eq(true)))
                .thenThrow(new RuntimeFaultFaultMsg(CLUSTER_CONFIGURATION_FAILED, new RuntimeFault()));
    }

    private void stubClusterConfigurationFailure(String message) {
        getObjectPropertiesMock.when(() -> GetObjectProperties.getObjectProperties(any(ConnectionResources.class),
                any(ManagedObjectReference.class), any()))
                .thenThrow(new RuntimeFaultFaultMsg(message, new RuntimeFault()));
    }

    private void verifyCommonReconfiguration() throws Exception {
        ConnectionResources resources = connectionResourcesConstruction.constructed().get(0);
        verify(resources).getConnection();
        verify(resources).getVimPortType();
        verify(vimPortMock).reconfigureComputeResourceTask(any(ManagedObjectReference.class),
                any(ClusterConfigSpecEx.class), eq(true));
        verify(taskMock).getValue();
        verify(connectionMock).disconnect();
        MorObjectHandler lastHandler = morObjectHandlerConstruction.constructed()
                .get(morObjectHandlerConstruction.constructed().size() - 1);
        verify(lastHandler).getSpecificMor(eq(resources), eq(rootFolderMock), anyString(), anyString());
    }

    private void addVmOverride() {
        ClusterDasVmConfigInfo override = new ClusterDasVmConfigInfo();
        ManagedObjectReference vmReference = new ManagedObjectReference();
        vmReference.setValue(VM_ID_VALUE);
        override.setKey(vmReference);
        ClusterDasVmSettings settings = new ClusterDasVmSettings();
        settings.setRestartPriority(DAS_RESTART_PRIORITY);
        override.setDasSettings(settings);
        clusterConfiguration.getDasVmConfig().add(override);
    }

    private void addHostGroup(String name) {
        ClusterHostGroup group = new ClusterHostGroup();
        group.setName(name);
        clusterConfiguration.getGroup().add(group);
    }

    private void addVmGroup(String name) {
        ClusterVmGroup group = new ClusterVmGroup();
        group.setName(name);
        clusterConfiguration.getGroup().add(group);
    }

    private void addRule(String name) {
        ClusterRuleInfo rule = new ClusterRuleInfo();
        rule.setName(name);
        rule.setKey(1);
        clusterConfiguration.getRule().add(rule);
    }

    private VmInputs getVmInputs() {
        return new VmInputs.VmInputsBuilder()
                .withClusterName("Cluster1")
                .withVmGroupName("DemoVmGroup")
                .withHostGroupName("DemoHostGroup")
                .withRuleName("DemoRule")
                .build();
    }

    private List<String> getList() {
        return new ArrayList<>(List.of("asd"));
    }

    private Map<String, String> resultMap(String successMessage, String failureMessage, boolean succeeded) {
        Map<String, String> result = new HashMap<>();
        result.put(Outputs.RETURN_CODE, succeeded ? Outputs.RETURN_CODE_SUCCESS : Outputs.RETURN_CODE_FAILURE);
        result.put(Outputs.RETURN_RESULT, succeeded ? successMessage : failureMessage);
        return result;
    }

    private void assertSuccess(Map<String, String> result) {
        assertNotNull(result);
        assertEquals(Outputs.RETURN_CODE_SUCCESS, result.get(Outputs.RETURN_CODE));
        assertEquals(SUCCESS_MESSAGE, result.get(Outputs.RETURN_RESULT));
    }

    private void assertFailure(Map<String, String> result) {
        assertNotNull(result);
        assertEquals(Outputs.RETURN_CODE_FAILURE, result.get(Outputs.RETURN_CODE));
        assertEquals(FAILURE_MESSAGE, result.get(Outputs.RETURN_RESULT));
    }
}
