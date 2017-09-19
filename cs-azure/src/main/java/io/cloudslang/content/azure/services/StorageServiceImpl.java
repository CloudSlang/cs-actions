/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.azure.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import io.cloudslang.content.azure.entities.StorageInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.azure.utils.Constants.*;
import static io.cloudslang.content.azure.utils.HttpUtils.getProxy;

/**
 * Created by victor on 25.10.2016.
 */
public final class StorageServiceImpl {
    @NotNull
    public static String createContainer(@NotNull final StorageInputs inputs) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(inputs);
        final CloudBlobContainer container = blobClient.getContainerReference(inputs.getContainerName());
        container.create();
        return inputs.getContainerName();
    }

    @NotNull
    public static String listContainers(@NotNull final StorageInputs inputs) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(inputs);
        final List<String> containerList = new ArrayList<>();
        for (final CloudBlobContainer blobItem : blobClient.listContainers()) {
            containerList.add(blobItem.getName());
        }
        return StringUtilities.join(containerList, COMMA);
    }

    @NotNull
    public static String deleteContainer(@NotNull final StorageInputs inputs) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(inputs);
        final CloudBlobContainer container = blobClient.getContainerReference(inputs.getContainerName());
        container.delete();
        return inputs.getContainerName();
    }

    @NotNull
    public static String listBlobs(@NotNull final StorageInputs inputs) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(inputs);
        final CloudBlobContainer container = blobClient.getContainerReference(inputs.getContainerName());
        final List<String> blobList = new ArrayList<>();
        for (final ListBlobItem blobItem : container.listBlobs()) {
            final String path = blobItem.getUri().getPath();
            blobList.add(path.substring(path.lastIndexOf(FORWARD_SLASH) + 1));
        }
        return StringUtilities.join(blobList, COMMA);
    }

    @NotNull
    public static String deleteBlob(@NotNull final StorageInputs inputs) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(inputs);
        final CloudBlobContainer container = blobClient.getContainerReference(inputs.getContainerName());
        final CloudBlockBlob blob = container.getBlockBlobReference(inputs.getBlobName());
        blob.delete();
        return inputs.getBlobName();
    }

    @NotNull
    public static CloudBlobClient getCloudBlobClient(@NotNull final StorageInputs inputs) throws Exception {
        final String storageConnectionString = String.format(STORAGE_AUTH_ENDPOINT, inputs.getStorageAccount(), inputs.getKey());
        OperationContext.setDefaultProxy(getProxy(inputs.getProxyHost(), inputs.getProxyPort(), inputs.getProxyUsername(), inputs.getProxyPassword()));
        final CloudBlobClient client = CloudStorageAccount.parse(storageConnectionString).createCloudBlobClient();
        if (inputs.getTimeout() != 0) {
            client.getDefaultRequestOptions().setTimeoutIntervalInMs(inputs.getTimeout());
        }
        return client;
    }
}
