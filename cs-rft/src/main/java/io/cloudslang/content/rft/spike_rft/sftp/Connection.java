package io.cloudslang.content.rft.spike_rft.sftp;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

public class Connection {

    private static Logger logger = Logger.getLogger(Connection.class);
    private String userName;
    private String password;
    private String host;
    private int port = 22;
    private String privateKey;
    private Session session;
    private ChannelSftp secureChannel;
    private int timeout;

    public Connection() {

    }

    public Connection(Session existingSession) {
        if (existingSession != null && existingSession.isConnected()) {
            logger.debug("using existing ssh session");
            this.session = existingSession;
        } else {
            throw new RuntimeException("Invalid ssh session");
        }
    }

    public void disconnect() {
        logger.debug("disconnect()");
        //Session object handles if it's already connected
        this.session.disconnect();
    }

    public void disconnectChannel() {
        logger.debug("disconnectChannel()");
        //Session object handles if it's already connected
        this.secureChannel.disconnect();
    }

    public void connect() throws Exception {
        connect(true);
    }

    public void connect(boolean connectChannel) throws Exception {
        logger.debug("connect()");

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
            session = jsch.getSession(userName, host, port);
            session.setUserInfo(ui);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            if (timeout > 0)
                session.connect(timeout);
            else
                session.connect();
        }
        if (connectChannel) {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            secureChannel = (ChannelSftp) channel;
            logger.debug("Connected to sftp server version: " + secureChannel.getServerVersion());
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
