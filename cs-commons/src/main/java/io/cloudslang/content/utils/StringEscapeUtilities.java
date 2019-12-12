/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of apaches StringEscapeUtils, including escapeChar and escapeChars methods.
 * Created by victor on 01.09.2016.
 */
public final class StringEscapeUtilities extends StringEscapeUtils {

    private StringEscapeUtilities() {
    }

    /**
     * Escapes all the occurrences of the <toEscape> character from the <string> if it's not escaped already
     *
     * @param string   the string from which to escape the character
     * @param toEscape the character to escape
     * @return a new string with the escaped <toEscape> character
     */
    @NotNull
    public static String escapeChar(@NotNull final String string, final char toEscape) {
        final String toEscapeStr = String.valueOf(toEscape);
        return string.replaceAll("\\\\" + toEscapeStr, toEscapeStr).replaceAll(toEscapeStr, "\\\\" + toEscapeStr);
    }

    /**
     * Unescape all the occurrences of the <toUnescape> character from the <string>
     *
     * @param string     the string from which to unescape the character
     * @param toUnescape the character to unescape
     * @return a new string with the unescaped <toUnescape> character
     */
    @NotNull
    public static String unescapeChar(@NotNull final String string, final char toUnescape) {
        final String toUnescapeStr = String.valueOf(toUnescape);
        return string.replaceAll("\\\\" + toUnescapeStr, toUnescapeStr);
    }

    /**
     * Escapes all the occurrences of the <toEscape> characters from the <string> if they are not escaped already
     *
     * @param string   the string from which to escape the characters
     * @param toEscape the characters to escape as Array
     * @return a new string with the escaped <toEscape> characters
     */
    @NotNull
    public static String escapeChars(@NotNull final String string, final char[] toEscape) {
        String toReturn = string;
        for (char character : toEscape) {
            toReturn = escapeChar(toReturn, character);
        }
        return toReturn;
    }

    /**
     * Unscape all the occurrences of the <toUnescape> characters from the <string>
     *
     * @param string     the string from which to unescape the characters
     * @param toUnescape the characters to unescape as Array
     * @return a new string with the unescaped <toUnescape> characters
     */
    @NotNull
    public static String unescapeChars(@NotNull final String string, final char[] toUnescape) {
        String toReturn = string;
        for (char character : toUnescape) {
            toReturn = unescapeChar(toReturn, character);
        }
        return toReturn;
    }

    /**
     * Removes all the occurrences of the \<toRemove> characters from the <string>
     *
     * @param string   the string from which to remove the character
     * @param toRemove the \character to remove from the <string>
     * @return a new string with the removed \<toRemove> character
     */
    @NotNull
    public static String removeEscapedChar(@NotNull final String string, final char toRemove) {
        return string.replaceAll("\\\\" + toRemove, "");
    }

    /**
     * Removes all the occurrences of the \<toRemove> characters from the <string>
     *
     * @param string   the string from which to remove the characters
     * @param toRemove the \character to remove from the <string>
     * @return a new string with the removed \<toRemove> characters
     */
    @NotNull
    public static String removeEscapedChars(@NotNull final String string, final char[] toRemove) {
        String toReturn = string;
        for (char character : toRemove) {
            toReturn = removeEscapedChar(toReturn, character);
        }
        return toReturn;
    }
}
