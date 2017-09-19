/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utils;

import io.cloudslang.content.constants.ExceptionValues;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by victor on 04.09.2016.
 */
public class CollectionUtilitiesTest {
    @Test
    public void toArrayWithEscaped() throws Exception {
        assertArrayEquals(CollectionUtilities.toArrayWithEscaped("a,b,c,d", ","), new String[]{"a", "b", "c", "d"});
        assertArrayEquals(CollectionUtilities.toArrayWithEscaped("a\\,b\\,c\\,d", ","), new String[]{"a\\", "b\\", "c\\", "d"});
        assertArrayEquals(CollectionUtilities.toArrayWithEscaped(null, ","), new String[0]);

    }

    @Test
    public void toArray() throws Exception {
        assertArrayEquals(CollectionUtilities.toArray("a,b,c,d", ","), new String[]{"a", "b", "c", "d"});
        assertArrayEquals(CollectionUtilities.toArray("a\\,b\\,c,d", ","), new String[]{"a\\,b\\,c", "d"});
        assertArrayEquals(CollectionUtilities.toArray(null, ","), new String[0]);
    }

    @Test
    public void toListWithEscaped() throws Exception {
        assertEquals(CollectionUtilities.toListWithEscaped("a,b,c,d", ","), Arrays.asList("a", "b", "c", "d"));
        assertEquals(CollectionUtilities.toListWithEscaped("a\\,b\\,c\\,d", ","), Arrays.asList("a\\", "b\\", "c\\", "d"));

    }

    @Test
    public void toList() throws Exception {
        assertEquals(CollectionUtilities.toList("a,b,c,d", ","), Arrays.asList("a", "b", "c", "d"));
        assertEquals(CollectionUtilities.toList("a\\,b\\,c,d", ","), Arrays.asList("a\\,b\\,c", "d"));
    }

    private void testInvalidMap(String mapStr, String pairDelimiter, String keyValueDelimiter) {
        try {
            CollectionUtilities.toMap(mapStr, pairDelimiter, keyValueDelimiter);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            assertEquals(iae.getMessage(), "a:b:c" + ExceptionValues.EXCEPTION_DELIMITER + ExceptionValues.INVALID_KEY_VALUE_PAIR);
        }
    }

    @Test
    public void toMap() throws Exception {
        assertEquals(CollectionUtilities.toMap("a:b,c:d,E:F", ",", ":").get("c"), "d");
        assertEquals(CollectionUtilities.toMap("a::b,c::d,E::F", ",", "::").get("a"), "b");
        assertEquals(CollectionUtilities.toMap("a=b;c=d;E=F", ";", "=").get("E"), "F");
        testInvalidMap("a:b:c,b:c", ",", ":");
    }

}