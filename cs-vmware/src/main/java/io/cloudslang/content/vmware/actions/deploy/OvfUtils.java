package io.cloudslang.content.vmware.actions.deploy;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.TextImpl;
import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.ImportSpec;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.OutOfBoundsFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.VmConfigFaultFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class OvfUtils {

    public static ManagedObjectReference getHttpNfcLease(ConnectionResources connectionResources, ImportSpec importSpec,
                                                   ManagedObjectReference resourcePool, ManagedObjectReference hostMor, ManagedObjectReference folderMor)
            throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, IOException, DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, OutOfBoundsFaultMsg {

        return connectionResources.getVimPortType().
                importVApp(resourcePool, importSpec, folderMor, hostMor);
    }

    @NotNull
    public static HttpNfcLeaseInfo getHttpNfcLeaseInfo(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, httpNfcLease, new String[]{"info"});
        HttpNfcLeaseInfo httpNfcLeaseInfo = null;
        if (objectContents != null && objectContents.length == 1) {
            List<DynamicProperty> dynamicProperties = objectContents[0].getPropSet();
            if (dynamicProperties != null && dynamicProperties.size() == 1 && dynamicProperties.get(0).getVal() instanceof HttpNfcLeaseInfo) {
                httpNfcLeaseInfo = (HttpNfcLeaseInfo) dynamicProperties.get(0).getVal();
            }
        }
        if (httpNfcLeaseInfo == null) {
            throw new RuntimeException("HttpNfcLeaseInfo could not be obtained");
        }
        return httpNfcLeaseInfo;
    }

    public static String gethttpNfcLeaseState(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws RuntimeFaultFaultMsg, InvalidPropertyFaultMsg {
        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, httpNfcLease, new String[]{"state"});
        if (objectContents != null && objectContents.length == 1) {
            List<DynamicProperty> dynamicProperties = objectContents[0].getPropSet();
            if (dynamicProperties != null && dynamicProperties.size() == 1) {
                return ((TextImpl) ((ElementNSImpl) dynamicProperties.get(0).getVal()).getFirstChild()).getData();
                }
            }
        return null;
    }

    public static String getHttpNfcLeaseError(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) {
        return null;   //TODO: get error message
    }
}
