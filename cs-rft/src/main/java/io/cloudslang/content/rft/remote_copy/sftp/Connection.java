/*
 * Copyright 2021-2024 Open Text
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

    private static final String ADDITIONAL_CIPHERS = ",aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc";
    private static final String KEX_ALGORITHMS = ",curve25519-sha256,curve25519-sha256@libssh.org,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521,diffie-hellman-group18-sha512,diffie-hellman-group16-sha512,diffie-hellman-group14-sha256,diffie-hellman-group-exchange-sha256,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1";
    private static final String ADDITIONAL_HOST_KEY_ALGORITHMS = ",ssh-ed25519,rsa-sha2-512,rsa-sha2-256,ssh-rsa,ssh-dss";
    private static final String ADDITIONAL_PUBKEY_ALGORITHMS = ",ssh-ed25519,rsa-sha2-512,rsa-sha2-256,ssh-rsa,ssh-dss";

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
            configureAlgorithms();

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

    private void configureAlgorithms() {
        String ciphersS2C = JSch.getConfig("cipher.s2c");
        if (ciphersS2C == null || !ciphersS2C.contains("aes128-ctr")) {
            JSch.setConfig("cipher.s2c", (ciphersS2C != null ? ciphersS2C : "") + ADDITIONAL_CIPHERS);
        }
        String ciphersC2S = JSch.getConfig("cipher.c2s");
        if (ciphersC2S == null || !ciphersC2S.contains("aes128-ctr")) {
            JSch.setConfig("cipher.c2s", (ciphersC2S != null ? ciphersC2S : "") + ADDITIONAL_CIPHERS);
        }
        String serverHostKey = JSch.getConfig("server_host_key");
        if (serverHostKey == null || !serverHostKey.contains("rsa-sha2-256")) {
            JSch.setConfig("server_host_key", (serverHostKey != null ? serverHostKey : "") + ADDITIONAL_HOST_KEY_ALGORITHMS);
        }
        String pubkeyAlgorithms = JSch.getConfig("PubkeyAcceptedAlgorithms");
        if (pubkeyAlgorithms == null || !pubkeyAlgorithms.contains("rsa-sha2-256")) {
            JSch.setConfig("PubkeyAcceptedAlgorithms", (pubkeyAlgorithms != null ? pubkeyAlgorithms : "") + ADDITIONAL_PUBKEY_ALGORITHMS);
        }
        String kex = JSch.getConfig("kex");
        if (kex == null || !kex.contains("curve25519-sha256")) {
            JSch.setConfig("kex", (kex != null ? kex : "") + KEX_ALGORITHMS);
        }
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
