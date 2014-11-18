package org.eclipse.score.content.ssh.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;
import org.eclipse.score.content.ssh.entities.SSHConnection;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import java.util.Collection;
import java.util.Map;

/**
 * Created by hasna on 11/13/2014.
 */

/**
 * Wrapper class on SSH session.
 */
public class SSHSessionResource extends SessionResource<Map<String, SSHConnection>> {
    private Map<String, SSHConnection> sshConnectionMap;

    public SSHSessionResource(Map<String, SSHConnection> sshConnectionMap) {
        this.sshConnectionMap = sshConnectionMap;
    }

    @Override
    public Map<String, SSHConnection> get() {
        return sshConnectionMap;
    }

    @Override
    public void release() {
        final Collection<SSHConnection> sshConnections = sshConnectionMap.values();
        for (SSHConnection sshConnection : sshConnections) {
            synchronized (sshConnection) {
                Session session = sshConnection.getSession();
                session.disconnect();
                Channel channel = sshConnection.getChannel();
                if (channel != null) {
                    channel.disconnect();
                }
            }
        }
        sshConnectionMap = null;
    }
}