package io.cloudslang.content.jclouds.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by persdana on 7/13/2015.
 */
public class InputsUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void validateInput() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("The required endpoint input is not specified!");

        InputsUtil.validateInput("", "endpoint");
    }

    @Test
    public void getMinInstancesCountBlank() {
        int testMinInstanceCount = InputsUtil.getMinInstancesCount("");
        assertEquals(1, testMinInstanceCount);
    }

    @Test
    public void getMinInstancesCountNegative() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Incorrect provided value: -1 input. The value doesn't meet conditions for general purpose usage.");

        InputsUtil.getMinInstancesCount("-1");
    }

    @Test
    public void getMinInstancesCountNotInt() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("The provided value: [abracadabra] input must be integer.");

        InputsUtil.getMinInstancesCount("[abracadabra]");
    }

    @Test
    public void getMinInstancesCount() {
        int testMinInstanceCount = InputsUtil.getMinInstancesCount("3");
        assertEquals(3, testMinInstanceCount);
    }

    @Test
    public void getMaxInstancesCountBlank() {
        int testGetMaxInstancesCount = InputsUtil.getMaxInstancesCount("");
        assertEquals(1, testGetMaxInstancesCount);
    }

    @Test
    public void getMaxInstancesCountOver() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Incorrect provided value: 51 input. The value doesn't meet conditions for general purpose usage.");

        InputsUtil.getMaxInstancesCount("51");
    }

    @Test
    public void getValidLongBlank() {
        long testLong = InputsUtil.getValidLong("", 20000L);
        assertEquals(20000, testLong);
    }

    @Test
    public void getValidLongNegative() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("Incorrect provided value: -1. Valid values are positive longs.");

        InputsUtil.getValidLong("-1", 0L);
    }

    @Test
    public void getValidLongNotLong() {
        exception.expect(RuntimeException.class);
        exception.expectMessage("The provided value: [anything_here] input must be long.");

        InputsUtil.getValidLong("[anything_here]", 0L);
    }

    @Test
    public void getValidLong() {
        long testLong = InputsUtil.getValidLong("60000", 20000L);
        assertEquals(60000, testLong);
    }
}
