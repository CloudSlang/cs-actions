/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionValues;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by victor on 04.09.2016.
 */
public class NumberUtilitiesTest {
    @Test
    public void isValidIntTrue() throws Exception {
        assertTrue(NumberUtilities.isValidInt("12345"));
        assertTrue(NumberUtilities.isValidInt("-123"));
        assertTrue(NumberUtilities.isValidInt("0", -1, 1));
        assertTrue(NumberUtilities.isValidInt("0", 0, 1, true, false));
        assertTrue(NumberUtilities.isValidInt("1", 1, 1, true, true));
        assertTrue(NumberUtilities.isValidInt("1", 0, 1, false, true));
        assertTrue(NumberUtilities.isValidInt("0", -1, 1, false, false));
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
            assertEquals(iae.getMessage(), ExceptionValues.INVALID_BOUNDS);
        }
    }


    private void testInvalidInteger(String value) {
        try {
            NumberUtilities.toInteger(value);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), value + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_INTEGER_VALUE);
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
        assertTrue(NumberUtilities.isValidDouble("0.1", 0.1, 1, true, false));
        assertTrue(NumberUtilities.isValidDouble("1.1", 1.1, 1.1, true, true));
        assertTrue(NumberUtilities.isValidDouble("-1.9", -2, -1.9, false, true));
        assertTrue(NumberUtilities.isValidDouble("0.0001", 0.00009, 0.00011, false, false));
    }


    private void testInvalidBoundsDouble(String value, double lowerBound, double upperBound) {
        try {
            NumberUtilities.isValidDouble(value, lowerBound, upperBound);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), ExceptionValues.INVALID_BOUNDS);
        }
    }

    @Test
    public void isValidDoubleFalse() throws Exception {
        assertFalse(NumberUtilities.isValidDouble(""));
        assertFalse(NumberUtilities.isValidDouble("one"));
        assertFalse(NumberUtilities.isValidDouble("fff9999999999", -19999999999L, 19999999999L));
        assertFalse(NumberUtilities.isValidDouble("f0.01", -1, 1));
        assertFalse(NumberUtilities.isValidDouble("0.1", 0, 0.1, true, false));
        assertFalse(NumberUtilities.isValidDouble("2.1", 1, 1.1, true, true));
        assertFalse(NumberUtilities.isValidDouble("-2", -2, -1.9, false, true));
        assertFalse(NumberUtilities.isValidDouble("0.0001", 0.0001, 0.00011, false, false));
        testInvalidBoundsDouble("f0.01", 1, -1);
    }

    @Test
    public void toDoubleValid() throws Exception {
        assertTrue(NumberUtilities.toDouble("12345") - 12345 == 0);
        assertTrue(NumberUtilities.toDouble("-123.99") + 123.99 == 0);
        assertTrue(NumberUtilities.toDouble(null, 0) == 0);
        assertTrue(NumberUtilities.toDouble(".111") - 0.111 == 0);

    }

    private void testInvalidDouble(String value) {
        try {
            NumberUtilities.toDouble(value);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), value + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_DOUBLE_VALUE);
        }
    }

    @Test
    public void toDoubleInvalid() throws Exception {
        testInvalidDouble("aaa");
        testInvalidDouble("fff.111");
    }

    @Test
    public void isValidLongTrue() throws Exception {
        assertTrue(NumberUtilities.isValidLong("12345"));
        assertTrue(NumberUtilities.isValidLong("-123"));
        assertTrue(NumberUtilities.isValidLong("999999999999999999", -1999999999999999999L, 1999999999999999999L));
        assertTrue(NumberUtilities.isValidLong("999999999999999999", -1999999999999999L, 1999999999999999999L, true, false));
        assertTrue(NumberUtilities.isValidLong("123456789123456789", 123456789123456789L, 1123456789123456789L, true, true));
        assertTrue(NumberUtilities.isValidLong("999999999999999999", -2, 1999999999999999999L, false, true));
        assertTrue(NumberUtilities.isValidLong("-123456789123456789", -1123456789123456789L, 0, false, false));
    }


    private void testInvalidBoundsLong(String value, long lowerBound, long upperBound) {
        try {
            NumberUtilities.isValidLong(value, lowerBound, upperBound);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), ExceptionValues.INVALID_BOUNDS);
        }
    }

    @Test
    public void isValidLongFalse() throws Exception {
        assertFalse(NumberUtilities.isValidLong(""));
        assertFalse(NumberUtilities.isValidLong("one"));
        assertFalse(NumberUtilities.isValidLong("fff9999999999", -19999999999L, 19999999999L));
        assertFalse(NumberUtilities.isValidLong("999999999999999", 9999999999999999L, 99999999999999999L, true, true));
        assertFalse(NumberUtilities.isValidLong("999999999999999", 0, 99999999999999L, false, true));
        testInvalidBoundsLong("10", 100, -100);
    }

    @Test
    public void toLongValid() throws Exception {
        assertEquals(NumberUtilities.toLong("12345"), 12345);
        assertEquals(NumberUtilities.toLong("-123"), -123);
        assertEquals(NumberUtilities.toLong(null, 0), 0);
        assertEquals(NumberUtilities.toLong("", 100), 100);
    }

    private void testInvalidLong(String value) {
        try {
            NumberUtilities.toLong(value);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), value + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_LONG_VALUE);
        }
    }

    @Test
    public void toLongInvalid() throws Exception {
        testInvalidLong("aaa");
        testInvalidLong("a0b0c0");
    }

}