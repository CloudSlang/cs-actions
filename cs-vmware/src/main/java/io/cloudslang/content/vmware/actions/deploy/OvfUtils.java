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
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.OutOfBoundsFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.VmConfigFaultFaultMsg;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;

import static org.apache.commons.io.IOUtils.toByteArray;

public class OvfUtils {

    private final static PathMatcher ovaMatcher = FileSystems.getDefault().getPathMatcher("glob:**.ova");
    private final static PathMatcher ovfMatcher = FileSystems.getDefault().getPathMatcher("glob:**.ovf");
    private final static PathMatcher vmdkMatcher = FileSystems.getDefault().getPathMatcher("glob:**.vmdk");
    private static final String LEASE_COULD_NOT_BE_OBTAINED = "HttpNfcLeaseInfo could not be obtained";
    private static final String LEASE_STATE_COULD_NOT_BE_OBTAINED = "HttpNfcLease state could not be obtained!";
    private static final String LEASE_ERROR_STATE_COULD_NOT_BE_OBTAINED = "HttpNfcLease error state could not be obtained!";

    public static ManagedObjectReference getHttpNfcLease(ConnectionResources connectionResources, ImportSpec importSpec,
                                                         ManagedObjectReference resourcePool, ManagedObjectReference hostMor, ManagedObjectReference folderMor)
            throws ConcurrentAccessFaultMsg, FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, IOException, DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, OutOfBoundsFaultMsg {

        return connectionResources.getVimPortType().
                importVApp(resourcePool, importSpec, folderMor, hostMor);
    }

    @NotNull
    public static HttpNfcLeaseInfo getHttpNfcLeaseInfo(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "info");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (dynamicProperties.size() != 0 && dynamicProperties.get(0).getVal() instanceof HttpNfcLeaseInfo) {
            return (HttpNfcLeaseInfo) dynamicProperties.get(0).getVal();
        }
        throw new RuntimeException(LEASE_COULD_NOT_BE_OBTAINED);
    }

    public static String getHttpNfcLeaseState(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "state");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (dynamicProperties.size() != 0) {
            return ((TextImpl) ((ElementNSImpl) dynamicProperties.get(0).getVal()).getFirstChild()).getData();
        }
        throw new Exception(LEASE_STATE_COULD_NOT_BE_OBTAINED);
    }

    public static String getHttpNfcLeaseErrorState(ConnectionResources connectionResources, ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "error");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (dynamicProperties.size() != 0 && dynamicProperties.get(0).getVal() instanceof LocalizedMethodFault) {
            return ((LocalizedMethodFault) dynamicProperties.get(0).getVal()).getLocalizedMessage();
        }
        throw new Exception(LEASE_ERROR_STATE_COULD_NOT_BE_OBTAINED);
    }

    public static String writeToString(final InputStream inputStream, final long length) throws IOException {
        byte[] byteArray = toByteArray(inputStream, length);
        return IOUtils.toString(byteArray, StandardCharsets.UTF_8.toString());
    }

    public static boolean isOva(final Path filePath) {
        return ovaMatcher.matches(filePath);
    }

    public static boolean isVmdk(final Path filePath) {
        return vmdkMatcher.matches(filePath);
    }

    public static boolean isOvf(final Path filePath) {
        return ovfMatcher.matches(filePath);
    }
}
