/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
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
public class RenameOperationServiceImpl implements OperationService {
    /**
     * Renames the tag or attribute of an XML (provided as String or file) at a given XPath.
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
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (Constants.Inputs.TYPE_ELEM.equals(inputs.getType())) {
                doc.renameNode(node, node.getNamespaceURI(), inputs.getValue());
            } else if (Constants.Inputs.TYPE_ATTR.equals(inputs.getType())) {
                String attributeValue = ((Element) node).getAttribute(inputs.getName());
                if ((attributeValue != null) && (!StringUtils.isEmpty(attributeValue))) {
                    ((Element) node).removeAttribute(inputs.getName());
                    ((Element) node).setAttribute(inputs.getValue(), attributeValue);
                }
            }
        }
        return DocumentUtils.documentToString(doc);
    }
}
