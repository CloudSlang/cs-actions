package io.cloudslang.content.xml.services.impl;

import io.cloudslang.content.xml.entities.inputs.EditXmlInputs;
import io.cloudslang.content.xml.services.OperationService;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.DocumentUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by moldovas on 7/8/2016.
 */
public class UpdateOperationServiceImpl implements OperationService {
    /**
     * Updates an XML (provided as String or file) with a provided value at a given XPath.
     *
     * @param inputs inputs
     * @return a String representation of the modified XML
     * @throws Exception in case something goes wrong
     */
    @Override
    public String execute(EditXmlInputs inputs) throws Exception {
        Document doc = XmlUtils.createDocument(inputs.getXml(), inputs.getFilePath(), inputs.getParsingFeatures());
        NodeList nodeList = XmlUtils.readNode(doc, inputs.getXpath1(), XmlUtils.getNamespaceContext(inputs.getXml(), inputs.getFilePath()));
        Node childNode = null;
        Node node;

        if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType())) {
            childNode = XmlUtils.stringToNode(inputs.getValue(), doc.getXmlEncoding(), inputs.getParsingFeatures());
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);

            if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType())) {
                childNode = doc.importNode(childNode, true);
                node.setTextContent(Constants.Inputs.EMPTY_STRING);
                node.appendChild(childNode);
            } else if (Constants.Inputs.TYPE_TEXT.equals(inputs.getType())) {
                node.setTextContent(inputs.getValue());
            } else if (Constants.Inputs.TYPE_ATTR.equals(inputs.getType())) {
                String attribute = ((Element) node).getAttribute(inputs.getName());
                if ((attribute != null) && (!StringUtils.isEmpty(attribute))) {
                    ((Element) node).setAttribute(inputs.getName(), inputs.getValue());
                }
            }
        }
        return DocumentUtils.documentToString(doc);
    }
}
