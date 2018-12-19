package io.cloudslang.content.office365.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.services.HttpClientService.*;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class HttpUtilsTest {

    private Map<String, String> initializeSuccessResult() {
        final Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, "returned result");
        result.put(RETURN_CODE, "0");
        result.put(STATUS_CODE, "200");
        return result;
    }

    private Map<String, String> initializeFailureResult() {
        final Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, "returned result");
        result.put(RETURN_CODE, "-1");
        result.put(STATUS_CODE, "401");
        return result;
    }

    @Test
    public void getOperationResultsValidTest() {
        final Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put(RETURN_RESULT, "returned result");
        expectedResult.put(RETURN_CODE, "0");
        expectedResult.put(DOCUMENT, "returned result");
        expectedResult.put(STATUS_CODE, "200");

        String returnedResult = initializeSuccessResult().get(RETURN_RESULT);
        Map<String, String> results = HttpUtils.getOperationResults(initializeSuccessResult(), returnedResult,
                returnedResult, returnedResult);

        assertThat(results, is(expectedResult));
        assertThat(results.size(), is(4));
    }

    @Test
    public void getOperationResultsInvalidTest() {
        final Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put(RETURN_RESULT, "returned result");
        expectedResult.put(RETURN_CODE, "-1");
        expectedResult.put(STATUS_CODE, "401");
        expectedResult.put(EXCEPTION, "returned result");

        String returnedResult = initializeFailureResult().get(RETURN_RESULT);
        Map<String, String> results = HttpUtils.getOperationResults(initializeFailureResult(), returnedResult,
                returnedResult, returnedResult);

        assertThat(results, is(expectedResult));
        assertThat(results.size(), is(4));
    }

    @Test
    public void getMessagesPathTest() {
        final String expectedPath = "/v1.0/users/test@test.onmicrosoft.com/messages/";
        final String path = getMessagesPath("test@test.onmicrosoft.com", "");
        assertEquals(path, expectedPath);
    }

    @Test
    public void getMessagesPathFolderTest() {
        final String expectedPath = "/v1.0/users/test@test.onmicrosoft.com/mailFolders/folderId/messages/";
        final String path = getMessagesPath("test@test.onmicrosoft.com", "", "folderId");
        assertEquals(path, expectedPath);
    }

    @Test
    public void getMessagesPathBothUserOptionsTest() {
        final String expectedPath = "/v1.0/users/userId/mailFolders/folderId/messages/";
        final String path = getMessagesPath("test@test.onmicrosoft.com", "userId", "folderId");
        assertEquals(path, expectedPath);
    }

    @Test
    public void getMessagePathTest() {
        final String expectedPath = "/v1.0/users/test@test.onmicrosoft.com/messages/messageId";
        final String path = getMessagePath("test@test.onmicrosoft.com", "", "messageId");
        assertEquals(path, expectedPath);
    }

    @Test
    public void getMessagePathFolderTest() {
        final String expectedPath = "/v1.0/users/test@test.onmicrosoft.com/mailFolders/folderId/messages/messageId";
        final String path = getMessagePath("test@test.onmicrosoft.com", "", "messageId", "folderId");
        assertEquals(path, expectedPath);
    }

    @Test
    public void getAuthHeaderTest() {
        final String expectedHeader = "Authorization:Bearer authToken";
        final String header = getAuthHeaders("authToken");
        assertEquals(header, expectedHeader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAuthHeaderNullTest() {
        getAuthHeaders(null);
    }

    @Test
    public void getQueryParamsTest() {
        String expectedQueryParams = "$select=param1,param2";
        String queryParams = getQueryParams("param1,param2");
        assertEquals(expectedQueryParams, queryParams);
    }
}
