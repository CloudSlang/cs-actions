package io.cloudslang.content.xml.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by markowis on 23/02/2016.
 */
public class XmlUtils {
    public static String nodeToString(Node node) throws TransformerException {

        if(node == null){
            return "";
        } else if(node.getNodeType() == 2) {
            return node.toString();
        }

        StringWriter sw = new StringWriter();

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(node), new StreamResult(sw));

        return sw.toString().trim();
    }

    public static NamespaceContext createNamespaceContext(String xmlDocument) throws Exception{
        NamespaceContext nsContext = null;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader evtReader = factory.createXMLEventReader(new StringReader(xmlDocument));
        while (evtReader.hasNext()) {
            XMLEvent event = evtReader.nextEvent();
            if (event.isStartElement()) {
                nsContext = ((StartElement) event)
                        .getNamespaceContext();
                break;
            }
        }

        return nsContext;
    }

    public static Document parseXML(String xmlDocument, boolean secure) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,secure);
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new InputSource(new StringReader(xmlDocument)));
        return doc;
    }

    public static NodeList evaluateXPathQuery(Document doc, NamespaceContext context, String xPathQuery) throws Exception{
        XPath xpath =  XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(context);
        XPathExpression expr = xpath.compile(xPathQuery);

        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nl;
    }

    public static boolean addAttributesToList(NodeList nodeList, String name, String value) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE){
                return false;
            }

            ((Element) node).setAttribute(name, value);
        }
        return true;
    }
}
