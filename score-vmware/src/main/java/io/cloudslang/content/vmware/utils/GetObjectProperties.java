package io.cloudslang.content.vmware.utils;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mihai Tusa.
 * 1/15/2016.
 */
public class GetObjectProperties {

    private GetObjectProperties() {}

    /**
     * Retrieve contents for a single object based on the property collector
     * registered with the service.
     *
     * @param mor        Managed Object Reference to get contents for
     * @param properties names of properties of object to retrieve
     * @return retrieved object contents
     */
    public static ObjectContent[] getObjectProperties(ConnectionResources connectionResources,
                                                      ManagedObjectReference mor,
                                                      String[] properties)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        if (mor == null) {
            return null;
        }

        PropertyFilterSpec spec = new PropertyFilterSpec();
        spec.getPropSet().add(new PropertySpec());

        if ((properties == null || properties.length == 0)) {
            spec.getPropSet().get(0).setAll(Boolean.TRUE);
        } else {
            spec.getPropSet().get(0).setAll(Boolean.FALSE);
        }

        spec.getPropSet().get(0).setType(mor.getType());
        spec.getPropSet().get(0).getPathSet().addAll(Arrays.asList(properties));
        spec.getObjectSet().add(new ObjectSpec());
        spec.getObjectSet().get(0).setObj(mor);
        spec.getObjectSet().get(0).setSkip(Boolean.FALSE);

        List<PropertyFilterSpec> propertyFilterSpecs = new ArrayList<>(1);
        propertyFilterSpecs.add(spec);
        List<ObjectContent> objectContentList = retrievePropertiesAllObjects(connectionResources, propertyFilterSpecs);

        return objectContentList.toArray(new ObjectContent[objectContentList.size()]);
    }

    /**
     * Uses the new RetrievePropertiesEx method to emulate the now deprecated
     * RetrieveProperties method
     *
     * @param propertyFilterSpecList
     * @return list of object content
     * @throws Exception
     */
    public static List<ObjectContent> retrievePropertiesAllObjects(ConnectionResources connectionResources,
                                                                   List<PropertyFilterSpec> propertyFilterSpecList)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        VimPortType vimPort = connectionResources.getVimPortType();
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);
        ManagedObjectReference propertyCollectorReference = serviceContent.getPropertyCollector();
        RetrieveOptions propertyObjectRetrieveOptions = new RetrieveOptions();
        List<ObjectContent> objectContentList = new ArrayList<>();

        RetrieveResult results = vimPort.retrievePropertiesEx(propertyCollectorReference,
                propertyFilterSpecList,
                propertyObjectRetrieveOptions);

        if (results != null && results.getObjects() != null && !results.getObjects().isEmpty()) {
            objectContentList.addAll(results.getObjects());
        }

        String token = null;
        if (results != null && results.getToken() != null) {
            token = results.getToken();
        }

        while (token != null && !token.isEmpty()) {
            results = vimPort.continueRetrievePropertiesEx(propertyCollectorReference, token);
            token = null;
            if (results != null) {
                token = results.getToken();
                if (results.getObjects() != null && !results.getObjects().isEmpty()) {
                    objectContentList.addAll(results.getObjects());
                }
            }
        }

        return objectContentList;
    }
}
