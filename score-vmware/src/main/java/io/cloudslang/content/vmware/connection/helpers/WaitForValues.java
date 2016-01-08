package io.cloudslang.content.vmware.connection.helpers;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.connection.Connection;
import org.w3c.dom.Element;

import java.util.Arrays;


/**
 * Created by Mihai Tusa.
 * 10/19/2015.
 */

public class WaitForValues {
    Connection connection;
    ServiceContent serviceContent;
    VimPortType vimPort;

    public WaitForValues(Connection connection) {
        this.connection = connection;
        this.serviceContent = connection.getServiceContent();
        this.vimPort = connection.getVimPort();
    }

    /**
     * Handle Updates for a single object. waits till expected values of
     * properties to check are reached Destroys the ObjectFilter when done.
     *
     * @param objmor       MOR of the Object to wait for</param>
     * @param filterProps  Properties list to filter
     * @param endWaitProps Properties list to check for expected values these be properties
     *                     of a property in the filter properties list
     * @param expectedVals values for properties to end the wait
     * @return true indicating expected values were met, and false otherwise
     * @throws RuntimeFaultFaultMsg
     * @throws InvalidPropertyFaultMsg
     * @throws InvalidCollectorVersionFaultMsg
     */
    public Object[] wait(ManagedObjectReference objmor,
                         String[] filterProps,
                         String[] endWaitProps,
                         Object[][] expectedVals)
            throws InvalidPropertyFaultMsg,
            RuntimeFaultFaultMsg,
            InvalidCollectorVersionFaultMsg {

        ManagedObjectReference filterSpecRef;
        String version = Constants.EMPTY;
        String stateVal = null;

        Object[] endVals = new Object[endWaitProps.length];
        Object[] filterVals = new Object[filterProps.length];


        PropertyFilterSpec spec = propertyFilterSpec(objmor, filterProps);
        filterSpecRef = vimPort.createFilter(serviceContent.getPropertyCollector(), spec, true);

        boolean reached = false;

        UpdateSet updateset;
        while (!reached) {
            updateset = vimPort.waitForUpdatesEx(serviceContent.getPropertyCollector(), version, new WaitOptions());

            int waitForUpdateCounter = 0;
            if (updateset == null || updateset.getFilterSet() == null) {
                waitForUpdateCounter++;
                if (waitForUpdateCounter <= Constants.MAX_TRIED_WAIT_FOR_UPDATE_COUNTER) {
                    continue;
                }
                break;
            }

            version = updateset.getVersion();

            for (PropertyFilterUpdate filtup : updateset.getFilterSet()) {
                for (ObjectUpdate objup : filtup.getObjectSet()) {
                    if (objup.getKind() == ObjectUpdateKind.MODIFY
                            || objup.getKind() == ObjectUpdateKind.ENTER
                            || objup.getKind() == ObjectUpdateKind.LEAVE) {

                        for (PropertyChange propchg : objup.getChangeSet()) {
                            updateValues(endWaitProps, endVals, propchg);
                            updateValues(filterProps, filterVals, propchg);
                        }
                    }
                }
            }

            // Check if the expected values have been reached and exit the loop if done.
            // Also exit the WaitForUpdates loop if this is the case.
            for (int chgi = 0; chgi < endVals.length && !reached; chgi++) {
                for (int vali = 0; vali < expectedVals[chgi].length && !reached; vali++) {
                    Object expctdval = expectedVals[chgi][vali];
                    if (endVals[chgi].toString().contains(Constants.KEY_VALUE_NULL_STRING)) {
                        Element stateElement = (Element) endVals[chgi];
                        if (stateElement != null && stateElement.getFirstChild() != null) {
                            stateVal = stateElement.getFirstChild().getTextContent();
                            reached = expctdval.toString().equalsIgnoreCase(stateVal);
                        }
                    } else {
                        expctdval = expectedVals[chgi][vali];
                        reached = expctdval.equals(endVals[chgi]);
                        stateVal = Constants.FILTER_VALUES;
                    }
                }
            }
        }

        try {
            vimPort.destroyPropertyFilter(filterSpecRef);
        } catch (RuntimeFaultFaultMsg e) {
            throw new RuntimeException(e.getMessage());
        }

        return getObjectState(stateVal, filterVals);
    }

    private Object[] getObjectState(String stateVal, Object[] filterVals) {
        Object[] retVal = null;
        if (stateVal != null) {
            if (Constants.READY.equalsIgnoreCase(stateVal)) {
                retVal = new Object[]{HttpNfcLeaseState.READY};
            }

            if (Constants.ERROR.equalsIgnoreCase(stateVal)) {
                retVal = new Object[]{HttpNfcLeaseState.ERROR};
            }

            if (Constants.FILTER_VALUES.equals(stateVal)) {
                retVal = filterVals;
            }
        } else {
            retVal = new Object[]{HttpNfcLeaseState.ERROR};
        }
        return retVal;
    }

    public PropertyFilterSpec propertyFilterSpec(ManagedObjectReference objmor, String[] filterProps) {
        PropertyFilterSpec spec = new PropertyFilterSpec();

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(objmor);
        oSpec.setSkip(Boolean.FALSE);

        spec.getObjectSet().add(oSpec);

        PropertySpec pSpec = new PropertySpec();
        pSpec.getPathSet().addAll(Arrays.asList(filterProps));
        pSpec.setType(objmor.getType());

        spec.getPropSet().add(pSpec);

        return spec;
    }

    void updateValues(String[] props, Object[] vals, PropertyChange propchg) {
        for (int findi = 0; findi < props.length; findi++) {
            if (propchg.getName().lastIndexOf(props[findi]) >= 0) {
                vals[findi] = propchg.getOp() == PropertyChangeOp.REMOVE ? Constants.EMPTY : propchg.getVal();
            }
        }
    }
}
