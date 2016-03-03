package io.cloudslang.content.xml.utils;

import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class ResultUtils {
    public static void populateFailureResult(Map<String, String> result, String returnResult){
        populateResult(result, Constants.FAILURE, returnResult, Constants.EMPTY_STRING);
    }

    public static void populateSuccessResult(Map<String, String> result, String returnResult, String resultXml){
        populateResult(result, Constants.SUCCESS, returnResult, resultXml);
    }

    private static void populateResult(Map<String, String> result, String resultText, String returnResult, String resultXml) {
        result.put(Constants.OutputNames.RESULT_TEXT, resultText);
        result.put(Constants.OutputNames.RETURN_RESULT, returnResult);
        result.put(Constants.OutputNames.RESULT_XML, resultXml);
    }
}
