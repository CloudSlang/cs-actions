/*
 * Copyright 2021-2023 Open Text
 * This program and the accompanying materials
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

import com.jcraft.jsch.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class Connection {

   // private static Logger logger = LoggerFactory.getLogger(Connection.class);
    private String username;
    private String password;
    private String host;
    private int port = 22;
    private String privateKey;
    private Session session;
    private ChannelSftp secureChannel;
    private int connectionTimeout;
    private int executionTimeout;

    public Connection() {

    }

    public Connection(Session existingSession) {
        if (existingSession != null && existingSession.isConnected()) {
    //        logger.debug("using existing ssh session");
            this.session = existingSession;
        } else {
            throw new RuntimeException("Invalid ssh session");
        }
    }

    public void disconnect() {
   //     logger.debug("disconnect()");
        //Session object handles if it's already connected
        this.session.disconnect();
    }

    public void disconnectChannel() {
    //    logger.debug("disconnectChannel()");
        //Session object handles if it's already connected
        this.secureChannel.disconnect();
    }

    public void connect() throws Exception {
        connect(true);
    }

    public void connect(boolean connectChannel) throws Exception {
    //    logger.debug("connect()");

        if (session == null || !session.isConnected()) {
            JSch jsch = new JSch();

            // username and password will be given via UserInfo interface.
            MyUserInfo uInfo = new MyUserInfo();
            uInfo.setPasswd(password);
            uInfo.setPromptYesNo(true);
            uInfo.setPassphrase(null);
            uInfo.setPromptPassword(true);
            if (this.privateKey != null)
                if (!this.privateKey.isEmpty()) {
                    uInfo.setPrivateKey(this.privateKey);
                    uInfo.setPassphrase(password);
                    jsch.addIdentity(uInfo.getPrivateKey(), password);
                }

            UserInfo ui = uInfo;
            session = jsch.getSession(username, host, port);
            session.setUserInfo(ui);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");

            if (connectionTimeout > 0)
                session.connect(connectionTimeout * 1000);
            else
                session.connect();

            if (executionTimeout > 0)
                session.setTimeout(executionTimeout * 1000);
        }
        if (connectChannel) {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            secureChannel = (ChannelSftp) channel;
    //        logger.debug("Connected to sftp server version: " + secureChannel.getServerVersion());
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public ChannelSftp getSecureChannel() {
        return secureChannel;
    }

    public Session getSession() {
        return session;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        if (port >= 0 && port <= 65535) {
            this.port = port;
        }
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(int executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

}
