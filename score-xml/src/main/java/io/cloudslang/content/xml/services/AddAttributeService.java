package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
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

            if(nodeList.getLength() == 0){
                populateResult(result, Constants.FAILURE, "Attribute not added: element not found.");
                return result;
            }

            if(XmlUtils.addAttributesToList(nodeList, customInputs.getAttributeName(), customInputs.getValue())){
                populateResult(result, Constants.SUCCESS, "Attribute set successfully.", XmlUtils.nodeToString(doc));
            } else {
                populateResult(result, Constants.FAILURE, "Addition of attribute failed: XPath must return element types.");
            }

        } catch (XPathExpressionException e) {
            populateResult(result, Constants.FAILURE, "XPath parsing error: " + e.getMessage());
        } catch (TransformerException te) {
            populateResult(result, Constants.FAILURE, "Transformer error: " + te.getMessage());
        } catch (Exception e) {
            populateResult(result, Constants.FAILURE, "Parsing error: " + e.getMessage());
        }

        return result;
    }

    private void populateResult(Map<String, String> result, String resultText, String returnResult){
        populateResult(result, resultText, returnResult, "");
    }

    private void populateResult(Map<String, String> result, String resultText, String returnResult, String resultXML){
        result.put(Constants.OutputNames.RESULT_TEXT, resultText);
        result.put(Constants.OutputNames.RETURN_RESULT, returnResult);
        result.put(Constants.OutputNames.RESULT_XML, resultXML);
    }
}
