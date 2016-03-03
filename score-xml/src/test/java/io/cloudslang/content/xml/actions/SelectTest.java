package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 22/02/2016.
 */
public class SelectTest {

    @Test
    public void testSelectValue() throws Exception {
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "Sub3";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectNode() throws Exception {
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectElementList() throws Exception {
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//subelement";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "<subelement attr=\"toDelete\">Sub2</subelement>,<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testSelectAttributeList() throws Exception {
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//root/@*";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "someid=\"5\"";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String selectedValue = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, selectedValue);
    }

    @Test
    public void testNotFoundValue() throws Exception {
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/root/element1/@id";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }

    @Test
    public void testNotFoundNode() throws Exception{
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/test.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "/root/element1/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = toTest.execute(xml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }

    @Test
    public void testFindWithNameSpace() throws Exception{
        Select toTest = new Select();

        URI resource = getClass().getResource("/xml/namespaceTest.xml").toURI();
        String namespaceXml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//foo:element1";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<foo:element1 xmlns:foo=\"http://www.foo.org/\">First element</foo:element1>";

        Map<String, String> result = toTest.execute(namespaceXml, xPathQuery, queryType, delimiter, "false");
        String returnResult = result.get(Constants.OutputNames.SELECTED_VALUE);

        Assert.assertEquals(expectedResult, returnResult);
    }
}