package io.cloudslang.content.mail.sslconfig;

import io.cloudslang.content.mail.constants.SecurityConstants;
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
        return getSSLSocketFactory().getDefaultCipherSuites();
    }


    @Override
    public String[] getSupportedCipherSuites() {
        return getSSLSocketFactory().getSupportedCipherSuites();
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

        if (this.protocols.contains(TlsVersions.TLSv1_2) && !this.cipherSuites.isEmpty()) {
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
};


