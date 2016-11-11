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