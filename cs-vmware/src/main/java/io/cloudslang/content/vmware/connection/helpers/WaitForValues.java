/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.connection.helpers;

import com.vmware.vim25.HttpNfcLeaseState;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.ObjectUpdate;
import com.vmware.vim25.ObjectUpdateKind;
import com.vmware.vim25.PropertyChange;
import com.vmware.vim25.PropertyChangeOp;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertyFilterUpdate;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.UpdateSet;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.WaitOptions;
import io.cloudslang.content.vmware.connection.Connection;
import io.cloudslang.content.vmware.constants.Constants;
import org.w3c.dom.Element;

import java.util.Arrays;

public class WaitForValues {
    private static final String ERROR = "error";
    private static final String READY = "ready";
    private static final String FILTER_VALUES = "filtervals";
    private static final String KEY_VALUE_NULL_STRING = "val: null";
    private static final int MAX_TRIED_WAIT_FOR_UPDATE_COUNTER = 100;

    private ServiceContent serviceContent;
    private VimPortType vimPort;

    public WaitForValues(Connection connection) {
        this.serviceContent = connection.getServiceContent();
        this.vimPort = connection.getVimPort();
    }

    /**
     * Handle Updates for a single object. waits till expected values of
     * properties to check are reached Destroys the ObjectFilter when done.
     *
     * @param objMor         MOR of the Object to wait for</param>
     * @param filterProps    Properties list to filter
     * @param endWaitProps   Properties list to check for expected values these be properties
     *                       of a property in the filter properties list
     * @param expectedValues values for properties to end the wait
     * @return true indicating expected values were met, and false otherwise
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    public Object[] wait(ManagedObjectReference objMor, String[] filterProps, String[] endWaitProps, Object[][] expectedValues)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        String version = Constants.EMPTY;
        String stateVal = null;

        Object[] endValues = new Object[endWaitProps.length];
        Object[] filterValues = new Object[filterProps.length];

        PropertyFilterSpec spec = propertyFilterSpec(objMor, filterProps);
        ManagedObjectReference filterSpecRef = vimPort.createFilter(serviceContent.getPropertyCollector(), spec, true);

        boolean reached = false;
        UpdateSet updateset;
        while (!reached) {
            updateset = vimPort.waitForUpdatesEx(serviceContent.getPropertyCollector(), version, new WaitOptions());

            int waitForUpdateCounter = 0;
            if (updateset == null || updateset.getFilterSet() == null) {
                waitForUpdateCounter++;
                if (waitForUpdateCounter <= MAX_TRIED_WAIT_FOR_UPDATE_COUNTER) {
                    continue;
                }
                break;
            }

            version = updateset.getVersion();
            for (PropertyFilterUpdate filtup : updateset.getFilterSet()) {
                for (ObjectUpdate objup : filtup.getObjectSet()) {
                    if (objup.getKind() == ObjectUpdateKind.MODIFY || objup.getKind() == ObjectUpdateKind.ENTER
                            || objup.getKind() == ObjectUpdateKind.LEAVE) {
                        for (PropertyChange propchg : objup.getChangeSet()) {
                            updateValues(endWaitProps, endValues, propchg);
                            updateValues(filterProps, filterValues, propchg);
                        }
                    }
                }
            }
            // Check if the expected values have been reached and exit the loop if done.
            // Also exit the WaitForUpdates loop if this is the case.
            for (int chgi = 0; chgi < endValues.length && !reached; chgi++) {
                for (int vali = 0; vali < expectedValues[chgi].length && !reached; vali++) {
                    Object expctdval = expectedValues[chgi][vali];
                    if (endValues[chgi].toString().contains(KEY_VALUE_NULL_STRING)) {
                        Element stateElement = (Element) endValues[chgi];
                        if (stateElement != null && stateElement.getFirstChild() != null) {
                            stateVal = stateElement.getFirstChild().getTextContent();
                            reached = expctdval.toString().equalsIgnoreCase(stateVal);
                        }
                    } else {
                        expctdval = expectedValues[chgi][vali];
                        reached = expctdval.equals(endValues[chgi]);
                        stateVal = FILTER_VALUES;
                    }
                }
            }
        }
        try {
            vimPort.destroyPropertyFilter(filterSpecRef);
        } catch (RuntimeFaultFaultMsg e) {
            throw new RuntimeException(e.getMessage());
        }

        return getObjectState(stateVal, filterValues);
    }

    private Object[] getObjectState(String stateVal, Object[] filterValues) {
        if (stateVal == null) {
            return new Object[]{HttpNfcLeaseState.ERROR};
        } else {
            switch (stateVal) {
                case READY:
                    return new Object[]{HttpNfcLeaseState.READY};
                case ERROR:
                    return new Object[]{HttpNfcLeaseState.ERROR};
                case FILTER_VALUES:
                    return filterValues;
                default:
                    return null;
            }
        }
    }

    private PropertyFilterSpec propertyFilterSpec(ManagedObjectReference objmor, String[] filterProps) {
        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(objmor);
        oSpec.setSkip(false);

        PropertySpec pSpec = new PropertySpec();
        pSpec.getPathSet().addAll(Arrays.asList(filterProps));
        pSpec.setType(objmor.getType());

        PropertyFilterSpec spec = new PropertyFilterSpec();
        spec.getObjectSet().add(oSpec);
        spec.getPropSet().add(pSpec);

        return spec;
    }

    private void updateValues(String[] props, Object[] vals, PropertyChange propchg) {
        for (int findi = 0; findi < props.length; findi++) {
            if (propchg.getName().lastIndexOf(props[findi]) >= 0) {
                vals[findi] = propchg.getOp() == PropertyChangeOp.REMOVE ? Constants.EMPTY : propchg.getVal();
            }
        }
    }
}
