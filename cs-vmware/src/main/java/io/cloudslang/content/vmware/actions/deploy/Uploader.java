package io.cloudslang.content.vmware.actions.deploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Uploader {

    private static final Logger logger = LoggerFactory.getLogger(Uploader.class);
    private static final int DEFAULT_BUFFER_SIZE = 1 << 12;

    public static HttpsURLConnection getHTTPSUploadConnection(URL url, String cookieStr, int chunkLength, long contentLength, boolean put) throws IOException {
        HttpsURLConnection conn = getBasicHTTPSConnection(url, cookieStr);

        conn.setChunkedStreamingMode(chunkLength);
        if (put) {
            conn.setRequestMethod("PUT");
        } else {
            conn.setRequestMethod("POST");
        }
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "application/x-vnd.mvware-streamVmdk");
        if (contentLength > 0) conn.setRequestProperty("Content-Length", Long.toString(contentLength));

        conn.connect();
        return conn;
    }

    private static HttpsURLConnection getBasicHTTPSConnection(URL url, String cookieStr) throws IOException {

        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(hv);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Cookie", cookieStr);
        return conn;
    }

    public static long copyAll(InputStream inputStream, OutputStream outputStream, ProgressUpdater progressUpdater) throws Exception {
        long bytesCopied = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        while ((read = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, read);
            outputStream.flush();
            bytesCopied += read;
            progressUpdater.updateBytesSent(read);
            logger.info("update progress bar with: " + read);
            System.out.println("Thread id: " + Thread.currentThread().getId() + " thread name: " + Thread.currentThread().getName());
        }
        return bytesCopied;
    }
}
