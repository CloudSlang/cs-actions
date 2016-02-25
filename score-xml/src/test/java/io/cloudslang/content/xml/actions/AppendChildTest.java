package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 25/02/2016.
 */
public class AppendChildTest {
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
    public void testAddChild() {
        AppendChild toTest = new AppendChild();

        String xPathQuery = "//element1";
        String xmlChild = "<sub1 attr=\"ibute\">Sub1</sub1>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testAddChildMultiple() {
        AppendChild toTest = new AppendChild();

        String xPathQuery = "//subelement";
        String xmlChild = "<subsub attr=\"ibute\">Deeply nested</subsub>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() {
        AppendChild toTest = new AppendChild();

        String xPathQuery = "/subelement";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testInvalidChild() {
        AppendChild toTest = new AppendChild();

        String xPathQuery = "//subelement";
        String xmlChild = "<open>Text</close>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() {
        AppendChild toTest = new AppendChild();

        String xPathQuery = "//element1/@attr";
        String xmlChild = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlChild, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
