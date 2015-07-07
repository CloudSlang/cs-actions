package io.cloudslang.content.jclouds.entities.inputs;

/**
 * Created by persdana on 5/27/2015.
 */
public class CommonInputs {

    public static final String PROVIDER = "provider";
    public static final String ENDPOINT = "endpoint";
    public static final String IDENTITY = "identity";
    public static final String CREDENTIAL = "credential";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";

    private String provider;
    private String identity;
    private String credential;
    private String endpoint;
    private String proxyHost;
    private String proxyPort;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }
}
