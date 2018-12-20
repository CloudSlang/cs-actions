/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.services;

import io.cloudslang.content.office365.entities.GetMessageInputs;
import io.cloudslang.content.office365.entities.Office365CommonInputs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmailServiceImpl.class)
public class EmailServiceImplTest {
    private final GetMessageInputs invalidGetMessageInputs = GetMessageInputs.builder()
            .messageId("")
            .folderId("")
            .oDataQuery("")
            .commonInputs(Office365CommonInputs.builder()
                    .authToken("")
                    .connectionsMaxPerRoute("")
                    .connectionsMaxTotal("")
                    .proxyHost("")
                    .proxyPort("")
                    .proxyUsername("")
                    .proxyPassword("")
                    .connectionsMaxTotal("")
                    .connectionsMaxPerRoute("")
                    .keepAlive("")
                    .responseCharacterSet("")
                    .connectTimeout("")
                    .trustAllRoots("")
                    .userId("")
                    .userPrincipalName("")
                    .x509HostnameVerifier("")
                    .trustKeystore("")
                    .trustPassword("")
                    .build())
            .build();

    @Test(expected = IllegalArgumentException.class)
    public void getMessageThrows() throws Exception {
        EmailServiceImpl.getMessage(invalidGetMessageInputs);
    }
}

