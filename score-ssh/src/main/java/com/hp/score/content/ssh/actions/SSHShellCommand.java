package com.hp.score.content.ssh.actions;

import com.hp.score.content.ssh.entities.CommandResult;
import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.services.impl.SSHServiceImpl;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.StringUtils;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.score.content.ssh.utils.Constants;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ioanvranauhp
 * Date: 10/29/14
 */
public class SSHShellCommand extends SSHShellAbstract {

    @Action(name = "SSH Command",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION),
                    @Output(Constants.STDOUT),
                    @Output(Constants.STDERR)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> runSshShellCommand(
            @Param(value = Constants.InputNames.HOST, required = true) String host,
            @Param(Constants.InputNames.PORT) String port,
            @Param(value = Constants.InputNames.USERNAME, required = true) String username,
            @Param(value = Constants.InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(Constants.PRIVATE_KEY_FILE) String privateKeyFile,
            @Param(value = Constants.COMMAND, required = true) String command,
            @Param(value = Constants.ARGS, description = Constants.ARGS_IS_DEPRECATED) String arguments,
            @Param(Constants.InputNames.CHARACTER_SET) String characterSet,
            @Param(value = Constants.PTY) String pty,  //, required = true
            @Param(Constants.InputNames.TIMEOUT) String timeout,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> sessionObject, //TODO SessionObject?
            @Param(Constants.USE_GLOBAL_CONTEXT) String useGlobalContext,
            @Param(Constants.CLOSE_SESSION) String closeSession) {

        Map<String, String> returnResult = new HashMap<>();
        SSHService service = null;
        boolean providerAdded = addSecurityProvider();
        String sessionId = "";

        try {
            if (command == null || command.length() == 0) {
                throw new RuntimeException("Command is not specified.");
            }

            // default values
            int portNumber = StringUtils.toInt(port, Constants.DEFAULT_PORT);
            boolean usePseudoTerminal = StringUtils.toBoolean(pty, Constants.DEFAULT_USE_PSEUDO_TERMINAL);
            boolean closeSessionBoolean = StringUtils.toBoolean(closeSession, Constants.DEFAULT_CLOSE_SESSION);
            if (arguments != null) {
                command = command + " " + arguments;
            }
            characterSet = StringUtils.toNotEmptyString(characterSet, Constants.DEFAULT_CHARACTER_SET);
            int timeoutNumber = StringUtils.toInt(timeout, Constants.DEFAULT_TIMEOUT);
            boolean useGlobalContextBoolean = StringUtils.toBoolean(useGlobalContext, Constants.DEFAULT_USE_GLOBAL_CONTEXT);

            sessionId = "sshSession:" + host + "-" + portNumber + "-" + username;

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(host, portNumber, username, password);
            KeyFile keyFile = getKeyFile(privateKeyFile, password);

            // get the cached SSH session
            service = getFromCache(globalSessionObject, sessionObject, useGlobalContextBoolean, sessionId);
            boolean saveSSHSession = false;
            if (service == null || !service.isConnected()) {
                saveSSHSession = true;
                service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT);
            }

            // run the SSH command
            CommandResult commandResult = service.runShellCommand(
                    command,
                    characterSet,
                    usePseudoTerminal,
                    Constants.DEFAULT_CONNECT_TIMEOUT,
                    timeoutNumber);

            if (closeSessionBoolean) {
                service.close();
                service.removeFromCache(globalSessionObject, sessionId);
                service.removeFromCache(sessionObject, sessionId);
            } else if (saveSSHSession) {
                // save SSH session in the cache
                saveToCache(globalSessionObject, sessionObject, useGlobalContextBoolean, service, sessionId);
            }

            // populate the results
            populateResult(returnResult, commandResult);
        } catch (Exception e) {
            if (service != null) {
                service.close();
                service.removeFromCache(globalSessionObject, sessionId);
                service.removeFromCache(sessionObject, sessionId);
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
