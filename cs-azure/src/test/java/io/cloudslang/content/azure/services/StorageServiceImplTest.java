/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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

import com.microsoft.azure.storage.StorageUri;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import io.cloudslang.content.azure.entities.StorageInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by victor on 10/31/16.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(StorageServiceImpl.class)
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

    @Test(expected = IllegalArgumentException.class)
    public void createContainerThrows() throws Exception {
        StorageServiceImpl.createContainer(invalidStorageInputs);
    }

    @Test
    public void createContainer() throws Exception {
        // can't test it because mockito v1 can't mock final classes
    }

    @Test(expected = IllegalArgumentException.class)
    public void listContainersThrows() throws Exception {
        StorageServiceImpl.listContainers(invalidStorageInputs);
    }

    @Test
    public void listContainers() throws Exception {
        // can't test it because mockito v1 can't mock final classes
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteContainerThrows() throws Exception {
        StorageServiceImpl.deleteContainer(invalidStorageInputs);
    }

    @Test
    public void deleteContainer() throws Exception {
        // can't test it because mockito v1 can't mock final classes
    }

    @Test(expected = IllegalArgumentException.class)
    public void listBlobsThrows() throws Exception {
        StorageServiceImpl.listBlobs(invalidStorageInputs);
    }

    @Test
    public void listBlobs() throws Exception {
        // can't test it because mockito v1 can't mock final classes
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteBlobThrows() throws Exception {
        StorageServiceImpl.deleteBlob(invalidStorageInputs);
    }

    @Test
    public void deleteBlob() throws Exception {
        // can't test it because mockito v1 can't mock final classes
    }

}