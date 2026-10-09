/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.execute;

import io.cloudslang.content.dropbox.entities.inputs.CommonInputs;
import io.cloudslang.content.dropbox.entities.inputs.FolderInputs;
import io.cloudslang.content.dropbox.execute.DropboxService;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.HashMap;

import static io.cloudslang.content.dropbox.utils.InputsUtil.getHttpClientInputs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by TusaM
 * 5/31/2017.
 */
public class DropboxServiceTest {
    private HttpClientService csHttpClientMock;
    private MockedConstruction<HttpClientService> httpClientServiceConstruction;

    private DropboxService toTest;

    @BeforeEach
    public void init() throws Exception {
        httpClientServiceConstruction = mockConstruction(HttpClientService.class, (mock, context) -> {
            csHttpClientMock = mock;
            when(mock.execute(any(HttpClientInputs.class))).thenReturn(new HashMap<>());
        });
        toTest = new DropboxService();
    }

    @AfterEach
    public void closeHttpClientServiceConstruction() {
        httpClientServiceConstruction.close();
    }

    @Test
    public void testCreateFolder() throws Exception {
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
    public void testCreateFolderDeprecatedApi() throws Exception {
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
    public void testCreateFolderNoApiSpecified() throws Exception {
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
    public void testDeleteFileOrFolder() throws Exception {
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
    public void testDeleteFileOrFolderDeprecatedApi() throws Exception {
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
    public void testDeleteFileOrFolderNoApiSpecified() throws Exception {
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