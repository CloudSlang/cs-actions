

package io.cloudslang.content.cyberark.utils;

import java.util.Map;

import static io.cloudslang.content.cyberark.utils.Constants.CommonConstants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CyberarkUtils {

    public static void processHttpResult(Map<String, String> httpResults) {

        httpResults.remove("responseHeaders");
        httpResults.remove("reasonPhrase");
        httpResults.remove("finalLocation");
        httpResults.remove("protocolVersion");

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            httpResults.put(RETURN_CODE, "-1");
            httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
        }
    }

    public static String getQueryParamsString(Map<String, String> queryParams) {

        String result = EMPTY;

        for (String key : queryParams.keySet())
            if (!StringUtils.isEmpty(queryParams.get(key)))
                result += key + "=" + queryParams.get(key) + "&";

        return StringUtils.isEmpty(result) ? result : (result.substring(0, result.length() - 1));
    }

    public static void validateProtocol(String protocol) throws Exception {
        if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))
            throw new Exception("Invalid protocol: " + protocol);
    }
}
