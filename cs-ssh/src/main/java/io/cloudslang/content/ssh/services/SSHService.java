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

package io.cloudslang.content.ssh.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import io.cloudslang.content.ssh.entities.CommandResult;
import io.cloudslang.content.ssh.entities.SSHConnection;

import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public interface SSHService extends AutoCloseable {

    /**
     * Run a Shell command(s) using SSH protocol, using a direct shell.
     *
     * @param command           The Shell command(s).
     * @param characterSet      The character set for the command and for the output of the command.
     * @param usePseudoTerminal If true the result will be formatted like in a terminal.
     * @param connectTimeout    The channel connection timeout.
     * @param commandTimeout    The command timeout.
     * @param agentForwarding   Weathers the agent forwarding is enabled or not.
     * @return the command result.
     */
    CommandResult runShell(String command, String characterSet, boolean usePseudoTerminal, int connectTimeout, int commandTimeout, boolean agentForwarding);

    /**
     * Run a Shell command(s) using SSH protocol.
     *
     * @param command           The Shell command(s).
     * @param characterSet      The character set for the command and for the output of the command.
     * @param usePseudoTerminal If true the result will be formatted like in a terminal.
     * @param connectTimeout    The channel connection timeout.
     * @param commandTimeout    The command timeout.
     * @param agentForwarding   Weathers the agent forwarding is enabled or not.
     * @return the command result.
     */
    CommandResult runShellCommand(String command, String characterSet, boolean usePseudoTerminal, int connectTimeout, int commandTimeout, boolean agentForwarding);

    /**
     * Checks the SSH session.
     *
     * @return true if the SSH session is opened, otherwise false.
     */
    boolean isConnected();

    /**
     * Close the SSH session.
     */
    void close();

    /**
     * Save the SSH session in the cache (Operation Orchestration session).
     *
     * @param sessionParam The cache (Operation Orchestration session).
     * @param sessionId    The key of the Operation Orchestration session.
     */
    boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId);

    /**
     * Remove SSH session from the cache (Operation Orchestration session).
     *
     * @param sessionParam The cache (Operation Orchestration session).
     * @param sessionId    The key of the Operation Orchestration session.
     */
    void removeFromCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId);

    /**
     * Create a local SSH tunnel (connect to a local port on the engine, which is then forwarded to the remote end of the tunnel.
     *
     * @param localPort  The local port.
     * @param remoteHost The remote host.
     * @param remotePort The remote port.
     */
    void createLocalTunnel(int localPort, String remoteHost, int remotePort);

    Session getSSHSession();

    Channel getExecChannel();
}
