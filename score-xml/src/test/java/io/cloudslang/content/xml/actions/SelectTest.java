package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by markowis on 22/02/2016.
 */
public class SelectTest {

    private Select select;
    String xml;
    private static final String XML_STRING = "xmlString";
    private static final String XML_PATH = "xmlPath";
    private static final String INVALID_XML_DOCUMENT_SOURCE = " is an invalid input value. Valid values are: xmlString and xmlPath and xmlUrl";

    @Before
    public void setUp() throws Exception{
        select = new Select();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        select = null;
        xml = null;
    }

    @Test
    public void testSelectValue() {
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "Sub3";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testSelectNode() {
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testSelectElementList() {
        String xPathQuery = "//subelement";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "<subelement attr=\"toDelete\">Sub2</subelement>,<subelement attr=\"toDelete\">Sub3</subelement>";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testSelectAttributeList() {
        String xPathQuery = "//root/@*";
        String queryType = Constants.QueryTypes.NODE_LIST;
        String delimiter = ",";
        String expectedResult = "someid=\"5\"";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFoundValue() {
        String xPathQuery = "/root/element1/@id";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFoundNode() {
        String xPathQuery = "/root/element1/subelement";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "";

        Map<String, String> result = select.execute(xml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testFindWithNameSpace() throws Exception{

        URI resource = getClass().getResource("/xml/namespaceTest.xml").toURI();
        String namespaceXml = FileUtils.readFileToString(new File(resource));

        String xPathQuery = "//foo:element1";
        String queryType = Constants.QueryTypes.NODE;
        String delimiter = null;
        String expectedResult = "<foo:element1 xmlns:foo=\"http://www.foo.org/\">First element</foo:element1>";

        Map<String, String> result = select.execute(namespaceXml, "", xPathQuery, queryType, delimiter, "false");

        Assert.assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        Assert.assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testSelectElementWithXmlPath() throws IOException, URISyntaxException {
        String path = getClass().getResource("/xml/test.xml").toURI().getPath();
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;
        String expectedResult = "Sub3";

        Map<String, String> result = select.execute(path, XML_PATH, xPathQuery, queryType, delimiter, "false");

        assertEquals(expectedResult, result.get(Constants.Outputs.SELECTED_VALUE));
        assertEquals(Constants.SuccessMessages.SELECT_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get("returnCode"));
    }

    @Test
    public void testSelectElementWithWrongXmlPath() throws IOException, URISyntaxException {
        String path = "/xml/Wrongtest.xml";
        String xPathQuery = "/root/element3/subelement";
        String queryType = Constants.QueryTypes.VALUE;
        String delimiter = null;

        Map<String, String> result = select.execute(path, XML_PATH, xPathQuery, queryType, delimiter, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get("returnCode"));
    }
}