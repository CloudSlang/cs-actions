package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionsValues;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by victor on 04.09.2016.
 */
public class NumberUtilitiesTest {
    @Test
    public void isValidIntTrue() throws Exception {
        assertTrue(NumberUtilities.isValidInt("12345"));
        assertTrue(NumberUtilities.isValidInt("-123"));
        assertTrue(NumberUtilities.isValidInt("0", -1, 1));
    }

    @Test
    public void isValidIntFalse() throws Exception {
        assertFalse(NumberUtilities.isValidInt("1234567898765432345678987654323456789098765432345678"));
        assertFalse(NumberUtilities.isValidInt("one"));
        assertFalse(NumberUtilities.isValidInt("0.01", -1, 1));
        assertFalse(NumberUtilities.isValidInt(null));
    }

    @Test
    public void toIntegerValid() throws Exception {
        assertEquals(NumberUtilities.toInteger("12345"), 12345);
        assertEquals(NumberUtilities.toInteger("-123"), -123);
        assertEquals(NumberUtilities.toInteger("", -1), -1);
        assertEquals(NumberUtilities.toInteger(null, -1), -1);
    }

    private void testInvalidBoundsInt(String value, int lowerBound, int upperBound) {
        try {
            NumberUtilities.isValidInt(value, lowerBound, upperBound);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), ExceptionsValues.INVALID_BOUNDS);
        }
    }


    private void testInvalidInteger(String value) {
        try {
            NumberUtilities.toInteger(value);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), value + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_INTEGER_VALUE);
        }
    }

    @Test
    public void toIntegerInvalid() throws Exception {
        testInvalidInteger("12345.5");
        testInvalidInteger("9999999999999999999999999999999999999999999999999");
        testInvalidInteger("fas999999999999999999999999999999999999");
        testInvalidBoundsInt("112312", 1, -1);
    }

    @Test
    public void isValidDoubleTrue() throws Exception {
        assertTrue(NumberUtilities.isValidDouble("12345"));
        assertTrue(NumberUtilities.isValidDouble("-123.99"));
        assertTrue(NumberUtilities.isValidDouble("9999999999", -19999999999L, 19999999999L));
    }


    private void testInvalidBoundsDoubel(String value, double lowerBound, double upperBound) {
        try {
            NumberUtilities.isValidDouble(value, lowerBound, upperBound);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), ExceptionsValues.INVALID_BOUNDS);
        }
    }

    @Test
    public void isValidDoubleFalse() throws Exception {
        assertFalse(NumberUtilities.isValidDouble(""));
        assertFalse(NumberUtilities.isValidDouble("one"));
        assertFalse(NumberUtilities.isValidDouble("fff9999999999", -19999999999L, 19999999999L));
        assertFalse(NumberUtilities.isValidDouble("f0.01", -1, 1));

        testInvalidBoundsDoubel("f0.01", 1, -1);
    }

    @Test
    public void toDoubleValid() throws Exception {
        assertTrue(NumberUtilities.toDouble("12345") - 12345 == 0);
        assertTrue(NumberUtilities.toDouble("-123.99") + 123.99 == 0);
        assertTrue(NumberUtilities.toDouble(null, 0) == 0);
        assertTrue(NumberUtilities.toDouble(".111") - 0.111 ==0);

    }

    private void testInvalidDouble(String value) {
        try {
            NumberUtilities.toDouble(value);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), value + ExceptionsValues.EXCEPTION_DELIMITER + ExceptionsValues.INVALID_DOUBLE_VALUE);
        }
    }

    @Test
    public void toDoubleInvalid() throws Exception {
        testInvalidDouble("aaa");
        testInvalidDouble("fff.111");
    }

}