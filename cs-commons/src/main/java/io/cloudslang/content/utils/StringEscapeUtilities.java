package io.cloudslang.content.utils;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by victor on 01.09.2016.
 */
public class StringEscapeUtilities extends StringEscapeUtils {

    /**
     * @param string
     * @param toEscape
     * @return
     */
    @NotNull
    public static String escapeChar(@NotNull final String string, final char toEscape) {
        final String toEscapeStr = String.valueOf(toEscape);
        return string.replaceAll("\\\\" + toEscapeStr, toEscapeStr).replaceAll(toEscapeStr, "\\\\" + toEscapeStr);
    }

    /**
     * @param string
     * @param toEscape
     * @return
     */
    @NotNull
    public static String escapeChars(@NotNull final String string, final char[] toEscape) {
        String toReturn = string;
        for (char character: toEscape) {
            toReturn = escapeChar(toReturn, character);
        }
        return toReturn;
    }

}
