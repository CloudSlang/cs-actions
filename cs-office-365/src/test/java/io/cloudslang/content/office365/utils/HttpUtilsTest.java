/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.services.HttpClientService.*;
import static io.cloudslang.content.office365.utils.Constants.ZERO;
import static io.cloudslang.content.office365.utils.HttpUtils.*;
import static io.cloudslang.content.office365.utils.Outputs.CommonOutputs.DOCUMENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class HttpUtilsTest {

    public static final String FORMAT_JSON = "$format=json";
    public static final String GET_QUERY_PARAMS = "$select=id,name&$format=json";
    private static final String SUCCESS_CODE = "200";
    private static final String RETURNED_RESULT = "returned result";
    private static final String FAILURE_RETURN_CODE = "-1";
    private static final String FAILURE_CODE = "401";
    private static final String GET_MESSAGES_EXPECTED = "/v1.0/users/test@test.onmicrosoft.com/messages";
    private static final String USER_PRINCIPAL_NAME = "test@test.onmicrosoft.com";
    private static final String GET_MESSAGE_FOLDER_EXPECTED = "/v1.0/users/test@test.onmicrosoft.com/mailFolders/folderId/messages";
    private static final String FOLDER_ID = "folderId";
    private static final String USER_ID = "userId";
    private static final String MESSAGE_ID = "messageId";
    private static final String AUTH_TOKEN = "authToken";
    private static final String EXPECTED_QUERY_PARAMS = "$top=2&$select=id,name&$format=json";
    private static final String EXPECTED_HEADER = "Authorization:Bearer authToken";
    private static final String EXPECTED_PATH = "/v1.0/users/test@test.onmicrosoft.com/mailFolders/folderId/messages/messageId";
    private static final String EXPECTED_PATH_WITH_USER_PRINCIPAL_NAME = "/v1.0/users/test@test.onmicrosoft.com/messages/messageId";
    private static final String EXPECTED_PATH_BOTH_OPTIONS_USER = "/v1.0/users/userId/mailFolders/folderId/messages";
    private static final String TOP_QUERY = "2";
    private static final String SELECT_QUERY = "id,name";
    private static final String O_DATA_QUERY = "?$format=json";
    private static final String O_DATA_QUERY2 = "&$format=json";

    private Map<String, String> initializeSuccessResult() {
        final Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, RETURNED_RESULT);
        result.put(RETURN_CODE, ZERO);
        result.put(STATUS_CODE, SUCCESS_CODE);
        return result;
    }

    private Map<String, String> initializeFailureResult() {
        final Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, RETURNED_RESULT);
        result.put(RETURN_CODE, FAILURE_RETURN_CODE);
        result.put(STATUS_CODE, FAILURE_CODE);
        return result;
    }

    @Test
    public void getOperationResultsValidTest() {
        final Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put(RETURN_RESULT, RETURNED_RESULT);
        expectedResult.put(RETURN_CODE, ZERO);
        expectedResult.put(DOCUMENT, RETURNED_RESULT);
        expectedResult.put(STATUS_CODE, SUCCESS_CODE);

        String returnedResult = initializeSuccessResult().get(RETURN_RESULT);
        Map<String, String> results = HttpUtils.getOperationResults(initializeSuccessResult(), returnedResult,
                returnedResult, returnedResult);

        assertThat(results, is(expectedResult));
        assertThat(results.size(), is(4));
    }

    @Test
    public void getOperationResultsInvalidTest() {
        final Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put(RETURN_RESULT, RETURNED_RESULT);
        expectedResult.put(RETURN_CODE, FAILURE_RETURN_CODE);
        expectedResult.put(STATUS_CODE, FAILURE_CODE);
        expectedResult.put(EXCEPTION, RETURNED_RESULT);

        String returnedResult = initializeFailureResult().get(RETURN_RESULT);
        Map<String, String> results = HttpUtils.getOperationResults(initializeFailureResult(), returnedResult,
                returnedResult, returnedResult);

        assertThat(results, is(expectedResult));
        assertThat(results.size(), is(4));
    }

    @Test
    public void getMessagesPathTest() {
        final String path = getMessagesPath(USER_PRINCIPAL_NAME, EMPTY);
        assertEquals(GET_MESSAGES_EXPECTED, path);
    }

    @Test
    public void getMessagesPathFolderTest() {
        final String path = getMessagesPath(USER_PRINCIPAL_NAME, EMPTY, FOLDER_ID);
        assertEquals(GET_MESSAGE_FOLDER_EXPECTED, path);
    }

    @Test
    public void getMessagesPathBothUserOptionsTest() {
        final String path = getMessagesPath(USER_PRINCIPAL_NAME, USER_ID, FOLDER_ID);
        assertEquals(EXPECTED_PATH_BOTH_OPTIONS_USER, path);
    }

    @Test
    public void getMessagePathTest() {
        final String path = getMessagePath(USER_PRINCIPAL_NAME, EMPTY, MESSAGE_ID);
        assertEquals(EXPECTED_PATH_WITH_USER_PRINCIPAL_NAME, path);
    }

    @Test
    public void getMessagePathFolderTest() {
        final String path = getMessagePath(USER_PRINCIPAL_NAME, EMPTY, MESSAGE_ID, FOLDER_ID);
        assertEquals(EXPECTED_PATH, path);
    }

    @Test
    public void getAuthHeaderTest() {
        final String header = getAuthHeaders(AUTH_TOKEN);
        assertEquals(EXPECTED_HEADER, header);
    }

    @Test
    public void getQueryParamsTest2() {
        String queryParams = getQueryParams(TOP_QUERY, SELECT_QUERY, O_DATA_QUERY);
        assertEquals(EXPECTED_QUERY_PARAMS, queryParams);
    }

    @Test
    public void getQueryParamsTestTwoArgs() {
        String queryParams = getQueryParams(SELECT_QUERY, O_DATA_QUERY);
        assertEquals(GET_QUERY_PARAMS, queryParams);
    }

    @Test
    public void getQueryParamsTestTwoArgsEmptySelectQuery() {
        String queryParams = getQueryParams(EMPTY, O_DATA_QUERY);
        assertEquals(FORMAT_JSON, queryParams);
    }


    @Test
    public void getQueryParamsTestTwoArgsEmptySelectQuery2() {
        String queryParams = getQueryParams(EMPTY, O_DATA_QUERY2);
        assertEquals(FORMAT_JSON, queryParams);
    }

}
