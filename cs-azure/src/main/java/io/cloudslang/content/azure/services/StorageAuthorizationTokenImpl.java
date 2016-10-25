package io.cloudslang.content.azure.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.core.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by victor on 25.10.2016.
 */
public class StorageAuthorizationTokenImpl {
    private static String method = "";
    private static String baseUrl = ".blob.core.windows.net";
    private static String protocol = "https://";
    private static String uriPart;
    private static String date;
    private static String apiVersion = "2015-04-05";
    private static String account = "";
    private static String key = "";
    private static String clientRequestId = "";
    private static String service = "";

    public static void signRequestSK(HttpURLConnection request, String account, String key) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";
        StringBuilder sb = new StringBuilder();
        sb.append(method + "\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-client-request-id:" + clientRequestId + "\n");
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + uriPart.replaceAll("=", ":").replace("?", "\n").replaceAll("&", "\n"));
        System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = new String(Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", apiVersion);
        request.setRequestProperty("Authorization", auth);
        request.setRequestProperty("x-ms-client-request-id", clientRequestId);
        request.setDoOutput(true);
        request.setFixedLengthStreamingMode(0);
        request.setRequestMethod(method);
        System.out.println(auth);
    }

    public static void signRequestSKDELETE(HttpURLConnection request, String account, String key) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(method + "\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + uriPart.replaceAll("=", ":").replace("?", "\n").replaceAll("&", "\n"));
        System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = new String(Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", apiVersion);
        request.setRequestProperty("Authorization", auth);
        request.setRequestMethod(method);
        System.out.println(auth);
    }

    public static void listContainer(String[] args) throws Exception {
        if(args.length != 4) {
            System.err.println("Insufficient input parameters to list containers.");
            System.err.println("Usage: java -jar GetStorageAzureToken.jar \"-listContainers\" \"storageAccountName\" \"accountKey\" \"date\"");
            System.err.println("Usage: java -jar GetStorageAzureToken.jar \"-listContainers\" \"myAccount\" \"accountKey\" \"Thu, 10 Mar 2016 11:58:47 GMT\"");
            System.exit(-1);
        }

        String account = args[1];
        String key = args[2];
        String date = args[3];
        URL url = new URL(protocol + account + baseUrl + "/?comp=list");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("GET\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + "/?comp=list");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8")));
        String auth = "SharedKeyLite " + account + ":" + authKey;
        connection.setRequestProperty("x-ms-date", date);
        connection.setRequestProperty("x-ms-version", apiVersion);
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestMethod("GET");
        System.out.println(auth);
    }

    public static void createContainerREST(String[] args) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";
        if(args.length != 4) {
            System.err.println("Insufficient input parameters to list containers.");
            System.err.println("Uage: java -jar GetStorageAzureToken.jar \"-createContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToCreate\"");
            System.err.println("Uage: java -jar GetStorageAzureToken.jar \"-createContainer\" \"myAccount\" \"accountKey\" \"MyContainerName\"");
            System.exit(-1);
        }

        String account = args[1];
        String key = args[2];
        String containerName = args[3].toLowerCase();
        URL url = new URL(protocol + account + baseUrl + "/" + containerName + "?restype=container");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("PUT\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-client-request-id:" + clientRequestId + "\n");
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + "/" + containerName + "\nrestype:container");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = new String(Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        connection.setRequestProperty("x-ms-date", date);
        connection.setRequestProperty("x-ms-version", apiVersion);
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("x-ms-client-request-id", clientRequestId);
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(0);
        connection.setRequestMethod("PUT");
        connection.connect();
        System.out.println(connection.getResponseMessage() + " : " + containerName);
    }

    public static String leaseContainer(String[] args) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";
        String account = args[1];
        String key = args[2];
        String containerName = args[3].toLowerCase();
        URL url = new URL(protocol + account + baseUrl + "/" + containerName + "?comp=lease&restype=container");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("PUT\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-client-request-id:" + clientRequestId + "\n");
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-lease-action:acquire\n");
        sb.append("x-ms-lease-duration:60\n");
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + "/" + containerName + "\ncomp:lease\nrestype:container");
        System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8")));
        String auth = "SharedKey " + account + ":" + authKey;
        connection.setRequestProperty("x-ms-date", date);
        connection.setRequestProperty("x-ms-version", apiVersion);
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("x-ms-client-request-id", clientRequestId);
        connection.setRequestProperty("x-ms-lease-action", "acquire");
        connection.setRequestProperty("x-ms-lease-duration", "60");
        connection.setRequestProperty("content-lenght", "0");
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(0);
        connection.setRequestMethod("PUT");
        System.out.println(auth);
        connection.connect();
        System.out.println(connection.getResponseMessage());
        return connection.getHeaderField("x-ms-lease-id");
    }

    public static void deleteContainerREST(String[] args) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";
        if(args.length != 4) {
            System.err.println("Insufficient input parameters to list containers.");
            System.err.println("Uage: java -jar GetStorageAzureToken.jar \"-createContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToDelete\"");
            System.err.println("Uage: java -jar GetStorageAzureToken.jar \"-createContainer\" \"myAccount\" \"accountKey\" \"MyContainerName\"");
            System.exit(-1);
        }

        String account = args[1];
        String key = args[2];
        String containerName = args[3].toLowerCase();
        String leaseId = leaseContainer(args);
        URL url = new URL(protocol + account + baseUrl + "/" + containerName + "?restype=container");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-client-request-id:" + clientRequestId + "\n");
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-lease-id:" + leaseId + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        sb.append("/" + account + "/" + containerName + "\nrestype:container");
        System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = new String(Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        connection.setRequestProperty("x-ms-date", date);
        connection.setRequestProperty("x-ms-version", apiVersion);
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("x-ms-client-request-id", clientRequestId);
        connection.setRequestProperty("x-ms-lease-id", leaseId);
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(0);
        connection.setRequestMethod("DELETE");
        System.out.println(auth);
        connection.connect();
        System.out.println(containerName);
    }

    public static void signRequestSKL(HttpURLConnection request, String account, String key) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(method + "\n");
        sb.append('\n');
        sb.append('\n');
        sb.append('\n');
        sb.append("x-ms-date:" + date + '\n');
        sb.append("x-ms-version:" + apiVersion + "\n");
        if(uriPart.startsWith("/?")) {
            sb.append("/" + account + uriPart);
        } else {
            sb.append("/" + account + uriPart.substring(0, uriPart.indexOf("?")) + "\n");
            uriPart.substring(uriPart.indexOf("?"));
            sb.append(uriPart.substring(uriPart.indexOf("?") + 1).replaceAll("=", ":").replaceAll("&", "\n"));
        }

        System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(Base64.decode(key), "HmacSHA256"));
        String authKey = Base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8")));
        String auth = "SharedKeyLite " + account + ":" + authKey;
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", apiVersion);
        request.setRequestProperty("Authorization", auth);
        request.setRequestMethod(method);
        System.out.println(auth);
    }

    public static void main(String[] args) throws Exception {
        StorageAuthorizationTokenImpl blobService = new StorageAuthorizationTokenImpl();
        if(args.length != 4) {
            System.err.println("Invalid input parameters.");
            System.err.println("Usage to list container SharedKey: java -jar azure-storage-service.jar \"-listContainers\" \"storageAccountName\" \"accountKey\" \"date\"");
            System.err.println("Usage to list container SharedKey: java -jar azure-storage-service.jar \"-listContainers\" \"myAccount\" \"accountKey\" \"Thu, 10 Mar 2016 11:58:47 GMT\"");
            System.err.println("Usage to create container: java -jar azure-storage-service.jar \"-createContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToCreate\"");
            System.err.println("Create container example: java -jar azure-storage-service.jar \"-createContainer\" \"myAccount\" \"accountKey\" \"mycontainername\"");
            System.err.println("Usage to delete container: java -jar azure-storage-service.jar \"-deleteContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToDelete\"");
            System.err.println("Delete container example: java -jar azure-storage-service.jar \"-deleteContainer\" \"myAccount\" \"accountKey\" \"mycontainername\"");
            System.exit(-1);
        }

        service = args[0];
        clientRequestId = UUID.randomUUID().toString();
        if(service.equalsIgnoreCase("-listContainers")) {
            listContainer(args);
        } else if(service.equalsIgnoreCase("-createContainer")) {
            blobService.createContainer(args);
        } else if(service.equalsIgnoreCase("-deleteContainer")) {
            blobService.deleteContainer(args);
        }

    }

    private void createContainer(String[] args) {
        if(args.length != 4) {
            System.err.println("Insufficient input parameters to create container.");
            System.err.println("Usage: java -jar azure-storage-service.jar \"-createContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToCreate\"");
            System.err.println("Usage: java -jar azure-storage-service.jar \"-createContainer\" \"myAccount\" \"accountKey\" \"mycontainername\"");
            System.exit(-1);
        }

        String accountName = args[1];
        String key = args[2];
        String containerName = args[3].toLowerCase();
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + accountName + ";" + "AccountKey=" + key;

        try {
            CloudStorageAccount e = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = e.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            boolean created = container.createIfNotExists();
            if(created) {
                System.out.println("Created: " + containerName);
            }
        } catch (InvalidKeyException var10) {
            var10.printStackTrace();
        } catch (URISyntaxException var11) {
            var11.printStackTrace();
        } catch (StorageException var12) {
            var12.printStackTrace();
        }

    }

    private void deleteContainer(String[] args) {
        if(args.length != 4) {
            System.err.println("Insufficient input parameters to delete container.");
            System.err.println("Usage: java -jar azure-storage-service.jar \"-deleteContainer\" \"storageAccountName\" \"accountKey\" \"containerNameToDelete\"");
            System.err.println("Usage: java -jar azure-storage-service.jar \"-deleteContainer\" \"myAccount\" \"accountKey\" \"mycontainername\"");
            System.exit(-1);
        }

        String accountName = args[1];
        String key = args[2];
        String containerName = args[3].toLowerCase();
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=" + accountName + ";" + "AccountKey=" + key;

        try {
            CloudStorageAccount e = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = e.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            boolean deleted = container.deleteIfExists();
            if(deleted) {
                System.out.println("Deleted: " + containerName);
            }
        } catch (InvalidKeyException var10) {
            var10.printStackTrace();
        } catch (URISyntaxException var11) {
            var11.printStackTrace();
        } catch (StorageException var12) {
            var12.printStackTrace();
        }

    }
}
