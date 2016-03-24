package io.cloudslang.content.xml.services;

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
            Document doc = XmlUtils.parseXML(commonInputs.getXmlDocument(), commonInputs.getSecureProcessing());
            NamespaceContext context = XmlUtils.createNamespaceContext(commonInputs.getXmlDocument());
            NodeList nodeList = XmlUtils.evaluateXPathQuery(doc, context, commonInputs.getXPathQuery());

            XmlUtils.validateNodeList(nodeList);

            addAttributesToNodeList(nodeList, customInputs.getAttributeName(), customInputs.getValue());
            ResultUtils.populateSuccessResult(result, "Attribute set successfully.", XmlUtils.nodeToString(doc));

        } catch (XPathExpressionException e) {
            ResultUtils.populateFailureResult(result, "XPath parsing error: " + e.getMessage());
        } catch (TransformerException te) {
            ResultUtils.populateFailureResult(result, "Transformer error: " + te.getMessage());
        } catch (Exception e) {
            ResultUtils.populateFailureResult(result, "Error: " + e.getMessage());
        }

        return result;
    }

    private static void addAttributesToNodeList(NodeList nodeList, String name, String value) throws Exception{
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE){
                throw new Exception("Addition of attribute failed: XPath must return element types.");
            }

            ((Element) node).setAttribute(name, value);
        }
    }
}
