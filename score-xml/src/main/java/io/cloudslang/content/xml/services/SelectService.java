package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.ResultUtils;
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

            String selection = xPathQuery(doc, expr, customInputs.getQueryType(), customInputs.getDelimiter());

            ResultUtils.populateValueResult(result, Constants.SUCCESS, "XPath queried successfully.", selection);

        } catch (XPathExpressionException e) {
            ResultUtils.populateValueResult(result, Constants.FAILURE, "XPath parsing error: " + e.getMessage(), Constants.EMPTY_STRING);
        } catch (TransformerException te) {
            ResultUtils.populateValueResult(result, Constants.FAILURE, "Transformer error: " + te.getMessage(), Constants.EMPTY_STRING);
        } catch (Exception e) {
            ResultUtils.populateValueResult(result, Constants.FAILURE, "Parsing error: " + e.getMessage(), Constants.EMPTY_STRING);
        }

        return result;
    }

    private static String xPathQuery(Document doc, XPathExpression expr, String queryType, String delimiter) throws Exception{
        if(queryType.equals(Constants.QueryTypes.NODE_LIST)) {
            return XmlUtils.xPathNodeListQuery(doc, expr, delimiter);
        }
        else if (queryType.equals(Constants.QueryTypes.NODE)) {
            return XmlUtils.xPathNodeQuery(doc, expr);
        }
        else if (queryType.equals(Constants.QueryTypes.VALUE)) {
            return XmlUtils.xPathValueQuery(doc, expr);
        }
        else{
            throw new Exception("Invalid query type");
        }
    }
}
