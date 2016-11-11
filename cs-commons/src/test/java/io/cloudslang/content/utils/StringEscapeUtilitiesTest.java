package io.cloudslang.content.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by victor on 04.09.2016.
 */
public class StringEscapeUtilitiesTest {
    @Test
    public void removeEscapedChar() throws Exception {
        assertEquals(StringEscapeUtilities.removeEscapedChar("\\a\\bcdea\\a\\ac\\a\\a\\a\\a", 'a'), "\\bcdeac");
    }

    @Test
    public void removeEscapedChars() throws Exception {
        assertEquals(StringEscapeUtilities.removeEscapedChars("\\a\\b\\cdea\\a\\ac\\a\\a\\a\\a", "ab".toCharArray()), "\\cdeac");
    }

    @Test
    public void unescapeChar() throws Exception {
        assertEquals(StringEscapeUtilities.unescapeChar("\\a\\bcde\\a\\a\\ac\\a\\a\\a\\a", 'a'), "a\\bcdeaaacaaaa");
    }

    @Test
    public void unescapeChars() throws Exception {
        assertEquals(StringEscapeUtilities.unescapeChars("\\a\\b\\c\\d\\e\\a\\a\\a\\c\\a\\a\\a\\a", "abcde".toCharArray()), "abcdeaaacaaaa");
    }

    @Test
    public void escapeChar() throws Exception {
        assertEquals(StringEscapeUtilities.escapeChar("abcdeaaaca\\aaa", 'a'), "\\abcde\\a\\a\\ac\\a\\a\\a\\a");
    }

    @Test
    public void escapeChars() throws Exception {
        assertEquals(StringEscapeUtilities.escapeChars("abcdeaaaca\\aaa", "abde".toCharArray()), "\\a\\bc\\d\\e\\a\\a\\ac\\a\\a\\a\\a");
    }

}