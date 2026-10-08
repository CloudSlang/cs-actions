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

import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMailAttachmentServiceTest {

    @Spy
    private GetMailAttachmentService serviceSpy = new GetMailAttachmentService();
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;
    private GetMailAttachmentInput.Builder inputBuilder;

    @BeforeEach
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(Constants.POP3_PORT);
        inputBuilder.protocol(Constants.POP3);
        inputBuilder.username("testUser");
        inputBuilder.password("testPassword");
        inputBuilder.folder("INBOX");
        inputBuilder.messageNumber("1");
    }

    @Test
    public void executeMessageNumberGreaterThanFolderMessageCountThrowsException() throws Exception {
        try (MockedStatic<SSLUtils> sslUtils = Mockito.mockStatic(SSLUtils.class)) {
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(1).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        when(SSLUtils.createMessageStore(any(GetMailInput.class))).thenReturn(storeMock);
        inputBuilder.messageNumber("2");

            assertThrows(IndexOutOfBoundsException.class, () -> serviceSpy.execute(inputBuilder.build()));
        }
    }
}
