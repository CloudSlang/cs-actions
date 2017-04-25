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