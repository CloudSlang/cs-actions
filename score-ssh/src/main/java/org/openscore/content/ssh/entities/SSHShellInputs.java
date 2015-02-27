package org.openscore.content.ssh.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;

import java.util.Map;

/**
 * Created by ioanvranauhp on 11/5/2014.
 */
public class SSHShellInputs {
    private String host;
    private String port;
    private String username;
    private String password;
    private String privateKeyFile;
    private String command;
    private String arguments;
    private String characterSet;
    private String pty;
    private String timeout;
    private GlobalSessionObject<Map<String, SSHConnection>> sshGlobalSessionObject;
    private String closeSession;
    private String characterDelay;
    private String newlineCharacters;
    private String sessionId;
    private String knownHostsPolicy;
    private String knownHostsPath;
    private String agentForwarding;

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

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public GlobalSessionObject<Map<String, SSHConnection>> getSshGlobalSessionObject() {
        return sshGlobalSessionObject;
    }

    public void setSshGlobalSessionObject(GlobalSessionObject<Map<String, SSHConnection>> sshGlobalSessionObject) {
        this.sshGlobalSessionObject = sshGlobalSessionObject;
    }

    public String getCloseSession() {
        return closeSession;
    }

    public void setCloseSession(String closeSession) {
        this.closeSession = closeSession;
    }

    public String getCharacterDelay() {
        return characterDelay;
    }

    public void setCharacterDelay(String characterDelay) {
        this.characterDelay = characterDelay;
    }

    public String getNewlineCharacters() {
        return newlineCharacters;
    }

    public void setNewlineCharacters(String newlineCharacters) {
        this.newlineCharacters = newlineCharacters;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getKnownHostsPolicy() {
        return knownHostsPolicy;
    }

    public void setKnownHostsPolicy(String knownHostsPolicy) {
        this.knownHostsPolicy = knownHostsPolicy;
    }

    public String getKnownHostsPath() {
        return knownHostsPath;
    }

    public void setKnownHostsPath(String knownHostsPath) {
        this.knownHostsPath = knownHostsPath;
    }

    public String getAgentForwarding() {
        return agentForwarding;
    }

    public void setAgentForwarding(String agentForwarding) {
        this.agentForwarding = agentForwarding;
    }
}
