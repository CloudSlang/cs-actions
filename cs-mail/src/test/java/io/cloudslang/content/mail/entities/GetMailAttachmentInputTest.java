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
