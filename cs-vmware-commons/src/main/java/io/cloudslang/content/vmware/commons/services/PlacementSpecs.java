/*
 * Copyright 2023 Open Text
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

import com.vmware.vcenter.VMTypes;
import com.vmware.vcenter.ovf.LibraryItemTypes;
import com.vmware.vcenter.vm_template.LibraryItems;
import com.vmware.vcenter.vm_template.LibraryItemsTypes;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import org.apache.commons.lang3.StringUtils;


public class PlacementSpecs {
    public static LibraryItemsTypes.DeploySpec getDeploySpec(DeployTemplateFromLibraryInputs deployInputs,
                                                             LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage,
                                                             LibraryItems.DeployPlacementSpec deployPlacementSpec) {
        LibraryItemsTypes.DeploySpec vmDeploySpec = new LibraryItemsTypes.DeploySpec();

        vmDeploySpec.setName(deployInputs.getVmName());
        if (!StringUtils.isEmpty(deployInputs.getDescription()))
            vmDeploySpec.setDescription(deployInputs.getDescription());
        vmDeploySpec.setVmHomeStorage(deploySpecVmHomeStorage);
        vmDeploySpec.setPlacement(deployPlacementSpec);

        return vmDeploySpec;
    }

    public static LibraryItemsTypes.CreateSpec getCreateSpec(DeployTemplateFromLibraryInputs deployInputs,
                                                             LibraryItemsTypes.CreateSpecVmHomeStorage deploySpecVmHomeStorage,
                                                             LibraryItemsTypes.CreatePlacementSpec deployPlacementSpec) {
        LibraryItemsTypes.CreateSpec vmCreateSpec = new LibraryItemsTypes.CreateSpec();

        vmCreateSpec.setName(deployInputs.getVmName());
        if (!StringUtils.isEmpty(deployInputs.getDescription()))
            vmCreateSpec.setDescription(deployInputs.getDescription());
        vmCreateSpec.setSourceVm(deployInputs.getVmSource());
        vmCreateSpec.setLibrary("94623efe-f568-460e-901c-431d5e458473");
        vmCreateSpec.setVmHomeStorage(deploySpecVmHomeStorage);
        vmCreateSpec.setPlacement(deployPlacementSpec);


        return vmCreateSpec;
    }
    public static LibraryItems.CreatePlacementSpec getCreatePlacementSpec(DeployTemplateFromLibraryInputs deployInputs) {
        LibraryItems.CreatePlacementSpec vmCreatePlacementSpec = new LibraryItems.CreatePlacementSpec();

            vmCreatePlacementSpec.setFolder(deployInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployInputs.getCluster()))
            vmCreatePlacementSpec.setCluster(deployInputs.getCluster());

        if (!StringUtils.isEmpty(deployInputs.getVmResourcePool()))
            vmCreatePlacementSpec.setResourcePool(deployInputs.getVmResourcePool());

        if (!StringUtils.isEmpty(deployInputs.getHostSystem()))
            vmCreatePlacementSpec.setHost(deployInputs.getHostSystem());

        return vmCreatePlacementSpec;
    }

    public static LibraryItems.DeployPlacementSpec getDeployPlacementSpec(DeployTemplateFromLibraryInputs deployInputs) {
        LibraryItems.DeployPlacementSpec vmDeployPlacementSpec = new LibraryItems.DeployPlacementSpec();

            vmDeployPlacementSpec.setFolder(deployInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployInputs.getCluster()))
            vmDeployPlacementSpec.setCluster(deployInputs.getCluster());

        if (!StringUtils.isEmpty(deployInputs.getVmResourcePool()))
            vmDeployPlacementSpec.setResourcePool(deployInputs.getVmResourcePool());

        if (!StringUtils.isEmpty(deployInputs.getHostSystem()))
            vmDeployPlacementSpec.setHost(deployInputs.getHostSystem());

        return vmDeployPlacementSpec;
    }

    public static LibraryItemsTypes.DeploySpecVmHomeStorage getDeploySpecVmHomeStorage(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {
        LibraryItemsTypes.DeploySpecVmHomeStorage deploySpecVmHomeStorage = new LibraryItemsTypes.DeploySpecVmHomeStorage();
        deploySpecVmHomeStorage.setDatastore(deployTemplateFromLibraryInputs.getDatastore());
        return deploySpecVmHomeStorage;
    }

    public static LibraryItemsTypes.CreateSpecVmHomeStorage getCreateSpecVmHomeStorage(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {

        LibraryItemsTypes.CreateSpecVmHomeStorage createSpecVmHomeStorage = new LibraryItemsTypes.CreateSpecVmHomeStorage();
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getDatastore()))
            createSpecVmHomeStorage.setDatastore(deployTemplateFromLibraryInputs.getDatastore());
        return createSpecVmHomeStorage;
    }

    public static VMTypes.ClonePlacementSpec getClonePlacementSpec(DeployTemplateFromLibraryInputs deployTemplateFromLibraryInputs) {

        VMTypes.ClonePlacementSpec vmClonePlacementSpec = new VMTypes.ClonePlacementSpec();
        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmFolder()))
            vmClonePlacementSpec.setFolder(deployTemplateFromLibraryInputs.getVmFolder());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getCluster()))
            vmClonePlacementSpec.setCluster(deployTemplateFromLibraryInputs.getCluster());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getVmResourcePool()))
            vmClonePlacementSpec.setResourcePool(deployTemplateFromLibraryInputs.getVmResourcePool());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getHostSystem()))
            vmClonePlacementSpec.setHost(deployTemplateFromLibraryInputs.getHostSystem());

        if (!StringUtils.isEmpty(deployTemplateFromLibraryInputs.getDatastore()))
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
}
