/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.rft.services;

import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.KeyFile;
import io.cloudslang.content.rft.entities.KnownHostsFile;
import io.cloudslang.content.rft.entities.RemoteSecureCopyInputs;
import io.cloudslang.content.rft.utils.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Date: 8/17/2015
 *
 * @author lesant
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class, SCPCopier.class})
public class SCPCopierTest {

    private static final Path KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");
    private static final String KNOWN_HOSTS_POLICY_ALLOW = "allow";
    private static final String KNOWN_HOSTS_POLICY_STRICT = "strict";
    private static final String KNOWN_HOSTS_POLICY_ADD = "add";

    private static final int CONNECT_TIMEOUT = 10000;
    public static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    public static final String STRICT = "yes";
    public static final String NONSTRICT = "no";
    public static final String ABSOLUTE_KNOWN_HOSTS_FILE_ERROR_MESSAGE = "The known_hosts file path should be absolute.";
    public static final String UNKNOWN_KNOWN_HOSTS_FILE_POLICY = "Unknown known_hosts file policy.";
    public static final String SRC_PASS = "src_pass";
    public static final String KEY_FILE_PATH = "path";
    public static final String PASS_PHRASE = "phrase";
    public static final String EXEC = "exec";


    @Mock
    private Session sessionMock;

    @Mock
    private ChannelExec channelExecMock;

    @Mock
    private File tempFileMock;
    @Mock
    private JSch jSchMock;

    @Mock
    private InputStream inputStreamMock;

    @Mock
    private OutputStream outputStreamMock;

    @Mock
    private FileInputStream fileInputStreamMock;

    @Mock
    private Path pathMock;

    @Mock
    private KnownHostsFile knownHostsFileMock;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private SCPCopier scpCopier;


    @Before
    public void setUp() throws Exception {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs) {
            protected void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch, Session session) throws JSchException, IOException {
            }

