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




package io.cloudslang.content.rft.services;

import com.jcraft.jsch.*;
import io.cloudslang.content.rft.entities.KeyFile;
import io.cloudslang.content.rft.entities.KnownHostsFile;
import io.cloudslang.content.rft.entities.RemoteSecureCopyInputs;
import io.cloudslang.content.rft.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

/**
 * Date: 8/17/2015
 *
 * @author lesant
 */
@ExtendWith(MockitoExtension.class)
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
    private Path pathMock;

    @Mock
    private KnownHostsFile knownHostsFileMock;

    private SCPCopier scpCopier;


    @BeforeEach
    public void setUp() throws Exception {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs) {
            protected void establishKnownHostsConfiguration(KnownHostsFile knownHostsFile, JSch jsch, Session session) throws JSchException, IOException {
            }

            protected void establishPrivateKeyFile(KeyFile keyFile, JSch jsch, Session session, boolean usesSrcPrivateKeyFile) {
            }
        };
    }

    @Test
    public void copyFromLocalToRemoteWithJSchException() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSchThrowing(new JSchException("connection failure"))) {
            assertThrows(RuntimeException.class, () -> scpCopier.copyFromLocalToRemote());
            verifyJSchWasConstructed(jschConstruction);
        }
    }

    @Test
    public void copyFromLocalToRemoteWithIOException() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSch()) {
            prepareChannel();
            when(channelExecMock.getOutputStream()).thenThrow(new IOException("stream failure"));
            assertThrows(RuntimeException.class, () -> scpCopier.copyFromLocalToRemote());
            verifyJSchWasConstructed(jschConstruction);
            verify(sessionMock).connect(anyInt());
        }
    }

    @Test
    public void copyFromLocalToRemote() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSch();
             MockedConstruction<FileInputStream> fileInputStreams = mockConstruction(FileInputStream.class)) {
            prepareChannel();
            prepareInputStream();
            when(channelExecMock.getOutputStream()).thenReturn(outputStreamMock);
            boolean isCopied = scpCopier.copyFromLocalToRemote();
            assertEquals(true, isCopied);

            verifyJSchWasConstructed(jschConstruction);
            verify(jschConstruction.constructed().get(0)).getSession(nullable(String.class), nullable(String.class), anyInt());
            assertEquals(1, fileInputStreams.constructed().size());
            verify(sessionMock).connect(anyInt());
            verify(sessionMock).openChannel(EXEC);
            verify(channelExecMock).setCommand(anyString());
            verify(channelExecMock).connect();
            verify(channelExecMock).disconnect();
            verify(sessionMock).disconnect();
        }
    }

    @Test
    public void copyFromRemoteToLocal() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSch()) {
            prepareChannel();
            prepareInputStream();
            when(channelExecMock.getOutputStream()).thenReturn(outputStreamMock);
            boolean isCopied = scpCopier.copyFromRemoteToLocal();
            assertEquals(true, isCopied);

            verifyJSchWasConstructed(jschConstruction);
            verify(jschConstruction.constructed().get(0)).getSession(nullable(String.class), nullable(String.class), anyInt());
            verify(sessionMock).connect(anyInt());
            verify(sessionMock).openChannel(EXEC);
            verify(channelExecMock).setCommand(anyString());
            verify(channelExecMock).connect();
            verify(channelExecMock).disconnect();
            verify(sessionMock).disconnect();
        }
    }

    @Test
    public void copyFromRemoteToLocalWithJSchException() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSchThrowing(new JSchException("connection failure"))) {
            assertThrows(RuntimeException.class, () -> scpCopier.copyFromRemoteToLocal());
            verifyJSchWasConstructed(jschConstruction);
        }
    }

    @Test
    public void copyFromRemoteToLocalWithIOException() throws Exception {
        try (MockedConstruction<JSch> jschConstruction = mockJSch()) {
            prepareChannel();
            when(channelExecMock.getOutputStream()).thenThrow(new IOException("stream failure"));
            assertThrows(RuntimeException.class, () -> scpCopier.copyFromRemoteToLocal());
            verifyJSchWasConstructed(jschConstruction);
            verify(sessionMock).connect(anyInt());
        }
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

        try (MockedStatic<File> mockedFile = mockStatic(File.class)) {
            mockedFile.when(() -> File.createTempFile("SCPCopy", ".tmp")).thenReturn(tempFileMock);
            when(tempFileMock.getCanonicalPath()).thenReturn("C:\\myTestFolder");

            boolean isCopied = scpCopier.copyFromRemoteToRemote();
            verify(tempFileMock).delete();

            assertEquals(true, isCopied);
        }
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

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> scpCopier.establishKnownHostsConfiguration(knownHostsFileMock, jSchMock, sessionMock));
        assertEquals(ABSOLUTE_KNOWN_HOSTS_FILE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    public void establishKnownHostsConfigurationAddUnknownPolicy() throws IOException, JSchException, URISyntaxException {
        RemoteSecureCopyInputs remoteSecureCopyInputs = getRemoteSecureCopyInputs();
        scpCopier = new SCPCopier(remoteSecureCopyInputs);
        when(knownHostsFileMock.getPath()).thenReturn(pathMock);
        when(knownHostsFileMock.getPolicy()).thenReturn(StringUtils.EMPTY_STRING);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> scpCopier.establishKnownHostsConfiguration(knownHostsFileMock, jSchMock, sessionMock));
        assertEquals(UNKNOWN_KNOWN_HOSTS_FILE_POLICY, exception.getMessage());

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

    private MockedConstruction<JSch> mockJSch() {
        return mockConstruction(JSch.class,
                (jsch, context) -> when(jsch.getSession(nullable(String.class), nullable(String.class), anyInt())).thenReturn(sessionMock));
    }

    private MockedConstruction<JSch> mockJSchThrowing(JSchException exception) {
        return mockConstruction(JSch.class,
                (jsch, context) -> when(jsch.getSession(nullable(String.class), nullable(String.class), anyInt())).thenThrow(exception));
    }

    private void prepareChannel() throws Exception {
        when(sessionMock.openChannel(EXEC)).thenReturn(channelExecMock);
    }

    private void prepareInputStream() throws IOException {
        when(channelExecMock.getInputStream()).thenReturn(inputStreamMock);
    }

    private void verifyJSchWasConstructed(MockedConstruction<JSch> construction) {
        assertEquals(1, construction.constructed().size());
    }

}