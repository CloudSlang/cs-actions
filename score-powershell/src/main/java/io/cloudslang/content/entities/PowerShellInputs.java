package io.cloudslang.content.entities;

/**
 * User: bancl
 * Date: 10/12/2015
 */
public class PowerShellInputs {
    private String host;
    private String username;
    private String password;
    private String script;
    private String enableHTTPS;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getEnableHTTPS() {
        return enableHTTPS;
    }

    public void setEnableHTTPS(String enableHTTPS) {
        this.enableHTTPS = enableHTTPS;
    }
}
