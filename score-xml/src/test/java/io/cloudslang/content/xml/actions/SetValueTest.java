package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class SetValueTest {
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
    public void testSetElementValue() {
        SetValue toTest = new SetValue();

        String xPathQuery = "//subelement";
        String name = null;
        String value = "test value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testSetAttributeValue() {
        SetValue toTest = new SetValue();

        String xPathQuery = "//subelement";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() {
        SetValue toTest = new SetValue();

        String xPathQuery = "/subelement";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() {
        SetValue toTest = new SetValue();

        String xPathQuery = "//element1/@attr";
        String name = "atName";
        String value = "test value";

        Map<String, String> result = toTest.execute(xml, xPathQuery, name, value, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
