/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.amazon.services.helpers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.ENCODING;

class UriEncoder {
    private static final Charset CHARSET_UTF_8 = Charset.forName(ENCODING);

    private UriEncoder() {
    }

    /**
     * Hexadecimal digits for escaping.
     */
    private static final char[] hexDigits = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Unreserved characters according to RFC 3986.
     */
    private static final char[] markChars = {'-', '_', '.', '~'};

    /**
     * Unreserved characters according to RFC 3986. Each character below ASCII 128
     * has single array item with true if it is unreserved and false if it is reserved.
     */
    private static final boolean[] unreserved = new boolean[128];

    static {
        // common unreserved characters
        for (char lowLetter = 'a'; lowLetter <= 'z'; lowLetter++) {
            unreserved[lowLetter] = true;
        }
        for (char capLetter = 'A'; capLetter <= 'Z'; capLetter++) {
            unreserved[capLetter] = true;
        }
        for (char digit = '0'; digit <= '9'; digit++) {
            unreserved[digit] = true;
        }
        for (char markChar : markChars) {
            unreserved[markChar] = true;
        }
    }

    /**
     * Encode all characters other than unreserved according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param string string to encode
     * @return encoded US-ASCII string
     */
    static String escapeString(String string) {
        return escapeString(string, false);
    }

    /**
     * Encode all characters other than unreserved according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param relax  see {@link #escapeString(String, boolean, boolean[])}
     * @param string string to encode
     * @return encoded US-ASCII string
     */
    private static String escapeString(String string, boolean relax) {
        return escapeString(string, relax, unreserved);
    }

    /**
     * Encode a string according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
     * escaping all characters where <code>unreserved[char] == false</code>, where <code>char</code>
     * is a single character such as 'a'.
     *
     * @param string     string to encode
     * @param relax      if true, then any sequence of chars in the input string that have the form '%XX', where XX are
     *                   two HEX digits, will not be encoded.
     * @param unreserved an array of booleans that indicates which characters are considered unreserved.
     *                   a character is considered unreserved if <code>unreserved[char] == true</code>,
     *                   in which case it will not be escaped
     * @return encoded US-ASCII string
     */
    private static String escapeString(String string, boolean relax, boolean[] unreserved) {
        if (string == null) {
            return null;
        }

        if (!needsEncoding(string, false, unreserved)) {
            return string;
        }

        // Encode to UTF-8
        ByteBuffer buffer = CHARSET_UTF_8.encode(string);
        // Prepare string buffer
        StringBuilder sb = new StringBuilder(buffer.remaining());
        // Now encode the characters
        while (buffer.hasRemaining()) {
            int remainingBuffer = buffer.get();
            if ((remainingBuffer == '%') && relax && (buffer.remaining() >= 2)) {
                int position = buffer.position();
                if (isHex(buffer.get(position)) && isHex(buffer.get(position + 1))) {
                    sb.append((char) remainingBuffer);
                    continue;
                }
            }

            if (remainingBuffer >= ' ' && unreserved[remainingBuffer]) {
                sb.append((char) remainingBuffer);
            } else {
                sb.append('%');
                sb.append(hexDigits[(remainingBuffer & 0xf0) >> 4]);
                sb.append(hexDigits[remainingBuffer & 0xf]);
            }
        }

        return sb.toString();
    }

    private static boolean isHex(int c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private static boolean needsEncoding(String s, boolean relax, boolean[] unreserved) {
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (c == '%' && relax) {
                continue;
            }
            if (c > unreserved.length) {
                return true;
            }
            if (!unreserved[c]) {
                return true;
            }
        }
        return false;
    }
}
