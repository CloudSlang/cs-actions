package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.xml.entities.SimpleNamespaceContext;
import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used for creating @Action operation to edit xml documents.
 * User: turcm
 * Date: 10/16/13
 * Time: 10:57 AM
 */
public class EditXml {

    private String features;

    /**
     * @param xml             The XML (in the form of a String).
     * @param filePath        Absolute or remote path of the XML file.
     * @param action          The edit action to take place.
     *                        Valid values: delete, insert, append, subnode, move, rename, update.
     * @param xpath1          The XPath Query that is wanted to be run.
     *                        The changes take place at the resulting elements.
     * @param xpath2          The XPath Query that is wanted to be run.
     *                        For the move action the results of xpath1 are moved to the results of xpath2.
     * @param value           The new value.
     *                        Examples: <newNode>newNodeValue</newNode> ,
     *                        <newNode newAttribute="newAttributeValue">newNodeValue</newNode>, new value.
     * @param type            Defines on what should the changes take effect :
     *                        the element, the value of the element or the attributes of the element.
     *                        Valid values: elem, text, attr
     * @param name            The name of the attribute in case the selected type is 'attr' .
     * @param parsingFeatures The list of XML parsing features separated by new line (CRLF).
     *                        The feature name - value must be separated by empty space.
     *                        Setting specific features this field could be used to avoid XML security issues like
     *                        "XML Entity Expansion injection" and "XML External Entity injection".
     *                        To avoid aforementioned security issues we strongly recommend to set this input to the following values:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     *                        When the "http://apache.org/xml/features/disallow-doctype-decl" feature is set to "true"
     *                        the parser will throw a FATAL ERROR if the incoming document contains a DOCTYPE declaration.
     *                        When the "http://xml.org/sax/features/external-general-entities" feature is set to "false"
     *                        the parser will not include external general entities.
     *                        When the "http://xml.org/sax/features/external-parameter-entities" feature is set to "false"
     *                        the parser will not include external parameter entities or the external DTD subset.
     *                        If any of the validations fails, the operation will fail with an error message describing the problem.
     *                        Default value:
     *                        http://apache.org/xml/features/disallow-doctype-decl true
     *                        http://xml.org/sax/features/external-general-entities false
     *                        http://xml.org/sax/features/external-parameter-entities false
     * @return map of results containing success or failure text, a result message, and the value selected
     */
    @Action(name = "Edit XML",
            outputs = {
                    @Output(Constants.Outputs.RETURN_RESULT),
                    @Output(Constants.Outputs.RETURN_CODE),
                    @Output(Constants.Outputs.EXCEPTION)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.ReturnCodes.SUCCESS, value = "0"),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.ReturnCodes.FAILURE, value = "-1")})
    public Map<String, String> xPathReplaceNode(
            @Param(value = Constants.Inputs.XML) String xml,
            @Param(value = Constants.Inputs.FILE_PATH) String filePath,
            @Param(value = Constants.Inputs.ACTION, required = true) String action,
            @Param(value = Constants.Inputs.XPATH1, required = true) String xpath1,
            @Param(value = Constants.Inputs.XPATH2) String xpath2,
            @Param(value = Constants.Inputs.VALUE) String value,
            @Param(value = Constants.Inputs.TYPE) String type,
            @Param(value = Constants.Inputs.TYPE_NAME) String name,
            @Param(value = Constants.Inputs.FEATURES) String parsingFeatures) {
        Map<String, String> result = new HashMap<>();
        try {
            validateInputs(xml, filePath, action.toLowerCase(), xpath1, xpath2, type, name, parsingFeatures);
            ActionsEnum myAction = getAction(action);

            switch (myAction) {
                case delete: {
                    result.put(Constants.Outputs.RETURN_RESULT, delete(xml, filePath, xpath1, type, name));
                    break;
                }
                case insert: {
                    result.put(Constants.Outputs.RETURN_RESULT, insert(xml, filePath, xpath1, value, type, name));
                    break;
                }
                case append: {
                    result.put(Constants.Outputs.RETURN_RESULT, append(xml, filePath, xpath1, value, type, name));
                    break;
                }
                case subnode: {
                    result.put(Constants.Outputs.RETURN_RESULT, subnode(xml, filePath, xpath1, value));
                    break;
                }
                case move: {
                    result.put(Constants.Outputs.RETURN_RESULT, move(xml, filePath, xpath1, xpath2));
                    break;
                }
                case rename: {
                    result.put(Constants.Outputs.RETURN_RESULT, rename(xml, filePath, xpath1, name, type, value));
                    break;
                }
                case update: {
                    result.put(Constants.Outputs.RETURN_RESULT, update(xml, filePath, xpath1, value, type, name));
                    break;
                }
            }
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.SUCCESS));
        } catch (IllegalArgumentException e) {
            result.put(Constants.Outputs.EXCEPTION, "Invalid action " + action);
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.FAILURE));
        } catch (Exception e) {
            result.put(Constants.Outputs.EXCEPTION, e.getMessage());
            result.put(Constants.Outputs.RETURN_CODE, String.valueOf(Constants.ReturnCodes.FAILURE));
        }
        return result;
    }

    private ActionsEnum getAction(String action) {
        return ActionsEnum.valueOf(action.toLowerCase());
    }

    /**
     * Validates the operation inputs.
     *
     * @param xml      the xml as String
     * @param filePath the path/remote path to the file
     * @param action   the edit action
     * @param xpath1   the first XPath
     * @param xpath2   the second XPath
     * @param type     the type on which the edit should occur
     * @param name     the name of the attribute
     * @throws Exception for invalid inputs
     */
    private void validateInputs(String xml, String filePath, String action, String xpath1, String xpath2, String type, String name, String features) throws Exception {
        validateXmlAndFilePathInputs(xml, filePath);
        validateIsNotEmpty(action, "ACTION input is required.");
        validateIsNotEmpty(xpath1, "XPATH1 input is required.");

        if (Constants.Inputs.MOVE_ACTION.equals(action)) {
            validateIsNotEmpty(xpath2, "XPATH2 input is required for action 'move' ");
        }
        if (!Constants.Inputs.SUBNODE_ACTION.equals(action) && !Constants.Inputs.MOVE_ACTION.equals(action)) {
            validateIsNotEmpty(type, "TYPE input is required for action '" + action + "'");
            if (!Constants.Inputs.TYPE_ELEM.equals(type) && !Constants.Inputs.TYPE_ATTR.equals(type) && !Constants.Inputs.TYPE_TEXT.equals(type)) {
                throw new Exception("Invalid type. Only supported : " + Constants.Inputs.TYPE_ELEM + ", " + Constants.Inputs.TYPE_ATTR + ", " + Constants.Inputs.TYPE_TEXT);
            }
            if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                validateIsNotEmpty(name, "NAME input is required for type 'attr' ");
            }
        }

        setFeatures(features);
    }

    private void validateIsNotEmpty(String action, String message) throws Exception {
        if (action == null || StringUtils.isEmpty(action)) {
            throw new Exception(message);
        }
    }

    private void validateXmlAndFilePathInputs(String xml, String filePath) throws Exception {
        if ((StringUtils.isBlank(filePath)) && (StringUtils.isBlank(xml))) {
            throw new Exception("Supplied parameters: either file path or xml is missing when one is required");
        }
        if ((!StringUtils.isEmpty(filePath)) && (!StringUtils.isEmpty(xml))) {
            throw new Exception("Supplied parameters: file path and xml when only one is required");
        }
    }

    /**
     * Deletes from an XML (provided as String or file) the element/attribute/value of element at a given XPath.
     *
     * @param xml      the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to delete
     * @param type     the type that should be deleted (element; attribute or text)
     * @param name     if type is 'attr' the name of the attribute to be deleted
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String delete(String xml, String filePath, String xpath, String type, String name) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node node;
        Node parentNode;
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(type) && node != doc.getDocumentElement()) {
                //check if provided xpath doesn't contain root node
                parentNode = node.getParentNode();
                parentNode.removeChild(node);
            } else if (Constants.Inputs.TYPE_TEXT.equals(type)) {
                node.setTextContent(Constants.Inputs.EMPTY_STRING);
            } else if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                ((Element) node).removeAttribute(name);
            }
        }
        return documentToString(doc);
    }

    /**
     * Updates an XML (provided as String or file) with a provided value at a given XPath.
     *
     * @param xml      the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to update
     * @param value    the new value
     * @param type     the type that should be updated (element; attribute or text)
     * @param name     if type is 'attr' the name of the attribute to be updated
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String update(String xml, String filePath, String xpath, String value, String type, String name) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;

        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = stringToNode(value, doc.getXmlEncoding());
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);

            if (Constants.Inputs.TYPE_ELEM.equals(type)) {
                childNode = doc.importNode(childNode, true);
                node.setTextContent(Constants.Inputs.EMPTY_STRING);
                node.appendChild(childNode);
            } else if (Constants.Inputs.TYPE_TEXT.equals(type)) {
                node.setTextContent(value);
            } else if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                String attribute = ((Element) node).getAttribute(name);
                if ((attribute != null) && (!StringUtils.isEmpty(attribute))) {
                    ((Element) node).setAttribute(name, value);
                }
            }
        }
        return documentToString(doc);
    }

    /**
     * Inserts in an XML (provided as String or file) a new element; attribute or text text at a given XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to insert
     * @param value    the new value
     * @param type     the type that should be inserted (element; attribute or text)
     * @param name     if type is 'attr' the name of the attribute to be inserted
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String insert(String xml, String filePath, String xpath, String value, String type, String name) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;
        Node parentNode;
        // create new Node to insert
        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = stringToNode(value, doc.getXmlEncoding());
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(type)) {
                //check if provided xpath doesn't contain root node
                if (node != doc.getDocumentElement()) {
                    childNode = doc.importNode(childNode, true);
                    parentNode = node.getParentNode();
                    parentNode.insertBefore(childNode, node);
                }
            } else if (Constants.Inputs.TYPE_TEXT.equals(type)) {
                node.setTextContent(value + node.getTextContent());
            } else if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                ((Element) node).setAttribute(name, value);
            }
        }
        return documentToString(doc);
    }

    /**
     * Appends in an XML (provided as String or file) a new element; attribute or text text at a given XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to append
     * @param value    the new value
     * @param type     the type that should be append (element; attribute or text)
     * @param name     if type is 'attr' the name of the attribute to be appended
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String append(String xml, String filePath, String xpath, String value, String type, String name) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;
        Node parentNode;
        Node nextSiblingNode;
        // create new Node to append
        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = stringToNode(value, doc.getXmlEncoding());
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(type) && node != doc.getDocumentElement()) {
                //check if provided xpath doesn't contain root node
                childNode = doc.importNode(childNode, true);
                parentNode = node.getParentNode();
                nextSiblingNode = node.getNextSibling();
                parentNode.insertBefore(childNode, nextSiblingNode);
            } else if (Constants.Inputs.TYPE_TEXT.equals(type)) {
                node.setTextContent(node.getTextContent() + value);
            } else if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                ((Element) node).setAttribute(name, value);
            }
        }
        return documentToString(doc);
    }

    /**
     * Renames the tag or attribute of an XML (provided as String or file) at a given XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to append
     * @param name     if type is 'attr' the original name of the attribute to be renamed
     * @param type     the type that should be renamed (element; attribute )
     * @param value    the new value
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String rename(String xml, String filePath, String xpath, String name, String type, String value) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node node;
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(type)) {
                doc.renameNode(node, node.getNamespaceURI(), value);
            } else if (Constants.Inputs.TYPE_ATTR.equals(type)) {
                String attributeValue = ((Element) node).getAttribute(name);
                if ((attributeValue != null) && (!StringUtils.isEmpty(attributeValue))) {
                    ((Element) node).removeAttribute(name);
                    ((Element) node).setAttribute(value, attributeValue);
                }
            }
        }
        return documentToString(doc);
    }

    /**
     * Creates a sub-node in an XML (provided as String or file) at a given XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to append
     * @param value    the new Node
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String subnode(String xml, String filePath, String xpath, String value) throws Exception {
        Document doc = createDocument(xml, filePath);
        NodeList nodeList = readNode(doc, xpath, getNamespaceContext(xml, filePath));
        Node node;
        Node childNode = stringToNode(value, doc.getXmlEncoding());

        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = doc.importNode(childNode, true);
            node = nodeList.item(i);
            node.appendChild(childNode);
        }
        return documentToString(doc);
    }

    /**
     * Moves the Nodes of an XML (provided as String or file) from a give XPath to their new location provided by the second  XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath1   the XPath to move
     * @param xpath2   the XPath to be moved at
     * @return String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    private String move(String xml, String filePath, String xpath1, String xpath2) throws Exception {
        Document doc = createDocument(xml, filePath);
        NamespaceContext ctx = getNamespaceContext(xml, filePath);
        NodeList nodeListToMove = readNode(doc, xpath1, ctx);
        NodeList nodeListWhereToMove = readNode(doc, xpath2, ctx);
        Node nodeToMove;
        Node nodeWhereToMove;

        if (!xpath2.contains(xpath1)) {
            // remove nodes
            for (int i = 0; i < nodeListToMove.getLength(); i++) {
                nodeToMove = nodeListToMove.item(i);
                if (nodeToMove != doc.getDocumentElement()) {
                    nodeToMove.getParentNode().removeChild(nodeToMove);
                }
            }
            //add nodes to designated location
            for (int i = 0; i < nodeListWhereToMove.getLength(); i++) {
                nodeWhereToMove = nodeListWhereToMove.item(i);
                for (int j = 0; j < nodeListToMove.getLength(); j++) {
                    nodeToMove = nodeListToMove.item(j);
                    nodeToMove = doc.importNode(nodeToMove, true);
                    nodeWhereToMove.appendChild(nodeToMove);
                }
            }
        }
        return documentToString(doc);
    }

    /**
     * Return all the nodes in an Document for a given XPath.
     *
     * @param doc        the document to read from
     * @param pathToNode the XPath to find
     * @return a NodeList object
     * @throws XPathExpressionException if  xpath exception occurred
     */
    private NodeList readNode(Document doc, String pathToNode, NamespaceContext ctx) throws XPathExpressionException {
        XPath xPath = createXpath();
        xPath.setNamespaceContext(ctx);
        return (NodeList) xPath.evaluate(pathToNode, doc, XPathConstants.NODESET);
    }

    /**
     * Returns a new instance of XPath.
     *
     * @return XPath object
     */
    private XPath createXpath() {
        return XPathFactory.newInstance().newXPath();
    }

    /**
     * Transforms a String or File provided by path to a Document object.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @return a  Document representation of the String or file
     * @throws Exception in case something goes wrong
     */
    private Document createDocument(String xml, String filePath) throws Exception {
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
     * Returns the InputStream representation of a file or string.
     *
     * @param xml      xml the xml as String
     * @param filePath he path/remote path to the file
     * @return the InputStream representation of a file or string
     * @throws IOException Exception in case something goes wrong
     */
    private InputStream getStream(String xml, String filePath) throws IOException {
        InputStream inputXML;
        if (filePath == null || StringUtils.isEmpty(filePath)) {
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

    /**
     * Returns the Namespaces context from an xml.
     *
     * @param xml      xml as string
     * @param filePath path to xml file
     * @return the Namespaces context from an xml.
     * @throws IOException        file reading exception
     * @throws XMLStreamException parsing exception
     */
    private NamespaceContext getNamespaceContext(String xml, String filePath) throws IOException, XMLStreamException {
        InputStream inputXML = getStream(xml, filePath);
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

    /**
     * @return a new instance of DocumentBuilder
     * @throws ParserConfigurationException
     */
    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        XmlUtils.setFeatures(factory, features);
        factory.setNamespaceAware(false);
        return factory.newDocumentBuilder();
    }

    /**
     * !!! This should be used instead on documentToString which uses deprecated classes. LSSerializer could not be configured to pretty print
     * Returns a String representation for the XML Document.
     *
     * @param xmlDocument the XML Document
     * @return String representation for the XML Document
     */
    private String documentToStringNoPrettyPrint(Document xmlDocument) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) xmlDocument.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
        LSOutput lsOutput = domImplementation.createLSOutput();
        lsOutput.setEncoding(xmlDocument.getXmlEncoding());
        Writer stringWriter = new StringWriter();
        lsOutput.setCharacterStream(stringWriter);
        lsSerializer.write(xmlDocument, lsOutput);
        return stringWriter.toString();
    }

    /**
     * Returns a String representation for the XML Document.
     *
     * @param xmlDocument the XML Document
     * @return String representation for the XML Document
     * @throws IOException
     */
    public String documentToString(Document xmlDocument) throws IOException {
        String encoding = (xmlDocument.getXmlEncoding() == null) ? "UTF-8" : xmlDocument.getXmlEncoding();
        OutputFormat format = new OutputFormat(xmlDocument);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        format.setEncoding(encoding);
        try (Writer out = new StringWriter()) {
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(xmlDocument);
            return out.toString();
        }
    }

    /**
     * Transforms a string representation of an XML Node to an Node
     *
     * @param value the node as String
     * @return the Node object
     * @throws Exception in case the String can't be represented as a Node
     */
    private Node stringToNode(String value, String encoding) throws Exception {
        Node node;
        InputStream inputStream = null;
        if (encoding == null) {
            encoding = "UTF-8";
        }
        try {
            // check if input value is a Node
            inputStream = new ByteArrayInputStream(value.getBytes(encoding));
            Document docNew = createDocumentBuilder().parse(inputStream);
            node = docNew.getDocumentElement();
        } catch (SAXException se) {
            throw new Exception("Value " + value + "is not valid XML element : " + se.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return node;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    /**
     * Enum of all possible edit actions of the operation.
     */
    public enum ActionsEnum {
        delete(Constants.Inputs.DELETE_ACTION),
        insert(Constants.Inputs.INSERT_ACTION),
        append(Constants.Inputs.APPEND_ACTION),
        subnode(Constants.Inputs.SUBNODE_ACTION),
        move(Constants.Inputs.MOVE_ACTION),
        rename(Constants.Inputs.RENAME_ACTION),
        update(Constants.Inputs.UPDATE_ACTION);
        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}

