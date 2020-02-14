package io.cloudslang.content.mail.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({InputBuilderUtils.class})
public class InputBuilderUtilsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Test
    public void buildPort_PortIsNullAndMandatory_Exception() throws Exception {
        final String port = null;
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNullAndNotMandatory_NullReturned() throws Exception {
        final String port = null;
        final boolean mandatory = false;

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertNull(result);
    }


    @Test
    public void buildPort_PortIsEmptyAndMandatory_Exception() throws Exception {
        final String port = StringUtils.EMPTY;
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsEmptyAndNotMandatory_NullReturned() throws Exception {
        final String port = StringUtils.EMPTY;
        final boolean mandatory = false;

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertNull(result);
    }


    @Test
    public void buildPort_PortIsInvalidAndMandatory_NumberFormatException() throws Exception {
        final String port = "asd";
        final boolean mandatory = true;
        exception.expect(NumberFormatException.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsInvalidAndNotMandatory_NumberFormatException() throws Exception {
        final String port = "asd";
        final boolean mandatory = false;
        exception.expect(NumberFormatException.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNotInRangeAndMandatory_Exception() throws Exception {
        final String port = "0";
        final boolean mandatory = true;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsNotInRangeAndNotMandatory_Exception() throws Exception {
        final String port = "0";
        final boolean mandatory = false;
        exception.expect(Exception.class);

        InputBuilderUtils.buildPort(port, mandatory);
    }


    @Test
    public void buildPort_PortIsValid_ValidResult() throws Exception {
        final String port = "1";
        final boolean mandatory = true;
        final Short expectedResult = Short.parseShort(port);

        Short result = InputBuilderUtils.buildPort(port, mandatory);

        assertEquals(expectedResult, result);
    }
}