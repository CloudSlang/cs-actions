package io.cloudslang.content.vmware.commons.services;

import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vapi.protocol.HttpConfiguration;
import com.vmware.vcenter.VM;
import com.vmware.vcenter.VMTypes;
import com.vmware.vcenter.ovf.LibraryItem;
import com.vmware.vcenter.ovf.LibraryItemTypes;
import com.vmware.vcenter.vm_template.LibraryItems;
import com.vmware.vcenter.vm_template.LibraryItemsTypes;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import vmware.samples.common.authentication.VapiAuthenticationHelper;

import java.util.Map;
import java.util.UUID;

import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.vmware.commons.utils.Utils.*;

public class DeployTemplateFromLibraryService {
    public static Map<String, String> execute(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) throws Exception {
        VapiAuthenticationHelper vapiHelper = new VapiAuthenticationHelper();

        try {
            HttpConfiguration httpConfig = vapiHelper.buildHttpConfiguration(true);
            StubConfiguration stubConfig = vapiHelper.loginByUsernameAndPassword(deployTemplateFromLibraryInputs.getHost(), deployTemplateFromLibraryInputs.getUsername(), deployTemplateFromLibraryInputs.getPassword(), httpConfig);
            StubFactory stubFactory = vapiHelper.getStubFactory();


            LibraryItems libraryItemsService = stubFactory.createStub(LibraryItems.class, stubConfig);
            LibraryItem libraryItemService = stubFactory.createStub(LibraryItem.class, stubConfig);

            //return getSuccessResultsMap(createTemplate(deployTemplateFromLibraryInputs, libraryItemsService));
            //   return getSuccessResultsMap(deployOVFTemplate(deployTemplateFromLibraryInputs, libraryItemService));
            return getSuccessResultsMap(deployTemplate(deployTemplateFromLibraryInputs, libraryItemsService));
            // return getSuccessResultsMap(cloneVM(getClonePlacementSpec(cloneVmInputs), cloneVmInputs, vmService).toString());}
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            vapiHelper.logout();
        }
    }


    private static String createTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItems libraryItemsService) {
        LibraryItemsTypes.CreateSpecVmHomeStorage createSpecVmHomeStorage = getCreateSpecVmHomeStorage(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.CreatePlacementSpec createPlacementSpec = getCreatePlacementSpec(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.CreateSpec createSpec = getCreateSpec(deployTemplateFromLibraryInputs, createSpecVmHomeStorage, createPlacementSpec);
        return libraryItemsService.create(createSpec);
    }

    private static String deployTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItems libraryItemsService) {
        LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage = getDeploySpecVmHomeStorage(deployTemplateFromLibraryInputs);
        LibraryItems.DeployPlacementSpec deployPlacementSpec = getDeployPlacementSpec(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.DeploySpec deploySpec = getDeploySpec(deployTemplateFromLibraryInputs, deploySpecVmHomeStorage, deployPlacementSpec);
        return libraryItemsService.deploy(deployTemplateFromLibraryInputs.getVmSource(), deploySpec);
    }

    private static String deployOVFTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItem libraryItemService) {
        LibraryItemTypes.DeploymentTarget deploymentTarget = getDeploymentTarget(deployTemplateFromLibraryInputs);
        LibraryItemTypes.ResourcePoolDeploymentSpec resourcePoolDeploymentSpec = getResourcePoolDeploymentSpec(deployTemplateFromLibraryInputs);
        return libraryItemService.deploy(UUID.randomUUID().toString(), deployTemplateFromLibraryInputs.getVmSource(), deploymentTarget, resourcePoolDeploymentSpec).toString();
    }


    private static VMTypes.Info cloneVM(VMTypes.ClonePlacementSpec vmClonePlacementSpec, DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, VM vmService) {
        VMTypes.CloneSpec.Builder specBuilder = new VMTypes.CloneSpec.Builder(
                deployTemplateFromLibraryInputs.getVmSource(), deployTemplateFromLibraryInputs.getVmName()).setPlacement(vmClonePlacementSpec);
        // setPowerOn(this.powerOn);

        VMTypes.CloneSpec vmCloneSpec = specBuilder.build();


        System.out.println("\n\n#### Example: Clone VM with spec:\n" + vmCloneSpec);
        String resultVMId = vmService.clone(vmCloneSpec);
        VMTypes.Info vmInfo = vmService.get(resultVMId);
        System.out.println("\nVM Info:\n" + vmInfo);
        return vmInfo;
    }
}

