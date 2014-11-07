package com.hp.score.content.mail.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by giloan on 11/6/2014.
 */
public class GetMailMessageTest {

    public static final String RETURN_RESULT = "returnResult";
    public static final String RETURN_CODE = "returnCode";

    private GetMailMessage getMailMessage;
    private GetMailMessageInputs inputs;

    @Before
    public void setUp() throws Exception {
        getMailMessage = new GetMailMessage();
        inputs = new GetMailMessageInputs();
    }

    @After
    public void tearDown() throws Exception {
        getMailMessage = null;
        inputs = null;
    }

    @Test
    public void testOperation() throws Exception {
        inputs.setCharacterSet("");
        inputs.setDeleteUponRetrieval("false");
        inputs.setEnableSSL("false");
        inputs.setFolder("INBOX");
        inputs.setHostname("16.77.58.188");
        inputs.setMessageNumber("1");
        inputs.setPassword("B33f34t3r");
        inputs.setPort("110");
        inputs.setProtocol("pop3");
        inputs.setSubjectOnly("false");
        inputs.setTrustAllRoots("true");
        inputs.setUsername("ipv6@hpcluj.com");

        Map<String, String> result = getMailMessage.execute(inputs);
        assertEquals(GetMailMessage.SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
    }
}
