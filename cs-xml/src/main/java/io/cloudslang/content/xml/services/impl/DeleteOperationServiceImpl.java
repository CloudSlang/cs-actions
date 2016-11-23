package io.cloudslang.content.xml.services.impl;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.DocumentUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by moldovas on 7/8/2016.
 */
public class DeleteOperationServiceImpl implements OperationService {

    /**
     * Deletes from an XML (provided as String or file) the element/attribute/value of element at a given XPath.
     *
     * @param inputs inputs
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    @Override
    public String execute(EditXmlInputs inputs) throws Exception {
        Document doc = XmlUtils.createDocument(inputs.getXml(), inputs.getFilePath(), inputs.getParsingFeatures());
        NodeList nodeList = XmlUtils.readNode(doc, inputs.getXpath1(), XmlUtils.getNamespaceContext(inputs.getXml(), inputs.getFilePath()));
        Node node;
        Node parentNode;
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType()) && node != doc.getDocumentElement()) {
                //check if provided xpath doesn't contain root node
                parentNode = node.getParentNode();
                parentNode.removeChild(node);
            } else if (Constants.Inputs.TYPE_TEXT.equals(inputs.getType())) {
                node.setTextContent(Constants.Inputs.EMPTY_STRING);
            } else if (Constants.Inputs.TYPE_ATTR.equals(inputs.getType())) {
                ((Element) node).removeAttribute(inputs.getName());
            }
        }
        return DocumentUtils.documentToString(doc);
    }
}
