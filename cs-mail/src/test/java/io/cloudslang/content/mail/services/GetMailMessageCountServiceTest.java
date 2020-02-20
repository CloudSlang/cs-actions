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

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Folder;
import javax.mail.Store;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLUtils.class})
public class GetMailMessageCountServiceTest {

    @Spy
    private GetMailMessageCountService serviceSpy = new GetMailMessageCountService();
    @Mock
    private Folder folderMock;
    @Mock
    private Store storeMock;
    private GetMailMessageCountInput.Builder inputBuilder;

    @Before
    public void setUp() {
        inputBuilder = new GetMailMessageCountInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username("username");
        inputBuilder.password("password");
        inputBuilder.folder("folder");
    }

    @Test
    public void testExecute() throws Exception {
        doReturn(3).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        doNothing().when(folderMock).open(Matchers.anyInt());
        doReturn(folderMock).when(storeMock).getFolder(Matchers.anyString());
        doNothing().when(storeMock).connect();
        PowerMockito.mockStatic(SSLUtils.class);
        when(SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
        when(SSLUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
        when(SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        when(SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        PowerMockito.doNothing().when(SSLUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());
        inputBuilder.enableTLS(String.valueOf(true));

        Map<String, String> results = serviceSpy.execute(inputBuilder.build());

        assertEquals(results.get(OutputNames.RETURN_RESULT), "3");
    }

}
