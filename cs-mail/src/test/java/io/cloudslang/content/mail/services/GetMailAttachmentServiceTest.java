/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.mail.services;

import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.GetMailBaseInput;
import io.cloudslang.content.mail.utils.MessageStoreUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageStoreUtils.class})
public class GetMailAttachmentServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Spy
    private GetMailAttachmentService serviceSpy = new GetMailAttachmentService();
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;
    private GetMailAttachmentInput.Builder inputBuilder;

    @Before
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username("testUser");
        inputBuilder.password("testPassword");
        inputBuilder.folder("INBOX");
        inputBuilder.messageNumber("1");
    }

    @Test
    public void executeMessageNumberGreaterThanFolderMessageCountThrowsException() throws Exception {
        PowerMockito.mockStatic(MessageStoreUtils.class);
        doReturn(folderMock).when(storeMock).getFolder(Matchers.anyString());
        doReturn(1).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        when(MessageStoreUtils.createMessageStore(any(GetMailBaseInput.class))).thenReturn(storeMock);
        inputBuilder.messageNumber("2");

        try {
            serviceSpy.execute(inputBuilder.build());
        } catch (Exception ex) {
            if (!(ex instanceof IndexOutOfBoundsException)) {
                fail();
            }
        }
    }
}
