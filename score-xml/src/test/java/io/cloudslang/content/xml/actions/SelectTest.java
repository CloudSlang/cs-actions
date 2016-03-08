package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 22/02/2016.
 */
public class SelectTest {

    private Select select;
    Map<String, String> result;
    String xml;

    @Before
    public void setUp() throws Exception{
        select = new Select();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        select = null;
        result = null;
        xml = null;
    }

    @Test
    public void testSelectValue() {
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "Sub3";

        result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testSelectNode() {
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<subelement attr=\"toDelete\">Sub3</subelement>";

        result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testSelectElementList() {
        String xPathQuery = "//subelement";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "<subelement attr=\"toDelete\">Sub2</subelement>,<subelement attr=\"toDelete\">Sub3</subelement>";

        result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testSelectAttributeList() {
        String xPathQuery = "//root/@*";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "someid=\"5\"";

        result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testNotFoundValue() {
        String xPathQuery = "/root/element1/@id";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "";

        result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testNotFoundNode() {
        String xPathQuery = "/root/element1/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "";

       result = select.execute(xml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }

    @Test
    public void testFindWithNameSpace() throws Exception{

        URI resource = getClass().getResource("/xml/namespaceTest.xml").toURI();
        String namespaceXml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//foo:element1";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<foo:element1 xmlns:foo=\"http://www.foo.org/\">First element</foo:element1>";

        result = select.execute(namespaceXml, xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.OutputNames.SELECTED_VALUE));
    }
}