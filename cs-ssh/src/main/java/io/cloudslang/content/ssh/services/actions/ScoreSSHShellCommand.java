/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.ssh.services.actions;

import com.jcraft.jsch.ProxyHTTP;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.ssh.entities.*;
import io.cloudslang.content.ssh.services.SSHService;
import io.cloudslang.content.ssh.services.impl.SSHServiceImpl;
import io.cloudslang.content.ssh.utils.Constants;
import io.cloudslang.content.ssh.utils.IdentityKeyUtils;
import io.cloudslang.content.ssh.utils.ProxyUtils;
import io.cloudslang.content.ssh.utils.StringUtils;
import io.cloudslang.content.utils.StringUtilities;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ioanvranauhp on 11/5/2014.
 */
public class ScoreSSHShellCommand extends SSHShellAbstract {

    public Map<String, String> execute(SSHShellInputs sshShellInputs) {
        Map<String, String> returnResult = new HashMap<>();
        SSHService service = null;
        boolean providerAdded = addSecurityProvider();
        String sessionId = "";

        try {
            if (StringUtilities.isEmpty(sshShellInputs.getCommand())) {
                throw new RuntimeException(COMMAND_IS_NOT_SPECIFIED_MESSAGE);
            }
            if (sshShellInputs.getArguments() != null) {
                sshShellInputs.setCommand(sshShellInputs.getCommand() + " " + sshShellInputs.getArguments());
            }

            int portNumber = StringUtils.toInt(sshShellInputs.getPort(), Constants.DEFAULT_PORT);
            String knownHostsPolicy = StringUtils.toNotEmptyString(sshShellInputs.getKnownHostsPolicy(), Constants.DEFAULT_KNOWN_HOSTS_POLICY);
            Path knownHostsPath = StringUtils.toPath(sshShellInputs.getKnownHostsPath(), Constants.DEFAULT_KNOWN_HOSTS_PATH);

            sessionId = "sshSession:" + sshShellInputs.getHost() + "-" + portNumber + "-" + sshShellInputs.getUsername();

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(sshShellInputs.getHost(), portNumber, sshShellInputs.getUsername(), sshShellInputs.getPassword());
            IdentityKey identityKey = IdentityKeyUtils.getIdentityKey(sshShellInputs.getPrivateKeyFile(), sshShellInputs.getPrivateKeyData(), sshShellInputs.getPassword());
            KnownHostsFile knownHostsFile = new KnownHostsFile(knownHostsPath, knownHostsPolicy);

            // get the cached SSH session
            service = getSshServiceFromCache(sshShellInputs, sessionId);
            boolean saveSSHSession = false;
            if (service == null || !service.isConnected()) {
                saveSSHSession = true;
                ProxyHTTP proxyHTTP = ProxyUtils.getHTTPProxy(sshShellInputs.getProxyHost(), sshShellInputs.getProxyPort(), sshShellInputs.getProxyUsername(), sshShellInputs.getProxyPassword());
                service = new SSHServiceImpl(connection, identityKey, knownHostsFile, sshShellInputs.getConnectTimeout(), sshShellInputs.isAllowExpectCommands(), proxyHTTP, sshShellInputs.getAllowedCiphers());
            }

            runSSHCommand(sshShellInputs, returnResult, service, sessionId, saveSSHSession);
        } catch (Exception e) {
            if (service != null) {
                cleanupService(sshShellInputs, service, sessionId);
            }
            populateResult(returnResult, e);
        } finally {
            if (providerAdded) {
                removeSecurityProvider();
            }
        }
        return returnResult;
    }

    private SSHService getSshServiceFromCache(SSHShellInputs sshShellInputs, String sessionId) {
        SSHService service = getFromCache(sshShellInputs, sessionId);
        return service;
    }

    private void runSSHCommand(
            SSHShellInputs sshShellInputs,
            Map<String, String> returnResult,
            SSHService service, String sessionId,
            boolean saveSSHSession) {

        int timeoutNumber = StringUtils.toInt(sshShellInputs.getTimeout(), Constants.DEFAULT_TIMEOUT);
        boolean usePseudoTerminal = StringUtils.toBoolean(sshShellInputs.getPty(), Constants.DEFAULT_USE_PSEUDO_TERMINAL);
        boolean agentForwarding = StringUtils.toBoolean(sshShellInputs.getAgentForwarding(), Constants.DEFAULT_USE_AGENT_FORWARDING);
        sshShellInputs.setCharacterSet(StringUtils.toNotEmptyString(sshShellInputs.getCharacterSet(), Constants.DEFAULT_CHARACTER_SET));

        // run the SSH command
        CommandResult commandResult;

        if (sshShellInputs.isUseShell()) {
            commandResult = service.runShell(
                    sshShellInputs.getCommand(),
                    sshShellInputs.getCharacterSet(),
                    usePseudoTerminal,
                    sshShellInputs.getConnectTimeout(),
                    timeoutNumber,
                    agentForwarding);
        } else {
            commandResult = service.runShellCommand(
                    sshShellInputs.getCommand(),
                    sshShellInputs.getCharacterSet(),
                    usePseudoTerminal,
                    sshShellInputs.getConnectTimeout(),
                    timeoutNumber,
                    agentForwarding);
        }

        handleSessionClosure(sshShellInputs, service, sessionId, saveSSHSession);

        // populate the results
        populateResult(returnResult, commandResult);
    }

    private void handleSessionClosure(SSHShellInputs sshShellInputs, SSHService service, String sessionId, boolean saveSSHSession) {
        boolean closeSessionBoolean = StringUtils.toBoolean(sshShellInputs.getCloseSession(), Constants.DEFAULT_CLOSE_SESSION);
        if (closeSessionBoolean) {
            cleanupService(sshShellInputs, service, sessionId);
        } else if (saveSSHSession) {
            // save SSH session in the cache
            final boolean saved = saveToCache(sshShellInputs.getSshGlobalSessionObject(), service, sessionId);
            if (!saved) {
                throw new RuntimeException("The SSH session could not be saved in the given sessionParam.");
            }
        }
    }

    protected void cleanupService(SSHShellInputs sshShellInputs, SSHService service, String sessionId) {
        service.close();
        service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sessionId);
    }

    private void populateResult(Map<String, String> returnResult, CommandResult commandResult) {
        returnResult.put(Constants.STDERR, commandResult.getStandardError());
        returnResult.put(Constants.STDOUT, commandResult.getStandardOutput());
        if (commandResult.getExitCode() >= 0) {
            returnResult.put(OutputNames.RETURN_RESULT, commandResult.getStandardOutput());
            returnResult.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
        } else {
            returnResult.put(OutputNames.RETURN_RESULT, commandResult.getStandardError());
            returnResult.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        }
        returnResult.put(Constants.EXIT_STATUS, String.valueOf(commandResult.getExitCode()));
    }

}
