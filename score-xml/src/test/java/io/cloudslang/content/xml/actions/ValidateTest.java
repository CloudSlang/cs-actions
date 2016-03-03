package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Map;

/**
 * Created by markowis on 18/02/2016.
 */
public class ValidateTest {

    @Test
    public void testWithWellFormedXML() {
        Validate toTest = new Validate();
        String xml = "<root>toot</root>";

        Map<String, String> result = toTest.execute(xml, null);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testWithNonWellFormedXML() {
        Validate toTest = new Validate();
        String xml = "<root>toot</roo>";

        Map<String, String> result = toTest.execute(xml, null);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

    @Test
    public void testWithValidXML() throws Exception {
        Validate toTest = new Validate();

        URI resourceXML = getClass().getResource("/xml/valid.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = toTest.execute(xml, xsd);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testWithNotValidXML() throws Exception {
        Validate toTest = new Validate();

        URI resourceXML = getClass().getResource("/xml/notValid.xml").toURI();
        String xml = FileUtils.readFileToString(new File(resourceXML));

        URI resourceXSD = getClass().getResource("/xml/test.xsd").toURI();
        String xsd = FileUtils.readFileToString(new File(resourceXSD));

        Map<String, String> result = toTest.execute(xml, xsd);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

}
