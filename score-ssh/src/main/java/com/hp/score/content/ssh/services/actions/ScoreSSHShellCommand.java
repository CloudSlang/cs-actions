package com.hp.score.content.ssh.services.actions;

import com.hp.score.content.ssh.entities.CommandResult;
import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHShellInputs;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.services.impl.SSHServiceImpl;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ioanvranauhp on 11/5/2014.
 *
 */
public class ScoreSSHShellCommand extends SSHShellAbstract {

    public Map<String, String> execute(SSHShellInputs sshShellInputs) {
        Map<String, String> returnResult = new HashMap<>();
        SSHService service = null;
        boolean providerAdded = addSecurityProvider();
        String sessionId = "";

        try {
            if (StringUtils.isEmpty(sshShellInputs.getCommand())) {
                throw new RuntimeException(COMMAND_IS_NOT_SPECIFIED_MESSAGE);
            }
            if (sshShellInputs.getArguments() != null) {
                sshShellInputs.setCommand(sshShellInputs.getCommand() + " " + sshShellInputs.getArguments());
            }

            int portNumber = StringUtils.toInt(sshShellInputs.getPort(), Constants.DEFAULT_PORT);
            sessionId = "sshSession:" + sshShellInputs.getHost() + "-" + portNumber + "-" + sshShellInputs.getUsername();

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(sshShellInputs.getHost(), portNumber,  sshShellInputs.getUsername(),  sshShellInputs.getPassword());
            KeyFile keyFile = getKeyFile(sshShellInputs.getPrivateKeyFile(), sshShellInputs.getPassword());

            // get the cached SSH session
            service = getSshServiceFromCache(sshShellInputs, sessionId);
            boolean saveSSHSession = false;
            if (service == null || !service.isConnected()) {
                saveSSHSession = true;
                service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT);
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
//        boolean useGlobalContextBoolean = StringUtils.toBoolean(sshShellInputs.getUseGlobalContext(), Constants.DEFAULT_USE_GLOBAL_CONTEXT); // TODO use global
        SSHService service = getFromCache(sshShellInputs, sessionId); // TODO SESSION, GLOBAL
        return service;
    }

    private void runSSHCommand(SSHShellInputs sshShellInputs, Map<String, String> returnResult, SSHService service, String sessionId, boolean saveSSHSession) {

        int timeoutNumber = StringUtils.toInt(sshShellInputs.getTimeout(), Constants.DEFAULT_TIMEOUT);
        boolean usePseudoTerminal = StringUtils.toBoolean(sshShellInputs.getPty(), Constants.DEFAULT_USE_PSEUDO_TERMINAL);
        sshShellInputs.setCharacterSet(StringUtils.toNotEmptyString(sshShellInputs.getCharacterSet(), Constants.DEFAULT_CHARACTER_SET));

        // run the SSH command
        CommandResult commandResult = service.runShellCommand(
                sshShellInputs.getCommand(),
                sshShellInputs.getCharacterSet(),
                usePseudoTerminal,
                Constants.DEFAULT_CONNECT_TIMEOUT,
                timeoutNumber);

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
            saveToCache(sshShellInputs.getSshGlobalSessionObject(), service, sessionId);
        }
    }

    protected void cleanupService(SSHShellInputs sshShellInputs, SSHService service, String sessionId) {
        service.close();
        service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sessionId);
//        service.removeFromCache(sshShellInputs.getSshSessionObject(), sessionId); //TODO session object
    }

    private void populateResult(Map<String, String> returnResult, CommandResult commandResult) {
        returnResult.put(Constants.STDERR, commandResult.getStandardError());
        returnResult.put(Constants.STDOUT, commandResult.getStandardOutput());
        if (commandResult.getExitCode() >= 0) {
            returnResult.put(Constants.OutputNames.RETURN_RESULT, commandResult.getStandardOutput());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        } else {
            returnResult.put(Constants.OutputNames.RETURN_RESULT, commandResult.getStandardError());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }
    }

}
