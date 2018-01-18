/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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
import com.google.gson.JsonObject;
import com.vmware.vim25.*;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.ClusterParameter;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.Constants.RESTART_PRIORITY;
import static io.cloudslang.content.vmware.constants.Constants.VM_ID;
import static io.cloudslang.content.vmware.constants.ErrorMessages.*;
import static io.cloudslang.content.vmware.utils.ConnectionUtils.clearConnectionFromContext;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by das giloan on 8/30/2016.
 */
public class ClusterComputeResourceService {

    /**
     * Das method looks into das Cluster’s list of VM overrides to update das VM’s restartPriority value.
     * If a VM override is found, das value will be updated, otherwise a new “override” will be created and added to das list.
     *
     * @param httpInputs
     * @param vmInputs
     * @param restartPriority
     * @return
     * @throws Exception
     */
    public Map<String, String> updateOrAddVmOverride(final HttpInputs httpInputs, final VmInputs vmInputs, final String restartPriority) throws Exception {
        final ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {
            final ManagedObjectReference vmMor = getVirtualMachineReference(vmInputs, connectionResources);

            final ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            final ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName());
            final ClusterDasVmConfigSpec clusterDasVmConfigSpec = getClusterVmConfiguration(clusterConfigInfoEx, vmMor, restartPriority);

            final ManagedObjectReference task = connectionResources.getVimPortType()
                    .reconfigureComputeResourceTask(clusterMor, createClusterConfigSpecEx(clusterConfigInfoEx, clusterDasVmConfigSpec), true);

            return new ResponseHelper(connectionResources, task)
                    .getResultsMap(String.format(SUCCESS_MSG, vmInputs.getClusterName(), task.getValue()),
                            String.format(FAILURE_MSG, vmInputs.getClusterName()));
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    public String getVmOverride(final HttpInputs httpInputs, final VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {


            final ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            final ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName());

            final String restartPriority;
            if (StringUtilities.isNotBlank(vmInputs.getVirtualMachineId()) || StringUtilities.isNotBlank(vmInputs.getVirtualMachineName())) {
                final ManagedObjectReference vmMor = getVirtualMachineReference(vmInputs, connectionResources);
                restartPriority = getVmRestartPriority(clusterConfigInfoEx, vmMor);
            } else {
                restartPriority = getVmRestartPriority(clusterConfigInfoEx);
            }

            return restartPriority;
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    private ManagedObjectReference getVirtualMachineReference(final VmInputs vmInputs, final ConnectionResources connectionResources) throws Exception {
        if (StringUtilities.isNotEmpty(vmInputs.getVirtualMachineName())) {
            return getVmMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
        }
        return new MorObjectHandler().getMorById(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineId());
    }

    public Map<String, String> createVmGroup(HttpInputs httpInputs, VmInputs vmInputs, List<String> vmNameList) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ClusterVmGroup clusterVmGroup = new ClusterVmGroup();
            clusterVmGroup.setName(vmInputs.getVmGroupName());
            clusterVmGroup.getVm().addAll(getVmManagedObjectReferences(vmNameList, connectionResources));

            return createGroup(vmInputs, connectionResources, clusterVmGroup);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    private Map<String, String> createGroup(VmInputs vmInputs, ConnectionResources connectionResources, ClusterGroupInfo clusterGroupInfo) throws Exception {
        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

        ClusterGroupSpec clusterGroupSpec = new ClusterGroupSpec();
        clusterGroupSpec.setInfo(clusterGroupInfo);
        clusterGroupSpec.setOperation(ArrayUpdateOperation.ADD);

        return reconfigureClusterGroup(vmInputs, connectionResources, clusterMor, clusterGroupSpec);
    }

    private Map<String, String> reconfigureClusterGroup(VmInputs vmInputs, ConnectionResources connectionResources, ManagedObjectReference clusterMor, ClusterGroupSpec clusterGroupSpec) throws RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg, InvalidPropertyFaultMsg {
        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        clusterConfigSpecEx.getGroupSpec().add(clusterGroupSpec);

        return reconfigureCluster(vmInputs, connectionResources, clusterMor, clusterConfigSpecEx);
    }

    private Map<String, String> reconfigureCluster(VmInputs vmInputs, ConnectionResources connectionResources, ManagedObjectReference clusterMor, ClusterConfigSpecEx clusterConfigSpecEx) throws RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg, InvalidPropertyFaultMsg {
        ManagedObjectReference task = connectionResources.getVimPortType()
                .reconfigureComputeResourceTask(clusterMor, clusterConfigSpecEx, true);

        return new ResponseHelper(connectionResources, task)
                .getResultsMap(String.format(SUCCESS_MSG, vmInputs.getClusterName(), task.getValue()),
                        String.format(FAILURE_MSG, vmInputs.getClusterName()));
    }

    public Map<String, String> deleteVmGroup(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            ClusterVmGroup clusterVmGroup = new ClusterVmGroup();
            clusterVmGroup.setName(vmInputs.getVmGroupName());

            ClusterGroupSpec clusterGroupSpec = new ClusterGroupSpec();
            clusterGroupSpec.setInfo(clusterVmGroup);
            clusterGroupSpec.setOperation(ArrayUpdateOperation.REMOVE);
            clusterGroupSpec.setRemoveKey(vmInputs.getVmGroupName());

            return reconfigureClusterGroup(vmInputs, connectionResources, clusterMor, clusterGroupSpec);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    public String listGroups(HttpInputs httpInputs, String clusterName, String delimiter, Class clazz) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), clusterName);

            ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, clusterName);

