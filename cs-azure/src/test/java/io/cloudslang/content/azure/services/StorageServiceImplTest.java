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

import io.cloudslang.content.azure.entities.StorageInputs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by victor on 10/31/16.
 */

public class StorageServiceImplTest {
    private final StorageInputs invalidStorageInputs = StorageInputs.builder()
            .storageAccount("")
            .key("")
            .containerName("")
            .blobName("")
            .proxyHost("")
            .proxyPort(8080)
            .proxyUsername("")
            .proxyPassword("")
            .timeout(0)
            .build();

    @Test
    public void createContainerThrows() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StorageServiceImpl.createContainer(invalidStorageInputs));
    }

    @Test
    public void createContainer() throws Exception {
        // Not exercised here; this requires Azure storage integration.
    }

    @Test
    public void listContainersThrows() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StorageServiceImpl.listContainers(invalidStorageInputs));
    }

    @Test
    public void listContainers() throws Exception {
        // Not exercised here; this requires Azure storage integration.
    }

    @Test
    public void deleteContainerThrows() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StorageServiceImpl.deleteContainer(invalidStorageInputs));
    }

    @Test
    public void deleteContainer() throws Exception {
        // Not exercised here; this requires Azure storage integration.
    }

    @Test
    public void listBlobsThrows() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StorageServiceImpl.listBlobs(invalidStorageInputs));
    }

    @Test
    public void listBlobs() throws Exception {
        // Not exercised here; this requires Azure storage integration.
    }

    @Test
    public void deleteBlobThrows() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> StorageServiceImpl.deleteBlob(invalidStorageInputs));
    }

    @Test
    public void deleteBlob() throws Exception {
        // Not exercised here; this requires Azure storage integration.
    }

}