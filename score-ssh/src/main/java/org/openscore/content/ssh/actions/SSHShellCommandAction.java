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
 * The operation executes a Shell command(s) on the remote machine using the SSH protocol.
 *
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class SSHShellCommandAction {

    /**
     * Executes a Shell command(s) on the remote machine using the SSH protocol.
     *
     * @param host The hostname or the ip address of the remote machine.
     * @param port The port number for running the command. It overwrites the port given inside the host input (in a syntax like host:port), if this exists.
     * @param username The username of the account on the remote machine.
     * @param password The password of the user. If using a private key file this will be used as the passphrase for the file.
     * @param privateKeyFile The path to the private key file (OpenSSH type) on the machine where is the worker.
     * @param knownHostsPolicy The policy used for managing known_hosts file. Valid values: allow, strict, add. Default value: allow
     * @param knownHostsPath The path to the known hosts file.
     * @param command The command(s) to execute.
     * @param arguments The arguments to pass to the command.
     * @param characterSet The character encoding used for input stream encoding from the target machine.
     *                     Valid values: SJIS, EUC-JP, UTF-8. Default value: UTF-8.
     * @param pty Whether to use a pseudo-terminal (PTY) session. Valid values: false, true. Default value: false
     * @param agentForwarding Enables or disables the forwarding of the authentication agent connection. 
     *                        Agent forwarding should be enabled with caution.
     * @param timeout Time in milliseconds to wait for the command to complete. Default value is 90000 (90 seconds)
     * @param globalSessionObject the sessionObject that holds the connection if the close session is false.
     * @param closeSession If true it closes the SSH session at completion of this operation.
     *                     If false the SSH session will be cached for future calls of this operation during the life of the flow.
     *                     Valid values: false, true. Default value: false
     * @return - a map containing the output of the operation. Keys present in the map are:
     *     <br><b>returnResult</b> - The primary output.
     *     <br><b>STDOUT</b> - The standard output of the command(s).
     *     <br><b>visualized</b> - The output of the command in XML format.
     *     <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     *     <br><b>exception</b> - the exception message if the operation goes to failure.
     *
     */

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
            @Param(value = Constants.InputNames.PASSWORD, encrypted = true) String password,
            @Param(Constants.PRIVATE_KEY_FILE) String privateKeyFile,
            @Param(Constants.KNOWN_HOSTS_POLICY) String knownHostsPolicy,
            @Param(Constants.KNOWN_HOSTS_PATH) String knownHostsPath,
            @Param(value = Constants.COMMAND, required = true) String command,
            @Param(value = Constants.ARGS, description = Constants.ARGS_IS_DEPRECATED) String arguments,
            @Param(Constants.InputNames.CHARACTER_SET) String characterSet,
            @Param(value = Constants.PTY) String pty,
            @Param(value = Constants.InputNames.AGENT_FORWARDING) String agentForwarding,
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
        sshShellInputs.setAgentForwarding(agentForwarding);
        sshShellInputs.setTimeout(timeout);
        sshShellInputs.setSshGlobalSessionObject(globalSessionObject);
        sshShellInputs.setCloseSession(closeSession);
        sshShellInputs.setKnownHostsPolicy(knownHostsPolicy);
        sshShellInputs.setKnownHostsPath(knownHostsPath);

        return new ScoreSSHShellCommand().execute(sshShellInputs);
    }
}
