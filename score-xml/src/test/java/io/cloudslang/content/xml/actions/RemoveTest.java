package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class RemoveTest {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<root someid=\"5\" attr=\"ibute\">\n" +
                    "  <element1>First element</element1>\n" +
                    "  <element2>\n" +
                    "    <subelement attr=\"toDelete\">Sub2</subelement>\n" +
                    "  </element2>\n" +
                    "  <element3>\n" +
                    "    <subelement attr=\"toDelete\">Sub3</subelement>\n" +
                    "  </element3>\n" +
                    "</root>";

    @Test
    public void testRemoveElements() {
        Remove toTest = new Remove();

        String xPathQuery = "//subelement";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testRemoveAttributes() {
        Remove toTest = new Remove();

        String xPathQuery = "//subelement";
        String name = "attr";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() {
        Remove toTest = new Remove();

        String xPathQuery = "/subelement";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() {
        Remove toTest = new Remove();

        String xPathQuery = "//subelement/@attr";
        String name = null;

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

}
