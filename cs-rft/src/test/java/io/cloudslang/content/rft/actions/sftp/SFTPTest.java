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

package io.cloudslang.content.rft.actions.sftp;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.rft.entities.sftp.SFTPCommonInputs;
import io.cloudslang.content.rft.entities.sftp.SFTPConnection;
import io.cloudslang.content.rft.services.SFTPCopier;
import io.cloudslang.content.rft.services.SFTPService;
import io.cloudslang.content.rft.utils.CacheUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SFTPTest {

    @Mock
    private SFTPCopier sftpCopierMock;
    @Mock
    private SessionResource<Map<String,SFTPConnection>> sessionResourceMock;
    @Mock
    private SFTPCommonInputs sftpCommonInputsMock;
    @Mock
    private GlobalSessionObject<Map<String,SFTPConnection>> globalSessionObjectMock;

    private SFTPDownloadFile sftpDownloadFile;
    private SFTPUploadFile sftpUploadFile;
    private SFTPGetChildren sftpGetChildren;

    @BeforeEach
    public void setUp()  {
        sftpDownloadFile = new SFTPDownloadFile();
        sftpUploadFile = new SFTPUploadFile();
        sftpGetChildren = new SFTPGetChildren();
    }

    @Test
    public void testGetFromCache(){
        SFTPService sftpService = new SFTPService();
        String sessionId = "sessionId";

        when(sftpCommonInputsMock.getGlobalSessionObject()).thenReturn(globalSessionObjectMock);
        when(sftpCommonInputsMock.getSftpCommonInputs()).thenReturn(sftpCommonInputsMock);
        when(globalSessionObjectMock.getResource()).thenReturn(sessionResourceMock);

        try (MockedStatic<CacheUtils> cacheUtils = mockStatic(CacheUtils.class)) {
            cacheUtils.when(() -> CacheUtils.getFromCache(sessionResourceMock, sessionId)).thenReturn(sftpCopierMock);
            SFTPCopier copierFromCache = sftpService.getSftpCopierFromCache(sftpCommonInputsMock, sessionId);
            assertNotNull(copierFromCache);
            assertEquals(sftpCopierMock, copierFromCache);
            copierFromCache = sftpService.getSftpCopierFromCache(sftpCommonInputsMock, "");
            assertNull(copierFromCache);
            copierFromCache = sftpService.getSftpCopierFromCache(sftpCommonInputsMock, null);
            assertNull(copierFromCache);
        }
    }

    @Test
    public void testSaveToCache(){
        SFTPService sftpService = new SFTPService();
        String sessionId = "sessionId";
        boolean savedToCache = sftpService.saveToCache(globalSessionObjectMock,sftpCopierMock,sessionId);
        assertFalse(savedToCache);

        when(sftpCopierMock.saveToCache(globalSessionObjectMock,sessionId)).thenReturn(true);
        savedToCache = sftpService.saveToCache(globalSessionObjectMock,sftpCopierMock,sessionId);
        assertTrue(savedToCache);

        final GlobalSessionObject<Map<String, SFTPConnection>> sessionParam = new GlobalSessionObject<>();
        when(sftpCopierMock.saveToCache(sessionParam,sessionId)).thenReturn(true);
        savedToCache = sftpService.saveToCache(sessionParam,sftpCopierMock,sessionId);
        assertEquals(sessionParam.getName(),"sshSessions:default-id");
        assertTrue(savedToCache);
    }
}