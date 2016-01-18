package io.cloudslang.content.vmware.connection.helpers;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.helpers.build.ObjectSpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.PropertyFilterSpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.PropertySpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.TraversalSpecBuilder;
import io.cloudslang.content.vmware.constants.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Mihai Tusa.
 * 10/19/2015.
 */
public class GetMOREF {
    Connection connection;
    VimPortType vimPort;
    ServiceContent serviceContent;

    public GetMOREF(Connection connection) {
        this.connection = connection;
        this.serviceContent = connection.getServiceContent();
        this.vimPort = connection.getVimPort();
    }

    /**
     * Initialize the helper object on the current connection at invocation time. Do not initialize on construction
     * since the connection may not be ready yet.
     */

    public RetrieveResult containerViewByType(
            final ManagedObjectReference container,
            final String morefType,
            final RetrieveOptions retrieveOptions
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        return this.containerViewByType(container, morefType, retrieveOptions, Constants.NAME);
    }

    /**
     * Returns the raw RetrieveResult object for the provided container filtered on properties list
     *
     * @param container       - container to look in
     * @param morefType       - type to filter for
     * @param morefProperties - properties to include
     * @return com.vmware.vim25.RetrieveResult for this query
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     */
    public RetrieveResult containerViewByType(
            final ManagedObjectReference container,
            final String morefType,
            final RetrieveOptions retrieveOptions,
            final String... morefProperties
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(container, morefType, morefProperties);

        return containerViewByType(retrieveOptions, propertyFilterSpecs);
    }

    public PropertyFilterSpec[] propertyFilterSpecs(
            ManagedObjectReference container,
            String morefType,
            String... morefProperties
    ) throws RuntimeFaultFaultMsg {

        ManagedObjectReference viewManager = serviceContent.getViewManager();
        ManagedObjectReference containerView = vimPort.createContainerView(viewManager,
                container,
                Arrays.asList(morefType),
                Boolean.TRUE);

        return new PropertyFilterSpec[]{
                new PropertyFilterSpecBuilder()
                        .propSet(new PropertySpecBuilder()
                                        .all(Boolean.FALSE)
                                        .type(morefType)
                                        .pathSet(morefProperties)
                        )
                        .objectSet(new ObjectSpecBuilder()
                                        .obj(containerView)
                                        .skip(Boolean.TRUE)
                                        .selectSet(new TraversalSpecBuilder()
                                                        .name(Constants.VIEW)
                                                        .path(Constants.VIEW)
                                                        .skip(Boolean.FALSE)
                                                        .type(Constants.CONTAINER_VIEW)
                                        )
                        )
        };
    }

    public RetrieveResult containerViewByType(final RetrieveOptions retrieveOptions,
                                              final PropertyFilterSpec... propertyFilterSpecs)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {

        return vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(),
                Arrays.asList(propertyFilterSpecs),
                retrieveOptions
        );
    }

    /**
     * Returns all the MOREFs of the specified type that are present under the
     * container
     *
     * @param folder    {@link ManagedObjectReference} of the container to begin the
     *                  search from
     * @param morefType Type of the managed entity that needs to be searched
     * @return Map of name and MOREF of the managed objects present. If none
     * exist then empty Map is returned
     * @throws InvalidPropertyFaultMsg
     * @throws RuntimeFaultFaultMsg
     */
    public Map<String, ManagedObjectReference> inContainerByType(ManagedObjectReference folder,
                                                                 String morefType,
                                                                 RetrieveOptions retrieveOptions)
            throws InvalidPropertyFaultMsg,
            RuntimeFaultFaultMsg {

        RetrieveResult results = containerViewByType(folder, morefType, retrieveOptions);

        return toMap(results);
    }

    public Map<String, ManagedObjectReference> toMap(RetrieveResult rslts)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        final Map<String, ManagedObjectReference> tgtMoref = new HashMap<>();
        String token;
        token = populate(rslts, tgtMoref);

        while (token != null && !token.isEmpty()) {
            // fetch results based on new token
            rslts = vimPort.continueRetrievePropertiesEx(serviceContent.getPropertyCollector(), token);
            token = populate(rslts, tgtMoref);
        }

        return tgtMoref;
    }

    public static String populate(final RetrieveResult results, final Map<String, ManagedObjectReference> tgtMoref) {
        String token = null;
        if (results != null) {
            token = results.getToken();
            for (ObjectContent oc : results.getObjects()) {
                ManagedObjectReference mr = oc.getObj();
                String entityNm = null;
                List<DynamicProperty> dps = oc.getPropSet();
                if (dps != null) {
                    for (DynamicProperty dp : dps) {
                        entityNm = (String) dp.getVal();
                    }
                }
                tgtMoref.put(entityNm, mr);
            }
        }

        return token;
    }

    /**
     * Method to retrieve properties of a {@link ManagedObjectReference}
     *
     * @param entityMor {@link ManagedObjectReference} of the entity
     * @param props     Array of properties to be looked up
     * @return Map of the property name and its corresponding value
     * @throws InvalidPropertyFaultMsg If a property does not exist
     * @throws RuntimeFaultFaultMsg
     */
    public Map<String, Object> entityProps(ManagedObjectReference entityMor, String[] props)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {

        final HashMap<String, Object> retVal = new HashMap<>();

        // Create PropertyFilterSpec using the PropertySpec and ObjectPec
        PropertyFilterSpec[] propertyFilterSpecs = {
                new PropertyFilterSpecBuilder()
                        .propSet(
                                // Create Property Spec
                                new PropertySpecBuilder()
                                        .all(Boolean.FALSE)
                                        .type(entityMor.getType())
                                        .pathSet(props)
                        )
                        .objectSet(
                                // Now create Object Spec
                                new ObjectSpecBuilder()
                                        .obj(entityMor)
                        )
        };

        List<ObjectContent> oCont =
                vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(),
                        Arrays.asList(propertyFilterSpecs), new RetrieveOptions()).getObjects();

        if (oCont != null) {
            for (ObjectContent oc : oCont) {
                List<DynamicProperty> dps = oc.getPropSet();
                for (DynamicProperty dp : dps) {
                    retVal.put(dp.getName(), dp.getVal());
                }
            }
        }
        return retVal;
    }

    public Map<String, ManagedObjectReference> inContainerByType(ManagedObjectReference container,
                                                                 String morefType)
            throws InvalidPropertyFaultMsg,
            RuntimeFaultFaultMsg {

        return inContainerByType(container, morefType, new RetrieveOptions());
    }
}