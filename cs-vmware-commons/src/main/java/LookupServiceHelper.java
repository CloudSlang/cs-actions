/*
 * (c) Copyright 2023 Open Text
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


import com.vmware.content.Library;
import com.vmware.vcenter.VM;
import com.vmware.vcenter.lookup.*;

import java.util.List;

public class LookupServiceHelper {

//    public String getMgmtNodeId(String targetNodeName) throws RuntimeFaultFaultMsg {
//        // 1 - List the vCenter Server instances.
//        List<LookupServiceRegistrationInfo> serviceInfos = lookupServiceUrls("com.vmware.cis",
//                "vcenterserver",
//                "vmomi",
//                "com.vmware.vim");
//        // 2 - Find the matching node name and save the ID.
//        for (LookupServiceRegistrationInfo serviceInfo : serviceInfos)
//            for (LookupServiceRegistrationAttribute serviceAttr : serviceInfo.getServiceAttributes())
//                if ("com.vmware.vim.vcenter.instanceName".equals(serviceAttr.getKey()))
//                    if (serviceAttr.getValue().equals(targetNodeName))
//                        return serviceInfo.getNodeId();
//        return "";
//    }

//    VM vmService = stubFactory.createStub(VM.class, stubConfig);
//
//
//    // Create service stubs for the Content Library service.
//    Library libraryService = stubFactory.createStub(Library.class, stubConfig);
//
//    List<String> listContentLibraries = libraryService.list();

    //This returns the data of a content library by id
//        System.out.println(libraryService.get("94623efe-f568-460e-901c-431d5e458473"));
//        System.out.println(Arrays.toString(listContentLibraries.toArray()));


//            Item ItemsService = stubFactory.createStub(Item.class, stubConfig);
//
//           String libraryItemId = getLibraryItemIdByName("delete_me12373",ItemsService,"94623efe-f568-460e-901c-431d5e458473");
//           System.out.println(libraryItemId);

}
