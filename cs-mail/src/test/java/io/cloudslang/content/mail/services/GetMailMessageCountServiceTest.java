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


package io.cloudslang.content.mail.services;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.Folder;
import jakarta.mail.Store;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMailMessageCountServiceTest {

    @Spy
    private GetMailMessageCountService serviceSpy = new GetMailMessageCountService();
    @Mock
    private Folder folderMock;
    @Mock
    private Store storeMock;
    private GetMailMessageCountInput.Builder inputBuilder;

    @BeforeEach
    public void setUp() {
        inputBuilder = new GetMailMessageCountInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(Constants.POP3_PORT);
        inputBuilder.protocol(Constants.POP3);
        inputBuilder.username("username");
        inputBuilder.password("password");
        inputBuilder.folder("folder");
    }

    @Test
    public void testExecute() throws Exception {
        doReturn(3).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        doNothing().when(folderMock).open(anyInt());
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doNothing().when(storeMock).connect("host", "username", "password");
        try (MockedStatic<SSLUtils> sslUtils = Mockito.mockStatic(SSLUtils.class)) {
            sslUtils.when(() -> SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
            sslUtils.when(() -> SSLUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
            sslUtils.when(() -> SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            sslUtils.when(() -> SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            sslUtils.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenAnswer(invocation -> null);
            inputBuilder.enableTLS(String.valueOf(true));

            Map<String, String> results = serviceSpy.execute(inputBuilder.build());

            assertEquals("3", results.get(OutputNames.RETURN_RESULT));
        }
    }

}
