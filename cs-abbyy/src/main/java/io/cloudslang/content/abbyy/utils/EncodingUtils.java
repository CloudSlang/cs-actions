/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abbyy.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class EncodingUtils {

    static final String BOM_CHAR = "\uFEFF";
    static final String FALLBACK_CHAR = "\uFFFD";
    static final String POTENTIAL_MALICIOUS_CHARS = "?!@./#%^*()_=~+{}";


    private EncodingUtils() {

    }


    public static String discardBOMChar(@NotNull String str) {
        if (str.startsWith(BOM_CHAR)) {
            return str.substring(1);
        }
        if (str.startsWith(StringUtils.repeat(FALLBACK_CHAR, 3))) {
            return str.substring(3);
        }
        return str;
    }


    public static String toUTF8(@NotNull String str, @NotNull String crtEncoding) throws UnsupportedEncodingException {
        return new String(str.getBytes(crtEncoding), StandardCharsets.UTF_8);
    }


    public static String escapePotentialMaliciousCharsInTxt(@NotNull String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (POTENTIAL_MALICIOUS_CHARS.contains(String.valueOf(ch))) {
                sb.append('%').append(Integer.toHexString(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }


    public static String escapePotentialMaliciousCharsInXml(@NotNull String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isInsideElement = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == '<') {
                sb.append(ch);
                isInsideElement = true;
            } else if (ch == '>') {
                sb.append(ch);
                isInsideElement = false;
            } else if (isInsideElement) {
                sb.append(ch);
            } else if (POTENTIAL_MALICIOUS_CHARS.contains(String.valueOf(ch))) {
                sb.append('%').append(Integer.toHexString(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
