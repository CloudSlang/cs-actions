package io.cloudslang.content.sharepoint.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Utils {
    public static String getQueryParamsString(Map<String, String> queryParams) {

        String result = EMPTY;

        for (String key : queryParams.keySet())
            if (!StringUtils.isEmpty(queryParams.get(key)))
                result += key + "=" + queryParams.get(key) + "&";

        return StringUtils.isEmpty(result) ? result : (result.substring(0, result.length() - 1));
    }
}
