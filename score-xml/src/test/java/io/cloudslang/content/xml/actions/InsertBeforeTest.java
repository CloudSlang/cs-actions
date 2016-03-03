package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 25/02/2016.
 */
public class InsertBeforeTest {

    @Test
    public void testInsertBefore() throws Exception {
        InsertBefore toTest = new InsertBefore();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//element1";
        String xmlElement = "<element0>Zero</element0>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testAddChildMultiple() throws Exception {
        InsertBefore toTest = new InsertBefore();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String xmlElement = "<presub>Zero</presub>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() throws Exception {
        InsertBefore toTest = new InsertBefore();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/subelement";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testInvalidElement() throws Exception {
        InsertBefore toTest = new InsertBefore();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String xmlElement = "<open>Text</close>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() throws Exception {
        InsertBefore toTest = new InsertBefore();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//element1/@attr";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
