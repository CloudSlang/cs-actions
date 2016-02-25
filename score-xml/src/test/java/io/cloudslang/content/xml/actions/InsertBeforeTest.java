package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 25/02/2016.
 */
public class InsertBeforeTest {
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<root someid=\"5\">\n" +
            "  <element1>First element</element1>\n" +
            "  <element2>\n" +
            "    <subelement>Sub2</subelement>\n" +
            "  </element2>\n" +
            "  <element3>\n" +
            "    <subelement>Sub3</subelement>\n" +
            "  </element3>\n" +
            "</root>";

    @Test
    public void testInsertBefore() {
        InsertBefore toTest = new InsertBefore();

        String xPathQuery = "//element1";
        String xmlElement = "<element0>Zero</element0>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testAddChildMultiple() {
        InsertBefore toTest = new InsertBefore();

        String xPathQuery = "//subelement";
        String xmlElement = "<presub>Zero</presub>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testNotFound() {
        InsertBefore toTest = new InsertBefore();

        String xPathQuery = "/subelement";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testInvalidElement() {
        InsertBefore toTest = new InsertBefore();

        String xPathQuery = "//subelement";
        String xmlElement = "<open>Text</close>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testNonElementXPathQuery() {
        InsertBefore toTest = new InsertBefore();

        String xPathQuery = "//element1/@attr";
        String xmlElement = "<toAdd>Text</toAdd>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, xmlElement, "false");
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }
}
