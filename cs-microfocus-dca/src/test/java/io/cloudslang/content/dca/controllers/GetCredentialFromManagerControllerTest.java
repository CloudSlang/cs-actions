package io.cloudslang.content.dca.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static io.cloudslang.content.dca.controllers.GetCredentialFromManagerController.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

public class GetCredentialFromManagerControllerTest {
    private static final String INVALID_KEY = "invalid_key";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String JSON_ARRAY =
            "[{\"key\":\"username\", \"value\":\"username\"},{\"key\":\"password\", \"value\":\"password\"}]";
    private static final String JSON_ARRAY_WITHOUT_PASSWORD =
            "[{\"key\":\"username\", \"value\":\"username\"}]";
    private static final String JSON_ARRAY_WITHOUT_USERNAME =
            "[{\"key\":\"password\", \"value\":\"password\"}]";
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testGetValueFromDataArray() throws Exception {
        assertEquals(USERNAME, getValueFromDataArray(mapper.readTree(JSON_ARRAY), USERNAME));
        assertEquals(PASSWORD, getValueFromDataArray(mapper.readTree(JSON_ARRAY), PASSWORD));

        assertEquals(EMPTY, getValueFromDataArray(mapper.readTree(JSON_ARRAY), INVALID_KEY));
    }

    @Test
    public void testGetUsernameFromDataArray() throws Exception {
        assertEquals(USERNAME, getUsernameFromDataArray(mapper.readTree(JSON_ARRAY)));

        assertEquals(EMPTY, getUsernameFromDataArray(mapper.readTree(JSON_ARRAY_WITHOUT_USERNAME)));
    }

    @Test
    public void testGetPasswordFromDataArray() throws Exception {
        assertEquals(PASSWORD, getPasswordFromDataArray(mapper.readTree(JSON_ARRAY)));

        assertEquals(EMPTY, getPasswordFromDataArray(mapper.readTree(JSON_ARRAY_WITHOUT_PASSWORD)));
    }
}
