package io.cloudslang.content.xml.utils;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.build.auth.AuthTypes;
import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.entities.SimpleNamespaceContext;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class XmlUtils {
    private static final String OK_STATUS_CODE = "200";

    private XmlUtils() {
    }

    public static String nodeToString(Node node) throws TransformerException {

        if (node == null) {
            return Constants.EMPTY_STRING;
        } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            return node.toString();
        } else {
            return transformElementNode(node);
        }
    }

    /**
     * Returns the Namespaces context from an xml.
     *
     * @param xmlString      xml as string
     * @param xmlFilePath path to xml file
     * @return the Namespaces context from an xml.
     * @throws IOException        file reading exception
     * @throws XMLStreamException parsing exception
     */
    public static NamespaceContext getNamespaceContext(String xmlString, String xmlFilePath) throws Exception {
        InputStream inputXML = getStream(xmlString, xmlFilePath);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(inputXML);
        Map<String, String> namespaces = new HashMap<>();
        while (reader.hasNext()) {
            int evt = reader.next();
            if (evt == XMLStreamConstants.START_ELEMENT) {
                QName qName = reader.getName();
                if (qName != null) {
                    if (qName.getPrefix() != null && qName.getPrefix().compareTo("") != 0)
                        namespaces.put(qName.getPrefix(), qName.getNamespaceURI());
                }
            }
        }
        return new SimpleNamespaceContext(namespaces);
    }

    public static Document parseXML(String xmlDocument, boolean secure) throws Exception {
        DocumentBuilder builder = getDocumentBuilder(secure);

        return builder.parse(new InputSource(new StringReader(xmlDocument)));
    }

    public static DocumentBuilder getDocumentBuilder(boolean secure) throws ParserConfigurationException {
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
        return factory.newDocumentBuilder();
    }

    /**
     * This method creates an XML document from a given path which respects the encoding specified in the xml header. Otherwise it defaults to UTF-8.
     *
     * @param path xml given path
     * @param secure secure
     * @return created xml document form the given path
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document createDocumentFromFile(String path, boolean secure) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilder builder = XmlUtils.getDocumentBuilder(secure);
        File initialFile = new File(path);
        InputStream targetStream = new FileInputStream(initialFile);
        InputSource is = new InputSource(targetStream);
        return builder.parse(is);
    }

    /**
     * Transforms a String or File provided by path to a Document object.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param features parsing features to set on the document builder
     * @return a  Document representation of the String or file
     * @throws Exception in case something goes wrong
     */
    public static Document createDocument(String xml, String filePath, String features) throws Exception {
        Document xmlDocument;
        try {
            InputStream inputXML = getStream(xml, filePath);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            XmlUtils.setFeatures(builderFactory, features);

            builderFactory.setNamespaceAware(true);
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            xmlDocument = builder.parse(inputXML);
        } catch (MalformedURLException e) {
            throw new Exception((new StringBuilder("Unable to open remote file requested, file path[")).append(filePath).append("], error[").append(e.getMessage()).append("]").toString(), e);
        } catch (IOException e) {
            throw new Exception((new StringBuilder("Unable to open file requested, filename[")).append(filePath).append("], error[").append(e.getMessage()).append("]").toString(), e);
        }
        return xmlDocument;
    }

    /**
     * Return all the nodes in an Document for a given XPath.
     *
     * @param doc        the document to read from
     * @param pathToNode the XPath to find
     * @return a NodeList object
     * @throws XPathExpressionException if  xpath exception occurred
     */
    public static NodeList readNode(Document doc, String pathToNode, NamespaceContext ctx) throws XPathExpressionException {
        XPath xPath = createXpath();
        xPath.setNamespaceContext(ctx);
        return (NodeList) xPath.evaluate(pathToNode, doc, XPathConstants.NODESET);
    }

    /**
     * Transforms a string representation of an XML Node to an Node
     *
     * @param value the node as String
     * @param features parsing features to set on the document builder
     * @return the Node object
     * @throws Exception in case the String can't be represented as a Node
     */
    public static Node stringToNode(String value, String encoding, String features) throws Exception {
        Node node;
        if (StringUtils.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        try (InputStream inputStream = new ByteArrayInputStream(value.getBytes(encoding))){
            // check if input value is a Node
            Document docNew = DocumentUtils.createDocumentBuilder(features).parse(inputStream);
            node = docNew.getDocumentElement();
        } catch (SAXException se) {
            throw new Exception("Value " + value + "is not valid XML element : " + se.getMessage());
        }
        return node;
    }

    /**
     * Returns a new instance of XPath.
     *
     * @return XPath object
     */
    private static XPath createXpath() {
        return XPathFactory.newInstance().newXPath();
    }

    /**
     * Returns the InputStream representation of a file or string.
     *
     * @param xml      xml the xml as String
     * @param filePath he path/remote path to the file
     * @return the InputStream representation of a file or string
     * @throws IOException Exception in case something goes wrong
     */
    private static InputStream getStream(String xml, String filePath) throws IOException {
        InputStream inputXML;
        if (StringUtils.isEmpty(filePath)) {
            inputXML = new ByteArrayInputStream(xml.getBytes());
        } else {
            if (filePath.startsWith(Constants.Inputs.HTTP_PREFIX_STRING) || filePath.startsWith(Constants.Inputs.HTTPS_PREFIX_STRING)) {
                inputXML = new java.net.URL(filePath).openStream();
            } else {
                inputXML = new FileInputStream(new File(filePath));
            }
        }
        return inputXML;
    }

    public static StringWriter getStringWriter(Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, streamResult);
        return writer;
    }


    public static NodeList evaluateXPathQuery(Document doc, NamespaceContext context, String xPathQuery) throws XPathExpressionException {
        XPathExpression expr = createXPathExpression(context, xPathQuery);
        return (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }

    public static XPathExpression createXPathExpression(NamespaceContext context, String xPathQuery) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(context);
        return xpath.compile(xPathQuery);
    }

    public static void validateNodeList(NodeList nodeList) throws Exception {
        if (nodeList.getLength() == 0) {
            throw new Exception(Constants.ErrorMessages.ELEMENT_NOT_FOUND);
        }
    }

    private static String transformElementNode(Node node) throws TransformerException {
        StringWriter stringWriter = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, Constants.YES);
        transformer.setOutputProperty(OutputKeys.INDENT, Constants.YES);
        transformer.transform(new DOMSource(node), new StreamResult(stringWriter));

        return stringWriter.toString().trim();
    }

    public static Document getDocument(CommonInputs commonInputs) throws Exception {
        Document doc;
        if (Constants.XML_PATH.equalsIgnoreCase(commonInputs.getXmlDocumentSource())) {
            doc = XmlUtils.createDocumentFromFile(commonInputs.getXmlDocument(), commonInputs.getSecureProcessing());
        } else {
            doc = XmlUtils.parseXML(commonInputs.getXmlDocument(), commonInputs.getSecureProcessing());
        }
        return doc;
    }

    public static NamespaceContext getNamespaceContext(CommonInputs commonInputs, Document doc) throws Exception {
        NamespaceContext context;
        if (Constants.XML_PATH.equalsIgnoreCase(commonInputs.getXmlDocumentSource())) {
            StringWriter writer = XmlUtils.getStringWriter(doc);
            context = XmlUtils.getNamespaceContext(writer.toString(), "");
        } else {
            context = XmlUtils.getNamespaceContext(commonInputs.getXmlDocument(), "");
        }
        return context;
    }

    public static String createXmlDocumentFromUrl(CommonInputs commonInputs) throws ParserConfigurationException, SAXException, IOException {
            CSHttpClient scoreHttpClient = new CSHttpClient();
            HttpClientInputs httpClientInputs = new HttpClientInputs();
            httpClientInputs.setMethod(HttpGet.METHOD_NAME);
            httpClientInputs.setUrl(commonInputs.getXmlDocument());

            if (commonInputs.getUsername().isEmpty()) {
                httpClientInputs.setAuthType(AuthTypes.ANONYMOUS);
            } else {
                httpClientInputs.setAuthType(AuthTypes.BASIC);
                httpClientInputs.setUsername(commonInputs.getUsername());
                httpClientInputs.setPassword(commonInputs.getPassword());
            }
            httpClientInputs.setRequestCharacterSet(StandardCharsets.UTF_8.toString());
            httpClientInputs.setResponseCharacterSet(StandardCharsets.UTF_8.toString());
            httpClientInputs.setTrustAllRoots(commonInputs.getTrustAllRoots());
            httpClientInputs.setKeystore(commonInputs.getKeystore());
            httpClientInputs.setKeystorePassword(commonInputs.getKeystorePassword());
            httpClientInputs.setTrustKeystore(commonInputs.getTrustKeystore());
            httpClientInputs.setTrustPassword(commonInputs.getTrustPassword());
            httpClientInputs.setX509HostnameVerifier(commonInputs.getX509Hostnameverifier());
            httpClientInputs.setProxyHost(commonInputs.getProxyHost());
            httpClientInputs.setProxyPort(commonInputs.getProxyPort());
            httpClientInputs.setProxyUsername(commonInputs.getProxyUsername());
            httpClientInputs.setProxyPassword(commonInputs.getProxyPassword());

            Map<String, String> requestResponse = scoreHttpClient.execute(httpClientInputs);
            if (!OK_STATUS_CODE.equals(requestResponse.get(CSHttpClient.STATUS_CODE))) {
                throw new RuntimeException("Http request to specified URL: " + commonInputs.getXmlDocument() + " failed with status code: " + requestResponse.get(CSHttpClient.STATUS_CODE) + ". Request response is: " + requestResponse.get(Constants.Outputs.RETURN_RESULT));
            }
            return requestResponse.get(Constants.Outputs.RETURN_RESULT);
    }

    public static void setFeatures(DocumentBuilderFactory reader, String features) throws ParserConfigurationException {
        if(StringUtils.isNotBlank(features)) {
            Map featuresMap = parseFeatures(features);

            for (Object o : featuresMap.keySet()) {
                String key = (String) o;
                reader.setFeature(key, (Boolean) featuresMap.get(key));
            }
        }

    }

    private static Map<String, Boolean> parseFeatures(String features) {
        Map<String, Boolean> map = new HashMap<>();
        String[] featuresList = features.split("\\n");
        int len$ = featuresList.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String element = featuresList[i$];
            String[] keyValue = element.split("\\s");
            if(keyValue.length != 2) {
                throw new IllegalArgumentException("Wrong format for \'features\' input field!");
            }

            map.put(keyValue[0], Boolean.valueOf(keyValue[1]));
        }

        return map;
    }
}
