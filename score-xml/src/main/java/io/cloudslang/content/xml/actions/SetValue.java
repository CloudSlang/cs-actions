package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.SetValueService;

import java.util.Map;

/**
 * Created by markowis on 23/02/2016.
 */
public class SetValue {
    /**
     * Sets the value of an existing XML element or attribute.
     *
     * @param xmlDocument       XML string in which to set an element or attribute value
     * @param xmlDocumentSource The source type of the xml document.
     *                          Valid values: xmlString, xmlPath
     *                          Default value: xmlString
     * @param xPathQuery        XPATH query that results in an element or element list to set the value of an element
     *                          or element list containing the attribute to set the value of
     * @param attributeName     optional - name of attribute to set the value of if setting an attribute value
     * @param value             value to set for element or attribute
     * @param secureProcessing  optional - whether to use secure processing
     * @return  map of results containing success or failure text, a result message, and the modified XML
     */
    @Action(name = "Set Value",
            outputs = {
                    @Output(Constants.Outputs.RETURN_CODE),
                    @Output(Constants.Outputs.RETURN_RESULT),
                    @Output(Constants.Outputs.RESULT_XML),
                    @Output(Constants.Outputs.ERROR_MESSAGE)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.Outputs.RETURN_CODE, value = Constants.ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.Outputs.RETURN_CODE, value = Constants.ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.Inputs.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.Inputs.XML_DOCUMENT_SOURCE, required = true) String xmlDocumentSource,
            @Param(value = Constants.Inputs.XPATH_ELEMENT_QUERY, required = true) String xPathQuery,
            @Param(value = Constants.Inputs.ATTRIBUTE_NAME, required = false) String attributeName,
            @Param(value = Constants.Inputs.VALUE, required = true) String value,
            @Param(value = Constants.Inputs.SECURE_PROCESSING) String secureProcessing) {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withXpathQuery(xPathQuery)
                .withSecureProcessing(secureProcessing)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withAttributeName(attributeName)
                .withValue(value)
                .build();

        return new SetValueService().execute(inputs, customInputs);
    }

}
