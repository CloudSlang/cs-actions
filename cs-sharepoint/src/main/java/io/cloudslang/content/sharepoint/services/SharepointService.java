package io.cloudslang.content.sharepoint.services;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Constants.EXCEPTION;
import static io.cloudslang.content.sharepoint.utils.Constants.NEGATIVE_RETURN_CODE;
import static io.cloudslang.content.sharepoint.utils.Outputs.STATUS_CODE;

public class SharepointService {
    public static void processHttpResult(Map<String, String> httpResults) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION)))
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
            httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        }
    }
}
