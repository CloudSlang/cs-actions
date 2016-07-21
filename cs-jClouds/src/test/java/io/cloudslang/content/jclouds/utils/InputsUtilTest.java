package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.services.impl.MockingHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class InputsUtilTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getMinInstancesCountBlank() {
        int testMinInstanceCount = InputsUtil.getValidInstancesCount("");

        assertEquals(1, testMinInstanceCount);
    }

    @Test
    public void getMinInstancesCountNegative() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect provided value: -1 input. The value doesn't meet conditions for general purpose usage.");

        InputsUtil.getValidInstancesCount("-1");
    }

    @Test
    public void getMinInstancesCountNotInt() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The provided value: [abracadabra] input must be integer.");

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
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect provided value: 51 input. The value doesn't meet conditions for general purpose usage.");

        InputsUtil.getValidInstancesCount("51");
    }

    @Test
    public void getValidLongBlank() {
        long testLong = InputsUtil.getValidLong("", 20000L);

        assertEquals(20000, testLong);
    }

    @Test
    public void getValidLongNegative() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect provided value: -1. Valid values are positive longs.");

        InputsUtil.getValidLong("-1", 0L);
    }

    @Test
    public void getValidLongNotLong() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "The provided value: [anything_here] input must be long.");

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

    @Test
    public void getValidVolumeAmountEmpty() {
        String testedString = InputsUtil.getValidVolumeAmount("");

        assertEquals("Not relevant", testedString);
    }

    @Test
    public void getValidVolumeAmountWrongValue() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class,
                "Incorrect provided value: 16385. Valid values are positive floats between 0.5f and 16000.0f.");

        InputsUtil.getValidVolumeAmount("16385");
    }

    @Test
    public void getValidVolumeAmountWrong() {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "The provided value: blahblah input must be float.");

        InputsUtil.getValidVolumeAmount("blahblah");
    }

    @Test
    public void getValidVolumeAmount() {
        String testedString = InputsUtil.getValidVolumeAmount("0.5");

        assertEquals("0.5", testedString);
    }

    @Test
    public void getImageNoRebootFlag() {
        boolean flag = InputsUtil.getImageNoRebootFlag("null or empty or anything but true");

        assertTrue(flag);
    }
}
