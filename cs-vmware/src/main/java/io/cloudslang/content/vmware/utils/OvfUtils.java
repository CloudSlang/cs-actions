/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.utils;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.TextImpl;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HttpNfcLeaseInfo;
import com.vmware.vim25.ImportSpec;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.ProgressUpdater;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.IOUtils.toByteArray;

public class OvfUtils {

    private static final PathMatcher ovaMatcher = FileSystems.getDefault().getPathMatcher("glob:**.ova");
    private static final PathMatcher ovfMatcher = FileSystems.getDefault().getPathMatcher("glob:**.ovf");
    private static final PathMatcher vmdkMatcher = FileSystems.getDefault().getPathMatcher("glob:**.vmdk");
    private static final String LEASE_COULD_NOT_BE_OBTAINED = "HttpNfcLeaseInfo could not be obtained";
    private static final String LEASE_STATE_COULD_NOT_BE_OBTAINED = "HttpNfcLease state could not be obtained!";
    private static final String LEASE_ERROR_STATE_COULD_NOT_BE_OBTAINED = "HttpNfcLease error state could not be obtained!";
    private static final String ARRAYS_LENGTH_MUST_MATCH = "The array length of the json keys and values inputs must match!";

    public static ManagedObjectReference getHttpNfcLease(final ConnectionResources connectionResources, final ImportSpec importSpec,
                                                         final ManagedObjectReference resourcePool, final ManagedObjectReference hostMor,
                                                         final ManagedObjectReference folderMor) throws Exception {
        return connectionResources.getVimPortType().
                importVApp(resourcePool, importSpec, folderMor, hostMor);
    }

    public static HttpNfcLeaseInfo getHttpNfcLeaseInfo(final ConnectionResources connectionResources, final ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "info");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (firstElementIsOfClass(dynamicProperties, HttpNfcLeaseInfo.class)) {
            return (HttpNfcLeaseInfo) dynamicProperties.get(0).getVal();
        }
        throw new RuntimeException(LEASE_COULD_NOT_BE_OBTAINED);
    }

    public static String getHttpNfcLeaseState(final ConnectionResources connectionResources, final ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "state");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (dynamicProperties.size() != 0) {
            return ((TextImpl) ((ElementNSImpl) dynamicProperties.get(0).getVal()).getFirstChild()).getData();
        }
        throw new Exception(LEASE_STATE_COULD_NOT_BE_OBTAINED);
    }

    public static String getHttpNfcLeaseErrorState(final ConnectionResources connectionResources, final ManagedObjectReference httpNfcLease) throws Exception {
        final ObjectContent objectContent = GetObjectProperties.getObjectProperty(connectionResources, httpNfcLease, "error");
        final List<DynamicProperty> dynamicProperties = objectContent.getPropSet();
        if (firstElementIsOfClass(dynamicProperties, LocalizedMethodFault.class)) {
            return ((LocalizedMethodFault) dynamicProperties.get(0).getVal()).getLocalizedMessage();
        }
        throw new Exception(LEASE_ERROR_STATE_COULD_NOT_BE_OBTAINED);
    }

    private static boolean firstElementIsOfClass(List<DynamicProperty> dynamicProperties, Class clazz) {
        return dynamicProperties.size() != 0 && dynamicProperties.get(0).getVal().getClass().isAssignableFrom(clazz);
    }

    public static String writeToString(final InputStream inputStream, final long length) throws IOException {
        final byte[] byteArray = toByteArray(inputStream, length);
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

    public static Map<String, String> getOvfMappings(String keysJson, String valuesJson) throws Exception {
        Map<String, String> map = new HashMap<>();
        List<String> keys = resolveJSONStringArrayParm(keysJson);
        List<String> values = resolveJSONStringArrayParm(valuesJson);
        if (keys != null && values != null) {
            if (keys.size() != values.size()) {
                throw new Exception(ARRAYS_LENGTH_MUST_MATCH);
            } else {
                for (int i = 0; i < keys.size(); i++) {
                    map.put(keys.get(i), values.get(i));
                }
            }
        }
        return map;
    }

    public static List<String> resolveJSONStringArrayParm(String jsonString) {
        return JsonConverter.convertFromJson(jsonString, List.class);
    }

    public static long writeToStream(OutputStream outputStream, ProgressUpdater progressUpdater, long bytesCopied, byte[] buffer, int read) throws Exception {
        outputStream.write(buffer, 0, read);
        outputStream.flush();
        bytesCopied += read;
        progressUpdater.updateBytesSent(read);
        return bytesCopied;
    }
}
