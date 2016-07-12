package io.cloudslang.content.xml.utils;

import io.cloudslang.content.xml.entities.Constants;

import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class ResultUtils {
    private ResultUtils() {}

    public static void populateFailureResult(Map<String, String> result, String errorMessage) {
        populateResult(result, Constants.FAILURE, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.Outputs.RESULT_XML, Constants.ReturnCodes.FAILURE, errorMessage);
    }

    public static void populateSuccessResult(Map<String, String> result, String returnResult, String resultXml) {
        populateResult(result, Constants.SUCCESS, returnResult, resultXml, Constants.Outputs.RESULT_XML, Constants.ReturnCodes.SUCCESS, Constants.EMPTY_STRING);
    }

    public static void populateValueResult(Map<String, String> result, String resultText, String returnResult, String selectedValue, String returnCode) {
        populateResult(result, resultText, returnResult, selectedValue, Constants.Outputs.SELECTED_VALUE, returnCode, Constants.EMPTY_STRING);
    }

    private static void populateResult(Map<String, String> result, String resultText,
                                       String returnResult, String resultXml, String resultKey, String returnCode, String errorMessage) {
        result.put(Constants.Outputs.RESULT_TEXT, resultText);
        result.put(Constants.Outputs.RETURN_RESULT, returnResult);
        result.put(Constants.Outputs.RETURN_CODE, returnCode);
        result.put(Constants.Outputs.ERROR_MESSAGE, errorMessage);
        result.put(resultKey, resultXml);
    }
}
