package io.cloudslang.content.vmware.commons.utils;

import com.vmware.content.Library;
import com.vmware.content.library.Item;
import com.vmware.vcenter.*;
import com.vmware.vcenter.ovf.LibraryItemTypes;
import com.vmware.vcenter.vm_template.LibraryItems;
import com.vmware.vcenter.vm_template.LibraryItemsTypes;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Utils {
    public static LibraryItemsTypes.DeploySpec getDeploySpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs,
                                                             LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage,
                                                             LibraryItems.DeployPlacementSpec deployPlacementSpec) {
        LibraryItemsTypes.DeploySpec vmDeploySpec = new LibraryItemsTypes.DeploySpec();

        vmDeploySpec.setName(deployTemplateFromLibraryInputs.getVmName());
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getDescription()))
            vmDeploySpec.setDescription(deployTemplateFromLibraryInputs.getDescription());
        vmDeploySpec.setVmHomeStorage(deploySpecVmHomeStorage);
        vmDeploySpec.setPlacement(deployPlacementSpec);

        return vmDeploySpec;
    }

    public static LibraryItemsTypes.CreateSpec getCreateSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs,
                                                             LibraryItemsTypes.CreateSpecVmHomeStorage deploySpecVmHomeStorage,
                                                             LibraryItemsTypes.CreatePlacementSpec deployPlacementSpec) {
        LibraryItemsTypes.CreateSpec vmCreateSpec = new LibraryItemsTypes.CreateSpec();

        vmCreateSpec.setName(deployTemplateFromLibraryInputs.getVmName());
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getDescription()))
            vmCreateSpec.setDescription(deployTemplateFromLibraryInputs.getDescription());
        vmCreateSpec.setSourceVm(deployTemplateFromLibraryInputs.getVmSource());
        vmCreateSpec.setLibrary("94623efe-f568-460e-901c-431d5e458473");
        vmCreateSpec.setVmHomeStorage(deploySpecVmHomeStorage);
        vmCreateSpec.setPlacement(deployPlacementSpec);


        return vmCreateSpec;
    }
    public static LibraryItems.CreatePlacementSpec getCreatePlacementSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {
        LibraryItems.CreatePlacementSpec vmCreatePlacementSpec = new LibraryItems.CreatePlacementSpec();
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmFolder()))
            vmCreatePlacementSpec.setFolder(deployTemplateFromLibraryInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getCluster()))
            vmCreatePlacementSpec.setCluster(deployTemplateFromLibraryInputs.getCluster());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmResourcePool()))
            vmCreatePlacementSpec.setResourcePool(deployTemplateFromLibraryInputs.getVmResourcePool());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getHost()))
            vmCreatePlacementSpec.setHost(deployTemplateFromLibraryInputs.getHost());

        return vmCreatePlacementSpec;
    }

    public static LibraryItems.DeployPlacementSpec getDeployPlacementSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {
        LibraryItems.DeployPlacementSpec vmDeployPlacementSpec = new LibraryItems.DeployPlacementSpec();
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmFolder()))
            vmDeployPlacementSpec.setFolder(deployTemplateFromLibraryInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getCluster()))
            vmDeployPlacementSpec.setCluster(deployTemplateFromLibraryInputs.getCluster());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmResourcePool()))
            vmDeployPlacementSpec.setResourcePool(deployTemplateFromLibraryInputs.getVmResourcePool());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getHost()))
            vmDeployPlacementSpec.setHost(deployTemplateFromLibraryInputs.getHost());

        return vmDeployPlacementSpec;
    }

    public static LibraryItemsTypes.DeploySpecVmHomeStorage getDeploySpecVmHomeStorage(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {
        LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage = new LibraryItemsTypes.DeploySpecVmHomeStorage();
        deploySpecVmHomeStorage.setDatastore(deployTemplateFromLibraryInputs.getDatastore());
        return deploySpecVmHomeStorage;
    }

    public static LibraryItemsTypes.CreateSpecVmHomeStorage getCreateSpecVmHomeStorage(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {
        LibraryItemsTypes.CreateSpecVmHomeStorage createSpecVmHomeStorage = new LibraryItemsTypes.CreateSpecVmHomeStorage();
        createSpecVmHomeStorage.setDatastore(deployTemplateFromLibraryInputs.getDatastore());
        return createSpecVmHomeStorage;
    }

    public static VMTypes.ClonePlacementSpec getClonePlacementSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {

        VMTypes.ClonePlacementSpec vmClonePlacementSpec = new VMTypes.ClonePlacementSpec();
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmFolder()))
            vmClonePlacementSpec.setFolder(deployTemplateFromLibraryInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getCluster()))
            vmClonePlacementSpec.setCluster(deployTemplateFromLibraryInputs.getCluster());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmResourcePool()))
            vmClonePlacementSpec.setResourcePool(deployTemplateFromLibraryInputs.getVmResourcePool());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getHost()))
            vmClonePlacementSpec.setHost(deployTemplateFromLibraryInputs.getHost());

        if (StringUtils.isEmpty(deployTemplateFromLibraryInputs.getDatastore()))
            vmClonePlacementSpec.setDatastore(deployTemplateFromLibraryInputs.getDatastore());

        return vmClonePlacementSpec;
    }

    public static LibraryItemTypes.DeploymentTarget getDeploymentTarget(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs){
        LibraryItemTypes.DeploymentTarget deploymentTarget = new LibraryItemTypes.DeploymentTarget();
        deploymentTarget.setFolderId(deployTemplateFromLibraryInputs.getVmFolder());
        deploymentTarget.setHostId(deployTemplateFromLibraryInputs.getHost());
        deploymentTarget.setResourcePoolId(deployTemplateFromLibraryInputs.getVmResourcePool());
        return deploymentTarget;
    }

    public static LibraryItemTypes.ResourcePoolDeploymentSpec getResourcePoolDeploymentSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs){
        LibraryItemTypes.ResourcePoolDeploymentSpec resourcePoolDeploymentSpec = new LibraryItemTypes.ResourcePoolDeploymentSpec();
        resourcePoolDeploymentSpec.setDefaultDatastoreId(deployTemplateFromLibraryInputs.getDatastore());
        resourcePoolDeploymentSpec.setName(deployTemplateFromLibraryInputs.getVmName());
        resourcePoolDeploymentSpec.setAcceptAllEULA(true);
        return resourcePoolDeploymentSpec;
    }

    public static String getLibraryItemIdByName(String templateName, Item libItemService, String libraryId) {
        List<String> itemIds = libItemService.list(libraryId);
        for (String itemId : itemIds) {
            if (libItemService.get(itemId).getName().equals(templateName))
                return itemId;
        }
        throw new RuntimeException("The item was not found in the provided Content Library" + templateName);
    }

    public static String getLibraryIdByName(String libraryName, Library libraryService) {
        List<String> libraries = libraryService.list();
        for (String id : libraries) {
            if (libraryService.get(id).getName().equals(libraryName)) {
                return id;
            }
        }
        throw new RuntimeException("Library not found: " + libraryName);
    }


    public static String getVmIdByName(VM vmService, String vmName) {
        List<VMTypes.Summary> vmList = vmService.list(new VMTypes.FilterSpec());
        for (VMTypes.Summary summary : vmList) {
            if (summary.getName().equals(vmName)) {
                return summary.getVm();
            }
        }
        throw new RuntimeException("Virtual machine or template not found: " + vmName);
    }

    public static String getClusterIdByName(Cluster clusterService, String clusterName) {
        List<ClusterTypes.Summary> clusterList = clusterService.list(new ClusterTypes.FilterSpec());
        for (ClusterTypes.Summary summary : clusterList) {
            if (summary.getName().equals(clusterName)) {
                return summary.getCluster();
            }
        }
        throw new RuntimeException("Cluster not found: " + clusterName);
    }

    public static String getFolderIdByName(Folder folderService, String folderName) {
        List<FolderTypes.Summary> folderList = folderService.list(new FolderTypes.FilterSpec());
        for (FolderTypes.Summary summary : folderList) {
            if (summary.getName().equals(folderName)) {
                return summary.getFolder();
            }
        }
        throw new RuntimeException("Folder not found: " + folderName);
    }
}
