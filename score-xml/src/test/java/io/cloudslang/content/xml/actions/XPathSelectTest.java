package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by markowis on 22/02/2016.
 */
public class XPathSelectTest {

    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<root someid=\"5\" attr=\"ibute\">\n" +
                    "  <element1>First element</element1>\n" +
                    "  <element2>\n" +
                    "    <subelement>Sub2</subelement>\n" +
                    "  </element2>\n" +
                    "  <element3>\n" +
                    "    <subelement>Sub3</subelement>\n" +
                    "  </element3>\n" +
                    "</root>";

    String namespaceXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                            "<root xmlns:foo=\"http://www.foo.org/\">\n" +
                            "  <foo:element1>First element</foo:element1>\n" +
                            "</root>";

    @Test
    public void testSelectValue() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "Sub3";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectNode() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<subelement>Sub3</subelement>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectElementList() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "//subelement";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "<subelement>Sub2</subelement>,<subelement>Sub3</subelement>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectAttributeList() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "//root/@*";

        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "attr=\"ibute\",someid=\"5\"";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testNotFoundValue() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "/root/element1/@id";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }

    @Test
    public void testNotFoundNode() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "/root/element1/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }

    @Test
    public void testFindWithNameSpace() {
        XPathSelect toTest = new XPathSelect();

        String xPathQuery = "//foo:element1";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<foo:element1 xmlns:foo=\"http://www.foo.org/\">First element</foo:element1>";

        Map<String, String> result = toTest.execute(namespaceXml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }
}