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
package io.cloudslang.content.vmware.commons.utils;

import com.vmware.content.Library;
import com.vmware.content.library.Item;
import com.vmware.content.library.ItemTypes;
import com.vmware.vapi.bindings.StubConfiguration;
import com.vmware.vapi.bindings.StubFactory;
import com.vmware.vcenter.*;
import io.cloudslang.content.vmware.commons.entities.DeployTemplateFromLibraryInputs;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.vmware.commons.utils.Constants.*;

public class Utils {
    public static String getLibraryItemIdByName(Item libItemService, String templateName) {
        ItemTypes.FindSpec findSpec = new ItemTypes.FindSpec();
        findSpec.setName(templateName);
        try {
            return libItemService.find(findSpec).get(0);
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_LIBRARY_ITEM_NOT_FOUND + templateName);
        }
    }

    public static String getDatastoreIdByName(Datastore datastoreService, String datastoreName) {
        DatastoreTypes.FilterSpec filterSpec = new DatastoreTypes.FilterSpec();
        filterSpec.setNames(Collections.singleton(datastoreName));
        try {
            return datastoreService.list(filterSpec).get(0).getDatastore();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_DATASTORE_NOT_FOUND + datastoreName);
        }
    }

    public static String getLibraryIdByName(String libraryName, Library libraryService) {
        List<String> libraries = libraryService.list();
        for (String id : libraries) {
            if (libraryService.get(id).getName().equals(libraryName)) {
                return id;
            }
        }
        throw new RuntimeException(EXCEPTION_LIBRARY_NOT_FOUND + libraryName);
    }

    public static String getVmIdByName(VM vmService, String vmName) {
        List<VMTypes.Summary> vmList = vmService.list(new VMTypes.FilterSpec());
        for (VMTypes.Summary summary : vmList) {
            if (summary.getName().equals(vmName)) {
                return summary.getVm();
            }
        }
        throw new RuntimeException(EXCEPTION_VM_NOT_FOUND + vmName);
    }

    public static String getClusterIdByName(Cluster clusterService, String clusterName) {
        ClusterTypes.FilterSpec filterSpec = new ClusterTypes.FilterSpec();
        filterSpec.setNames(Collections.singleton(clusterName));
        try {
            return clusterService.list(filterSpec).get(0).getCluster();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_CLUSTER_NOT_FOUND + clusterName);
        }
    }

    public static String getFolderIdByName(Folder folderService, String folderName) {
        Folder.FilterSpec filterSpec = new Folder.FilterSpec();
        filterSpec.setNames(Collections.singleton(folderName));
        try {
            return folderService.list(filterSpec).get(0).getFolder();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_VM_FOLDER_NOT_FOUND + folderName);
        }
    }

    public static String getResourcePoolIdByName(ResourcePool resourcePoolService, String resourcePoolName) {
        ResourcePool.FilterSpec filterSpec = new ResourcePool.FilterSpec();
        filterSpec.setNames(Collections.singleton(resourcePoolName));
        try {
            return resourcePoolService.list(filterSpec).get(0).getResourcePool();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_RESOURCE_POOL_NOT_FOUND + resourcePoolName);
        }
    }

    public static String getHostIdByName(Host hostService, String hostSystemName) {
        Host.FilterSpec filterSpec = new Host.FilterSpec();
        filterSpec.setNames(Collections.singleton(hostSystemName));
        try {
            return hostService.list(filterSpec).get(0).getHost();
        } catch (Exception e) {
            throw new RuntimeException(EXCEPTION_HOST_NOT_FOUND + hostSystemName);
        }
    }

    public static void setResourcesIds(DeployTemplateFromLibraryInputs deployInputs, StubConfiguration stubConfig, StubFactory stubFactory) {
        Item itemService = stubFactory.createStub(Item.class, stubConfig);
        deployInputs.setVmSource(getLibraryItemIdByName(itemService, deployInputs.getVmSource()));

        if (!StringUtils.isEmpty(deployInputs.getVmFolder())) {
            Folder folderService = stubFactory.createStub(Folder.class, stubConfig);
            deployInputs.setVmFolder(getFolderIdByName(folderService, deployInputs.getVmFolder()));
        }
        if (!StringUtils.isEmpty(deployInputs.getCluster())) {
            Cluster clusterService = stubFactory.createStub(Cluster.class, stubConfig);
            deployInputs.setCluster(getClusterIdByName(clusterService, deployInputs.getCluster()));
        }
        if (!StringUtils.isEmpty(deployInputs.getDatastore())) {
            Datastore datastoreService = stubFactory.createStub(Datastore.class, stubConfig);
            deployInputs.setDatastore(getDatastoreIdByName(datastoreService, deployInputs.getDatastore()));
        }
        if (!StringUtils.isEmpty(deployInputs.getVmResourcePool())) {
            ResourcePool resourcePoolService = stubFactory.createStub(ResourcePool.class, stubConfig);
            deployInputs.setVmResourcePool(getResourcePoolIdByName(resourcePoolService, deployInputs.getVmResourcePool()));
        }
        if (!StringUtils.isEmpty(deployInputs.getHostSystem())) {
            Host hostService = stubFactory.createStub(Host.class, stubConfig);
            deployInputs.setHostSystem(getHostIdByName(hostService, deployInputs.getHostSystem()));
        }
    }
}
