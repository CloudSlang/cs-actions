package io.cloudslang.content.xml.utils;

import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.ScoreHttpClient;
import io.cloudslang.content.httpclient.build.auth.AuthTypes;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.http.client.methods.HttpGet;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
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

    public static NamespaceContext createNamespaceContext(String xmlDocument) throws Exception {
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
     * @param path
     * @param secure
     * @return
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
            context = XmlUtils.createNamespaceContext(writer.toString());
        } else {
            context = XmlUtils.createNamespaceContext(commonInputs.getXmlDocument());
        }
        return context;
    }

    public static String createXmlDocumentFromUrl(CommonInputs commonInputs) throws ParserConfigurationException, SAXException, IOException {
        if (commonInputs.getXmlDocument().startsWith("http")) {
            ScoreHttpClient scoreHttpClient = new ScoreHttpClient();
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
//            httpClientInputs.setHeaders("Content-Type: application/xml");
//            httpClientInputs.setHeaders("Accept: application/xml");

            Map<String, String> requestResponse = scoreHttpClient.execute(httpClientInputs);
            if (!OK_STATUS_CODE.equals(requestResponse.get(ScoreHttpClient.STATUS_CODE))) {
                throw new RuntimeException("Http request to specified URL: " + commonInputs.getXmlDocument() + " failed with status code: " + requestResponse.get(ScoreHttpClient.STATUS_CODE) + ". Request response is: " + requestResponse.get(Constants.OutputNames.RETURN_RESULT));
            }
            return requestResponse.get(Constants.OutputNames.RETURN_RESULT);
        } else {
            InputStream inputStream = new FileInputStream(new File(commonInputs.getXmlDocument()));
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8.toString());
        }
    }

    public static void setFeatures(DocumentBuilderFactory reader, String features) throws ParserConfigurationException {
        if(!StringUtils.isEmpty(features)) {
            Map featuresMap = parseFeatures(features);
            Iterator it = featuresMap.keySet().iterator();

            while(it.hasNext()) {
                String key = (String)it.next();
                reader.setFeature(key, ((Boolean)featuresMap.get(key)).booleanValue());
            }
        }

    }

    private static Map<String, Boolean> parseFeatures(String features) {
        HashMap map = new HashMap();
        String[] featuresList = features.split("\\n");
        String[] arr$ = featuresList;
        int len$ = featuresList.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String element = arr$[i$];
            String[] keyValue = element.split("\\s");
            if(keyValue.length != 2) {
                throw new IllegalArgumentException("Wrong format for \'features\' input field!");
            }

            map.put(keyValue[0], Boolean.valueOf(keyValue[1]));
        }

        return map;
    }
}
