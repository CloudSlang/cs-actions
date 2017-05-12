/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.ssh.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.services.SSHService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 11/19/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CacheUtils.class})

public class CacheUtilsTest {

    @Mock
    private SessionResource<Map<String, SSHConnection>> resourceMock;
    @Mock
    private Session sessionMock;
    @Mock
    private ChannelShell channelShellMock;
    @Mock
    private GlobalSessionObject<Map<String, SSHConnection>> sshGlobalSessionObjectMock;
    @Mock
    private SessionResource<Map<String, SSHConnection>> sessionResourceMock;
    @Mock
    private Map<String, SSHConnection> mapMock;
    @Mock
    private SSHConnection sshConnectionMock;

    final String sessionId = "sessionId";

    @Test
    public void testSaveSshSessionAndChannel() {
        boolean saved = CacheUtils.saveSshSessionAndChannel(sessionMock, channelShellMock, sshGlobalSessionObjectMock, sessionId);
        assertEquals(true, saved);

        saved = CacheUtils.saveSshSessionAndChannel(null, null, null, null);
        assertEquals(false, saved);

        saved = CacheUtils.saveSshSessionAndChannel(null, null, null, "");
        assertEquals(false, saved);

        when(sshGlobalSessionObjectMock.get()).thenReturn(null);
        saved = CacheUtils.saveSshSessionAndChannel(sessionMock, channelShellMock, sshGlobalSessionObjectMock, sessionId);
        assertEquals(true, saved);

        when(sshGlobalSessionObjectMock.get()).thenReturn(mapMock);
        saved = CacheUtils.saveSshSessionAndChannel(sessionMock, channelShellMock, sshGlobalSessionObjectMock, sessionId);
        assertEquals(true, saved);
    }

    @Test
    public void testRemoveSshSession() {

        when(sshGlobalSessionObjectMock.getResource()).thenReturn(resourceMock);
        when(resourceMock.get()).thenReturn(mapMock);
        CacheUtils.removeSshSession(sshGlobalSessionObjectMock, sessionId);
        CacheUtils.removeSshSession(null, null);
        CacheUtils.removeSshSession(null, "");

        verify(mapMock, Mockito.times(1)).remove(Mockito.anyString());
    }

    @Test
    public void testGetFromCache() {
        when(sessionResourceMock.get()).thenReturn(mapMock);
        when(mapMock.get(sessionId)).thenReturn(sshConnectionMock);
        when(sshConnectionMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.isConnected()).thenReturn(true);
        SSHService fromCache = CacheUtils.getFromCache(sessionResourceMock, sessionId);
        assertNotNull(fromCache);
        assertEquals(fromCache.getSSHSession(), sessionMock);
        assertEquals(fromCache.getExecChannel(), null);

        fromCache = CacheUtils.getFromCache(null, null);
        assertNull(fromCache);
    }
}
