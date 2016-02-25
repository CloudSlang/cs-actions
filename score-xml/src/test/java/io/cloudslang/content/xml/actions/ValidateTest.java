package io.cloudslang.content.xml.actions;

import io.cloudslang.content.xml.utils.Constants;
import org.junit.Assert;
import org.junit.Test;

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
    public void testWithValidXML() {
        Validate toTest = new Validate();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<root someid=\"5\">\n" +
                        "  <element1>First element</element1>\n" +
                        "  <element2>Second element</element2>\n" +
                        "</root>";

        String xsd = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                        "  <xs:element name=\"root\">\n" +
                        "    <xs:complexType>\n" +
                        "      <xs:sequence>\n" +
                        "        <xs:element name=\"element1\" type=\"xs:string\"/>\n" +
                        "        <xs:element name=\"element2\" type=\"xs:string\"/>\n" +
                        "      </xs:sequence>\n" +
                        "      <xs:attribute name=\"someid\" type=\"xs:string\" use=\"required\"/>\n" +
                        "    </xs:complexType>\n" +
                        "  </xs:element>\n" +
                        "</xs:schema>";

        Map<String, String> result = toTest.execute(xml, xsd);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.SUCCESS, resultText);
    }

    @Test
    public void testWithNotValidXML() {
        Validate toTest = new Validate();
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<root>\n" +
                "  <element1>First element</element1>\n" +
                "  <element2>Second element</element2>\n" +
                "</root>";

        String xsd = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "  <xs:element name=\"root\">\n" +
                "    <xs:complexType>\n" +
                "      <xs:sequence>\n" +
                "        <xs:element name=\"element1\" type=\"xs:string\"/>\n" +
                "        <xs:element name=\"element2\" type=\"xs:string\"/>\n" +
                "      </xs:sequence>\n" +
                "      <xs:attribute name=\"someid\" type=\"xs:string\" use=\"required\"/>\n" +
                "    </xs:complexType>\n" +
                "  </xs:element>\n" +
                "</xs:schema>";

        Map<String, String> result = toTest.execute(xml, xsd);
        String resultText = result.get(Constants.OutputNames.RESULT_TEXT);

        Assert.assertEquals(Constants.FAILURE, resultText);
    }

}
