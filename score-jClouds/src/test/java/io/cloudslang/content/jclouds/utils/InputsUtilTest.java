package io.cloudslang.content.jclouds.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class InputsUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validateInput() {
        setExpectedExceptions(RuntimeException.class, "The required endpoint input is not specified!");

        InputsUtil.validateInput("", "endpoint");
    }

    @Test
    public void getMinInstancesCountBlank() {
        int testMinInstanceCount = InputsUtil.getValidInstancesCount("");

        assertEquals(1, testMinInstanceCount);
    }

    @Test
    public void getMinInstancesCountNegative() {
        setExpectedExceptions(RuntimeException.class, "Incorrect provided value: -1 input. The value doesn't meet " +
                "conditions for general purpose usage.");

        InputsUtil.getValidInstancesCount("-1");
    }

    @Test
    public void getMinInstancesCountNotInt() {
        setExpectedExceptions(RuntimeException.class, "The provided value: [abracadabra] input must be integer.");

        InputsUtil.getValidInstancesCount("[abracadabra]");
    }

    @Test
    public void getMinInstancesCount() {
        int testMinInstanceCount = InputsUtil.getValidInstancesCount("3");

        assertEquals(3, testMinInstanceCount);
    }

    @Test
    public void getMaxInstancesCountBlank() {
        int testGetMaxInstancesCount = InputsUtil.getValidInstancesCount("");

        assertEquals(1, testGetMaxInstancesCount);
    }

    @Test
    public void getMaxInstancesCountOver() {
        setExpectedExceptions(RuntimeException.class, "Incorrect provided value: 51 input. The value doesn't meet " +
                "conditions for general purpose usage.");

        InputsUtil.getValidInstancesCount("51");
    }

    @Test
    public void getValidLongBlank() {
        long testLong = InputsUtil.getValidLong("", 20000L);

        assertEquals(20000, testLong);
    }

    @Test
    public void getValidLongNegative() {
        setExpectedExceptions(RuntimeException.class, "Incorrect provided value: -1. Valid values are positive longs.");

        InputsUtil.getValidLong("-1", 0L);
    }

    @Test
    public void getValidLongNotLong() {
        setExpectedExceptions(RuntimeException.class, "The provided value: [anything_here] input must be long.");

        InputsUtil.getValidLong("[anything_here]", 0L);
    }

    @Test
    public void getValidLong() {
        long testLong = InputsUtil.getValidLong("60000", 20000L);

        assertEquals(60000, testLong);
    }

    @Test
    public void getValidInstanceStateInvalid() {
        int testedValue = InputsUtil.getValidInstanceStateCode("");

        assertEquals(-1, testedValue);
    }

    @Test
    public void getValidInstanceState() {
        int testedValue = InputsUtil.getValidInstanceStateCode("running");

        assertEquals(16, testedValue);
    }

    @Test
    public void getRelevantBooleanStringNotRelevant() {
        String testedString = InputsUtil.getRelevantBooleanString("anything but true or false");

        assertEquals("Not relevant", testedString);
    }

    @Test
    public void getRelevantBooleanStringTrue() {
        String testedString = InputsUtil.getRelevantBooleanString("true");

        assertEquals("true", testedString);
    }

    @Test
    public void getRelevantBooleanStringFalse() {
        String testedString = InputsUtil.getRelevantBooleanString("false");

        assertEquals("false", testedString);
    }

    private void setExpectedExceptions(Class<?> type, String message) {
        exception.expect((Class<? extends Throwable>) type);
        exception.expectMessage(message);
    }
}
