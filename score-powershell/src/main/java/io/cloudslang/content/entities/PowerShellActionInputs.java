package io.cloudslang.content.entities;

/**
 * User: bancl
 * Date: 10/12/2015
 */
public class PowerShellActionInputs {
    private String host;
    private String port;
    private String username;
    private String password;
    private String script;
    private String winrmEnableHTTPS;
    private String winrmTimeout;
    private String winrmContext;
    private String winrmEnvelopSize;
    private String winrmHttpsCertificateTrustStrategy;
    private String winrmHttpsHostnameVerificationStrategy;
    private String winrmKerberosAddPortToSpn;
    private String winrmKerberosTicketCache;
    private String winrmKerberosUseHttpSpn;
    private String winrmLocale;
    private String connectionType;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getWinrmEnableHTTPS() {
        return winrmEnableHTTPS;
    }

    public void setWinrmEnableHTTPS(String winrmEnableHTTPS) {
        this.winrmEnableHTTPS = winrmEnableHTTPS;
    }

    public void setWinrmTimeout(String winrmTimeout) {
        this.winrmTimeout = winrmTimeout;
    }

    public String getWinrmTimeout() {
        return winrmTimeout;
    }

    public void setWinrmContext(String winrmContext) {
        this.winrmContext = winrmContext;
    }

    public String getWinrmContext() {
        return winrmContext;
    }

    public void setWinrmEnvelopSize(String winrmEnvelopSize) {
        this.winrmEnvelopSize = winrmEnvelopSize;
    }

    public String getWinrmEnvelopSize() {
        return winrmEnvelopSize;
    }

    public void setWinrmHttpsCertificateTrustStrategy(String winrmHttpsCertificateTrustStrategy) {
        this.winrmHttpsCertificateTrustStrategy = winrmHttpsCertificateTrustStrategy;
    }

    public String getWinrmHttpsCertificateTrustStrategy() {
        return winrmHttpsCertificateTrustStrategy;
    }

    public void setWinrmHttpsHostnameVerificationStrategy(String winrmHttpsHostnameVerificationStrategy) {
        this.winrmHttpsHostnameVerificationStrategy = winrmHttpsHostnameVerificationStrategy;
    }

    public String getWinrmHttpsHostnameVerificationStrategy() {
        return winrmHttpsHostnameVerificationStrategy;
    }

    public void setWinrmKerberosAddPortToSpn(String winrmKerberosAddPortToSpn) {
        this.winrmKerberosAddPortToSpn = winrmKerberosAddPortToSpn;
    }

    public String getWinrmKerberosAddPortToSpn() {
        return winrmKerberosAddPortToSpn;
    }

    public void setWinrmKerberosTicketCache(String winrmKerberosTicketCache) {
        this.winrmKerberosTicketCache = winrmKerberosTicketCache;
    }

    public String getWinrmKerberosTicketCache() {
        return winrmKerberosTicketCache;
    }

    public void setWinrmKerberosUseHttpSpn(String winrmKerberosUseHttpSpn) {
        this.winrmKerberosUseHttpSpn = winrmKerberosUseHttpSpn;
    }

    public String getWinrmKerberosUseHttpSpn() {
        return winrmKerberosUseHttpSpn;
    }

    public void setWinrmLocale(String winrmLocale) {
        this.winrmLocale = winrmLocale;
    }

    public String getWinrmLocale() {
        return winrmLocale;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }
}
