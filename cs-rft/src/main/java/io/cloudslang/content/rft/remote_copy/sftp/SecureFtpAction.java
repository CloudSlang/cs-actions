/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.remote_copy.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class SecureFtpAction {
    private ChannelSftp channelSftpObj;
    private boolean cachedSession = false; //by default not using a cached session.
    private Connection connectionObj = null;

    public void connect(String username, String password, String host, int port, int connectionTimeout, int executionTimeout)
            throws Exception {
        connectionObj = new Connection();
        connectionObj.setUserName(username);
        connectionObj.setPassword(password);
        connectionObj.setHost(host);
        connectionObj.setPort(port);
        connectionObj.setConnectionTimeout(connectionTimeout);
        connectionObj.setExecutionTimeout(executionTimeout);
        connectionObj.connect();
    }

    public void connect(String username, String password, String privateKey, String host, int port, int connectionTimeout,
                        int executionTimeout)
            throws Exception {
        connectionObj = new Connection();
        connectionObj.setUserName(username);
        connectionObj.setPassword(password);
        connectionObj.setHost(host);
        connectionObj.setPort(port);
        connectionObj.setPrivateKey(privateKey);
        connectionObj.setConnectionTimeout(connectionTimeout);
        connectionObj.setExecutionTimeout(executionTimeout);
        connectionObj.connect();
    }

    /**
     * Connect with an existing ssh session.
     *
     * @param session
     * @throws Exception
     */
    public void connect(Session session) throws Exception {
        connectionObj = new Connection(session);
        cachedSession = true; //this session was created elsewhere.
        connectionObj.connect();
    }

    /**
     * @param strCommand
     * @param strSourceFile
     * @param strDestination
     * @param fileNameEncoding
     * @throws Exception
     */
    public void SFTPAction(String strCommand, String strSourceFile, String strDestination, String fileNameEncoding) throws Exception {
        if (strCommand.equalsIgnoreCase("get") || strCommand.equalsIgnoreCase("put")) {
            if (strSourceFile == null) {
                throw new Exception("Remote FileName Required");
            }
        }
        channelSftpObj = connectionObj.getSecureChannel();
        channelSftpObj.setFilenameEncoding(fileNameEncoding);

        if (strCommand.equals("get") || strCommand.equals("put")) {
            if ((strSourceFile == null && strDestination == null) || (strSourceFile == null)) {
                throw new Exception("command " + strCommand + " failed");
            }
            if (strCommand.equals("get")) {
                int iMode = ChannelSftp.OVERWRITE;
                channelSftpObj.get(strSourceFile, strDestination, null, iMode);
            } else {
                int iMode = ChannelSftp.OVERWRITE;
                channelSftpObj.put(strSourceFile, strDestination, null, iMode);
            }
        }
    }

    public void cleanup() {
        if (null != this.connectionObj) {
            if (cachedSession) {
                this.connectionObj.disconnectChannel();
                //this session was created elsewhere, it will be cleaned up by it's creator.
                //just disconnect the channel.
            } else {
                this.connectionObj.disconnect();
            }
        }
    }
}
