

package io.cloudslang.content.oracle.oci.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.services.HttpClientService.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.ZERO;
import static io.cloudslang.content.oracle.oci.utils.Outputs.CommonOutputs.DOCUMENT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HttpUtilsTest {

    private static final String SUCCESS_CODE = "200";
    private static final String RETURNED_RESULT = "returned result";
    private static final String FAILURE_RETURN_CODE = "-1";
    private static final String FAILURE_CODE = "401";

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


}
