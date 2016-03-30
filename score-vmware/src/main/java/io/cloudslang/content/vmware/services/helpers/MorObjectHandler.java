package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import java.util.List;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public class MorObjectHandler {
    public ManagedObjectReference getVmMor(ConnectionResources connectionResources, String filter, String parameter) throws Exception {
        ManagedObjectReference reference = connectionResources.getMorRootFolder();
        return getSpecificMor(connectionResources, reference, filter, parameter);
    }

    public Map<String, ManagedObjectReference> getSpecificObjectsMap(ConnectionResources connectionResources, String objectType)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getGetMOREF().inContainerByType(connectionResources.getMorRootFolder(), objectType);
    }

    public ManagedObjectReference getEnvironmentBrowser(ConnectionResources connectionResources, String filter)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference reference = connectionResources.getComputeResourceMor();
        return getProperty(connectionResources, reference, filter, filter);
    }

    public ManagedObjectReference getSpecificMor(ConnectionResources connectionResources, ManagedObjectReference reference,
                                                 String filter, String parameter) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getGetMOREF().inContainerByType(reference, filter, new RetrieveOptions()).get(parameter);
    }

    public Object getObjectProperties(ConnectionResources connectionResources, ManagedObjectReference reference, String filter)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return connectionResources.getGetMOREF().entityProps(reference, new String[]{filter}).get(filter);
    }

    private ManagedObjectReference getProperty(ConnectionResources connectionResources, ManagedObjectReference reference,
                                               String filter, String parameter) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return (ManagedObjectReference) connectionResources.getGetMOREF().entityProps(reference, new String[]{filter}).get(parameter);
    }
}