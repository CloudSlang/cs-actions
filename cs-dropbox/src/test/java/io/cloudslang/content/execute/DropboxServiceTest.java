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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

import java.util.HashMap;

import static io.cloudslang.content.dropbox.utils.InputsUtil.getHttpClientInputs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

/**
 * Created by TusaM
 * 5/31/2017.
 */
@ExtendWith(MockitoExtension.class)
public class DropboxServiceTest {
    private DropboxService toTest;
    private MockedStatic<HttpClientService> httpClientService;

    private void verifyHttpClientExecute(VerificationMode mode) {
        httpClientService.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)), mode);
    }

    @BeforeEach
    public void init() throws Exception {
        httpClientService = Mockito.mockStatic(HttpClientService.class);
        httpClientService.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                .thenReturn(new HashMap<String, String>());
        toTest = new DropboxService();
    }

    @AfterEach
    public void tearDown() {
        httpClientService.close();
    }

    @Test
    public void testCreateFolder() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/2/files/create_folder_v2", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.build().getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testCreateFolderDeprecatedApi() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/1/files/create_folder", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.build().getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testCreateFolderNoApiSpecified() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/2/files/create_folder_v2", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
        assertTrue(httpClientInputs.build().getBody().contains("\"autorename\":false"));
    }

    @Test
    public void testDeleteFileOrFolder() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/2/files/delete_v2", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
    }

    @Test
    public void testDeleteFileOrFolderDeprecatedApi() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/1/files/delete", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
    }

    @Test
    public void testDeleteFileOrFolderNoApiSpecified() throws Exception {
        HttpClientInputs.HttpClientInputsBuilder httpClientInputs = getHttpClientInputs("", "", "", "", "", "", "", "", "", "", "", "", "", "", "POST");

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

        verifyHttpClientExecute(times(1));

        assertEquals("https://api.dropboxapi.com/2/files/delete_v2", httpClientInputs.build().getUrl());
        assertEquals("Authorization:Bearer testToken", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("\"path\":\"/testPath\""));
    }
}