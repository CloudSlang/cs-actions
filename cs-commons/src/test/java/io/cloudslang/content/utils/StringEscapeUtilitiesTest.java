package io.cloudslang.content.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by victor on 04.09.2016.
 */
public class StringEscapeUtilitiesTest {
    @Test
    public void escapeChar() throws Exception {
        assertEquals(StringEscapeUtilities.escapeChar("abcdeaaaca\\aaa", 'a'), "\\abcde\\a\\a\\ac\\a\\a\\a\\a");
    }

    @Test
    public void escapeChars() throws Exception {
        assertEquals(StringEscapeUtilities.escapeChars("abcdeaaaca\\aaa", "abcde".toCharArray()), "\\a\\b\\c\\d\\e\\a\\a\\a\\c\\a\\a\\a\\a");
    }

}