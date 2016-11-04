package io.cloudslang.content.vmware.services;

import com.vmware.vim25.ArrayUpdateOperation;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.ClusterDasVmConfigSpec;
import com.vmware.vim25.ClusterDasVmSettings;
import com.vmware.vim25.ClusterGroupInfo;
import com.vmware.vim25.ClusterGroupSpec;
import com.vmware.vim25.ClusterHostGroup;
import com.vmware.vim25.ClusterRuleInfo;
import com.vmware.vim25.ClusterRuleSpec;
import com.vmware.vim25.ClusterVmGroup;
import com.vmware.vim25.ClusterVmHostRuleInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.RuntimeFaultFaultMsg;
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

import static io.cloudslang.content.vmware.constants.ErrorMessages.AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.ANOTHER_FAILURE_MSG;
import static io.cloudslang.content.vmware.constants.ErrorMessages.ANTI_AFFINE_HOST_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.CLUSTER_RULE_COULD_NOT_BE_FOUND;
import static io.cloudslang.content.vmware.constants.ErrorMessages.FAILURE_MSG;
import static io.cloudslang.content.vmware.constants.ErrorMessages.RULE_ALREADY_EXISTS;
import static io.cloudslang.content.vmware.constants.ErrorMessages.SUCCESS_MSG;
import static io.cloudslang.content.vmware.constants.ErrorMessages.VM_GROUP_DOES_NOT_EXIST;
import static io.cloudslang.content.vmware.constants.ErrorMessages.VM_NOT_FOUND;

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

        final ManagedObjectReference vmMor = getVirtualMachineReference(vmInputs, connectionResources);

        final ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

        final ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName());
        final ClusterDasVmConfigSpec clusterDasVmConfigSpec = getClusterVmConfiguration(clusterConfigInfoEx, vmMor, restartPriority);

        final ManagedObjectReference task = connectionResources.getVimPortType().
                reconfigureComputeResourceTask(clusterMor, createClusterConfigSpecEx(clusterConfigInfoEx, clusterDasVmConfigSpec), true);

        final Map<String, String> resultMap = new ResponseHelper(connectionResources, task)
                .getResultsMap(String.format(SUCCESS_MSG, vmInputs.getClusterName(), task.getValue()),
                        String.format(FAILURE_MSG, vmInputs.getClusterName()));
        connectionResources.getConnection().disconnect();
        return resultMap;
    }

    private ManagedObjectReference getVirtualMachineReference(final VmInputs vmInputs, final ConnectionResources connectionResources) throws Exception {
        if (StringUtilities.isNotEmpty(vmInputs.getVirtualMachineName())) {
            return getVmMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
        } else {
            return new MorObjectHandler().getMorById(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
        }
    }

    public Map<String, String> createVmGroup(HttpInputs httpInputs, VmInputs vmInputs, List<String> vmNameList) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ClusterVmGroup clusterVmGroup = new ClusterVmGroup();
        clusterVmGroup.setName(vmInputs.getVmGroupName());
        clusterVmGroup.getVm().addAll(getVmManagedObjectReferences(vmNameList, connectionResources));

        return createGroup(vmInputs, connectionResources, clusterVmGroup);
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
        ManagedObjectReference task = connectionResources.getVimPortType().
                reconfigureComputeResourceTask(clusterMor, clusterConfigSpecEx, true);

        Map<String, String> resultMap = new ResponseHelper(connectionResources, task)
                .getResultsMap(String.format(SUCCESS_MSG, vmInputs.getClusterName(), task.getValue()),
                        String.format(FAILURE_MSG, vmInputs.getClusterName()));
        connectionResources.getConnection().disconnect();
        return resultMap;
    }

    public Map<String, String> deleteVmGroup(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

        ClusterVmGroup clusterVmGroup = new ClusterVmGroup();
        clusterVmGroup.setName(vmInputs.getVmGroupName());

        ClusterGroupSpec clusterGroupSpec = new ClusterGroupSpec();
        clusterGroupSpec.setInfo(clusterVmGroup);
        clusterGroupSpec.setOperation(ArrayUpdateOperation.REMOVE);
        clusterGroupSpec.setRemoveKey(vmInputs.getVmGroupName());

        return reconfigureClusterGroup(vmInputs, connectionResources, clusterMor, clusterGroupSpec);
    }

    public String listGroups(HttpInputs httpInputs, String clusterName, String delimiter, Class clazz) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), clusterName);

        ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, clusterName);

        List<String> groupNameList = new ArrayList<>();
        for (ClusterGroupInfo clusterGroupInfo : clusterConfigInfoEx.getGroup()) {
            if (clusterGroupInfo.getClass().isAssignableFrom(clazz)) {
                groupNameList.add(clusterGroupInfo.getName());
            }
        }
        String result = StringUtilities.join(groupNameList, delimiter);
        connectionResources.getConnection().disconnect();
        return result;
    }

    public Map<String, String> createHostGroup(HttpInputs httpInputs, VmInputs vmInputs, List<String> hostNameList) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ClusterHostGroup clusterHostGroup = new ClusterHostGroup();
        clusterHostGroup.setName(vmInputs.getHostGroupName());
        clusterHostGroup.getHost().addAll(getHostManagedObjectReferences(hostNameList, connectionResources));

        return createGroup(vmInputs, connectionResources, clusterHostGroup);
    }

    public Map<String, String> deleteHostGroup(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

        ClusterHostGroup clusterHostGroup = new ClusterHostGroup();
        clusterHostGroup.setName(vmInputs.getHostGroupName());

        ClusterGroupSpec clusterGroupSpec = new ClusterGroupSpec();
        clusterGroupSpec.setInfo(clusterHostGroup);
        clusterGroupSpec.setOperation(ArrayUpdateOperation.REMOVE);
        clusterGroupSpec.setRemoveKey(vmInputs.getHostGroupName());

        return reconfigureClusterGroup(vmInputs, connectionResources, clusterMor, clusterGroupSpec);
    }

    public Map<String, String> createAffinityRule(HttpInputs httpInputs, VmInputs vmInputs,
                                                  String affineHostGroupName, String antiAffineHostGroupName) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

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
    }

    private Map<String, String> reconfigureClusterRule(VmInputs vmInputs, ConnectionResources connectionResources, ManagedObjectReference clusterMor, ClusterRuleSpec clusterRuleSpec) throws RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg, InvalidPropertyFaultMsg {
        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        clusterConfigSpecEx.getRulesSpec().add(clusterRuleSpec);

        return reconfigureCluster(vmInputs, connectionResources, clusterMor, clusterConfigSpecEx);
    }

    public Map<String, String> deleteClusterRule(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs);

        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), vmInputs.getClusterName());

        List<ClusterRuleInfo> clusterRuleInfoList = getClusterConfiguration(connectionResources, clusterMor, vmInputs.getClusterName()).getRule();

        ClusterRuleInfo clusterRuleInfo = getClusterRuleInfo(clusterRuleInfoList, vmInputs.getRuleName());
        ClusterRuleSpec clusterRuleSpec = new ClusterRuleSpec();
        clusterRuleSpec.setInfo(clusterRuleInfo);
        clusterRuleSpec.setOperation(ArrayUpdateOperation.REMOVE);
        clusterRuleSpec.setRemoveKey(clusterRuleInfo.getKey());

        return reconfigureClusterRule(vmInputs, connectionResources, clusterMor, clusterRuleSpec);
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
        if (StringUtilities.isNotBlank(affineHostGroupName)) {
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
