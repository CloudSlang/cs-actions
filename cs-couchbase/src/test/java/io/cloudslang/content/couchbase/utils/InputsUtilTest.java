package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.AuthType;
import io.cloudslang.content.couchbase.entities.couchbase.BucketType;
import io.cloudslang.content.couchbase.entities.couchbase.ClusterUri;
import io.cloudslang.content.couchbase.entities.couchbase.ConflictResolutionType;
import io.cloudslang.content.couchbase.entities.couchbase.EvictionPolicy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValidValuesString;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidIntValue;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getValidPort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by TusaM
 * 4/24/2017.
 */
public class InputsUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetEnumValidAuthTypeValuesString() {
        String toTest = getEnumValidValuesString(AuthType.class);

        assertEquals("none, sasl", toTest);
    }

    @Test
    public void testGetEnumValidBucketTypeValuesString() {
        String toTest = getEnumValidValuesString(BucketType.class);

        assertEquals("couchbase, membase", toTest);
    }

    @Test
    public void testGetEnumValidConflictResolutionTypeValuesString() {
        String toTest = getEnumValidValuesString(ConflictResolutionType.class);

        assertEquals("lww, seqno", toTest);
    }

    @Test
    public void testGetEnumValidEvictionPolicyValuesString() {
        String toTest = getEnumValidValuesString(EvictionPolicy.class);

        assertEquals("fullEviction, valueOnly", toTest);
    }

    @Test
    public void testGetEnumValidValuesStringInvalidEnum() {
        String toTest = getEnumValidValuesString(ClusterUri.class);

        assertTrue(isEmpty(toTest));
    }

    @Test
    public void testGetValidIntValueExceedMaxValue() {
        setExpectedExceptions(RuntimeException.class, exception, "The value doesn't meet conditions for general " +
                "purpose usage. See operation inputs description section for details.");

        getValidIntValue("65536", 0, 65535, 80);
    }

    @Test
    public void testGetValidIntValueBellowMinValue() {
        setExpectedExceptions(RuntimeException.class, exception, "The value doesn't meet conditions for general " +
                "purpose usage. See operation inputs description section for details.");

        getValidIntValue("-1", 0, 65535, 80);
    }

    @Test
    public void testGetValidIntValueEmptyInput() {
        int toTest = getValidIntValue("", 0, 65535, 80);

        assertEquals(80, toTest);
    }

    @Test
    public void testGetValidIntValueValidInput() {
        int toTest = getValidIntValue("11215", 0, 65535, 80);

        assertEquals(11215, toTest);
    }

    @Test
    public void testGetValidPortNoValue() {
        assertEquals(11215, getValidPort(""));
    }

    @Test
    public void testGetValidPort() {
        assertEquals(11211, getValidPort("11211"));
    }

    @Test
    public void testGetValidPortWrongvalue() {
        setExpectedExceptions(IllegalArgumentException.class, exception, "Incorrect provided value: not integer input. " +
                "The value doesn't meet conditions for general purpose usage. See operation inputs description section for details.");

        getValidPort("not integer");
    }

    @SuppressWarnings("unchecked")
    private static void setExpectedExceptions(Class<?> type, ExpectedException exception, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }
}