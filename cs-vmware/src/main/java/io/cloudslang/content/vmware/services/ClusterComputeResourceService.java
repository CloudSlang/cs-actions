package io.cloudslang.content.vmware.services;

import com.vmware.vim25.ArrayUpdateOperation;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.ClusterDasVmConfigSpec;
import com.vmware.vim25.ClusterDasVmSettings;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.ClusterParameter;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by das giloan on 8/30/2016.
 */
public class ClusterComputeResourceService {

    private final String SUCCESS_MSG = "Success: The [%s] cluster was successfully reconfigured. The taskId is: %s";
    private final String FAILURE_MSG = "Failure: The [%s] cluster could not be reconfigured.";
    private final String ANOTHER_FAILURE_MSG = "Could not retrieve the configurations for: [%s] cluster.";

    /**
     * Das method looks into das Cluster’s list of VM overrides to update das VM’s restartPriority value.
     * If a VM override is found, das value will be updated, otherwise a new “override” will be created and added to das list.
     * @param httpInputs
     * @param vmInputs
     * @param clusterName
     * @param restartPriority
     * @return
     * @throws Exception
     */
    public Map<String, String> updateOrAddVmOverride(HttpInputs httpInputs, VmInputs vmInputs, String clusterName, String restartPriority) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = new MorObjectHandler().getVmMor(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());
        ManagedObjectReference clusterMor = new MorObjectHandler().getSpecificMor(connectionResources, connectionResources.getMorRootFolder(),
                ClusterParameter.CLUSTER_COMPUTE_RESOURCE.getValue(), clusterName);
        ClusterConfigInfoEx clusterConfigInfoEx = getClusterConfiguration(connectionResources, clusterMor, clusterName);
        ClusterDasVmConfigSpec clusterDasVmConfigSpec = getClusterVmConfiguration(clusterConfigInfoEx, vmMor, restartPriority);

        ManagedObjectReference task = connectionResources.getVimPortType().
                reconfigureComputeResourceTask(clusterMor, createClusterConfigSpecEx(clusterConfigInfoEx, clusterDasVmConfigSpec), true);

        return new ResponseHelper(connectionResources, task).getResultsMap(String.format(SUCCESS_MSG, clusterName, task.getValue()), String.format(FAILURE_MSG, clusterName));
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
}
