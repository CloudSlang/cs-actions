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


package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.constants.ExceptionMsgs;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class GetMailMessageInputTest {

    private GetMailMessageInput.Builder inputsBuilder;


    @BeforeEach
    public void setUp() {
        inputsBuilder = getPopulatedInputsBuilder();
    }


    @AfterEach
    public void tearDown() {
        inputsBuilder = null;
    }


    @Test
    public void testProcessInputHostNull() throws Exception {
        inputsBuilder.hostname(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.HOST_NOT_SPECIFIED, exception.getMessage());
    }


    @Test
    public void testProcessInputHostEmpty() throws Exception {
        inputsBuilder.hostname(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.HOST_NOT_SPECIFIED, exception.getMessage());
    }


    @Test
    public void testProcessInputUsernameNull() throws Exception {
        inputsBuilder.username(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.USERNAME_NOT_SPECIFIED, exception.getMessage());
    }


    @Test
    public void testProcessInputUsernameEmpty() throws Exception {
        inputsBuilder.username(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.USERNAME_NOT_SPECIFIED, exception.getMessage());
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
        inputsBuilder.messageNumber(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED, exception.getMessage());
    }


    @Test
    public void testProcessInputMessageNumberNull() throws Exception {
        inputsBuilder.messageNumber(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED, exception.getMessage());
    }


    @Test
    public void testProcessInputMessageNumberZero() throws Exception {
        inputsBuilder.messageNumber("0");

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1, exception.getMessage());
    }

    @Test
    public void testProcessInputProtocolNull() throws Exception {
        inputsBuilder.protocol(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT, exception.getMessage());
    }


    @Test
    public void testProcessInputProtocolEmpty() throws Exception {
        inputsBuilder.protocol(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT, exception.getMessage());
    }


    @Test
    public void testProcessInputPortEmpty() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL, exception.getMessage());
    }


    @Test
    public void testProcessInputPortNull() throws Exception {
        inputsBuilder.port(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL, exception.getMessage());
    }

    @Test
    public void testProcessInputPortProtocolNull() throws Exception {
        inputsBuilder.port(null)
                .protocol(null);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH, exception.getMessage());
    }


    @Test
    public void testProcessInputPortProtocolEmpty() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(StringUtils.EMPTY);

        Exception exception = assertThrows(Exception.class, () -> inputsBuilder.build());
        org.junit.jupiter.api.Assertions.assertEquals(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH, exception.getMessage());
    }


    @Test
    public void testProcessInputProtocolNullPortImap() throws Exception {
        inputsBuilder.port(Constants.IMAP_PORT)
                .protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputProtocolNullPortPop3() throws Exception {
        inputsBuilder.port(Constants.POP3_PORT)
                .protocol(StringUtils.EMPTY);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolImap() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(Constants.IMAP);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolImap4() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(Constants.IMAP4);

        inputsBuilder.build();
    }


    @Test
    public void testProcessInputPortEmptyProtocolPop3() throws Exception {
        inputsBuilder.port(StringUtils.EMPTY)
                .protocol(Constants.POP3);

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
