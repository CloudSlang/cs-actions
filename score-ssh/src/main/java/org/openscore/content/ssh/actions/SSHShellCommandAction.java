package org.openscore.content.ssh.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.openscore.content.ssh.entities.SSHConnection;
import org.openscore.content.ssh.entities.SSHShellInputs;
import org.openscore.content.ssh.services.actions.ScoreSSHShellCommand;
import org.openscore.content.ssh.utils.Constants;

import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class SSHShellCommandAction {

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
            @Param(Constants.KNOWN_HOSTS_POLICY) String knownHostsPolicy,
            @Param(Constants.KNOWN_HOSTS_PATH) String knownHostsPath,
            @Param(value = Constants.COMMAND, required = true) String command,
            @Param(value = Constants.ARGS, description = Constants.ARGS_IS_DEPRECATED) String arguments,
            @Param(Constants.InputNames.CHARACTER_SET) String characterSet,
            @Param(value = Constants.PTY) String pty,
            @Param(Constants.InputNames.TIMEOUT) String timeout,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.CLOSE_SESSION) String closeSession) {

        SSHShellInputs sshShellInputs = new SSHShellInputs();
        sshShellInputs.setHost(host);
        sshShellInputs.setPort(port);
        sshShellInputs.setUsername(username);
        sshShellInputs.setPassword(password);
        sshShellInputs.setPrivateKeyFile(privateKeyFile);
        sshShellInputs.setCommand(command);
        sshShellInputs.setArguments(arguments);
        sshShellInputs.setCharacterSet(characterSet);
        sshShellInputs.setPty(pty);
        sshShellInputs.setTimeout(timeout);
        sshShellInputs.setSshGlobalSessionObject(globalSessionObject);
        sshShellInputs.setCloseSession(closeSession);
        sshShellInputs.setKnownHostsPolicy(knownHostsPolicy);
        sshShellInputs.setKnownHostsPath(knownHostsPath);

        return new ScoreSSHShellCommand().execute(sshShellInputs);
    }
}
