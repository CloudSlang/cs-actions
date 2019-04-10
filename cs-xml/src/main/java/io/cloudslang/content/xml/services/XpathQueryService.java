

package io.cloudslang.content.xml.services;

import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.ResultUtils;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.NO_MATCH_FOUND;
import static io.cloudslang.content.xml.utils.Constants.Outputs.SELECTED_VALUE;
import static io.cloudslang.content.xml.utils.Constants.SuccessMessages.SELECT_SUCCESS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by markowis on 03/03/2016.
 */
public class XpathQueryService {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs) {
        Map<String, String> result = new HashMap<>();

        try {
            Document doc = XmlUtils.getDocument(commonInputs);
            NamespaceContext context = XmlUtils.getNamespaceContext(commonInputs, doc);

            XPathExpression expr = XmlUtils.createXPathExpression(context, commonInputs.getXPathQuery());

            String selection = xPathQuery(doc, expr, customInputs.getQueryType(), customInputs.getDelimiter());

            if (isBlank(selection)) {
                ResultUtils.populateValueResult(result, ResponseNames.SUCCESS, SELECT_SUCCESS, NO_MATCH_FOUND, SUCCESS);
            } else {
                ResultUtils.populateValueResult(result, ResponseNames.SUCCESS, SELECT_SUCCESS, selection, SUCCESS);
            }
        } catch (Exception e) {
            ResultUtils.populateFailureResult(result, ExceptionUtils.getStackTrace(e));
            result.put(SELECTED_VALUE, EMPTY);
        }
        return result;
    }

    private static String xPathQuery(Document doc, XPathExpression expr, String queryType, String delimiter) throws Exception {
        switch (queryType) {
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
        return nodeListToString(nodeList, delimiter);
    }

    private static String xPathNodeQuery(Document doc, XPathExpression expr) throws Exception {
        Node n = (Node) expr.evaluate(doc, XPathConstants.NODE);
        return XmlUtils.nodeToString(n);
    }

    private static String xPathValueQuery(Document doc, XPathExpression expr) throws Exception {
        return (String) expr.evaluate(doc, XPathConstants.STRING);
    }

    private static String nodeListToString(NodeList nodeList, String delimiter) throws TransformerException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nodeList.getLength() - 1; i++) {
            sb.append(XmlUtils.nodeToString(nodeList.item(i)));
            sb.append(delimiter);
        }
        sb.append(XmlUtils.nodeToString(nodeList.item(nodeList.getLength() - 1)));
        return sb.toString();
    }
}
