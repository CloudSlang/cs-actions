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
 * Created by markowis on 18/02/2016.
 */
public class ValidateTest {

    private Validate validate;
    String xml;

    @Before
    public void setUp() throws Exception{
        validate = new Validate();
    }

    @After
    public void tearDown(){
        validate = null;
        xml = null;
    }

    @Test
    public void testWithWellFormedXML() {
        xml = "<root>toot</root>";

        Map<String, String> result = validate.execute(xml, null, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals("Parsing successful.", result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testWithNonWellFormedXML() {
        xml = "<root>toot</roo>";

        Map<String, String> result = validate.execute(xml, null, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals("Parsing error: The element type \"root\" must be terminated by the matching end-tag \"</root>\".", result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testWithValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/valid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, xsd, "false");

        Assert.assertEquals(Constants.SUCCESS, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals("XML is valid.", result.get(Constants.OutputNames.RETURN_RESULT));
    }

    @Test
    public void testWithNotValidXML() throws Exception {
        URI resourceXML = getClass().getResource("/xml/notValid.xml").toURI();
        xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = validate.execute(xml, xsd, "false");

        Assert.assertEquals(Constants.FAILURE, result.get(Constants.OutputNames.RESULT_TEXT));
        Assert.assertEquals("Parsing error: cvc-complex-type.4: Attribute 'someid' must appear on element 'root'.", result.get(Constants.OutputNames.RETURN_RESULT));
    }
}
