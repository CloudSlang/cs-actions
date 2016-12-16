/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.ssh.utils;

import com.hp.oo.sdk.content.plugin.SessionResource;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import io.cloudslang.content.ssh.entities.SSHConnection;

import java.util.Collection;
import java.util.Map;

/**
 * Wrapper class on SSH session.
 *
 * @author octavian-h
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