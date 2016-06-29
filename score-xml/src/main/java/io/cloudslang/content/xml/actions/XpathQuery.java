package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.SelectService;
import io.cloudslang.content.xml.entities.Constants;

import java.util.Map;

/**
 * Created by markowis on 22/02/2016.
 */
public class XpathQuery {
    /**
     * Selects from an XML document using an XPATH query.
     *
     * @param xmlDocument       XML string or path to xml file
     * @param xmlDocumentSource The source type of the xml document.
     *                          Valid values: xmlString, xmlPath
     *                          Default value: xmlString
     * @param xPathQuery        XPATH query
     * @param queryType         type of selection result from query attribute value
     * @param delimiter         optional - string to use as delimiter in case query_type is nodelist
     * @param secureProcessing  optional - whether to use secure processing
     * @return map of results containing success or failure text, a result message, and the value selected
     */
    @Action(name = "XpathQuery",
            outputs = {
                    @Output(Constants.Outputs.RESULT_TEXT),
                    @Output(Constants.Outputs.RETURN_RESULT),
                    @Output(Constants.Outputs.SELECTED_VALUE)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.Outputs.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.Outputs.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.Inputs.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.Inputs.XML_DOCUMENT_SOURCE) String xmlDocumentSource,
            @Param(value = Constants.Inputs.XPATH_QUERY, required = true) String xPathQuery,
            @Param(value = Constants.Inputs.QUERY_TYPE, required = true) String queryType,
            @Param(Constants.Inputs.DELIMITER) String delimiter,
            @Param(Constants.Inputs.SECURE_PROCESSING) String secureProcessing) {

        CommonInputs commonInputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withXpathQuery(xPathQuery)
                .withSecureProcessing(secureProcessing)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withQueryType(queryType)
                .withDelimiter(delimiter)
                .build();

        return new SelectService().execute(commonInputs, customInputs);
    }
}
