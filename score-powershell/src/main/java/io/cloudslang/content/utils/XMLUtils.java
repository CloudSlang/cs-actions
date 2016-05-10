package io.cloudslang.content.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by giloan on 3/28/2016.
 */
public class XMLUtils {

    private static final String RESPONSE_IS_NOT_WELL_FORMED = "The http response document is not a Well-formed XML: ";

    private XMLUtils() {
    }

    /**
     * Parse the content of the given xml input and evaluates the given XPath expression.
     *
     * @param xml        The xml source.
     * @param expression The XPath expression.
     * @return Evaluate the XPath expression and return the result as a String.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     */
    public static String parseXml(String xml, String expression) throws ParserConfigurationException, IOException, XPathExpressionException, SAXException {
        DocumentBuilder builder = ResourceLoader.getDocumentBuilder();
        Document document;
        try {
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException e) {
            throw new RuntimeException(RESPONSE_IS_NOT_WELL_FORMED + xml, e);
        }
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.compile(expression).evaluate(document);
    }
}
