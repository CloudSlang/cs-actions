package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
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
    Map<String, String> result;
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
        result = null;
        xml = null;
    }

    @Test
    public void testInsertBefore() {
        String xPathQuery = "//element1";
        String xmlElement = "<element0>Zero</element0>";

        result = insertBefore.execute(xml, xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testAddChildMultiple() {
        String xPathQuery = "//subelement";
        String xmlElement = "<presub>Zero</presub>";

        result = insertBefore.execute(xml, xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String xmlElement = "<toAdd>Text</toAdd>";

        result = insertBefore.execute(xml, xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testInvalidElement() {
        String xPathQuery = "//subelement";
        String xmlElement = "<open>Text</close>";

        result = insertBefore.execute(xml, xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String xmlElement = "<toAdd>Text</toAdd>";

        result = insertBefore.execute(xml, xPathQuery, xmlElement, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
    }
}
