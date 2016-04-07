package io.cloudslang.content.xml.services;

import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.ResultUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
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

            ResultUtils.populateValueResult(result, Constants.SUCCESS, Constants.SuccessMessages.SELECT_SUCCESS,
                    selection);

        } catch (XPathExpressionException e) {
            ResultUtils.populateValueResult(result, Constants.FAILURE,
                    Constants.ErrorMessages.XPATH_PARSING_ERROR + e.getMessage(), Constants.EMPTY_STRING);
        } catch (TransformerException te) {
            ResultUtils.populateValueResult(result, Constants.FAILURE,
                    Constants.ErrorMessages.TRANSFORMER_ERROR + te.getMessage(), Constants.EMPTY_STRING);
        } catch (Exception e) {
            ResultUtils.populateValueResult(result, Constants.FAILURE,
                    Constants.ErrorMessages.PARSING_ERROR + e.getMessage(), Constants.EMPTY_STRING);
        }

        return result;
    }

    private static String xPathQuery(Document doc, XPathExpression expr, String queryType, String delimiter) throws Exception{
        switch (queryType){
            case Constants.QueryTypes.NODE_LIST:
                return xPathNodeListQuery(doc, expr, delimiter);
            case Constants.QueryTypes.NODE:
                return xPathNodeQuery(doc, expr);
            case Constants.QueryTypes.VALUE:
                return xPathValueQuery(doc, expr);
            default:
                throw new Exception("Invalid query type");
        }
    }

    private static String xPathNodeListQuery(Document doc, XPathExpression expr, String delimiter) throws Exception {
        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        return nodeListToString(nodeList,delimiter);
    }

    private static String xPathNodeQuery(Document doc, XPathExpression expr) throws Exception {
        Node n = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return XmlUtils.nodeToString(n);
    }

    private static String xPathValueQuery(Document doc, XPathExpression expr) throws Exception {
        return  (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    private static String nodeListToString(NodeList nodeList, String delimiter) throws  TransformerException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodeList.getLength() - 1; i++) {
            sb.append(XmlUtils.nodeToString(nodeList.item(i)));
            sb.append(delimiter);
        }
        sb.append(XmlUtils.nodeToString(nodeList.item(nodeList.getLength()-1)));
        return sb.toString();
    }


}
