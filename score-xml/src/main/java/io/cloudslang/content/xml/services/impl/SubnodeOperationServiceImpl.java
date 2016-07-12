package io.cloudslang.content.xml.services.impl;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.DocumentUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by moldovas on 7/8/2016.
 */
public class SubnodeOperationServiceImpl implements OperationService {
    /**
     * Creates a sub-node in an XML (provided as String or file) at a given XPath.
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
        Node childNode = XmlUtils.stringToNode(inputs.getValue(), doc.getXmlEncoding(), inputs.getParsingFeatures());

        for (int i = 0; i < nodeList.getLength(); i++) {
            childNode = doc.importNode(childNode, true);
            node = nodeList.item(i);
            node.appendChild(childNode);
        }
        return DocumentUtils.documentToString(doc);
    }
}