            List<String> groupNameList = new ArrayList<>();
            for (ClusterGroupInfo clusterGroupInfo : clusterConfigInfoEx.getGroup()) {
                if (clusterGroupInfo.getClass().isAssignableFrom(clazz)) {
                    groupNameList.add(clusterGroupInfo.getName());
                }
            }
            return StringUtilities.join(groupNameList, delimiter);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    public Map<String, String> createHostGroup(HttpInputs httpInputs, VmInputs vmInputs, List<String> hostNameList) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ClusterHostGroup clusterHostGroup = new ClusterHostGroup();
            clusterHostGroup.setName(vmInputs.getHostGroupName());
            clusterHostGroup.getHost().addAll(getHostManagedObjectReferences(hostNameList, connectionResources));

            return createGroup(vmInputs, connectionResources, clusterHostGroup);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    public Map<String, String> deleteHostGroup(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            ClusterHostGroup clusterHostGroup = new ClusterHostGroup();
            clusterHostGroup.setName(vmInputs.getHostGroupName());

            ClusterGroupSpec clusterGroupSpec = new ClusterGroupSpec();
            clusterGroupSpec.setInfo(clusterHostGroup);
            clusterGroupSpec.setOperation(ArrayUpdateOperation.REMOVE);
            clusterGroupSpec.setRemoveKey(vmInputs.getHostGroupName());

            return reconfigureClusterGroup(vmInputs, connectionResources, clusterMor, clusterGroupSpec);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    public Map<String, String> createAffinityRule(HttpInputs httpInputs, VmInputs vmInputs,
                                                  String affineHostGroupName, String antiAffineHostGroupName) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            if (ruleExists(getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName()), vmInputs.getRuleName())) {
                throw new Exception(String.format(RULE_ALREADY_EXISTS, vmInputs.getRuleName()));
            } else {
                ClusterVmHostRuleInfo clusterVmHostRuleInfo = getClusterVmHostRuleInfo(getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName()), vmInputs, affineHostGroupName, antiAffineHostGroupName);

                ClusterRuleSpec clusterRuleSpec = new ClusterRuleSpec();
                clusterRuleSpec.setInfo(clusterVmHostRuleInfo);
                clusterRuleSpec.setOperation(ArrayUpdateOperation.ADD);

