package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class Remove {
    @Action(name = "Remove",
            outputs = {
                    @Output(Constants.OutputNames.RESULT_TEXT),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RESULT_XML)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.InputNames.XPATH_ELEMENT_QUERY, required = true) String xPathQuery,
            @Param(value = Constants.InputNames.ATTRIBUTE_NAME, required = false) String attributeName,
            @Param(value = Constants.InputNames.SECURE_PROCESSING, required = false) String secureProcessing) {

        Map<String, String> result = new HashMap<>();

        try {
            Document doc = XmlUtils.parseXML(xmlDocument, Boolean.parseBoolean(secureProcessing));
            NamespaceContext context = XmlUtils.createNamespaceContext(xmlDocument);
            NodeList nl = XmlUtils.evaluateXPathQuery(doc,context,xPathQuery);

            if(nl.getLength() == 0){
                result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
                result.put(Constants.OutputNames.RETURN_RESULT, "Removal failed: element not found");
                return result;
            }

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);

                if(node.getNodeType() != Node.ELEMENT_NODE){
                    result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
                    result.put(Constants.OutputNames.RETURN_RESULT, "Removal failed: XPath must return element types.");
                    return result;
                }

                if (attributeName == null) {
                    node.getParentNode().removeChild(node);
                }
                else{
                    node.getAttributes().removeNamedItem(attributeName);
                }
            }

            result.put(Constants.OutputNames.RESULT_TEXT, Constants.SUCCESS);
            result.put(Constants.OutputNames.RETURN_RESULT, "Removed successfully.");
            result.put(Constants.OutputNames.RESULT_XML, XmlUtils.nodeToString(doc));

        } catch (XPathExpressionException e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "XPath parsing error: " + e.getMessage());
        } catch (TransformerException te) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "Transform error: " + te.getMessage());
        } catch (Exception e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, "Parsing error: " + e.getMessage());
        }

        return result;
    }
}
