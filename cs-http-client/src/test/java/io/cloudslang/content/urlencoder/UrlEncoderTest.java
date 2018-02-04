package io.cloudslang.content.urlencoder;

import io.cloudslang.content.httpclient.actions.URLEncoderAction;
import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static junit.framework.Assert.assertEquals;

public class UrlEncoderTest {
    private final URLEncoderAction u = new URLEncoderAction();

    @Test
    public void testWithUnknownEncodingCharacterSet() {
        final Map<String, String> result = u.execute("https://mywebsite/docs/english/site/mybook.do", "testCharSet");
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals("testCharSet", result.get(RETURN_RESULT));

    }

    @Test
    public void testWithEmptyCharacterSet() {
        final Map<String, String> result = u.execute("https://mywebsite/docs/english/site/mybook.do", "");
        assertEquals("https%3A%2F%2Fmywebsite%2Fdocs%2Fenglish%2Fsite%2Fmybook.do", result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));

    }

}
