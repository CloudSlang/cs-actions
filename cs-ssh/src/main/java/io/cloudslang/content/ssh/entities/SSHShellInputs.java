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



package io.cloudslang.content.ssh.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.ssh.utils.Constants;
import io.cloudslang.content.ssh.utils.StringUtils;

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
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String privateKeyData;
    private String allowedCiphers;
    private boolean allowExpectCommands;
    private int connectTimeout;
    private boolean useShell;
    private boolean removeEscapeSequences;

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

    public void setPrivateKeyData(String privateKeyData) {
        this.privateKeyData = privateKeyData;
    }

    public String getPrivateKeyData() {
        return privateKeyData;
    }

    public String getAllowedCiphers() {
        return allowedCiphers;
    }

    public void setAllowedCiphers(String allowedCiphers) {
        this.allowedCiphers = allowedCiphers;
    }

    public void setAllowExpectCommands(String allowExpectCommands) {
        this.allowExpectCommands =  StringUtils.toBoolean(allowExpectCommands, Constants.DEFAULT_ALLOW_EXPECT_COMMANDS);
    }

    public boolean isAllowExpectCommands() {
        return allowExpectCommands;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = StringUtils.toInt(connectTimeout, Constants.DEFAULT_CONNECT_TIMEOUT);

    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public boolean isUseShell() {
        return useShell;
    }

    public void setUseShell(String useShell) {
        this.useShell = StringUtils.toBoolean(useShell, Constants.DEFAULT_USE_SHELL);
    }

    public boolean isRemoveEscapeSequences() {
        return removeEscapeSequences;
    }

    public void setRemoveEscapeSequences(String removeEscapeSequences) {
        this.removeEscapeSequences = StringUtils.toBoolean(removeEscapeSequences, Constants.DEFAULT_REMOVE_ESCAPE_SEQUENCES);
    }
}
