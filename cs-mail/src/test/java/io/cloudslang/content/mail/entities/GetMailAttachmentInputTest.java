package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.utils.Constants;
import org.junit.Before;
import org.junit.Test;

import static io.cloudslang.content.mail.utils.Constants.Inputs.*;
import static io.cloudslang.content.mail.utils.Constants.SMTPProps.PASSWORD;

public class GetMailAttachmentInputTest {
    private GetMailAttachmentInput.Builder inputBuilder;

    @Before
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname(Constants.Props.HOST);
        inputBuilder.port(Constants.POPProps.POP3_PORT);
        inputBuilder.protocol(Constants.POPProps.POP3);
        inputBuilder.username(USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(FOLDER);
        inputBuilder.messageNumber(MESSAGE_NUMBER);
    }

    @Test(expected = Exception.class)
    public void executeMessageNumberLessThanOneThrowsException() throws Exception {
        inputBuilder.messageNumber("0");

        inputBuilder.build();
    }
}
