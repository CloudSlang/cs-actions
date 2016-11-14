package io.cloudslang.content.xml.actions;

import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.ELEMENT_NOT_FOUND;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.INSERT_BEFORE_FAILURE;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.NEED_ELEMENT_TYPE;
import static io.cloudslang.content.xml.utils.Constants.ErrorMessages.PARSING_ERROR;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_TEXT;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.INSERT_BEFORE_SUCCESS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 25/02/2016.
 */
public class InsertBeforeTest {

    private InsertBefore insertBefore;
    String xml;

    @Before
    public void setUp() throws Exception{
        insertBefore = new InsertBefore();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        insertBefore = null;
        xml = null;
    }

    @Test
    public void testInsertBefore() {
        String xPathQuery = "//element1";
        String xmlElement = "<element0>Zero</element0>";

        Map<String, String> result = insertBefore.execute(xml, EMPTY, xPathQuery, xmlElement, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(INSERT_BEFORE_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testAddChildMultiple() {
        String xPathQuery = "//subelement";
        String xmlElement = "<presub>Zero</presub>";

        Map<String, String> result = insertBefore.execute(xml, EMPTY, xPathQuery, xmlElement, FALSE);

        assertEquals(ResponseNames.SUCCESS, result.get(RESULT_TEXT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(INSERT_BEFORE_SUCCESS, result.get(RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = insertBefore.execute(xml, EMPTY, xPathQuery, xmlElement, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + ELEMENT_NOT_FOUND, result.get(ERROR_MESSAGE));
    }

    @Test
    public void testInvalidElement() {
        String xPathQuery = "//subelement";
        String xmlElement = "<open>Text</close>";

        Map<String, String> result = insertBefore.execute(xml, EMPTY, xPathQuery, xmlElement, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR +
                "The element type \"open\" must be terminated by the matching end-tag \"</open>\".",
                result.get(ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = insertBefore.execute(xml, EMPTY, xPathQuery, xmlElement, FALSE);

        assertEquals(ResponseNames.FAILURE, result.get(RESULT_TEXT));
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertEquals(PARSING_ERROR + INSERT_BEFORE_FAILURE + NEED_ELEMENT_TYPE, result.get(ERROR_MESSAGE));
    }
}