                return reconfigureClusterRule(vmInputs, connectionResources, clusterMor, clusterRuleSpec);
            }
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    private Map<String, String> reconfigureClusterRule(VmInputs vmInputs, ConnectionResources connectionResources, ManagedObjectReference clusterMor, ClusterRuleSpec clusterRuleSpec) throws RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg, InvalidPropertyFaultMsg {
        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        clusterConfigSpecEx.getRulesSpec().add(clusterRuleSpec);

        return reconfigureCluster(vmInputs, connectionResources, clusterMor, clusterConfigSpecEx);
    }

    public Map<String, String> deleteClusterRule(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);
        try {
            ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                    ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

            List<ClusterRuleInfo> clusterRuleInfoList = getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName()).getRule();

            ClusterRuleInfo clusterRuleInfo = getClusterRuleInfo(clusterRuleInfoList, vmInputs.getRuleName());
            ClusterRuleSpec clusterRuleSpec = new ClusterRuleSpec();
            clusterRuleSpec.setInfo(clusterRuleInfo);
            clusterRuleSpec.setOperation(ArrayUpdateOperation.REMOVE);
            clusterRuleSpec.setRemoveKey(clusterRuleInfo.getKey());

            return reconfigureClusterRule(vmInputs, connectionResources, clusterMor, clusterRuleSpec);
        } finally {
            if (httpInputs.isCloseSession()) {
                connectionResources.getConnection().disconnect();
                clearConnectionFromContext(httpInputs.getGlobalSessionObject());
            }
        }
    }

    @NotNull
    private List<ManagedObjectReference> getVmManagedObjectReferences(List<String> vmNames, ConnectionResources connectionResources) throws Exception {
        List<ManagedObjectReference> vmMorList = new ArrayList<>();
        for (String vmName : vmNames) {
            vmMorList.add(new MorObjectHandler().getMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmName));
        }
        return vmMorList;
    }

    private List<ManagedObjectReference> getHostManagedObjectReferences(List<String> hostNames, ConnectionResources connectionResources) throws Exception {
        List<ManagedObjectReference> hostMorList = new ArrayList<>();
        for (String hostName : hostNames) {
            hostMorList.add(new MorObjectHandler().getMor(connectionResources, ManagedObjectType.HOST_SYSTEM.getValue(), hostName));
        }
        return hostMorList;
    }

    private ClusterRuleInfo getClusterRuleInfo(List<ClusterRuleInfo> clusterRuleInfoList, String ruleName) throws Exception {
        for (ClusterRuleInfo ruleInfo : clusterRuleInfoList) {
            if (StringUtilities.equals(ruleName, ruleInfo.getName())) {
                ClusterRuleInfo clusterRuleInfo = new ClusterRuleInfo();
                clusterRuleInfo.setName(ruleName);
                clusterRuleInfo.setKey(ruleInfo.getKey());
                return clusterRuleInfo;
            }
        }
        throw new Exception(String.format(CLUSTER_RULE_COULD_NOT_BE_FOUND, ruleName));
    }

    @NotNull
    private ClusterVmHostRuleInfo getClusterVmHostRuleInfo(ClusterConfigInfoEx clusterConfigInfoEx,
                                                           VmInputs vmInputs, String affineHostGroupName, String antiAffineHostGroupName) throws Exception {
        ClusterVmHostRuleInfo clusterVmHostRuleInfo = new ClusterVmHostRuleInfo();
        clusterVmHostRuleInfo.setName(vmInputs.getRuleName());
        if (isNotBlank(affineHostGroupName)) {
            clusterVmHostRuleInfo = addAffineGroupToRule(clusterVmHostRuleInfo, clusterConfigInfoEx, affineHostGroupName);
        } else {
            clusterVmHostRuleInfo = addAntiAffineGroupToRule(clusterVmHostRuleInfo, clusterConfigInfoEx, antiAffineHostGroupName);
        }
        if (existsGroup(clusterConfigInfoEx, vmInputs.getVmGroupName(), ClusterVmGroup.class)) {
            clusterVmHostRuleInfo.setVmGroupName(vmInputs.getVmGroupName());
            clusterVmHostRuleInfo.setMandatory(true);
            clusterVmHostRuleInfo.setEnabled(true);
        } else {
            throw new Exception(VM_GROUP_DOES_NOT_EXIST);
        }
        return clusterVmHostRuleInfo;
    }

    private ClusterVmHostRuleInfo addAntiAffineGroupToRule(ClusterVmHostRuleInfo clusterVmHostRuleInfo, ClusterConfigInfoEx clusterConfigInfoEx, String antiAffineHostGroupName) throws Exception {
        if (existsGroup(clusterConfigInfoEx, antiAffineHostGroupName, ClusterHostGroup.class)) {
            clusterVmHostRuleInfo.setAntiAffineHostGroupName(antiAffineHostGroupName);
        } else {
            throw new Exception(ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST);
        }
        return clusterVmHostRuleInfo;
    }

    private ClusterVmHostRuleInfo addAffineGroupToRule(ClusterVmHostRuleInfo clusterVmHostRuleInfo, ClusterConfigInfoEx clusterConfigInfoEx, String affineHostGroupName) throws Exception {
        if (existsGroup(clusterConfigInfoEx, affineHostGroupName, ClusterHostGroup.class)) {
            clusterVmHostRuleInfo.setAffineHostGroupName(affineHostGroupName);
        } else {
            throw new Exception(AFFINE_HOST_GROUP_DOES_NOT_EXIST);
        }
        return clusterVmHostRuleInfo;
    }

    private boolean ruleExists(ClusterConfigInfoEx clusterConfigInfoEx, String ruleName) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        for (ClusterRuleInfo clusterRuleInfo : clusterConfigInfoEx.getRule()) {
            if (clusterRuleInfo.getName().equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    private boolean existsGroup(ClusterConfigInfoEx clusterConfigInfoEx, String hostGroupName, Class classOfGroup) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        for (ClusterGroupInfo clusterGroupInfo : clusterConfigInfoEx.getGroup()) {
            if (clusterGroupInfo.getClass().isAssignableFrom(classOfGroup)) {
                if (clusterGroupInfo.getName().equals(hostGroupName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param clusterConfigInfoEx
     * @param clusterDasVmConfigSpec
     * @return
     */
    private ClusterConfigSpecEx createClusterConfigSpecEx(ClusterConfigInfoEx clusterConfigInfoEx, ClusterDasVmConfigSpec clusterDasVmConfigSpec) {
        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        clusterConfigSpecEx.setDasConfig(clusterConfigInfoEx.getDasConfig());
        List<ClusterDasVmConfigSpec> clusterDasVmConfigSpecs = clusterConfigSpecEx.getDasVmConfigSpec();
        clusterDasVmConfigSpecs.add(clusterDasVmConfigSpec);
        return clusterConfigSpecEx;
    }

    /**
     * @param clusterDasVmConfigInfo
     * @param operation
     * @return
     */
    private ClusterDasVmConfigSpec createClusterDasVmConfigSpec(ClusterDasVmConfigInfo clusterDasVmConfigInfo, ArrayUpdateOperation operation) {
        ClusterDasVmConfigSpec clusterDasVmConfigSpec = new ClusterDasVmConfigSpec();
        clusterDasVmConfigSpec.setInfo(clusterDasVmConfigInfo);
        clusterDasVmConfigSpec.setOperation(operation);
        return clusterDasVmConfigSpec;
    }

    /**
     * @param clusterConfigInfoEx
     * @param vmMor
     * @param restartPriority
     * @return
     */
    private ClusterDasVmConfigSpec getClusterVmConfiguration(ClusterConfigInfoEx clusterConfigInfoEx, ManagedObjectReference vmMor, String restartPriority) {
        List<ClusterDasVmConfigInfo> dasVmConfig = clusterConfigInfoEx.getDasVmConfig();
        for (ClusterDasVmConfigInfo clusterDasVmConfigInfo : dasVmConfig) {
            if (vmMor.getValue().equals(clusterDasVmConfigInfo.getKey().getValue())) {
                clusterDasVmConfigInfo.getDasSettings().setRestartPriority(restartPriority);
                return createClusterDasVmConfigSpec(clusterDasVmConfigInfo, ArrayUpdateOperation.EDIT);
            }
        }
        return createClusterDasVmConfigSpec(createVmOverrideConfiguration(vmMor, restartPriority), ArrayUpdateOperation.ADD);
    }

    /**
     * Das method adds a vm override to a HA enabled Cluster.
     *
     * @param vmMor
     * @param restartPriority
     * @return
     */
    private ClusterDasVmConfigInfo createVmOverrideConfiguration(ManagedObjectReference vmMor, String restartPriority) {
        ClusterDasVmConfigInfo clusterDasVmConfigInfo = new ClusterDasVmConfigInfo();
        clusterDasVmConfigInfo.setKey(vmMor);
        clusterDasVmConfigInfo.setDasSettings(createClusterDasVmSettings(restartPriority));
        return clusterDasVmConfigInfo;
    }

    /**
     * @param clusterConfigInfoEx
     * @param vmMor
     * @return
     */
    private String getVmRestartPriority(ClusterConfigInfoEx clusterConfigInfoEx, ManagedObjectReference vmMor) {
        List<ClusterDasVmConfigInfo> dasVmConfig = clusterConfigInfoEx.getDasVmConfig();
        for (ClusterDasVmConfigInfo clusterDasVmConfigInfo : dasVmConfig) {
            if (vmMor.getValue().equals(clusterDasVmConfigInfo.getKey().getValue())) {
                return clusterDasVmConfigInfo.getDasSettings().getRestartPriority();
            }
        }
        return "unknown configuration";
    }

    /**
     * @param clusterConfigInfoEx
     * @return
     */
    private String getVmRestartPriority(ClusterConfigInfoEx clusterConfigInfoEx) {
        JsonArray resultArray = new JsonArray();
        List<ClusterDasVmConfigInfo> dasVmConfig = clusterConfigInfoEx.getDasVmConfig();
        for (ClusterDasVmConfigInfo clusterDasVmConfigInfo : dasVmConfig) {
            JsonObject vmRestartPriority = new JsonObject();
            vmRestartPriority.addProperty(VM_ID, clusterDasVmConfigInfo.getKey().getValue());
            vmRestartPriority.addProperty(RESTART_PRIORITY, clusterDasVmConfigInfo.getDasSettings().getRestartPriority());
            resultArray.add(vmRestartPriority);
        }
        return resultArray.toString();
    }

    /**
     * Das method creates a cluster vm setting.
     *
     * @param restartPriority
     * @return
     */
    private ClusterDasVmSettings createClusterDasVmSettings(String restartPriority) {
        ClusterDasVmSettings clusterDasVmSettings = new ClusterDasVmSettings();
        clusterDasVmSettings.setRestartPriority(restartPriority);
        return clusterDasVmSettings;
    }

    /**
     * Das method gets the current cluster configurations.
     *
     * @param connectionResources
     * @param clusterMor
     * @param clusterName
     * @return
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    private ClusterConfigInfoEx getClusterConfiguration(ConnectionResources connectionResources, ManagedObjectReference clusterMor,
                                                        String clusterName) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, clusterMor, new String[]{ClusterParameter.CONFIGURATION_EX.getValue()});
        if (objectContents != null && objectContents.length == 1) {
            List<DynamicProperty> dynamicProperties = objectContents[0].getPropSet();
            if (dynamicProperties != null && dynamicProperties.size() == 1 && dynamicProperties.get(0).getVal() instanceof ClusterConfigInfoEx) {
                return (ClusterConfigInfoEx) dynamicProperties.get(0).getVal();
            }
        }
        throw new RuntimeException(String.format(ANOTHER_FAILURE_MSG, clusterName));
    }

    private ManagedObjectReference getVmMor(final ConnectionResources connectionResources, final String value, final String virtualMachineName) throws Exception {
        final ManagedObjectReference mor = new MorObjectHandler().getMor(connectionResources, value, virtualMachineName);
        if (mor != null) {
            return mor;
        }
        throw new RuntimeException(VM_NOT_FOUND);
    }
}
