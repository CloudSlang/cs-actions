package io.cloudslang.content.httpclient.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.httpclient.utils.Constants.MINUS_1;

public class ErrorHandler {
    public static Map<String, String> handleErrors(Exception e, Map<String, String> results) {
        results.put(RETURN_CODE, MINUS_1);
        try {
            results.put(RETURN_RESULT, ExceptionUtils.getRootCause(e).toString());
        } catch (NullPointerException exception) {
            results.put(RETURN_RESULT, e.getMessage());
        }
        results.put(EXCEPTION, ExceptionUtils.getStackTrace(e));
        return results;
    }
}
