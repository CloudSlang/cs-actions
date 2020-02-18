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

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.constants.ImapPropNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static io.cloudslang.content.mail.constants.Constants.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessageInput.class, GetMailMessageInput.Builder.class, System.class})
public class GetMailMessageInputTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private GetMailMessageInput.Builder inputsBuilder;


    @Before
    public void setUp() {
        inputsBuilder = getPopulatedInputsBuilder();
    }


    @After
    public void tearDown() {
        inputsBuilder = null;
    }


    @Test
    public void testProcessInputHostNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.HOST_NOT_SPECIFIED);
        inputsBuilder.hostname(null);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputHostEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.HOST_NOT_SPECIFIED);
        inputsBuilder.hostname(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test(expected = Exception.class)
    public void testProcessInputUsernameNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
        inputsBuilder.username(null);

        inputsBuilder.build();
    }


    @Test(expected = Exception.class)
    public void testProcessInputUsernameEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
        inputsBuilder.username(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPasswordEmpty() throws Exception {
        inputsBuilder.password(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test()
    public void testProcessInputPasswordNull() throws Exception {
        inputsBuilder.password(null);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputMessageNumberEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
        inputsBuilder.messageNumber(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputMessageNumberNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
        inputsBuilder.messageNumber(null);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputMessageNumberZero() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1);
        inputsBuilder.messageNumber("0");

        inputsBuilder.build();
    }

    @Test
    public void testProcessInputProtocolNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
        inputsBuilder.protocol(null);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputProtocolEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
        inputsBuilder.protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
        inputsBuilder.port(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
        inputsBuilder.port(null);

        inputsBuilder.build();
    }

    @Test
    public void testProcessInputPortProtocolNull() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
        inputsBuilder.port(null)
                .protocol(null);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortProtocolEmpty() throws Exception {
        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputProtocolNullPortImap() throws Exception {
        inputsBuilder.port(ImapPropNames.PORT)
                .protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputProtocolNullPortPop3() throws Exception {
        inputsBuilder.port(PopPropNames.POP3_PORT)
                .protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolImap() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(ImapPropNames.IMAP);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolImap4() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(ImapPropNames.IMAP4);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolPop3() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(PopPropNames.POP3);

        inputsBuilder.build();
    }


    private GetMailMessageInput.Builder getPopulatedInputsBuilder() {
        return new GetMailMessageInput.Builder()
                .hostname("test")
                .port("8080")
                .protocol("test")
                .username("test")
                .password("test")
                .folder("test")
                .trustAllRoots("test")
                .messageNumber("3")
                .subjectOnly("test")
                .enableSSL("test")
                .keystore("test")
                .keystorePassword("test")
                .trustKeystore("test")
                .trustPassword("test")
                .characterSet("test")
                .deleteUponRetrieval("test");
    }
}
