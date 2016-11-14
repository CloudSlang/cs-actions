package io.cloudslang.content.xml.utils;

import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.utils.StringUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_XML;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by markowis on 03/03/2016.
 */
public class ResultUtils {
    private ResultUtils() {}

    public static void populateFailureResult(Map<String, String> result, String errorMessage) {
        populateResult(result, ResponseNames.FAILURE, Constants.EMPTY_STRING, EMPTY, RESULT_XML, FAILURE, errorMessage);
    }

    public static void populateSuccessResult(Map<String, String> result, String returnResult, String resultXml) {
        populateResult(result, ResponseNames.SUCCESS, returnResult, resultXml, RESULT_XML, SUCCESS, EMPTY);
    }

    public static void populateValueResult(Map<String, String> result, String resultText, String returnResult, String selectedValue, String returnCode) {
        populateResult(result, resultText, returnResult, selectedValue, SELECTED_VALUE, returnCode, EMPTY);
    }

    private static void populateResult(Map<String, String> result, String resultText,
                                       String returnResult, String resultXml, String resultKey, String returnCode, String errorMessage) {
        result.put(RESULT_TEXT, resultText);
        result.put(RETURN_RESULT, returnResult);
        result.put(RETURN_CODE, returnCode);
        result.put(ERROR_MESSAGE, errorMessage);
        result.put(resultKey, resultXml);
    }
}
