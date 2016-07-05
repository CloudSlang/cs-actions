package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 25/02/2016.
 */
public class AppendChildTest {

    private AppendChild appendChild;
    String xml;

    @Before
    public void setUp() throws Exception{
        appendChild = new AppendChild();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        appendChild = null;
        xml = null;
    }

    @Test
    public void testAppendChild() {
        String xPathQuery = "//element1";
        String xmlChild = "<sub1 attr=\"ibute\">Sub1</sub1>";

        Map<String, String> result = appendChild.execute(xml, "", xPathQuery, xmlChild, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.APPEND_CHILD_SUCCESS,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testAppendChildMultiple() {
        String xPathQuery = "//subelement";
        String xmlChild = "<subsub attr=\"ibute\">Deeply nested</subsub>";

        Map<String, String> result = appendChild.execute(xml, "", xPathQuery, xmlChild, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.APPEND_CHILD_SUCCESS,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = appendChild.execute(xml, "", xPathQuery, xmlChild, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.ELEMENT_NOT_FOUND,
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testInvalidChild() {
        String xPathQuery = "//subelement";
        String xmlChild = "<open>Text</close>";

        Map<String, String> result = appendChild.execute(xml, "", xPathQuery, xmlChild, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                "The element type \"open\" must be terminated by the matching end-tag \"</open>\".",
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = appendChild.execute(xml, "", xPathQuery, xmlChild, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.APPEND_CHILD_FAILURE +
                Constants.ErrorMessages.NEED_ELEMENT_TYPE, result.get(Constants.Outputs.ERROR_MESSAGE));
    }
}
