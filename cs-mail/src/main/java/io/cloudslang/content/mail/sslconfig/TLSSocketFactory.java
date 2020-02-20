/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.mail.sslconfig;

import io.cloudslang.content.mail.constants.TlsVersions;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

class TLSSocketFactory extends SSLSocketFactory {

    private List<String> protocols;
    private List<String> cipherSuites;


    TLSSocketFactory(List<String> protocols, List<String> cipherSuites) {
        this.protocols = protocols;
        this.cipherSuites = cipherSuites;
    }


    @Override
    public String[] getDefaultCipherSuites() {
        if(shouldUseSpecificCipherSuite()) {
            return getSupportedCipherSuites();
        } else {
            return getSSLSocketFactory().getDefaultCipherSuites();
        }
    }


    @Override
    public String[] getSupportedCipherSuites() {
        if(shouldUseSpecificCipherSuite()) {
            String[] enabledCipherSuites = new String[this.cipherSuites.size()];
            return this.cipherSuites.toArray(enabledCipherSuites);
        } else {
            return getSSLSocketFactory().getSupportedCipherSuites();
        }
    }


    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        Socket sslSocket = getSSLSocketFactory().createSocket(socket, host, port, autoClose);
        configureSSLSocket(sslSocket);
        return sslSocket;
    }


    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        Socket sslSocket = getSSLSocketFactory().createSocket(s, i);
        configureSSLSocket(sslSocket);
        return sslSocket;
    }


    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        Socket sslSocket = getSSLSocketFactory().createSocket(s, i, inetAddress, i1);
        configureSSLSocket(sslSocket);
        return sslSocket;
    }


    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        Socket sslSocket = getSSLSocketFactory().createSocket(inetAddress, i);
        configureSSLSocket(sslSocket);
        return sslSocket;
    }


    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        Socket sslSocket = getSSLSocketFactory().createSocket(inetAddress, i, inetAddress1, i1);
        configureSSLSocket(sslSocket);
        return sslSocket;
    }


    private void configureSSLSocket(Socket socket) {
        SSLSocket sslSocket = (SSLSocket) socket;

        String[] enabledProtocols = new String[this.protocols.size()];
        this.protocols.toArray(enabledProtocols);
        sslSocket.setEnabledProtocols(enabledProtocols);

        if (shouldUseSpecificCipherSuite()) {
            String[] enabledCipherSuites = new String[this.cipherSuites.size()];
            this.cipherSuites.toArray(enabledCipherSuites);
            sslSocket.setEnabledCipherSuites(enabledCipherSuites);
        }
    }


    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            return SSLContext.getDefault().getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean shouldUseSpecificCipherSuite() {
        return this.protocols.contains(TlsVersions.TLSv1_2) && !this.cipherSuites.isEmpty();
    }
}