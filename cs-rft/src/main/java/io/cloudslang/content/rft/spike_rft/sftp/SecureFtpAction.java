/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.rft.spike_rft.sftp;
/*
 * @(#)SecureFtpAction.java	11.14 10/07/2006
*
* Copyright 2006 Quintegra Solutions. All rights reserved.
* Quintegra Solutions PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
/**
 * <p>
 * Singleton implementation of SFTP commands
 * <p/>
 * </p>
 *
 * @version 1.0, 12/07/2006
 * @author <a href="mailto:bhat@quintegrasolutions.com">Jayanarayana Bhat U </a>
 */


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class SecureFtpAction {
    private ChannelSftp channelSftpObj;
    private boolean cachedSession = false; //by default not using a cached session.
    private Connection connectionObj = null;

    /**
     * connect
     *
     * @param _userName
     * @param _password
     * @param _host
     * @param _port
     * @param _timeout
     * @throws Exception
     */
    public void connect(String _userName, String _password, String _host, int _port, int _timeout) throws Exception {
        connectionObj = new Connection();
        connectionObj.setUserName(_userName);
        connectionObj.setPassword(_password);
        connectionObj.setHost(_host);
        connectionObj.setPort(_port);
        connectionObj.setTimeout(_timeout);
        connectionObj.connect();
    }

    /**
     * Connect with a privateKey
     *
     * @param _userName
     * @param _password
     * @param _privateKey
     * @param _host
     * @param _port
     * @param _timeout
     * @throws Exception
     */
    public void connect(String _userName, String _password, String _privateKey, String _host, int _port, int _timeout) throws Exception {
        connectionObj = new Connection();
        connectionObj.setUserName(_userName);
        connectionObj.setPassword(_password);
        connectionObj.setHost(_host);
        connectionObj.setPort(_port);
        connectionObj.setPrivateKey(_privateKey);
        connectionObj.setTimeout(_timeout);
        connectionObj.connect();
    }

    /**
     * Connect with an existing ssh session.
     *
     * @param _session
     * @throws Exception
     */
    public void connect(Session _session) throws Exception {
        connectionObj = new Connection(_session);
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
