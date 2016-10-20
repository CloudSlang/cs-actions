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
        for (char c = 'a'; c <= 'z'; c++) {
            unreserved[c] = true;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            unreserved[c] = true;
        }
        for (char c = '0'; c <= '9'; c++) {
            unreserved[c] = true;
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
            int c = buffer.get();
            if ((c == '%') && relax && (buffer.remaining() >= 2)) {
                int position = buffer.position();
                if (isHex(buffer.get(position)) && isHex(buffer.get(position + 1))) {
                    sb.append((char) c);
                    continue;
                }
            }

            if (c >= ' ' && unreserved[c]) {
                sb.append((char) c);
            } else {
                sb.append('%');
                sb.append(hexDigits[(c & 0xf0) >> 4]);
                sb.append(hexDigits[c & 0xf]);
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