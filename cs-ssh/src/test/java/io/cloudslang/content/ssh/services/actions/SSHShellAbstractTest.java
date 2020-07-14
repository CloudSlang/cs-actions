/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.ssh.services.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.ssh.entities.IdentityKey;
import io.cloudslang.content.ssh.entities.KeyData;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.entities.SSHShellInputs;
import io.cloudslang.content.ssh.services.SSHService;
import io.cloudslang.content.ssh.services.impl.SSHServiceImpl;
import io.cloudslang.content.ssh.utils.CacheUtils;
import io.cloudslang.content.ssh.utils.IdentityKeyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CacheUtils.class})
public class SSHShellAbstractTest {

    @Mock
    private SSHServiceImpl sshServiceMock;
    @Mock
    private SessionResource<Map<String, SSHConnection>> sessionResourceMock;
    @Mock
    private SSHShellInputs sshShellInputsMock;
    @Mock
    private GlobalSessionObject<Map<String, SSHConnection>> sshGlobalSessionObjectMock;

    @Test
    public void testAddSecurityProvider() {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.addSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertFalse(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
    }

    @Test
    public void testRemoveSecurityProvider() {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        boolean securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
        securityProviderAdded = sshShellAbstract.addSecurityProvider();
        assertTrue(securityProviderAdded);
        sshShellAbstract.removeSecurityProvider();
    }

    @Test
    public void testGetKeyFile() {
        final String myPrivateKeyFileName = "myPrivateKeyFile";
        final String myPrivateKeyPassPhraseName = "myPrivateKeyPassPhrase";
        final String myPrivatePassword = "myPrivatePassword";
        IdentityKey keyFile = IdentityKeyUtils.getIdentityKey(myPrivateKeyFileName, null, myPrivatePassword);
        assert(keyFile instanceof KeyFile);
        assertEquals(myPrivatePassword, new String(keyFile.getPassPhrase(), StandardCharsets.UTF_8));
        assertEquals(myPrivateKeyFileName, ((KeyFile) keyFile).getKeyFilePath());

        keyFile = IdentityKeyUtils.getIdentityKey(null, myPrivateKeyPassPhraseName, myPrivatePassword);
        assert(keyFile instanceof KeyData);
        assertEquals(myPrivatePassword, new String(keyFile.getPassPhrase(), StandardCharsets.UTF_8));
        assertEquals(myPrivatePassword, new String(keyFile.getPassPhrase(), StandardCharsets.UTF_8));

        keyFile = IdentityKeyUtils.getIdentityKey(null, null, myPrivatePassword);
        assertNull(keyFile);
    }

    @Test
    public void testGetFromCache() {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        mockStatic(CacheUtils.class);
        String sessionId = "sessionId";

        when(sshShellInputsMock.getSshGlobalSessionObject()).thenReturn(sshGlobalSessionObjectMock);
        when(sshGlobalSessionObjectMock.getResource()).thenReturn(sessionResourceMock);
        when(CacheUtils.getFromCache(sessionResourceMock, sessionId)).thenReturn(sshServiceMock);
        SSHService serviceFromCache = sshShellAbstract.getFromCache(sshShellInputsMock, sessionId);
        assertNotNull(serviceFromCache);
        assertEquals(sshServiceMock, serviceFromCache);
        serviceFromCache = sshShellAbstract.getFromCache(sshShellInputsMock, "");
        assertNull(serviceFromCache);
        serviceFromCache = sshShellAbstract.getFromCache(sshShellInputsMock, null);
        assertNull(serviceFromCache);
    }

    @Test
    public void testSaveToCache() {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        String sessionId = "sessionId";
        boolean savedToCache = sshShellAbstract.saveToCache(sshGlobalSessionObjectMock, sshServiceMock, sessionId);
        assertEquals(false, savedToCache);

        when(sshServiceMock.saveToCache(sshGlobalSessionObjectMock, sessionId)).thenReturn(true);
        savedToCache = sshShellAbstract.saveToCache(sshGlobalSessionObjectMock, sshServiceMock, sessionId);
        assertEquals(true, savedToCache);

        final GlobalSessionObject<Map<String, SSHConnection>> sessionParam = new GlobalSessionObject<>();
        when(sshServiceMock.saveToCache(sessionParam, sessionId)).thenReturn(true);
        savedToCache = sshShellAbstract.saveToCache(sessionParam, sshServiceMock, sessionId);
        assertEquals(sessionParam.getName(), "sshSessions:default-id");
        assertEquals(true, savedToCache);
    }

    @Test
    public void testPopulateResult() {
        SSHShellAbstract sshShellAbstract = new SSHShellAbstract() {
        };
        Map<String, String> returnResult = new HashMap<>();
        final String testExceptionMessage = "Test exception";
        Exception testException = new Exception(testExceptionMessage);
        sshShellAbstract.populateResult(returnResult, testException);

        assertEquals(returnResult.get("returnResult"), testExceptionMessage);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        testException.printStackTrace(pw);
        pw.close();

        assertEquals(returnResult.get("exception"), sw.toString());
        assertEquals(returnResult.get("returnCode"), "-1");
    }
}