/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.ResultUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class SetValueService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) {
        Map<String, String> result = new HashMap<>();

        try {
            Document doc = XmlUtils.getDocument(commonInputs);
            NamespaceContext context = XmlUtils.getNamespaceContext(commonInputs, doc);
            NodeList nodeList = XmlUtils.evaluateXPathQuery(doc, context, commonInputs.getXPathQuery());

            XmlUtils.validateNodeList(nodeList);

            setValueToNodeList(nodeList, customInputs.getAttributeName(), customInputs.getValue());
            ResultUtils.populateSuccessResult(result, Constants.SuccessMessages.SET_VALUE_SUCCESS,
                    XmlUtils.nodeToString(doc));

        } catch (XPathExpressionException e) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.XPATH_PARSING_ERROR + e.getMessage());
        } catch (TransformerException te) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.TRANSFORMER_ERROR + te.getMessage());
        } catch (Exception e) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.PARSING_ERROR + e.getMessage());
        }

        return result;
    }

    private static void setValueToNodeList(NodeList nodeList, String attributeName, String value) throws Exception {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() != Node.ELEMENT_NODE) {
                throw new Exception(Constants.ErrorMessages.SET_VALUE_FAILURE +
                        Constants.ErrorMessages.NEED_ELEMENT_TYPE);
            }

            if (StringUtils.isBlank(attributeName)) {
                node.setTextContent(value);
            } else {
                ((Element) node).setAttribute(attributeName, value);
            }
        }
    }
}
