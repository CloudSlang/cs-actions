package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 18/02/2016.
 */
public class ValidateTest {

    private static final String EMPTY_STR = "";
    private Validate validate;
    String xml;

    @Before
    public void setUp() throws Exception{
        validate = new Validate();
    }

    @After
    public void tearDown(){
        validate = null;
        xml = null;
    }

    @Test
    public void testWithWellFormedXML() {
        xml = "<root>toot</root>";

        Map<String, String> result = validate.execute(xml, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, null, "false");

        assertEquals(Constants.ResponseNames.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.PARSING_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testWithNonWellFormedXML() {
        xml = "<root>toot</roo>";

        Map<String, String> result = validate.execute(xml, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, null, "false");

        assertEquals(Constants.ResponseNames.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                "The element type \"root\" must be terminated by the matching end-tag \"</root>\".",
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testWithValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/valid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, EMPTY_STR, xsd, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, "false");

        assertEquals(Constants.ResponseNames.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.VALIDATION_SUCCESS,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testWithNotValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/notValid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, EMPTY_STR, xsd, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, "false");

        assertEquals(Constants.ResponseNames.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                "cvc-complex-type.4: Attribute 'someid' must appear on element 'root'.",
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }
}
