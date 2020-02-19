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
package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.constants.InputNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.PropNames;
import org.junit.Before;
import org.junit.Test;

import static io.cloudslang.content.mail.constants.SmtpPropNames.PASSWORD;

public class GetMailAttachmentInputTest {
    private GetMailAttachmentInput.Builder inputBuilder;

    @Before
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname(PropNames.HOST);
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username(InputNames.USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(InputNames.FOLDER);
        inputBuilder.messageNumber(InputNames.MESSAGE_NUMBER);
    }

    @Test(expected = Exception.class)
    public void executeMessageNumberLessThanOneThrowsException() throws Exception {
        inputBuilder.messageNumber("0");

        inputBuilder.build();
    }
}
