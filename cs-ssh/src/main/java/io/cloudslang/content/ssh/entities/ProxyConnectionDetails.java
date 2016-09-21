package io.cloudslang.content.ssh.entities;


public class ProxyConnectionDetails {

    private String proxyHost;
    private String proxyUsername;
    private String proxyPassword;
    private int proxyPort;

    public ProxyConnectionDetails() {
    }

    public ProxyConnectionDetails(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        this(proxyHost, proxyPort);
        this.setProxyUsername(proxyUsername);
        this.setProxyPassword(proxyPassword);
    }

    public ProxyConnectionDetails(String proxyHost, int proxyPort) {
        this.setProxyHost(proxyHost);
        this.setProxyPort(proxyPort);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProxyConnectionDetails that = (ProxyConnectionDetails) o;

        if (proxyPort != that.proxyPort) {
            return false;
        }
        if (proxyHost != null ? !proxyHost.equals(that.proxyHost) : that.proxyHost != null) {
            return false;
        }
        if (proxyUsername != null ? !proxyUsername.equals(that.proxyUsername) : that.proxyUsername != null) {
            return false;
        }
        return proxyPassword != null ? proxyPassword.equals(that.proxyPassword) : that.proxyPassword == null;

    }

    @Override
    public int hashCode() {
        int result = proxyHost != null ? proxyHost.hashCode() : 0;
        result = 31 * result + proxyPort;
        result = 31 * result + (proxyUsername != null ? proxyUsername.hashCode() : 0);
        result = 31 * result + (proxyPassword != null ? proxyPassword.hashCode() : 0);
        return result;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
}
