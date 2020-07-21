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

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

import static java.util.Objects.isNull;

public class MySSLSocketFactory extends SSLSocketFactory {

    private static boolean trustAllRoots;
    private static String keystore;
    private static String keystorePassword;
    private static String trustKeystore;
    private static String trustPassword;
    private static Exception exception;
    private SSLSocketFactory socketFactory;

    public MySSLSocketFactory() {

        exception = null;
        try {
            socketFactory = addSSLSettings(trustAllRoots, keystore, keystorePassword, trustKeystore, trustPassword);
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

    public static void setKeystore(String keystore) {
        MySSLSocketFactory.keystore = keystore;
    }

    public static void setKeystorePassword(String keystorePassword) {
        MySSLSocketFactory.keystorePassword = keystorePassword;
    }

    public static void setTrustKeystore(String trustKeystore) {
        MySSLSocketFactory.trustKeystore = trustKeystore;
    }

    public static void setTrustPassword(String trustPassword) {
        MySSLSocketFactory.trustPassword = trustPassword;
    }

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

    public static KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray() : null);
        return kmfactory.getKeyManagers();
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException {
        return socketFactory.createSocket(s, i);
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

    private SSLSocketFactory addSSLSettings(boolean trustAllRoots, String keystore,
                                            String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        /*java.lang.System.setProperty(
                "sun.security.ssl.allowUnsafeRenegotiation", "true");*/

        //use a client certificates keystore in case the server requires SSL client authentication
        boolean useClientCert = !isNull(keystore) && !trustAllRoots;
        //validate SSL certificates sent by the server
        boolean useTrustCert = !isNull(trustKeystore) && !trustAllRoots;

        String javaKeystore = System.getProperty("java.home") + "jre/lib/security/cacerts";
        if (!useClientCert) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? javaKeystore : null;
            keystorePassword = (storeExists) ? ((keystorePassword.equals("")) ? "changeit" : keystorePassword) : null;
        } else {
            if (!keystore.startsWith("http"))
                keystore = "" + keystore;
        }
        if (!useTrustCert) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? javaKeystore : null;
            trustPassword = (storeExists) ? ((trustPassword.equals("")) ? "changeit" : trustPassword) : null;
        } else {
            if (!trustKeystore.startsWith("http"))
                trustKeystore = "" + trustKeystore;
        }

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (useTrustCert) {
            KeyStore trustKeyStore = createKeyStore(new FileInputStream(trustKeystore), trustPassword);
            trustManagers = createAuthTrustManagers(trustKeyStore);
        }
        if (useClientCert) {
            KeyStore clientKeyStore = createKeyStore(new FileInputStream(keystore), keystorePassword);
            keyManagers = createKeyManagers(clientKeyStore, keystorePassword);
        }

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager()};
        }
        context.init(keyManagers, trustManagers, new SecureRandom());

        // Use the above SSLContext to create your socket factory

        return context.getSocketFactory();
    }
}
