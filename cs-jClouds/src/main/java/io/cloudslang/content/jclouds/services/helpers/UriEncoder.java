package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class UriEncoder {
    private static final Charset CHARSET_UTF_8 = Charset.forName(Constants.Miscellaneous.ENCODING);

    private UriEncoder() {
        // no instances
    }

    /**
     * Hexadecimal digits for escaping.
     */
    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Unreserved characters according to RFC 3986.
     */
    private static final char[] markChars = {'-', '_', '.', '~'};
    private static final char[] unreservedUserInfoChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':'};
    private static final char[] unreservedPathSegmentChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@'};
    private static final char[] unreservedMatrixChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ':', '@'};
    private static final char[] unreservedPathChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@', '/'};
    private static final char[] unreservedUriChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@', '/', '?', '#', '[', ']'};
    private static final char[] unreservedUriTemplateChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@', '/', '{', '}'};
    private static final char[] unreservedFragmentChars = unreservedUriChars;
    private static final char[] unreservedQueryParamChars = {'!', '$', '\'', '(', ')', '*', '+', ',', ';', ':', '@', '/', '?'};
    private static final char[] unreservedQueryChars = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '=', ':', '@', '/', '?'};

    /**
     * Unreserved characters according to RFC 3986. Each character below ASCII 128
     * has single array item with true if it is unreserved and false if it is reserved.
     */
    private static final boolean[] unreserved = new boolean[128];
    private static final boolean[] unreservedUserInfo = new boolean[128];
    private static final boolean[] unreservedPathSegment = new boolean[128];
    private static final boolean[] unreservedMatrix = new boolean[128];
    private static final boolean[] unreservedPath = new boolean[128];
    private static final boolean[] unreservedUri = new boolean[128];
    private static final boolean[] unreservedUriTemplate = new boolean[128];
    private static final boolean[] unreservedQuery = new boolean[128];
    private static final boolean[] unreservedQueryParam = new boolean[128];
    private static final boolean[] unreservedFragment = new boolean[128];

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
        System.arraycopy(unreserved, 0, unreservedUserInfo, 0, unreserved.length);
        for (char uChar : unreservedUserInfoChars) {
            unreservedUserInfo[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedPathSegment, 0, unreserved.length);
        for (char uChar : unreservedPathSegmentChars) {
            unreservedPathSegment[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedMatrix, 0, unreserved.length);
        for (char uChar : unreservedMatrixChars) {
            unreservedMatrix[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedPath, 0, unreserved.length);
        for (char uChar : unreservedPathChars) {
            unreservedPath[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedUri, 0, unreserved.length);
        for (char uChar : unreservedUriChars) {
            unreservedUri[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedUriTemplate, 0, unreserved.length);
        for (char uChar : unreservedUriTemplateChars) {
            unreservedUriTemplate[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedQuery, 0, unreserved.length);
        for (char uChar : unreservedQueryChars) {
            unreservedQuery[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedQueryParam, 0, unreserved.length);
        for (char uChar : unreservedQueryParamChars) {
            unreservedQueryParam[uChar] = true;
        }
        System.arraycopy(unreserved, 0, unreservedFragment, 0, unreserved.length);
        for (char uChar : unreservedFragmentChars) {
            unreservedFragment[uChar] = true;
        }
    }

    /**
     * Encode all characters other than unreserved according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param string string to encode
     * @return encoded US-ASCII string
     */
    public static String escapeString(String string) {
        return escapeString(string, false);
    }

    /**
     * Encode all characters other than unreserved according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param relax  see {@link #escapeString(String, boolean, boolean[])}
     * @param string string to encode
     * @return encoded US-ASCII string
     */
    public static String escapeString(String string, boolean relax) {
        return escapeString(string, relax, unreserved);
    }

    /**
     * Encode user info according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param userInfo the user info to encode
     * @param relax    see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded user info string
     */
    public static String escapeUserInfo(String userInfo, boolean relax) {
        return escapeString(userInfo, relax, unreservedUserInfo);
    }

    /**
     * Encode a path segment (without matrix parameters) according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param segment the segment (without matrix parameters) to encode
     * @param relax   see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded segment string
     */
    public static String escapePathSegment(String segment, boolean relax) {
        return escapeString(segment, relax, unreservedPathSegment);
    }

    /**
     * Encode a matrix parameter (name or value) according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param matrix the matrix parameter (name or value) to encode
     * @param relax  see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded matrix string
     */
    public static String escapeMatrix(String matrix, boolean relax) {
        return escapeString(matrix, relax, unreservedMatrix);
    }

    /**
     * Encode a complete path string according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param path  the path string to encode
     * @param relax see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded path string
     */
    public static String escapePath(String path, boolean relax) {
        return escapeString(path, relax, unreservedPath);
    }

    /**
     * Encode a query parameter (name or value) according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param queryParam the query parameter string to encode
     * @param relax      see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded query parameter string
     */
    public static String escapeQueryParam(String queryParam, boolean relax) {
        return escapeString(queryParam, relax, unreservedQueryParam);
    }

    /**
     * Encode a complete query string according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param query the query string to encode
     * @param relax see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded query string
     */
    public static String escapeQuery(String query, boolean relax) {
        return escapeString(query, relax, unreservedQuery);
    }

    /**
     * Encode a fragment string according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param fragment the fragment string to encode
     * @param relax    see {@link #escapeString(String, boolean, boolean[])}
     * @return encoded fragment string
     */
    public static String escapeFragment(String fragment, boolean relax) {
        return escapeString(fragment, relax, unreservedFragment);
    }

    /**
     * Encode a uri according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
     * escaping all reserved characters.
     *
     * @param uri   string to encode
     * @param relax if true, then any sequence of chars in the input of the form '%XX', where XX are
     *              two HEX digits, will not be encoded.
     * @return encoded US-ASCII string
     */
    public static String escapeUri(String uri, boolean relax) {
        return escapeString(uri, relax, unreservedUri);
    }

    /**
     * Encode a uri template according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
     * escaping all reserved characters, except fot '{' and '}'.
     *
     * @param uriTemplate template to encode
     * @param relax       if true, then any sequence of chars in the input of the form '%XX', where XX are
     *                    two HEX digits, will not be encoded.
     * @return encoded US-ASCII string
     */
    public static String escapeUriTemplate(String uriTemplate, boolean relax) {
        return escapeString(uriTemplate, relax, unreservedUriTemplate);
    }

    /**
     * Encode a string according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
     * escaping all characters that don't exist in the provided unreserved char array.
     *
     * @param string          string to encode
     * @param relax           if true, then any sequence of chars in the input string that have the form '%XX', where XX are
     *                        two HEX digits, will not be encoded.
     * @param unreservedChars an array of chars that indicates which characters are considered unreserved.
     *                        unreserved chars are not encoded
     * @return encoded US-ASCII string
     */
    public static String escapeString(String string, boolean relax, char[] unreservedChars) {

        boolean[] unreservedBooleans = new boolean[128];
        System.arraycopy(unreserved, 0, unreservedBooleans, 0, unreservedBooleans.length);

        for (int i = 0; i < unreservedChars.length; i++) {
            unreservedBooleans[unreservedChars[i]] = true;
        }
        return escapeString(string, relax, unreservedBooleans);
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
    public static String escapeString(String string, boolean relax, boolean[] unreserved) {
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

    /**
     * Determines if the input string contains any invalid URI characters that require encoding
     *
     * @param uri the string to test
     * @return true if the the input string contains only valid URI characters
     */
    public static boolean needsEncoding(String uri) {
        return needsEncoding(uri, true, unreservedUri);
    }

    /**
     * Decode US-ASCII uri according to <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>.
     *
     * @param string US-ASCII uri to decode
     * @return decoded uri
     */
    public static String unescapeString(String string) {
        if (string == null) {
            return null;
        }

        if (!needsDecoding(string)) {
            return string;
        }

        int len = string.length();
        // Prepare byte buffer
        ByteBuffer buffer = ByteBuffer.allocate(len);
        // decode string into byte buffer
        for (int i = 0; i < len; ++i) {
            char c = string.charAt(i);
            if (c == '%' && (i + 2 < len)) {
                int v = 0;
                int d1 = decodeHexDigit(string.charAt(i + 1));
                int d2 = decodeHexDigit(string.charAt(i + 2));
                if (d1 >= 0 && d2 >= 0) {
                    v = d1;
                    v = v << 4 | d2;
                    buffer.put((byte) v);
                    i += 2;
                } else {
                    buffer.put((byte) c);
                }
            } else {
                buffer.put((byte) c);
            }
        }
        // Decode byte buffer from UTF-8
        buffer.flip();
        return CHARSET_UTF_8.decode(buffer).toString();
    }

    private static int decodeHexDigit(char c) {
        // Decode single hexadecimal digit. On error returns 0 (ignores errors).
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        } else if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        } else {
            return 0;
        }
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

    private static boolean needsDecoding(String s) {
        return s.indexOf('%') != -1;
    }
}