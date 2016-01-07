package io.cloudslang.content.entities.http;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class HttpInputs {
    private String host;
    private Integer port;
    private String protocol;
    private String username;
    private String password;
    private Boolean x509HostnameVerifier;

    public HttpInputs(String host,
                      Integer port,
                      String protocol,
                      String username,
                      String password,
                      Boolean x509HostnameVerifier) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.x509HostnameVerifier = x509HostnameVerifier;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public void setX509HostnameVerifier(Boolean x509HostnameVerifier) {
        this.x509HostnameVerifier = x509HostnameVerifier;
    }
}
