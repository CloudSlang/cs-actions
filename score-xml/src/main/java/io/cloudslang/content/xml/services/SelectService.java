package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 03/03/2016.
 */
public class SelectService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs){
        Map<String, String> result = new HashMap<>();

        try {
            Document doc = XmlUtils.parseXML(commonInputs.getXmlDocument(), commonInputs.getSecureProcessing());
            NamespaceContext context = XmlUtils.createNamespaceContext(commonInputs.getXmlDocument());
            XPathExpression expr = XmlUtils.createXPathExpression(context, commonInputs.getXPathQuery());

            String selection = XmlUtils.xPathQuery(doc, expr, customInputs.getQueryType(), customInputs.getDelimiter());

            populateResult(result, Constants.SUCCESS, "XPath queried successfully.", selection);

        } catch (XPathExpressionException e) {
            populateResult(result, Constants.FAILURE, "XPath parsing error: " + e.getMessage(), Constants.EMPTY_STRING);
        } catch (TransformerException te) {
            populateResult(result, Constants.FAILURE, "Transformer error: " + te.getMessage(), Constants.EMPTY_STRING);
        } catch (Exception e) {
            populateResult(result, Constants.FAILURE, "Parsing error: " + e.getMessage(), Constants.EMPTY_STRING);
        }

        return result;
    }

    private static void populateResult(Map<String, String> result, String resultText, String returnResult, String selectedValue) {
        result.put(Constants.OutputNames.RESULT_TEXT, resultText);
        result.put(Constants.OutputNames.RETURN_RESULT, returnResult);
        result.put(Constants.OutputNames.SELECTED_VALUE, selectedValue);
    }
}
