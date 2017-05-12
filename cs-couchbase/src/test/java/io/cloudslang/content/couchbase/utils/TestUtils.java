/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.utils;

import org.junit.rules.ExpectedException;

/**
 * Created by TusaM
 * 4/25/2017.
 */
public class TestUtils {
    private TestUtils() {
        // prevent instantiation
    }

    @SuppressWarnings("unchecked")
    public static void setExpectedExceptions(Class<?> type, ExpectedException exception, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }
}