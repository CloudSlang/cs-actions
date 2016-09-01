package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by victor on 31.08.2016.
 */
public class StringUtilities {

    /**
     * @param string
     * @return
     */
    public static boolean isEmpty(@Nullable final String string) {
        return StringUtils.isEmpty(string);
    }

    /**
     * @param string
     * @return
     */
    public static boolean isBlank(@Nullable final String string) {
        return StringUtils.isBlank(string);
    }

    /**
     * @param string
     * @param defaultValue
     * @return
     */
    @NotNull
    public static String defaultIfEmpty(@Nullable final String string, @NotNull final String defaultValue ) {
        return isEmpty(string) ? defaultValue : string;
    }

    /**
     * @param string
     * @param defaultValue
     * @return
     */
    @NotNull
    public static String defaultIfBlank(@Nullable final String string, @NotNull final String defaultValue ) {
        return isBlank(string) ? defaultValue : string;
    }

}
