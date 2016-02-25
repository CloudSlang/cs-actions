package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 25/02/2016.
 */
public class AddAttributeTest {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<root someid=\"5\">\n" +
            "  <element1 attr=\"ibute\">First element</element1>\n" +
            "  <element2>\n" +
            "    <subelement>Sub2</subelement>\n" +
            "  </element2>\n" +
            "  <element3>\n" +
            "    <subelement>Sub3</subelement>\n" +
            "  </element3>\n" +
            "</root>";

    @Test
    public void testAddAttribute() {
        AddAttribute toTest = new AddAttribute();

        String xPathQuery = "/root/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() {
        AddAttribute toTest = new AddAttribute();

        String xPathQuery = "/element1";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() {
        AddAttribute toTest = new AddAttribute();

        String xPathQuery = "//element1/@attr";
        String attributeName = "newAttr";
        String value = "New Value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, attributeName, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
