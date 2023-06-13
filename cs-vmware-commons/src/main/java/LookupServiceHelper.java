

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
