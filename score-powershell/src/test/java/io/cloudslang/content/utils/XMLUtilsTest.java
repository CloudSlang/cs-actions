package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by giloan on 5/9/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(XMLUtils.class)
public class XMLUtilsTest {

    private static final String xml = "<s:Envelope xml:lang=\"en-US\"\n" +
            "            xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
            "            xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"\n" +
            "            xmlns:w=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\"\n" +
            "            xmlns:rsp=\"http://schemas.microsoft.com/wbem/wsman/1/windows/shell\"\n" +
            "            xmlns:p=\"http://schemas.microsoft.com/wbem/wsman/1/wsman.xsd\">\n" +
            "\t<s:Header>\n" +
            "\t\t<a:Action>http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse</a:Action>\n" +
            "\t\t<a:MessageID>uuid:3F8709D8-2EE6-4D90-9092-081411C49E2A</a:MessageID>\n" +
            "\t\t<a:To>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</a:To>\n" +
            "\t\t<a:RelatesTo>uuid:27bd8084-22ac-4a7e-83e6-3326cb6cd2bf</a:RelatesTo>\n" +
            "\t</s:Header>\n" +
            "\t<s:Body>\n" +
            "\t\t<rsp:ReceiveResponse>\n" +
            "\t\t\t<rsp:Stream Name=\"stdout\"\n" +
            "\t\t\t            CommandId=\"CE0D0C63-9A3A-4638-AA95-DD305D4848CB\">Y29tcGF0aWJpbGl0eQ==</rsp:Stream>\n" +
            "\t\t\t<rsp:Stream Name=\"stdout\"\n" +
            "\t\t\t            CommandId=\"CE0D0C63-9A3A-4638-AA95-DD305D4848CB\">DQpjb250ZW50DQpkb2NzDQppbnN0YWxsZXItbGludXg2NC5iaW4NCmluc3RhbGxlci13aW42NC1zdHVkaW8uZXhlDQppbnN0YWxsZXItd2luNjQuZXhlDQpsaWNlbnNlDQpvby0xMC42MC4wMC1SQzEuemlwDQpQZXJmTG9ncw0KUHJvZ3JhbSBGaWxlcw0KUHJvZ3JhbSBGaWxlcyAoeDg2KQ0KUHJvZ3JhbXMNCnJlbGVhc2Utbm90ZXMucGRmDQpzZGsNCnNvdXJjZXMNCnVwZ3JhZGUuemlwDQpVc2Vycw0KV2luZG93cw0K</rsp:Stream>\n" +
            "\t\t\t<rsp:Stream Name=\"stdout\"\n" +
            "\t\t\t            CommandId=\"CE0D0C63-9A3A-4638-AA95-DD305D4848CB\"\n" +
            "\t\t\t            End=\"true\"/>\n" +
            "\t\t\t<rsp:CommandState CommandId=\"CE0D0C63-9A3A-4638-AA95-DD305D4848CB\"\n" +
            "\t\t\t                  State=\"http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done\">\n" +
            "\t\t\t\t<rsp:ExitCode>0</rsp:ExitCode>\n" +
            "\t\t\t</rsp:CommandState>\n" +
            "\t\t</rsp:ReceiveResponse>\n" +
            "\t</s:Body>\n" +
            "</s:Envelope>";

    private static final String HEADER_XPATH = "/Envelope/Header";
    private static final String COMMAND_STATE_XPATH = "/Envelope/Body/ReceiveResponse/CommandState/@State";
    private static final String COUNT_STREAMS_XPATH = "count(//Envelope/Body/ReceiveResponse/Stream)";
    private static final String SCRIPT_EXIT_CODE_XPATH = "/Envelope/Body/ReceiveResponse/CommandState/ExitCode";

    private static final String DONE_COMMAND_STATE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done";
    private static final String RECEIVE_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String RESPONSE_IS_NOT_WELL_FORMED = "The http response document is not a Well-formed XML: ";

    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    @Test
    public void testParseXml() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        assertTrue(StringUtils.contains(XMLUtils.parseXml(xml, HEADER_XPATH), RECEIVE_RESPONSE_ACTION));
        assertEquals(DONE_COMMAND_STATE_ACTION, XMLUtils.parseXml(xml, COMMAND_STATE_XPATH));
        assertEquals("3", XMLUtils.parseXml(xml, COUNT_STREAMS_XPATH));
        assertEquals("0", XMLUtils.parseXml(xml, SCRIPT_EXIT_CODE_XPATH));
    }

    @Test
    public void testParseXmlThrowsException() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        thrownException.expectMessage(RESPONSE_IS_NOT_WELL_FORMED);
        XMLUtils.parseXml("<note>\n" +
                "<to>Tove</to>\n" +
                "<from>Jani</from>", HEADER_XPATH);
    }
}
