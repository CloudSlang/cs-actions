package io.cloudslang.content.vmware.entities;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URL;

public class Uploader {

    public static HttpsURLConnection getHTTPSUploadConnection(final URL url, final int chunkLength,
                                                              final long contentLength, final boolean put) throws IOException {
        final HttpsURLConnection connection = getBasicHTTPSConnection(url);

        connection.setChunkedStreamingMode(chunkLength);
        if (put) {
            connection.setRequestMethod("PUT");
        } else {
            connection.setRequestMethod("POST");
        }
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/x-vnd.mvware-streamVmdk");
        if (contentLength > 0) connection.setRequestProperty("Content-Length", Long.toString(contentLength));
        connection.connect();
        return connection;
    }

    private static HttpsURLConnection getBasicHTTPSConnection(final URL url) throws IOException {

        final HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(hv);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(false);
        return conn;
    }
}
