package io.cloudslang.content.xml.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by markowis on 23/02/2016.
 */
public class XmlUtils {
    private XmlUtils(){}

    public static String nodeToString(Node node) throws TransformerException {

        if(node == null){
            return Constants.EMPTY_STRING;
        } else if(node.getNodeType() == Node.ATTRIBUTE_NODE) {
            return node.toString();
        } else {
            return transformElementNode(node);
        }
    }

    public static NamespaceContext createNamespaceContext(String xmlDocument) throws Exception{
        NamespaceContext nsContext = null;

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        XMLStreamReader streamReader = xif.createXMLStreamReader(new StringReader(xmlDocument));

        while (streamReader.hasNext()) {
            streamReader.next();
            if (streamReader.isStartElement()) {
                nsContext = streamReader.getNamespaceContext();
                break;
            }
        }

        return nsContext;
    }

    public static Document parseXML(String xmlDocument, boolean secure) throws Exception{
        String feature;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        feature = "http://xml.org/sax/features/external-general-entities";
        factory.setFeature(feature, false);
        feature = "http://xml.org/sax/features/external-parameter-entities";
        factory.setFeature(feature, false);
        feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
        factory.setFeature(feature, false);
        feature = "http://apache.org/xml/features/disallow-doctype-decl";
        factory.setFeature(feature, true);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, secure);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new InputSource(new StringReader(xmlDocument)));
    }

    public static NodeList evaluateXPathQuery(Document doc, NamespaceContext context, String xPathQuery) throws XPathExpressionException{
        XPathExpression expr = createXPathExpression(context, xPathQuery);
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

    public static XPathExpression createXPathExpression(NamespaceContext context, String xPathQuery) throws XPathExpressionException{
        XPath xpath =  XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(context);
        return xpath.compile(xPathQuery);
    }

    public static void validateNodeList(NodeList nodeList) throws Exception{
        if(nodeList.getLength() == 0){
            throw new Exception(Constants.ErrorMessages.ELEMENT_NOT_FOUND);
        }
    }

    private static String transformElementNode(Node node) throws TransformerException{
        StringWriter stringWriter = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, Constants.YES);
        transformer.setOutputProperty(OutputKeys.INDENT, Constants.YES);
        transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

        return stringWriter.toString().trim();
    }
}
