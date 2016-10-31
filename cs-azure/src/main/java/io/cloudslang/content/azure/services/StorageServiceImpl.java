package io.cloudslang.content.azure.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.azure.storage.core.Base64;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static io.cloudslang.content.azure.utils.Constants.NEW_LINE;
import static io.cloudslang.content.azure.utils.Constants.STORAGE_AUTH_ENDPOINT;
import static io.cloudslang.content.azure.utils.HttpUtils.getProxy;

/**
 * Created by victor on 25.10.2016.
 */
public class StorageServiceImpl {
    public static String createContainer(final String accountName, final String key, final String containerName, final String proxyHost,
                                         final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(accountName, key, proxyHost, proxyPort, proxyUsername, proxyPassword);
        final CloudBlobContainer container = blobClient.getContainerReference(containerName);
        container.create();
        return containerName;
    }

    public static String listContainers(final String accountName, final String key, final String proxyHost, final int proxyPort,
                                        final String proxyUsername, final String proxyPassword) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(accountName, key, proxyHost, proxyPort, proxyUsername, proxyPassword);
        final List<String> containerList = new ArrayList<>();
        for (CloudBlobContainer blobItem : blobClient.listContainers()) {
            containerList.add(blobItem.getName());
        }
        return StringUtilities.join(containerList, ',');
    }

    public static String deleteContainer(final String accountName, final String key, final String containerName, final String proxyHost,
                                         final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(accountName, key, proxyHost, proxyPort, proxyUsername, proxyPassword);
        final CloudBlobContainer container = blobClient.getContainerReference(containerName);
        container.delete();
        return containerName;
    }

    public static String listBlobs(final String accountName, final String key, final String containerName, final String proxyHost,
                                   final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(accountName, key, proxyHost, proxyPort, proxyUsername, proxyPassword);
        final CloudBlobContainer container = blobClient.getContainerReference(containerName);
        final List<String> blobList = new ArrayList<>();
        for (final ListBlobItem blobItem : container.listBlobs()) {
            final String path = blobItem.getUri().getPath();
            blobList.add(path.substring(path.lastIndexOf('/') + 1));
        }
        return StringUtilities.join(blobList, ',');
    }

    public static String deleteBlob(final String accountName, final String key, final String containerName, final String blobName,
                                    final String proxyHost, final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        final CloudBlobClient blobClient = getCloudBlobClient(accountName, key, proxyHost, proxyPort, proxyUsername, proxyPassword);
        final CloudBlobContainer container = blobClient.getContainerReference(containerName);
        final CloudBlockBlob blob = container.getBlockBlobReference(blobName);
        blob.delete();
        return blobName;
    }

    private static CloudBlobClient getCloudBlobClient(final String accountName, final String key, final String proxyHost,
                                                      final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        final String storageConnectionString = String.format(STORAGE_AUTH_ENDPOINT, accountName, key);
        OperationContext.setDefaultProxy(getProxy(proxyHost, proxyPort, proxyUsername, proxyPassword));
        return CloudStorageAccount.parse(storageConnectionString)
                .createCloudBlobClient();
    }
}
