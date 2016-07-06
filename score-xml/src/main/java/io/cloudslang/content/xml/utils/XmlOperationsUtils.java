package io.cloudslang.content.xml.utils;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;

/**
 * Created by moldovas on 7/6/2016.
 */
public class XmlOperationsUtils {
    /**
     * Deletes from an XML (provided as String or file) the element/attribute/value of element at a given XPath.
     *
     * @param xml      the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to delete
     * @param type     the type that should be deleted (element; attribute or text)
     * @param name     if type is 'attr' the name of the attribute to be deleted
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String delete(String xml, String filePath, String xpath, String type, String name, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
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
        return DocumentUtils.documentToString(doc);
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
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String update(String xml, String filePath, String xpath, String value, String type, String name, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;

        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = XmlUtils.stringToNode(value, doc.getXmlEncoding(), features);
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
        return DocumentUtils.documentToString(doc);
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
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String insert(String xml, String filePath, String xpath, String value, String type, String name, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;
        Node parentNode;
        // create new Node to insert
        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = XmlUtils.stringToNode(value, doc.getXmlEncoding(), features);
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
        return DocumentUtils.documentToString(doc);
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
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String append(String xml, String filePath, String xpath, String value, String type, String name, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
        Node childNode = null;
        Node node;
        Node parentNode;
        Node nextSiblingNode;
        // create new Node to append
        if (Constants.Inputs.TYPE_ELEM.equals(type)) {
            childNode = XmlUtils.stringToNode(value, doc.getXmlEncoding(), features);
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
        return DocumentUtils.documentToString(doc);
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
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String rename(String xml, String filePath, String xpath, String name, String type, String value, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
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
        return DocumentUtils.documentToString(doc);
    }

    /**
     * Creates a sub-node in an XML (provided as String or file) at a given XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath    the XPath were to append
     * @param value    the new Node
     * @param features parsing features to set on the document builder
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String createSubnode(String xml, String filePath, String xpath, String value, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NodeList nodeList = XmlUtils.readNode(doc, xpath, XmlUtils.getNamespaceContext(xml, filePath));
        Node node;
        Node childNode = XmlUtils.stringToNode(value, doc.getXmlEncoding(), features);

        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = doc.importNode(childNode, true);
            node = nodeList.item(i);
            node.appendChild(childNode);
        }
        return DocumentUtils.documentToString(doc);
    }

    /**
     * Moves the Nodes of an XML (provided as String or file) from a give XPath to their new location provided by the second  XPath.
     *
     * @param xml      xml the xml as String
     * @param filePath the path/remote path to the file
     * @param xpath1   the XPath to move
     * @param xpath2   the XPath to be moved at
     * @param features parsing features to set on the document builder
     * @return String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    public static String move(String xml, String filePath, String xpath1, String xpath2, String features) throws Exception {
        Document doc = XmlUtils.createDocument(xml, filePath, features);
        NamespaceContext ctx = XmlUtils.getNamespaceContext(xml, filePath);
        NodeList nodeListToMove = XmlUtils.readNode(doc, xpath1, ctx);
        NodeList nodeListWhereToMove = XmlUtils.readNode(doc, xpath2, ctx);
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
        return DocumentUtils.documentToString(doc);
    }
}
