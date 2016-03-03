package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class RemoveTest {

    @Test
    public void testRemoveElements() throws Exception {
        Remove toTest = new Remove();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testRemoveAttributes() throws Exception {
        Remove toTest = new Remove();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String name = "attr";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() throws Exception {
        Remove toTest = new Remove();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/subelement";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() throws Exception {
        Remove toTest = new Remove();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement/@attr";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

}
