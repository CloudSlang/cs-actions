/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.execute;

import io.cloudslang.content.dropbox.entities.inputs.CommonInputs;
import io.cloudslang.content.dropbox.entities.inputs.FolderInputs;
import io.cloudslang.content.dropbox.execute.DropboxService;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;

import static io.cloudslang.content.dropbox.utils.InputsUtil.getHttpClientInputs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by TusaM
 * 5/31/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientService.class, DropboxService.class})
public class DropboxServiceTest {
    @Mock
    private HttpClientService csHttpClientMock;

    private DropboxService toTest;

    @Before
    public void init() throws Exception {
        whenNew(HttpClientService.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(new HashMap<String, String>());
        toTest = new DropboxService();
    }

    @Test
    public void testCreateFolder() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("CreateFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("two")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withAutoRename("")
                .withCreateFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/2/files/create_folder_v2", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testCreateFolderDeprecatedApi() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("CreateFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("one")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withAutoRename("")
                .withCreateFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/1/files/create_folder", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testCreateFolderNoApiSpecified() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("CreateFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withAutoRename("")
                .withCreateFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/2/files/create_folder_v2", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testDeleteFileOrFolder() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("DeleteFileOrFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("two")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withDeleteFileOrFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/2/files/delete_v2", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
    }

    @Test
    public void testDeleteFileOrFolderDeprecatedApi() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("DeleteFileOrFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("one")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withAutoRename("")
                .withDeleteFileOrFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/1/files/delete", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
    }

    @Test
    public void testDeleteFileOrFolderNoApiSpecified() throws MalformedURLException {
        HttpClientInputs httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

        CommonInputs commonInputs = new CommonInputs.Builder()
                .withAccessToken("testToken")
                .withAction("DeleteFileOrFolder")
                .withApi("folders")
                .withEndpoint("https://api.dropboxapi.com")
                .withVersion("")
                .build();

        FolderInputs folderInputs = new FolderInputs.Builder()
                .withAutoRename("")
                .withDeleteFileOrFolderPath("/testPath")
                .build();

        toTest.execute(httpClientInputs, commonInputs, folderInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("https://api.dropboxapi.com/2/files/delete_v2", httpClientInputs.getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("\"path\":\"/testPath\""));
    }
}