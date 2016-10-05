package io.cloudslang.content.vmware.actions.deploy;

import io.cloudslang.content.vmware.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static io.cloudslang.content.vmware.constants.Constants.SIZE_4K;

public class Uploader {

    private static final Logger logger = LoggerFactory.getLogger(Uploader.class);

    public static HttpsURLConnection getHTTPSUploadConnection(URL url, int chunkLength, long contentLength, boolean put) throws IOException {
        HttpsURLConnection conn = getBasicHTTPSConnection(url);

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

    private static HttpsURLConnection getBasicHTTPSConnection(URL url) throws IOException {

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
        return conn;
    }
}
