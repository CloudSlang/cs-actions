package org.openscore.content.ssh.entities;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class SSHConnection {
    private Session session;
    private Channel channel;
    private int sessionsCounter;

    public SSHConnection(Session session) {
        this.session = session;
    }

    public SSHConnection(Session session, Channel channel) {
        this.session = session;
        this.channel = channel;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getSessionsCounter() {
        return sessionsCounter;
    }

    public void setSessionsCounter(int sessionsCounter) {
        this.sessionsCounter = sessionsCounter;
    }
}
