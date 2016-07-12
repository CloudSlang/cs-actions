package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.ResultUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
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
public class AddAttributeService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs){
        Map<String, String> result = new HashMap<>();

        try {
            Document doc = XmlUtils.getDocument(commonInputs);
            NamespaceContext context = XmlUtils.getNamespaceContext(commonInputs, doc);

            NodeList nodeList = XmlUtils.evaluateXPathQuery(doc, context, commonInputs.getXPathQuery());

            XmlUtils.validateNodeList(nodeList);

            addAttributesToNodeList(nodeList, customInputs.getAttributeName(), customInputs.getValue());
            ResultUtils.populateSuccessResult(result, Constants.SuccessMessages.ADD_ATTRIBUTE_SUCCESS, XmlUtils.nodeToString(doc));

        } catch (XPathExpressionException e) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.XPATH_PARSING_ERROR + e.getMessage());
        } catch (TransformerException te) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.TRANSFORMER_ERROR + te.getMessage());
        } catch (Exception e) {
            ResultUtils.populateFailureResult(result, Constants.ErrorMessages.GENERAL_ERROR + e.getMessage());
        }

        return result;
    }

    private static void addAttributesToNodeList(NodeList nodeList, String name, String value) throws Exception{
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE){
                throw new Exception(Constants.ErrorMessages.ADD_ATTRIBUTE_FAILURE + Constants.ErrorMessages.NEED_ELEMENT_TYPE);
            }

            ((Element) node).setAttribute(name, value);
        }
    }
}
