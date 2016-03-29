package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.VmInputs;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class MorObjectHandler {
    public ManagedObjectReference getVmMor(ConnectionResources connectionResources, String objectType, String objectName) throws Exception {
        return connectionResources.getGetMOREF().inContainerByType(connectionResources.getMorRootFolder(), objectType).get(objectName);
    }

    public Map<String, ManagedObjectReference> getSpecificObjectsMap(ConnectionResources connectionResources, String objectType)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getGetMOREF().inContainerByType(connectionResources.getMorRootFolder(), objectType);
    }

    public ManagedObjectReference getEnvironmentBrowser(ConnectionResources connectionResources, String objectType)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{objectType}).get(objectType);
    }
}