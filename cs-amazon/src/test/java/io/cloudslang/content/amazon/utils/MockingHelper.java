/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.utils;

import org.junit.rules.ExpectedException;

/**
 * Created by Mihai Tusa.
 * 6/23/2016.
 */
public class MockingHelper {
    private MockingHelper() {
    }

    @SuppressWarnings("unchecked")
    public static void setExpectedExceptions(ExpectedException exception, Class<?> type, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }
}
