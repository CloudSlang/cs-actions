package io.cloudslang.content.json.utils;

import com.hp.oo.sdk.content.annotations.Param;

/**
 * Created by ursan on 8/2/2016.
 */
public class InputUtils {
    private static boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static void validateBoolean(@Param(value = Constants.InputNames.INCLUDE_ROOT) String includeRootStr) throws Exception {
        if (!InputUtils.isBoolean(includeRootStr)) {
            throw new Exception(includeRootStr + " is not a valid value for Boolean");
        }
    }
}