            protected void establishPrivateKeyFile(KeyFile keyFile, JSch jsch, Session session, boolean usesSrcPrivateKeyFile) {
            }
        };
        PowerMockito.whenNew(JSch.class).withNoArguments().thenReturn(jSchMock);
        PowerMockito.when(jSchMock.getSession(anyString(), anyString(), anyInt())).thenReturn(sessionMock);
        PowerMockito.when(sessionMock.openChannel(EXEC)).thenReturn(channelExecMock);
        Mockito.doNothing().when(channelExecMock).connect(CONNECT_TIMEOUT);

        PowerMockito.when(channelExecMock.getInputStream()).thenReturn(inputStreamMock);
        PowerMockito.when(channelExecMock.getOutputStream()).thenReturn(outputStreamMock);
        PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStreamMock);

    }

    @Test
    public void copyFromLocalToRemoteWithJSchException() throws Exception {
        PowerMockito.when(jSchMock.getSession(anyString(), anyString(), anyInt())).thenThrow(JSchException.class);
        exception.expect(RuntimeException.class);
        scpCopier.copyFromLocalToRemote();

        verifyNew(JSch.class).withNoArguments();
    }

    @Test
    public void copyFromLocalToRemoteWithIOException() throws Exception {
        PowerMockito.when(channelExecMock.getOutputStream()).thenThrow(IOException.class);
        exception.expect(RuntimeException.class);
        scpCopier.copyFromLocalToRemote();

        verifyNew(JSch.class).withNoArguments();
        verify(sessionMock).connect();
    }

    @Test
    public void copyFromLocalToRemote() throws Exception {
        boolean isCopied = scpCopier.copyFromLocalToRemote();
        assertEquals(true, isCopied);

        verifyNew(JSch.class).withNoArguments();
        verify(jSchMock).getSession(anyString(), anyString(), anyInt());
        verify(sessionMock).connect(anyInt());
        verify(sessionMock).openChannel(EXEC);
        verify(channelExecMock).setCommand(anyString());
        verify(channelExecMock).connect();
        verify(channelExecMock).disconnect();
        verify(sessionMock).disconnect();
    }

    @Test
    public void copyFromRemoteToLocal() throws Exception {
        boolean isCopied = scpCopier.copyFromRemoteToLocal();
        assertEquals(true, isCopied);

        verifyNew(JSch.class).withNoArguments();
        verify(jSchMock).getSession(anyString(), anyString(), anyInt());
        verify(sessionMock).connect(anyInt());
        verify(sessionMock).openChannel(EXEC);
        verify(channelExecMock).setCommand(anyString());
        verify(channelExecMock).connect();
        verify(channelExecMock).disconnect();
        verify(sessionMock).disconnect();
    }

    @Test
    public void copyFromRemoteToLocalWithJSchException() throws Exception {
        PowerMockito.when(jSchMock.getSession(anyString(), anyString(), anyInt())).thenThrow(JSchException.class);
        exception.expect(RuntimeException.class);
        scpCopier.copyFromRemoteToLocal();

        verifyNew(JSch.class).withNoArguments();

    }

    @Test
    public void copyFromRemoteToLocalWithIOException() throws Exception {
        PowerMockito.when(channelExecMock.getOutputStream()).thenThrow(IOException.class);
        exception.expect(RuntimeException.class);
        scpCopier.copyFromRemoteToLocal();

        verifyNew(JSch.class).withNoArguments();
        verify(sessionMock).connect();
    }

    @Test
    public void copyFromRemoteToRemote() throws IOException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs) {
            protected void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch, Session session) throws JSchException, IOException {
            }

            protected void establishPrivateKeyFile(KeyFile keyFile, JSch jsch, Session session, boolean usesSrcPrivateKeyFile) {
            }

            protected boolean copyFromLocalToRemote(String srcPath, String destPath) {
                return true;
            }

            protected boolean copyFromRemoteToLocal(String srcPath, String destPath) {
                return true;
            }
        };

        mockStatic(File.class);
        when(File.createTempFile(anyString(), anyString())).thenReturn(tempFileMock);
        when(tempFileMock.getCanonicalPath()).thenReturn("C:\\myTestFolder");

        boolean isCopied = scpCopier.copyFromRemoteToRemote();
        verify(tempFileMock).delete();

        assertEquals(true, isCopied);
    }

    @Test
    public void establishKnownHostsConfigurationStrict() throws IOException, JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        scpCopier.establishKnownHostsConfiguration(new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY_STRICT), jSchMock, sessionMock);
        verify(sessionMock).setConfig(STRICT_HOST_KEY_CHECKING, STRICT);

    }

    @Test
    public void establishKnownHostsConfigurationAllow() throws IOException, JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        scpCopier.establishKnownHostsConfiguration(new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY_ALLOW), jSchMock, sessionMock);
        verify(sessionMock).setConfig(STRICT_HOST_KEY_CHECKING, NONSTRICT);

    }

    @Test
    public void establishKnownHostsConfigurationAdd() throws IOException, JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        scpCopier.establishKnownHostsConfiguration(new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY_ADD), jSchMock, sessionMock);
        verify(sessionMock).setConfig(STRICT_HOST_KEY_CHECKING, NONSTRICT);
    }

    @Test
    public void establishKnownHostsConfigurationAddKnownHostsFilePathNotAbsolute() throws IOException, JSchException, URISyntaxException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);
        when(knownHostsFileMock.getPath()).thenReturn(pathMock);
        when(knownHostsFileMock.getPolicy()).thenReturn(KNOWN_HOSTS_POLICY_ADD);
        when(pathMock.isAbsolute()).thenReturn(false);

        exception.expect(RuntimeException.class);
        exception.expectMessage(ABSOLUTE_KNOWN_HOSTS_FILE_ERROR_MESSAGE);

        scpCopier.establishKnownHostsConfiguration(knownHostsFileMock, jSchMock, sessionMock);
        verify(sessionMock).setConfig(STRICT_HOST_KEY_CHECKING, NONSTRICT);
    }

    @Test
    public void establishKnownHostsConfigurationAddUnknownPolicy() throws IOException, JSchException, URISyntaxException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);
        when(knownHostsFileMock.getPath()).thenReturn(pathMock);
        when(knownHostsFileMock.getPolicy()).thenReturn(StringUtils.EMPTY_STRING);
        when(pathMock.isAbsolute()).thenReturn(false);

        exception.expect(RuntimeException.class);
        exception.expectMessage(UNKNOWN_KNOWN_HOSTS_FILE_POLICY);

        scpCopier.establishKnownHostsConfiguration(knownHostsFileMock, jSchMock, sessionMock);
        verify(sessionMock).setConfig(STRICT_HOST_KEY_CHECKING, NONSTRICT);

    }

    @Test
    public void establishPrivateKeyFileWithPassword() throws JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        remoteSecureCopyInputs.setSrcPassword(SRC_PASS);
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        scpCopier.establishPrivateKeyFile(null, jSchMock, sessionMock, true);

        verify(sessionMock).setPassword(remoteSecureCopyInputs.getSrcPassword());

    }

    @Test
    public void establishPrivateKeyFile() throws JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        remoteSecureCopyInputs.setSrcPassword(SRC_PASS);
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        KeyFile key = new KeyFile(KEY_FILE_PATH);
        scpCopier.establishPrivateKeyFile(key, jSchMock, sessionMock, true);

        verify(jSchMock).addIdentity(key.getKeyFilePath());
    }

    @Test
    public void establishPrivateKeyFileWithPassphrase() throws JSchException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        remoteSecureCopyInputs.setSrcPassword(SRC_PASS);
        scpCopier = new SCPCopier(remoteSecureCopyInputs);

        KeyFile key = new KeyFile(KEY_FILE_PATH, PASS_PHRASE);
        scpCopier.establishPrivateKeyFile(key, jSchMock, sessionMock, true);

        verify(jSchMock).addIdentity(key.getKeyFilePath(), key.getPassPhrase());
    }

    private RemoteSecureCopyInputs getRemoteSecureCopyInputs() {
        return new RemoteSecureCopyInputs(StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING);
    }

}