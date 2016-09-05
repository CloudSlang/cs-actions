package io.cloudslang.content.utils;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringEscapeUtils;

/** An extension of apaches StringEscapeUtils, including escapeChar and escapeChars methods.
 * Created by victor on 01.09.2016.
 */
public final class StringEscapeUtilities extends StringEscapeUtils {

    private StringEscapeUtilities() {}

    /** Escapes all the occurrences of the <toEscape> character from the <string> if it's not escaped already
     * @param string the string from which to escape the character
     * @param toEscape the character to escape
     * @return a new string with the escaped <toEscape> character
     */
    @NotNull
    public static String escapeChar(@NotNull final String string, final char toEscape) {
        final String toEscapeStr = String.valueOf(toEscape);
        return string.replaceAll("\\\\" + toEscapeStr, toEscapeStr).replaceAll(toEscapeStr, "\\\\" + toEscapeStr);
    }

    /** Escapes all the occurrences of the <toEscape> characters from the <string> if they are not escaped already
     * @param string the string from which to escape the characters
     * @param toEscape the characters to escape as Array
     * @return a new string with the escaped <toEscape> characters
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
