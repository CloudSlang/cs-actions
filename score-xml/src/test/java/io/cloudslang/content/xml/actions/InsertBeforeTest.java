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

        Map<String, String> result = insertBefore.execute(xml, "", xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        Assert.assertEquals(Constants.SuccessMessages.INSERT_BEFORE_SUCCESS,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testAddChildMultiple() {
        String xPathQuery = "//subelement";
        String xmlElement = "<presub>Zero</presub>";

        Map<String, String> result = insertBefore.execute(xml, "", xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        Assert.assertEquals(Constants.SuccessMessages.INSERT_BEFORE_SUCCESS,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = insertBefore.execute(xml, "", xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        Assert.assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.ELEMENT_NOT_FOUND,
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testInvalidElement() {
        String xPathQuery = "//subelement";
        String xmlElement = "<open>Text</close>";

        Map<String, String> result = insertBefore.execute(xml, "", xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        Assert.assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                "The element type \"open\" must be terminated by the matching end-tag \"</open>\".",
                result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = insertBefore.execute(xml, "", xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        Assert.assertEquals(Constants.ErrorMessages.PARSING_ERROR +
                Constants.ErrorMessages.INSERT_BEFORE_FAILURE + Constants.ErrorMessages.NEED_ELEMENT_TYPE,
                result.get(Constants.Outputs.RETURN_RESULT));
    }
}
