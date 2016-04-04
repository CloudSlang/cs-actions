package io.cloudslang.content.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by giloan on 3/28/2016.
 */
public class XMLUtils {

    public static String transformDocumentToString(Document document) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

    public static String parseXml(String xml, String expression) throws ParserConfigurationException, IOException, XPathExpressionException, SAXException {
        DocumentBuilder builder = ResourceLoader.getDocumentBuilder();
        Document document;
        try {
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException e) {
            throw new SAXException("The http response document is not a Wellformed XML: " + xml, e);
        }
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath.compile(expression).evaluate(document);
    }

    public static Element getDocumentElement(Document doc, String headerName) {
        return (Element) doc.getElementsByTagName(headerName).item(0);
    }
}
