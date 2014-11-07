package com.hp.score.content.mail.entities;

import org.junit.After;
import org.junit.Before;

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


}
