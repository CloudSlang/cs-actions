/*
 * Copyright 2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */





package io.cloudslang.content.azure.services;

import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobContainerItem;
import io.cloudslang.content.azure.entities.StorageInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.azure.utils.Constants.*;

/**
 * Created by victor on 25.10.2016.
 */
public final class StorageServiceImpl {
    @NotNull
    public static String createContainer(@NotNull final StorageInputs inputs) throws Exception {
        final BlobServiceClient blobServiceClient = getBlobServiceClient(inputs);
        final BlobContainerClient container = blobServiceClient.getBlobContainerClient(inputs.getContainerName());
        container.create();
        return inputs.getContainerName();
    }

    @NotNull
    public static String listContainers(@NotNull final StorageInputs inputs) throws Exception {
        final BlobServiceClient blobServiceClient = getBlobServiceClient(inputs);
        final List<String> containerList = new ArrayList<>();
        for (final BlobContainerItem blobItem : blobServiceClient.listBlobContainers()) {
            containerList.add(blobItem.getName());
        }
        return StringUtilities.join(containerList, COMMA);
    }

    @NotNull
    public static String deleteContainer(@NotNull final StorageInputs inputs) throws Exception {
        final BlobServiceClient blobServiceClient = getBlobServiceClient(inputs);
        final BlobContainerClient container = blobServiceClient.getBlobContainerClient(inputs.getContainerName());
        container.delete();
        return inputs.getContainerName();
    }

    @NotNull
    public static String listBlobs(@NotNull final StorageInputs inputs) throws Exception {
        final BlobServiceClient blobServiceClient = getBlobServiceClient(inputs);
        final BlobContainerClient container = blobServiceClient.getBlobContainerClient(inputs.getContainerName());
        final List<String> blobList = new ArrayList<>();
        for (final BlobItem blobItem : container.listBlobsByHierarchy(FORWARD_SLASH)) {
            final String name = blobItem.getName();
            blobList.add(name.substring(name.lastIndexOf(FORWARD_SLASH) + 1));
        }
        return StringUtilities.join(blobList, COMMA);
    }

    @NotNull
    public static String deleteBlob(@NotNull final StorageInputs inputs) throws Exception {
        final BlobServiceClient blobServiceClient = getBlobServiceClient(inputs);
        final BlobContainerClient container = blobServiceClient.getBlobContainerClient(inputs.getContainerName());
        container.getBlobClient(inputs.getBlobName()).delete();
        return inputs.getBlobName();
    }

    @NotNull
    public static BlobServiceClient getBlobServiceClient(@NotNull final StorageInputs inputs) {
        final String storageConnectionString = String.format(STORAGE_AUTH_ENDPOINT, inputs.getStorageAccount(), inputs.getKey());
        final NettyAsyncHttpClientBuilder httpClientBuilder = new NettyAsyncHttpClientBuilder();
        if (!StringUtilities.isBlank(inputs.getProxyHost())) {
            final ProxyOptions proxyOptions = new ProxyOptions(
                    ProxyOptions.Type.HTTP,
                    new InetSocketAddress(inputs.getProxyHost(), inputs.getProxyPort()));
            if (StringUtilities.isNotEmpty(inputs.getProxyUsername())) {
                proxyOptions.setCredentials(inputs.getProxyUsername(), inputs.getProxyPassword());
            }
            httpClientBuilder.proxy(proxyOptions);
        }
        if (inputs.getTimeout() != 0) {
            httpClientBuilder.responseTimeout(Duration.ofMillis(inputs.getTimeout()));
        }
        return new BlobServiceClientBuilder()
                .connectionString(storageConnectionString)
                .httpClient(httpClientBuilder.build())
                .buildClient();
    }
}
