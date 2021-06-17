package io.cloudslang.content.ldap.utils;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.ldap.constants.Constants.ENABLED_PROTOCOLS;

public class CustomSSLSocketFactory extends SSLSocketFactory {
    private static boolean trustAllRoots;
    private static String trustKeystore;
    private static String trustPassword;
    private static String tlsVersion;
    private static String proxyHost;
    private static int proxyPort;
    private static String proxyUsername;
    private static String proxyPassword;
    private static List<String> allowedCiphers;
    private static Exception exception;
    private static boolean useHttps = true;
    private SSLSocketFactory socketFactory;

    public CustomSSLSocketFactory() {

        exception = null;
        try {
            socketFactory = addSSLSettings(trustAllRoots, trustKeystore, trustPassword, tlsVersion);
        } catch (Exception e) {
            exception = e;
        }
    }

    public static SSLSocketFactory getDefault() {

        return new CustomSSLSocketFactory();

    }

    public static Exception getException() {
        return exception;
    }

    public static void setUseHttps(boolean useHttps) {
        CustomSSLSocketFactory.useHttps = useHttps;
    }

    public static void setTrustKeystore(String trustKeystore) {
        CustomSSLSocketFactory.trustKeystore = trustKeystore;
    }

    public static void setTrustPassword(String trustPassword) {
        CustomSSLSocketFactory.trustPassword = trustPassword;
    }

    public static void setTlsVersion(String tlsVersion) {
        CustomSSLSocketFactory.tlsVersion = tlsVersion;
    }

    public static void setProxyHost(String proxyHost) {
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("http.proxyHost", proxyHost);
        CustomSSLSocketFactory.proxyHost = proxyHost;
    }

    public static void setProxyPort(int proxyPort) {
        System.setProperty("https.proxyPort", String.valueOf(proxyPort));
        System.setProperty("http.proxyPort", String.valueOf(proxyPort));
        CustomSSLSocketFactory.proxyPort = proxyPort;
    }

    public static void setProxyUsername(String proxyUsername) {
        System.setProperty("https.proxyUser", proxyUsername);
        System.setProperty("http.proxyUser", proxyUsername);
        CustomSSLSocketFactory.proxyUsername = proxyUsername;
    }

    public static void setProxyPassword(String proxyPassword) {
        System.setProperty("https.proxyPassword", proxyPassword);
        System.setProperty("http.proxyPassword", proxyPassword);
        CustomSSLSocketFactory.proxyPassword = proxyPassword;
    }

    public static void setAllowedCiphers(List<String> allowedCiphers) {
        CustomSSLSocketFactory.allowedCiphers = allowedCiphers;
    }

    private static TrustManager getTrustAllRoots() {
        return new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
    }

    public static void setTrustAllRoots(boolean trustAllRoots) {
        CustomSSLSocketFactory.trustAllRoots = trustAllRoots;
    }

    private SSLSocketFactory addSSLSettings(boolean trustAllRoots, String trustKeystore, String trustPassword, String tlsVersion) throws Exception {

        if (!useHttps) {
            return null;
        }
        if (!trustAllRoots) {
            try {
                System.setProperty("javax.net.ssl.keyStore", trustKeystore);
                System.setProperty("javax.net.ssl.keyStorePassword", trustPassword);
                System.setProperty("javax.net.ssl.trustStore", trustKeystore);
                System.setProperty("javax.net.ssl.trustStorePassword", trustPassword);

                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(new FileInputStream(trustKeystore), trustPassword.toCharArray());
                // Create key manager
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                keyManagerFactory.init(keyStore, trustPassword.toCharArray());
                KeyManager[] km = keyManagerFactory.getKeyManagers();
                // Create trust manager
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(keyStore);
                TrustManager[] tm = trustManagerFactory.getTrustManagers();
                // Initialize SSLContext
                SSLContext sslContext = SSLContext.getInstance(tlsVersion);
                sslContext.init(km, tm, new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        } else {
            try {
                SSLContext sslContext = SSLContext.getInstance(tlsVersion);
                sslContext.init(null, new TrustManager[]{getTrustAllRoots()}, new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return socketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
        Socket newSocket = socketFactory.createSocket(socket, s, i, b);
        configureSSLSocket(newSocket);
        return newSocket;
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        Socket socket = localCreateSocket(s, i);
        if (useHttps)
            return configureSSLSocket(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        Socket newSocket = socketFactory.createSocket(s, i, inetAddress, i1);
        configureSSLSocket(newSocket);
        return newSocket;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        Socket newSocket = socketFactory.createSocket(inetAddress, i);
        configureSSLSocket(newSocket);
        return newSocket;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        Socket newSocket = socketFactory.createSocket(inetAddress, i, inetAddress1, i1);
        configureSSLSocket(newSocket);
        return newSocket;
    }

    private Socket localCreateSocket(String host, int port) throws IOException {
        if (!proxyHost.isEmpty()) {
            Socket tunnel;
            try {
                tunnel = new Socket(proxyHost, proxyPort);
                doTunnelHandshake(tunnel, host, port);
            } catch (UnknownHostException e) {
                throw new UnknownHostException("Invalid proxy");
            }
            if (!useHttps) {
                return tunnel;
            }
            SSLSocket socket = null;
            //Overlay the tunnel socket with SSL.
            socket = (SSLSocket) socketFactory.createSocket(tunnel, host, port, true);
            //Register a callback for handshaking completion event
            socket.startHandshake();
            return socket;
        }
        return socketFactory.createSocket(host, port);
    }

    private Socket configureSSLSocket(Socket socket) {
        SSLSocket sslSocket = (SSLSocket) socket;
        sslSocket.setEnabledCipherSuites(allowedCiphers.toArray(new String[0]));
        sslSocket.setEnabledProtocols(ENABLED_PROTOCOLS);
        return sslSocket;
    }

    private void doTunnelHandshake(Socket tunnel, String host, int port)
            throws IOException {
        OutputStream out = tunnel.getOutputStream();
        String msg = "CONNECT " + host + ":" + port + " HTTP/1.0\n"
                + "User-Agent: " + sun.net.www.protocol.http.HttpURLConnection.userAgent + "\r\n\r\n";
        byte b[];
        try {
            //We really do want ASCII7 -- the http protocol doesn't change with locale.
            b = msg.getBytes("ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            //If ASCII7 isn't there, something serious is wrong, but Paranoia Is Good (tm)
            b = msg.getBytes();
        }
        out.write(b);
        out.flush();
        //We need to store the reply so we can create a detailed error message to the user.
        byte reply[] = new byte[200];
        int replyLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;     /* Done on first newline */
        InputStream in = tunnel.getInputStream();
        boolean error = false;
        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from proxy");
            }
            if (i == '\n') {
                headerDone = true;
                ++newlinesSeen;
            } else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone & replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                }
            }
        }
        /*
         * Converting the byte array to a string is slightly wasteful in the case where the connection was successful,
         * but it's insignificant compared to the network overhead.
         */
        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }

        if (!replyStr.toLowerCase().contains("200 connection established")) {
            throw new IOException("Unable to tunnel through "+proxyHost+". Proxy returns \""
                    + replyStr + "\"");
        }
    }
}
