package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 25/02/2016.
 */
public class AddAttributeTest {

    @Test
    public void testAddAttribute() throws Exception{
        AddAttribute toTest = new AddAttribute();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/root/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() throws Exception{
        AddAttribute toTest = new AddAttribute();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() throws Exception{
        AddAttribute toTest = new AddAttribute();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//element1/@attr";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
