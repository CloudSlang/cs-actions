package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import io.cloudslang.content.vmware.connection.ConnectionResources;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class MorHandler {
    public ManagedObjectReference getSpecificMorObject(ConnectionResources connectionResources, String objectType,
                                                       String objectName) throws Exception {
        ServiceContent serviceContent = getServiceContent(connectionResources);
        return FindObjects.findObject(connectionResources.getVimPortType(), serviceContent, objectType, objectName);
    }

    private ServiceContent getServiceContent(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        return connectionResources.getVimPortType().retrieveServiceContent(serviceInstance);
    }
}
