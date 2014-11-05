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

            // default values
            int portNumber = StringUtils.toInt(sshShellInputs.getPort(), Constants.DEFAULT_PORT);
            boolean usePseudoTerminal = StringUtils.toBoolean(sshShellInputs.getPty(), Constants.DEFAULT_USE_PSEUDO_TERMINAL);
            boolean closeSessionBoolean = StringUtils.toBoolean(sshShellInputs.getCloseSession(), Constants.DEFAULT_CLOSE_SESSION);
            if (sshShellInputs.getArguments() != null) {
                sshShellInputs.setCommand(sshShellInputs.getCommand() + " " + sshShellInputs.getArguments());
            }
            sshShellInputs.setCharacterSet(StringUtils.toNotEmptyString(sshShellInputs.getCharacterSet(), Constants.DEFAULT_CHARACTER_SET));
            int timeoutNumber = StringUtils.toInt(sshShellInputs.getTimeout(), Constants.DEFAULT_TIMEOUT);
            boolean useGlobalContextBoolean = StringUtils.toBoolean(sshShellInputs.getUseGlobalContext(), Constants.DEFAULT_USE_GLOBAL_CONTEXT);

            sessionId = "sshSession:" + sshShellInputs.getHost() + "-" + portNumber + "-" + sshShellInputs.getUsername();

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(sshShellInputs.getHost(), portNumber,  sshShellInputs.getUsername(),  sshShellInputs.getPassword());
            KeyFile keyFile = getKeyFile(sshShellInputs.getPrivateKeyFile(),  sshShellInputs.getPassword());

            // get the cached SSH session
            service = getFromCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSshSessionObject(), useGlobalContextBoolean, sessionId);
            boolean saveSSHSession = false;
            if (service == null || !service.isConnected()) {
                saveSSHSession = true;
                service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT);
            }

            // run the SSH command
            CommandResult commandResult = service.runShellCommand(
                    sshShellInputs.getCommand(),
                    sshShellInputs.getCharacterSet(),
                    usePseudoTerminal,
                    Constants.DEFAULT_CONNECT_TIMEOUT,
                    timeoutNumber);

            if (closeSessionBoolean) {
                service.close();
                service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sessionId);
                service.removeFromCache(sshShellInputs.getSshSessionObject(), sessionId);
            } else if (saveSSHSession) {
                // save SSH session in the cache
                saveToCache(sshShellInputs.getSshGlobalSessionObject(), sshShellInputs.getSshSessionObject(), useGlobalContextBoolean, service, sessionId);
            }

            // populate the results
            populateResult(returnResult, commandResult);
        } catch (Exception e) {
            if (service != null) {
                service.close();
                service.removeFromCache(sshShellInputs.getSshGlobalSessionObject(), sessionId);
                service.removeFromCache(sshShellInputs.getSshSessionObject(), sessionId);
            }

            populateResult(returnResult, e);
        } finally {
            if (providerAdded) {
                removeSecurityProvider();
            }
        }
        return returnResult;
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
