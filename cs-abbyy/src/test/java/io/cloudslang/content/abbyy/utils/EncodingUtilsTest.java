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

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EncodingUtilsTest {
    @Test
    public void discardBOMChar_strStartsWithBOMChar_BOMCharDiscarded() {
        // Arrange
        final String suffix = "qwerty";
        final String str = EncodingUtils.BOM_CHAR + suffix;
        // Act
        String result = EncodingUtils.discardBOMChar(str);
        // Assert
        assertEquals(suffix, result);
    }


    @Test
    public void discardBOMChar_strStartsWithFallbackChar_FallbackCharsDiscarded() {
        // Arrange
        final String suffix = "qwerty";
        final String str = EncodingUtils.FALLBACK_CHAR + EncodingUtils.FALLBACK_CHAR + EncodingUtils.FALLBACK_CHAR + suffix;
        // Act
        String result = EncodingUtils.discardBOMChar(str);
        // Assert
        assertEquals(suffix, result);
    }


    @Test
    public void discardBOMChar_strIsOk_sameStringReturned() {
        // Arrange
        final String str = "qwerty";
        // Act
        String result = EncodingUtils.discardBOMChar(str);
        // Assert
        assertEquals(str, result);
    }


    @Test
    public void toUTF8_crtEncodingIsUTF8_equalStringReturned() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwerty";
        final String crtEncoding = StandardCharsets.UTF_8.displayName();
        // Act
        String result = EncodingUtils.toUTF8(str, crtEncoding);
        // Assert
        assertEquals(str, result);
    }


    @Test
    public void toUTF8_crtEncodingIsSubsetOfUTF8AndStrDoesNotContainUTF8Chars_equalStringReturned() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwerty";
        final String crtEncoding = StandardCharsets.US_ASCII.displayName();
        // Act
        String result = EncodingUtils.toUTF8(str, crtEncoding);
        // Assert
        assertEquals(str, result);
    }


    @Test
    public void toUTF8_crtEncodingIsSubsetOfUTF8AndStrContainsUTF8Chars_fallbackCharsDisplayed() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwertyță";
        final String expectedStr = "qwerty??";
        final String crtEncoding = StandardCharsets.US_ASCII.displayName();
        // Act
        String result = EncodingUtils.toUTF8(str, crtEncoding);
        // Assert
        assertEquals(expectedStr, result);
    }


    @Test
    public void toUTF8_crtEncodingIsSupersetOfUTF8AndStrDoesNotContainsSupersetChars_equalStringReturned() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwer";
        final String crtEncoding = StandardCharsets.UTF_16LE.displayName();
        // Act
        String result = EncodingUtils.toUTF8(str, crtEncoding);
        // Assert
        assertEquals(str.charAt(0), result.charAt(0));
        assertEquals(str.charAt(1), result.charAt(2));
        assertEquals(str.charAt(2), result.charAt(4));
        assertEquals(str.charAt(3), result.charAt(6));
    }


    @Test
    public void toUTF8_crtEncodingIsSupersetOfUTF8AndStrContainsSupersetChars_fallbackCharsDisplayed() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwertyță";
        final String crtEncoding = StandardCharsets.UTF_16.displayName();
        // Act
        String result = EncodingUtils.toUTF8(str, crtEncoding);
        // Assert
        assertTrue(result.contains("�"));
    }


    @Test
    public void escapePotentialMaliciousCharsInTxt_strDoesNotContainMaliciousChars_equalStrReturned() throws UnsupportedEncodingException {
        // Arrange
        final String str = "qwerty";
        // Act
        String result = EncodingUtils.escapePotentialMaliciousCharsInTxt(str);
        // Assert
        assertEquals(str, result);
    }


    @Test
    public void escapePotentialMaliciousCharsInTxt_strDoesContainMaliciousChars_maliciousCharsEscaped() throws UnsupportedEncodingException {
        // Arrange
        final String str = EncodingUtils.POTENTIAL_MALICIOUS_CHARS;
        final String expectedStr = "%3f%21%40%2e%2f%23%25%5e%2a%28%29%5f%3d%7e%2b%7b%7d";
        // Act
        String result = EncodingUtils.escapePotentialMaliciousCharsInTxt(str);
        // Assert
        assertEquals(expectedStr, result);
    }


    @Test
    public void escapePotentialMaliciousCharsInXml_strDoesNotContainMaliciousChars_equalStrReturned() throws UnsupportedEncodingException {
        // Arrange
        final String str = "<xml>qwerty</xml>";
        // Act
        String result = EncodingUtils.escapePotentialMaliciousCharsInXml(str);
        // Assert
        assertEquals(str, result);
    }


    @Test
    public void escapePotentialMaliciousCharsInXml_strDoesContainMaliciousChars_maliciousCharsEscaped() throws UnsupportedEncodingException {
        // Arrange
        final String str = "<" + EncodingUtils.POTENTIAL_MALICIOUS_CHARS + ">" + EncodingUtils.POTENTIAL_MALICIOUS_CHARS;
        final String expectedStr = "<" + EncodingUtils.POTENTIAL_MALICIOUS_CHARS + ">%3f%21%40%2e%2f%23%25%5e%2a%28%29%5f%3d%7e%2b%7b%7d";
        // Act
        String result = EncodingUtils.escapePotentialMaliciousCharsInXml(str);
        // Assert
        assertEquals(expectedStr, result);
    }
}
