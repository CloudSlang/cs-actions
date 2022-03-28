package io.cloudslang.content.httpclient.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.httpclient.utils.Outputs.HTTPClientOutputs.STATUS_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpUtils {

    @NotNull
    public static Map<String, String> getOperationResults(@NotNull final Map<String, String> result,
                                                          final String successMessage,
                                                          final String failureMessage) {
        final Map<String, String> results;
        final String statusCode = result.get(STATUS_CODE);


        //Validation for empty status code
        if (statusCode.equals(EMPTY)) {

            results = getFailureResultsMap(failureMessage);

            if (result.get(EXCEPTION) != null)
                results.put(EXCEPTION, result.get(EXCEPTION));

            return results;
        }

        if (Integer.parseInt(statusCode) >= 200 && Integer.parseInt(statusCode) < 300)
            results = getSuccessResultsMap(successMessage);
        else
            results = getFailureResultsMap(failureMessage);

        results.put(STATUS_CODE, statusCode);

        if (result.get(EXCEPTION) != null)
            results.put(EXCEPTION, result.get(EXCEPTION));

        return results;
    }

    @NotNull
    public static void parseApiExceptionMessage(@NotNull final Map<String, String> result) {

        String exception = result.get(EXCEPTION);

        //to do
    }
}
