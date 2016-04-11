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
 * Created by markowis on 23/02/2016.
 */
public class RemoveTest {

    private Remove remove;
    String xml;

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
        String name = null;

        Map<String, String> result = remove.execute(xml, xPathQuery, name, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals(Constants.SuccessMessages.REMOVE_SUCCESS, result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testRemoveAttributes() {
        String xPathQuery = "//subelement";
        String name = "attr";

        Map<String, String> result = remove.execute(xml, xPathQuery, name, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals(Constants.SuccessMessages.REMOVE_SUCCESS, result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String name = null;

        Map<String, String> result = remove.execute(xml, xPathQuery, name, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.ELEMENT_NOT_FOUND,
                result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//subelement/@attr";
        String name = null;

        Map<String, String> result = remove.execute(xml, xPathQuery, name, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals(Constants.ErrorMessages.PARSING_ERROR + Constants.ErrorMessages.REMOVE_FAILURE +
                Constants.ErrorMessages.NEED_ELEMENT_TYPE, result.get(Constants.OutputNames.RETURN_RESULT));
    }
}
