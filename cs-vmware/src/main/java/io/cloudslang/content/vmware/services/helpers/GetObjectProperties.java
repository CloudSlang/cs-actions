/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Mihai Tusa.
 * 1/15/2016.
 */
public class GetObjectProperties {
    private GetObjectProperties() {
        // prevent instantiation
    }

    /**
     * Retrieve contents for a single object based on the property collector registered with the service.
     *
     * @param mor        Managed Object Reference to get contents for
     * @param properties names of properties of object to retrieve
     * @return retrieved object contents
     */
    @SuppressWarnings("ConstantConditions")
    @NotNull
    public static ObjectContent[] getObjectProperties(ConnectionResources connectionResources, ManagedObjectReference mor, String[] properties)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        if (mor == null) {
            return new ObjectContent[0];
        }

        PropertyFilterSpec spec = new PropertyFilterSpec();
        spec.getPropSet().add(new PropertySpec());

        if ((properties == null || properties.length == 0)) {
            spec.getPropSet().get(0).setAll(true);
        } else {
            spec.getPropSet().get(0).setAll(false);
        }

        spec.getPropSet().get(0).setType(mor.getType());
        spec.getPropSet().get(0).getPathSet().addAll(asList(properties));
        spec.getObjectSet().add(new ObjectSpec());
        spec.getObjectSet().get(0).setObj(mor);
        spec.getObjectSet().get(0).setSkip(false);

        List<PropertyFilterSpec> propertyFilterSpecs = new ArrayList<>(1);
        propertyFilterSpecs.add(spec);
        List<ObjectContent> objectContentList = retrievePropertiesAllObjects(connectionResources, propertyFilterSpecs);

        return objectContentList.toArray(new ObjectContent[objectContentList.size()]);
    }

    @NotNull
    public static ObjectContent getObjectProperty(final ConnectionResources connectionResources, final ManagedObjectReference mor,
                                                  final String property) throws Exception {
        final ObjectContent[] objectContents = getObjectProperties(connectionResources, mor, new String[]{property});
        if (objectContents.length != 0 && objectContents[0] != null) {
            return objectContents[0];
        }

        throw new Exception();
    }

    /**
     * Uses the new RetrievePropertiesEx method to emulate the now deprecated
     * RetrieveProperties method
     *
     * @param propertyFilterSpecList
     * @return list of object content
     * @throws Exception
     */
    private static List<ObjectContent> retrievePropertiesAllObjects(ConnectionResources connectionResources, List<PropertyFilterSpec> propertyFilterSpecList)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        VimPortType vimPort = connectionResources.getVimPortType();
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);
        ManagedObjectReference propertyCollectorReference = serviceContent.getPropertyCollector();
        RetrieveOptions propertyObjectRetrieveOptions = new RetrieveOptions();
        List<ObjectContent> objectContentList = new ArrayList<>();

        RetrieveResult results = vimPort.retrievePropertiesEx(propertyCollectorReference, propertyFilterSpecList, propertyObjectRetrieveOptions);

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
