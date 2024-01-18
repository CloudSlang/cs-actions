/*
 * (c) Copyright 2024 Open Text
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
/*
 * Copyright 2024 Open Text
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
import io.cloudslang.content.vmware.commons.utils.ExecutionTimeout;
import vmware.samples.common.authentication.VapiAuthenticationHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.vmware.commons.services.PlacementSpecs.*;
import static io.cloudslang.content.vmware.commons.utils.Constants.NAME;
import static io.cloudslang.content.vmware.commons.utils.Constants.ZERO;
import static io.cloudslang.content.vmware.commons.utils.Utils.setResourcesIds;


public class DeployTemplateFromLibraryService {
    public static Map<String, String> execute(DeployTemplateFromLibraryInputs deployInputs) throws Exception {

        if (deployInputs.getTimeout().equals(ZERO))
            return executeOperation(deployInputs);
        else {
            final Map<String, String> result = new HashMap<>();
            ExecutionTimeout.runWithTimeout(new Runnable() {

                @Override
                public void run() {
                    result.putAll(DeployTemplateFromLibraryService.executeOperation(deployInputs));
                }
            }, Integer.parseInt(deployInputs.getTimeout()), TimeUnit.SECONDS);
            return result;
        }
    }

    private static Map<String, String> executeOperation(DeployTemplateFromLibraryInputs deployInputs) {
        VapiAuthenticationHelper vapiHelper = new VapiAuthenticationHelper();

        try {
            HttpConfiguration httpConfig = vapiHelper.buildHttpConfiguration(true);
            StubConfiguration stubConfig = vapiHelper.loginByUsernameAndPassword(deployInputs.getHost(), deployInputs.getUsername(), deployInputs.getPassword(), httpConfig);
            StubFactory stubFactory = vapiHelper.getStubFactory();
            LibraryItems libraryItemsService = stubFactory.createStub(LibraryItems.class, stubConfig);

            if (deployInputs.getVmIdentifierType().equalsIgnoreCase(NAME))
                setResourcesIds(deployInputs, stubConfig, stubFactory);

            //these calls are for different use cases, maybe will be implemented in the future
            //return getSuccessResultsMap(createTemplate(deployInputs, libraryItemsService));
            //return getSuccessResultsMap(deployOVFTemplate(deployInputs, libraryItemService));
            //return getSuccessResultsMap(cloneVM(getClonePlacementSpec(cloneVmInputs), cloneVmInputs, vmService).toString());}
            return getSuccessResultsMap(deployTemplate(deployInputs, libraryItemsService));

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            vapiHelper.logout();
        }
    }

    //deploys a template from Content Library to a virtual machine in Inventory
    private static String deployTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItems libraryItemsService) {
        LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage = getDeploySpecVmHomeStorage(deployTemplateFromLibraryInputs);
        LibraryItems.DeployPlacementSpec deployPlacementSpec = getDeployPlacementSpec(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.DeploySpec deploySpec = getDeploySpec(deployTemplateFromLibraryInputs, deploySpecVmHomeStorage, deployPlacementSpec);
        return libraryItemsService.deploy(deployTemplateFromLibraryInputs.getVmSource(), deploySpec);
    }
    //creates a template in Content Library from a VM from Inventory
    private static String createTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItems libraryItemsService) {
        LibraryItemsTypes.CreateSpecVmHomeStorage createSpecVmHomeStorage = getCreateSpecVmHomeStorage(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.CreatePlacementSpec createPlacementSpec = getCreatePlacementSpec(deployTemplateFromLibraryInputs);
        LibraryItemsTypes.CreateSpec createSpec = getCreateSpec(deployTemplateFromLibraryInputs, createSpecVmHomeStorage, createPlacementSpec);
        return libraryItemsService.create(createSpec);
    }

    //used to deploy OVF Template
    private static String deployOVFTemplate(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs, LibraryItem libraryItemService) {
        LibraryItemTypes.DeploymentTarget deploymentTarget = getDeploymentTarget(deployTemplateFromLibraryInputs);
        LibraryItemTypes.ResourcePoolDeploymentSpec resourcePoolDeploymentSpec = getResourcePoolDeploymentSpec(deployTemplateFromLibraryInputs);
        return libraryItemService.deploy(UUID.randomUUID().toString(), deployTemplateFromLibraryInputs.getVmSource(), deploymentTarget, resourcePoolDeploymentSpec).toString();
    }

    //clones a VM
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

