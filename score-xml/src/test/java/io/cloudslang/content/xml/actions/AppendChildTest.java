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
public class AppendChildTest {

    @Test
    public void testAddChild() throws Exception {
        AppendChild toTest = new AppendChild();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//element1";
        String xmlChild = "<sub1 attr=\"ibute\">Sub1</sub1>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testAddChildMultiple() throws Exception {
        AppendChild toTest = new AppendChild();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String xmlChild = "<subsub attr=\"ibute\">Deeply nested</subsub>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() throws Exception{
        AppendChild toTest = new AppendChild();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/subelement";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testInvalidChild() throws Exception{
        AppendChild toTest = new AppendChild();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String xmlChild = "<open>Text</close>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() throws Exception{
        AppendChild toTest = new AppendChild();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//element1/@attr";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
