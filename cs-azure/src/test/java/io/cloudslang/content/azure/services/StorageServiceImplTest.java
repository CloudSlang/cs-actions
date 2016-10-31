package io.cloudslang.content.azure.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
    @Test(expected = IllegalArgumentException.class)
    public void createContainerThrows() throws Exception {
        StorageServiceImpl.createContainer("", "", "", "", 8080, "", "");
    }

    @Test
    public void createContainer() throws Exception {
//        mockStatic(StorageServiceImpl.class);
//        StorageServiceImpl.createContainer("", "", "", "", 8080, "", "");
//        verifyStatic();
//        StorageServiceImpl.getCloudBlobClient("", "", "", 8080, "", "");

    }

    @Test(expected = IllegalArgumentException.class)
    public void listContainersThrows() throws Exception {
        StorageServiceImpl.listContainers("", "", "", 8080, "", "");
    }

    @Test
    public void listContainers() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteContainerThrows() throws Exception {
        StorageServiceImpl.deleteContainer("", "", "", "", 8080, "", "");
    }

    @Test
    public void deleteContainer() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void listBlobsThrows() throws Exception {
        StorageServiceImpl.listBlobs("", "", "", "", 8080, "", "");
    }

    @Test
    public void listBlobs() throws Exception {

    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteBlobThrows() throws Exception {
        StorageServiceImpl.deleteBlob("", "", "", "", "", 8080, "", "");
    }

    @Test
    public void deleteBlob() throws Exception {

    }

}