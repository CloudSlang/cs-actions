package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.utils.Constants;
import org.w3c.dom.Document;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.*;
import java.util.HashMap;
import java.util.Map;

import io.cloudslang.content.xml.utils.XmlUtils;

/**
 * Created by markowis on 22/02/2016.
 */
public class XPathSelect {
    @Action(name = "XPathSelect",
            outputs = {
                    @Output(Constants.OutputNames.RESULT_TEXT),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.SELECTED_VALUE)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.InputNames.XPATH_QUERY, required = true) String xPathQuery,
            @Param(value = Constants.InputNames.QUERY_TYPE, required = true) String queryType,
            @Param(value = Constants.InputNames.DELIMITER, required = false) String delimiter,
            @Param(value = Constants.InputNames.SECURE_PROCESSING, required = false) String secureProcessing) {

        Map<String, String> result = new HashMap<>();

        if(delimiter == null){
            delimiter = ",";
        }

        try {
            Document doc = XmlUtils.parseXML(xmlDocument, Boolean.parseBoolean(secureProcessing));
            NamespaceContext context = XmlUtils.createNamespaceContext(xmlDocument);

            XPath xpath =  XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(context);
            XPathExpression expr = xpath.compile(xPathQuery);

            String selection;

            if(queryType.equals(Constants.QueryTypes.NODE_LIST)) {
                StringBuilder sb = new StringBuilder();
                NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                for (int i = 0; i < nl.getLength() - 1; i++) {
                    sb.append(XmlUtils.nodeToString(nl.item(i)));
                    sb.append(delimiter);
                }
                sb.append(XmlUtils.nodeToString(nl.item(nl.getLength()-1)));
                selection = sb.toString();
            }
            else if (queryType.equals(Constants.QueryTypes.NODE)) {
                Node n = (Node) expr.evaluate(doc, XPathConstants.NODE);
                selection = XmlUtils.nodeToString(n);
            }
            else if (queryType.equals(Constants.QueryTypes.VALUE)) {
                selection = (String) expr.evaluate(doc, XPathConstants.STRING);
            }
            else{
                result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
                result.put(Constants.OutputNames.RETURN_RESULT, "Invalid query type");
                return result;
            }

            result.put(Constants.OutputNames.RESULT_TEXT, Constants.SUCCESS);
            result.put(Constants.OutputNames.RETURN_RESULT, "XPath queried successfully.");
            result.put(Constants.OutputNames.SELECTED_VALUE, selection);

        } catch (XPathExpressionException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "XPath parsing error: " + e.getMessage());
        } catch (TransformerException te) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
        } catch (Exception e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "Parsing error: " + e.getMessage());
        }

        return result;
    }
}
