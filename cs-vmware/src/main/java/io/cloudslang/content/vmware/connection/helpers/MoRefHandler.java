package io.cloudslang.content.vmware.connection.helpers;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RetrieveResult;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.connection.helpers.build.ObjectSpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.PropertyFilterSpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.PropertySpecBuilder;
import io.cloudslang.content.vmware.connection.helpers.build.TraversalSpecBuilder;
import io.cloudslang.content.vmware.entities.ManagedObjectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.vmware.constants.ErrorMessages.REFERENCE_TYPE_WITH_ID_NOT_FOUND;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class MoRefHandler {
    private final VimPortType vimPort;
    private final ServiceContent serviceContent;

    public MoRefHandler(Connection connection) {
        this.serviceContent = connection.getServiceContent();
        this.vimPort = connection.getVimPort();
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
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        RetrieveResult results = containerViewByType(folder, morefType, retrieveOptions);

        return toMap(results);
    }

    public ManagedObjectReference findManagedObjectReferenceByTypeAndId(final ManagedObjectReference folder,
                                                                        final String morefType,
                                                                        final RetrieveOptions retrieveOptions, final String id) throws Exception {
        final PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(folder, morefType);
        final ManagedObjectReference searchedReference = findComponentReference(propertyFilterSpecs, retrieveOptions, id);
        if (searchedReference != null) {
            return searchedReference;
        }
        throw new RuntimeException(String.format(REFERENCE_TYPE_WITH_ID_NOT_FOUND, morefType, id));
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
        PropertyFilterSpec[] propertyFilterSpecs = {new PropertyFilterSpecBuilder().propSet(
                // Create Property Spec
                new PropertySpecBuilder().all(false).type(entityMor.getType()).pathSet(props))
                .objectSet(
                // Now create Object Spec
                new ObjectSpecBuilder().obj(entityMor))};

        List<ObjectContent> oCont = vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(),
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
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        return inContainerByType(container, morefType, new RetrieveOptions());
    }

    /**
     * Initialize the helper object on the current connection at invocation time. Do not initialize on construction
     * since the connection may not be ready yet.
     */

    private RetrieveResult containerViewByType(final ManagedObjectReference container,
                                               final String morefType,
                                               final RetrieveOptions retrieveOptions
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return this.containerViewByType(container, morefType, retrieveOptions, ManagedObjectType.NAME.getValue());
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
    private RetrieveResult containerViewByType(final ManagedObjectReference container,
                                               final String morefType,
                                               final RetrieveOptions retrieveOptions,
                                               final String... morefProperties
    ) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        PropertyFilterSpec[] propertyFilterSpecs = propertyFilterSpecs(container, morefType, morefProperties);

        return containerViewByType(retrieveOptions, propertyFilterSpecs);
    }

    private PropertyFilterSpec[] propertyFilterSpecs(ManagedObjectReference container,
                                                     String morefType,
                                                     String... morefProperties
    ) throws RuntimeFaultFaultMsg {
        ManagedObjectReference viewManager = serviceContent.getViewManager();
        ManagedObjectReference containerView = vimPort.createContainerView(viewManager, container,
                Arrays.asList(morefType), true);

        return new PropertyFilterSpec[]{
                new PropertyFilterSpecBuilder().propSet(new PropertySpecBuilder().all(false)
                        .type(morefType).pathSet(morefProperties)).objectSet(new ObjectSpecBuilder()
                        .obj(containerView).skip(true).selectSet(new TraversalSpecBuilder()
                                .name(ManagedObjectType.VIEW.getValue())
                                .path(ManagedObjectType.VIEW.getValue())
                                .skip(false)
                                .type(ManagedObjectType.CONTAINER_VIEW.getValue())))};
    }

    private RetrieveResult containerViewByType(final RetrieveOptions retrieveOptions,
                                               final PropertyFilterSpec... propertyFilterSpecs)
            throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        return vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(), Arrays.asList(propertyFilterSpecs),
                retrieveOptions);
    }

    private Map<String, ManagedObjectReference> toMap(RetrieveResult result)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        final Map<String, ManagedObjectReference> tgtMoref = new HashMap<>();

        String token = populate(result, tgtMoref);
        while (token != null && !token.isEmpty()) {
            // fetch results based on new token
            result = vimPort.continueRetrievePropertiesEx(serviceContent.getPropertyCollector(), token);
            token = populate(result, tgtMoref);
        }

        return tgtMoref;
    }

    private static String populate(final RetrieveResult results, final Map<String, ManagedObjectReference> tgtMoref) {
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

    private ManagedObjectReference findComponentReference(final PropertyFilterSpec[] propertyFilterSpecs,
                                                          final RetrieveOptions retrieveOptions, final String id) throws Exception {
        String token = null;
        ManagedObjectReference searched;
        do {
            final RetrieveResult retrieveResult = retrievePropertiesEx(Arrays.asList(propertyFilterSpecs), retrieveOptions, token);
            token = retrieveResult.getToken();
            searched = getFromRetrieveResult(retrieveResult, id);
        } while (searched == null && StringUtilities.isNotEmpty(token));
        return searched;
    }

    private RetrieveResult retrievePropertiesEx(final List<PropertyFilterSpec> propertyFilterSpecs, final RetrieveOptions retrieveOptions,
                                                final String token) throws Exception {
        if (isEmpty(token)) {
            return vimPort.retrievePropertiesEx(serviceContent.getPropertyCollector(), propertyFilterSpecs, retrieveOptions);
        }
        return vimPort.continueRetrievePropertiesEx(serviceContent.getPropertyCollector(), token);
    }

    private ManagedObjectReference getFromRetrieveResult(final RetrieveResult retrieveResult, final String id) {
        for (final ObjectContent oc : retrieveResult.getObjects()) {
            if (StringUtilities.equals(id, oc.getObj().getValue())) {
                return oc.getObj();
            }
        }
        return null;
    }
}
