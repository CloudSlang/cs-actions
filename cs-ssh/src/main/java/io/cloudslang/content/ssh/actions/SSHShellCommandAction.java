/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.ssh.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.actions.ScoreSSHShellCommand;
import io.cloudslang.content.ssh.utils.Constants;

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
     * @param host                The hostname or the ip address of the remote machine.
     * @param port                The port number for running the command. It overwrites the port given inside the host input (in a syntax like host:port), if this exists.
     * @param username            The username of the account on the remote machine.
     * @param password            The password of the user. If using a private key file this will be used as the passphrase for the file.
     * @param privateKeyFile      The path to the private key file (OpenSSH type) on the machine where is the worker.
     * @param privateKeyData      A string representing the private key (OpenSSH type) used for authenticating the user.
     *                            This string is usually the content of a private key file. The 'privateKeyData' and the
     *                            'privateKeyFile' inputs are mutually exclusive. For security reasons it is recommend
     *                            that the private key be protected by a passphrase that should be provided through the
     *                            'password' input.
     * @param knownHostsPolicy    The policy used for managing known_hosts file. Valid values: allow, strict, add. Default value: allow
     * @param knownHostsPath      The path to the known hosts file.
     * @param command             The command(s) to execute.
     * @param arguments           The arguments to pass to the command.
     * @param characterSet        The character encoding used for input stream encoding from the target machine.
     *                            Valid values: SJIS, EUC-JP, UTF-8. Default value: UTF-8.
     * @param pty                 Whether to use a pseudo-terminal (PTY) session. Valid values: false, true. Default value: false
     * @param agentForwarding     Enables or disables the forwarding of the authentication agent connection.
     *                            Agent forwarding should be enabled with caution.
     * @param timeout             Time in milliseconds to wait for the command to complete. Default value is 90000 (90 seconds)
     * @param connectTimeout      Time in milliseconds to wait for the connection to be made. Default value: 10000
     * @param allowedCiphers      A comma separated list of ciphers that will be used in the client-server handshake
     *                            mechanism when the connection is created. Check the notes section for security concerns
     *                            regarding your choice of ciphers. The default value will be used even if the input is not
     *                            added to the operation.
     *                            Default value: aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc
     * @param globalSessionObject the sessionObject that holds the connection if the close session is false.
     * @param closeSession        If true it closes the SSH session at completion of this operation.
     *                            If false the SSH session will be cached for future calls of this operation during the life of the flow.
     *                            Valid values: false, true. Default value: false
     * @param useShell            Specifies whether to use shell mode to run the commands. This will start a shell
     *                            session and run the command, after which it will issue an 'exit' command, to close
     *                            the shell.
     *                            Note: If the output does not show the whole expected output, increase the <timeout> value.
     *                            Valid values: true, false.
     *                            Default value: false.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * <br><b>returnResult</b> - The primary output.
     * <br><b>STDOUT</b> - The standard output of the command(s).
     * <br><b>visualized</b> - The output of the command in XML format.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */

    @Action(name = "SSH Command",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION),
                    @Output(Constants.STDOUT),
                    @Output(Constants.STDERR),
                    @Output(Constants.EXIT_STATUS)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> runSshShellCommand(
            @Param(value = Constants.InputNames.HOST, required = true) String host,
            @Param(Constants.InputNames.PORT) String port,
            @Param(value = Constants.InputNames.USERNAME, required = true) String username,
            @Param(value = Constants.InputNames.PASSWORD, encrypted = true) String password,
            @Param(Constants.PRIVATE_KEY_FILE) String privateKeyFile,
            @Param(value = Constants.PRIVATE_KEY_DATA, encrypted = true) String privateKeyData,
            @Param(Constants.KNOWN_HOSTS_POLICY) String knownHostsPolicy,
            @Param(Constants.KNOWN_HOSTS_PATH) String knownHostsPath,
            @Param(Constants.ALLOWED_CIPHERS) String allowedCiphers,
            @Param(value = Constants.COMMAND, required = true) String command,
            @Param(value = Constants.ARGS, description = Constants.ARGS_IS_DEPRECATED) String arguments,
            @Param(Constants.InputNames.CHARACTER_SET) String characterSet,
            @Param(value = Constants.PTY) String pty,
            @Param(value = Constants.InputNames.AGENT_FORWARDING) String agentForwarding,
            @Param(Constants.InputNames.TIMEOUT) String timeout,
            @Param(Constants.CONNECT_TIMEOUT) String connectTimeout,
            @Param(Constants.SSH_SESSIONS_DEFAULT_ID) GlobalSessionObject<Map<String, SSHConnection>> globalSessionObject,
            @Param(Constants.CLOSE_SESSION) String closeSession,
            @Param(Constants.PROXY_HOST) String proxyHost,
            @Param(Constants.PROXY_PORT) String proxyPort,
            @Param(Constants.PROXY_USERNAME) String proxyUsername,
            @Param(value = Constants.PROXY_PASSWORD, encrypted = true) String proxyPassword,
            @Param(Constants.ALLOW_EXPECT_COMMANDS) String allowExpectCommands,
            @Param(Constants.USE_SHELL) String useShell) {

        SSHShellInputs sshShellInputs = new SSHShellInputs();
        sshShellInputs.setHost(host);
        sshShellInputs.setPort(port);
        sshShellInputs.setUsername(username);
        sshShellInputs.setPassword(password);
        sshShellInputs.setPrivateKeyFile(privateKeyFile);
        sshShellInputs.setPrivateKeyData(privateKeyData);
        sshShellInputs.setCommand(command);
        sshShellInputs.setArguments(arguments);
        sshShellInputs.setCharacterSet(characterSet);
        sshShellInputs.setPty(pty);
        sshShellInputs.setAgentForwarding(agentForwarding);
        sshShellInputs.setTimeout(timeout);
        sshShellInputs.setConnectTimeout(connectTimeout);
        sshShellInputs.setSshGlobalSessionObject(globalSessionObject);
        sshShellInputs.setCloseSession(closeSession);
        sshShellInputs.setKnownHostsPolicy(knownHostsPolicy);
        sshShellInputs.setKnownHostsPath(knownHostsPath);
        sshShellInputs.setAllowedCiphers(allowedCiphers);
        sshShellInputs.setProxyHost(proxyHost);
        sshShellInputs.setProxyPort(proxyPort);
        sshShellInputs.setProxyUsername(proxyUsername);
        sshShellInputs.setProxyPassword(proxyPassword);
        sshShellInputs.setAllowExpectCommands(allowExpectCommands);
        sshShellInputs.setUseShell(useShell);

        return new ScoreSSHShellCommand().execute(sshShellInputs);
    }
}
