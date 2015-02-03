package org.openscore.content.httpclient.constants;

/**
 * Created by giloan on 2/2/2015.
 */
public final class Constants {

    public static final class HttpsProtocols {

        public static final String SSLv3 = "SSLv3";
        public static final String TLSv10 = "TLSv1";
        public static final String TLSv11 = "TLSv1.1";
        public static final String TLSv12 = "TLSv1.2";

        public static final String[] SUPPORTED_PROTOCOLS = new String[] {SSLv3, TLSv10, TLSv11, TLSv12};
    }
}
