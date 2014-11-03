package com.hp.score.content.ssh.actions;

import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.ExpectCommandResult;
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

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ioanvranauhp
 * Date: 11/03/14
 */
public class SSHShell extends SSHShellAbstract {

    @Action(name = "SSH Shell",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION),
                    @Output(Constants.STDOUT),
                    @Output(Constants.VISUALIZED)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> runSshExpectCommand(
            @Param(value = Constants.InputNames.HOST, required = true) String host,
            @Param(Constants.InputNames.PORT) String port,
            @Param(value = Constants.InputNames.USERNAME, required = true) String username,
            @Param(value = Constants.InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(Constants.PRIVATE_KEY_FILE) String privateKeyFile,
            @Param(value = Constants.COMMAND, required = true) String command,
            @Param(Constants.InputNames.CHARACTER_SET) String characterSet,
            @Param(Constants.CHARACTER_DELAY) String characterDelay,
            @Param(Constants.NEWLINE_SEQUENCE) String newlineCharacters,
            @Param(Constants.InputNames.TIMEOUT) String timeout,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> sessionObject, // todo session object global
            @Param(Constants.SESSION_ID) String sessionId) {

        Map<String, String> returnResult = new HashMap<>();
        boolean providerAdded = addSecurityProvider();

        try {
            if (command == null || command.length() == 0) {
                throw new RuntimeException("Command is not specified.");
            }

            // default values
            int portNumber = StringUtils.toInt(port, Constants.DEFAULT_PORT);
            characterSet = StringUtils.toNotEmptyString(characterSet, Constants.DEFAULT_CHARACTER_SET);
            int characterDelayNumber = StringUtils.toInt(characterDelay, Constants.DEFAULT_WRITE_CHARACTER_TIMEOUT);
            String newline = StringUtils.toNewline(newlineCharacters, Constants.DEFAULT_NEWLINE);
            int timeoutNumber = StringUtils.toInt(timeout, Constants.DEFAULT_TIMEOUT);

            // configure ssh parameters
            ConnectionDetails connection = new ConnectionDetails(host, portNumber, username, password);
            KeyFile keyFile = getKeyFile(privateKeyFile, password);

            // get the cached SSH session
            SSHService service = null;
            if (sessionId != null) {
                service = getFromCache(globalSessionObject, sessionObject, sessionId);
            }
            if (service == null || !service.isConnected() || !service.isExpectChannelConnected()) {
                service = new SSHServiceImpl(connection, keyFile, Constants.DEFAULT_CONNECT_TIMEOUT, true);
            }

            // run the SSH Expect command
            ExpectCommandResult commandResult = service.runExpectCommand(
                    command,
                    characterSet,
                    newline,
                    characterDelayNumber,
                    Constants.DEFAULT_CONNECT_TIMEOUT,
                    timeoutNumber);

            // populate the results
            populateResult(returnResult, commandResult);
        } catch (Exception e) {
            populateResult(returnResult, e);
        } finally {
            if (providerAdded) {
                removeSecurityProvider();
            }
        }
        return returnResult;
    }

    private void populateResult(Map<String, String> returnResult, ExpectCommandResult commandResult) {
        returnResult.put(Constants.STDOUT, commandResult.getStandardOutput());
        returnResult.put(Constants.VISUALIZED, commandResult.getExpectXmlOutputs());
        returnResult.put(Constants.OutputNames.RETURN_RESULT, commandResult.getStandardOutput());
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }
}
