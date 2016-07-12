package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by markowis on 23/02/2016.
 */
public class RemoveTest {

    private Remove remove;
    String xml;
    private static final String XML_STRING = "xmlString";
    private static final String XML_PATH = "xmlPath";
    private static final String XML_URL = "xmlUrl";
    private static final String INVALID_XML_DOCUMENT_SOURCE = " is an invalid input value. Valid values are: xmlString, xmlPath and xmlUrl";


    @Before
    public void setUp() throws Exception{
        remove = new Remove();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        remove = null;
        xml = null;
    }

    @Test
    public void testRemoveElements() {
        String xPathQuery = "//subelement";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, null, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.REMOVE_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testRemoveAttributes() {
        String xPathQuery = "//subelement";
        String name = "attr";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, name, "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.REMOVE_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String name = null;

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, name, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.ELEMENT_NOT_FOUND,
                result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//subelement/@attr";

        Map<String, String> result = remove.execute(xml, XML_STRING, xPathQuery, null, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.REMOVE_FAILURE +
                Constants.ErrorMessages.NEED_ELEMENT_TYPE, result.get(Constants.Outputs.ERROR_MESSAGE));
    }

    @Test
    public void testRemoveElementsWithWrongXmlPath() {
        String xPathQuery = "//subelement";

        Map<String, String> result = remove.execute(xml, "xmlpathd", xPathQuery, null, "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertTrue(result.get(Constants.Outputs.ERROR_MESSAGE).contains(INVALID_XML_DOCUMENT_SOURCE));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get("returnCode"));
        assertTrue(result.get("resultXML").isEmpty());
    }
    @Test
    public void testRemoveElementWithXmlPath() throws IOException, URISyntaxException {
        String path = getClass().getResource("/xml/test.xml").toURI().getPath();

        Map<String, String> result = remove.execute(path, XML_PATH, "//subelement", "attr", "false");

        assertEquals(Constants.SUCCESS, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.SuccessMessages.REMOVE_SUCCESS, result.get(Constants.Outputs.RETURN_RESULT));
        assertEquals(Constants.ReturnCodes.SUCCESS, result.get("returnCode"));
    }

    @Test
    public void testRemoveElementWithWrongXmlPath() throws IOException, URISyntaxException {

        Map<String, String> result = remove.execute("c:/InvalidtestWrong.xml", XML_PATH, "//subelement", "attr", "false");

        assertEquals(Constants.FAILURE, result.get(Constants.Outputs.RESULT_TEXT));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get(Constants.Outputs.RETURN_CODE));
        assertEquals(Constants.ReturnCodes.FAILURE, result.get("returnCode"));
    }
}
