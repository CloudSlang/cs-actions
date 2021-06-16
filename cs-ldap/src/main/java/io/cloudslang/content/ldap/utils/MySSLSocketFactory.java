/*
 * (c) Copyright 2020 Micro Focus
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
package io.cloudslang.content.ldap.utils;

import io.cloudslang.content.ldap.sslconfig.AuthSSLX509TrustManager;
import io.cloudslang.content.ldap.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.ldap.sslconfig.TLSSocketFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.ldap.constants.Constants.DEFAULT_PASSWORD_FOR_STORE;
import static io.cloudslang.content.ldap.constants.Constants.HTTP;
import static java.util.Objects.isNull;

public class MySSLSocketFactory extends SSLSocketFactory {

    private static boolean trustAllRoots;
    private static String trustKeystore;
    private static String trustPassword;
    private static String tlsVersion;
    private static List<String> allowedCiphers;
    private static Exception exception;
    private SSLSocketFactory socketFactory = new TLSSocketFactory(Collections.singletonList(tlsVersion), allowedCiphers);

    public MySSLSocketFactory() {

        exception = null;
        try {
            socketFactory = addSSLSettings(trustAllRoots, trustKeystore, trustPassword, tlsVersion);
        } catch (Exception e) {
            exception = e;
        }
    }

    public static SSLSocketFactory getDefault() {

        return new MySSLSocketFactory();

    }

    public static Exception getException() {
        return exception;
    }

    public static void setTrustAllRoots(boolean trustAllRoots) {
        MySSLSocketFactory.trustAllRoots = trustAllRoots;
    }

    public static void setTrustKeystore(String trustKeystore) {
        MySSLSocketFactory.trustKeystore = trustKeystore;
    }

    public static void setTrustPassword(String trustPassword) {
        MySSLSocketFactory.trustPassword = trustPassword;
    }

    public static void setTlsVersion(String tlsVersion) { MySSLSocketFactory.tlsVersion = tlsVersion; }

    public static void setAllowedCiphers(List<String> allowedCiphers) { MySSLSocketFactory.allowedCiphers = allowedCiphers; }

    /* SSL authentication methods
     */
    public static TrustManager[] createAuthTrustManagers(final KeyStore keystore)
            throws KeyStoreException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {
            if (trustmanagers[i] instanceof X509TrustManager) {
                trustmanagers[i] = new AuthSSLX509TrustManager(
                        (X509TrustManager) trustmanagers[i]);
            }
        }
        return trustmanagers;
    }

    public static KeyStore createKeyStore(final FileInputStream location, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        if (location == null) {
            throw new IllegalArgumentException("Keystore location may not be null");
        }
        KeyStore keystore = KeyStore.getInstance("jks");
        try {
            keystore.load(location, password != null ? password.toCharArray() : null);
        } finally {
            location.close();
        }
        return keystore;
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException {
        return localCreateSocket(s, i);
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i2) throws IOException {
        return socketFactory.createSocket(s, i, inetAddress, i2);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return socketFactory.createSocket(inetAddress, i);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return socketFactory.createSocket(inetAddress, i, inetAddress2, i2);
    }

    @Override
    public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
        return socketFactory.createSocket(socket, s, i, b);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    private SSLSocketFactory addSSLSettings(boolean trustAllRoots, String trustKeystore, String trustPassword,
                                            String tlsVersion) throws Exception {
        /*java.lang.System.setProperty(
                "sun.security.ssl.allowUnsafeRenegotiation", "true");*/

        //validate SSL certificates sent by the server
        boolean useTrustCert = !isNull(trustKeystore) && !trustAllRoots;

        String javaKeystore = System.getProperty("java.home") + "jre/lib/security/cacerts";

        if (!useTrustCert) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? javaKeystore : null;
            trustPassword = (storeExists) ? ((trustPassword.equals("")) ? DEFAULT_PASSWORD_FOR_STORE : trustPassword) : null;
        } else {
            if (!trustKeystore.startsWith(HTTP))
                trustKeystore = "" + trustKeystore;
        }

        SSLContext context = SSLContext.getInstance(tlsVersion);
        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (useTrustCert) {
            KeyStore trustKeyStore = createKeyStore(new FileInputStream(trustKeystore), trustPassword);
            trustManagers = createAuthTrustManagers(trustKeyStore);
        }

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager()};
        }

//        context.init(keyManagers, trustManagers, new SecureRandom());
        context.init(null, trustManagers, new SecureRandom());
        // Use the above SSLContext to create your socket factory

        return context.getSocketFactory();
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

    private Socket localCreateSocket(String host, int port) throws IOException {
        Socket tunnel = new Socket("web-proxy.eu.softwaregrp.net", 8080);
        doTunnelHandshake(tunnel, host, port);
        if (true) {
            SSLSocket socket = null;
            //Overlay the tunnel socket with SSL.
            socket = (SSLSocket) socketFactory.createSocket(tunnel, host, port, true);
            //Register a callback for handshaking completion event
            socket.startHandshake();
            return socket;
        }
        return tunnel;
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

        if (replyStr.toLowerCase().indexOf("200 connection established") == -1) {
            throw new IOException("Unable to tunnel through " + ":" + ".  Proxy returns \""
                    + replyStr + "\"");
        }
    }
}
