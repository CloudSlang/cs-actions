

package io.cloudslang.content.cyberark.utils;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class CyberarkUtils {

    public static void removeUnusedHttpResults(Map<String, String> httpResults) {
        httpResults.remove("responseHeaders");
        httpResults.remove("reasonPhrase");
        httpResults.remove("finalLocation");
        httpResults.remove("protocolVersion");
    }

    public static String getQueryParamsString(Map<String, String> queryParams) {

        String result = EMPTY;

        for (String key : queryParams.keySet())
            if (!StringUtils.isEmpty(queryParams.get(key)))
                result += key + "=" + queryParams.get(key) + "&";

        return StringUtils.isEmpty(result) ? result : (result.substring(0, result.length() - 1));
    }

    public static void validateProtocol(String protocol) throws Exception {
        if (protocol.toLowerCase() != "http" && protocol.toLowerCase() != "https")
            throw new Exception("Invalid protocol: " + protocol);
    }
}
