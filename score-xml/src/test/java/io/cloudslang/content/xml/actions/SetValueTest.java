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
public class SetValueTest {

    private SetValue setValue;
    Map<String, String> result;
    String xml;

    @Before
    public void setUp() throws Exception{
        setValue = new SetValue();
        URI resource = getClass().getResource("/xml/test.xml").toURI();
        xml = FileUtils.readFileToString(new File(resource));
    }

    @After
    public void tearDown(){
        setValue = null;
        result = null;
        xml = null;
    }

    @Test
    public void testSetElementValue() {
        String xPathQuery = "//subelement";
        String name = null;
        String value = "test value";

        result = setValue.execute(xml, xPathQuery, name, value, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testSetAttributeValue() {
        String xPathQuery = "//subelement";
        String name = "atName";
        String value = "test value";

        result = setValue.execute(xml, xPathQuery, name, value, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testNotFound() {
        String xPathQuery = "/subelement";
        String name = "atName";
        String value = "test value";

        result = setValue.execute(xml, xPathQuery, name, value, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
    }

    @Test
    public void testNonElementXPathQuery() {
        String xPathQuery = "//element1/@attr";
        String name = "atName";
        String value = "test value";

        result = setValue.execute(xml, xPathQuery, name, value, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
    }
}
