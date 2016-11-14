package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.AddAttributeService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.ATTRIBUTE_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.SECURE_PROCESSING;
import static io.cloudslang.content.xml.utils.Constants.Inputs.VALUE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XPATH_ELEMENT_QUERY;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_XML;

/**
 * Created by markowis on 25/02/2016.
 */
public class AddAttribute {
    /**
     * Adds an attribute to an XML element or replaces the value if the attribute already exists.
     *
     * @param xmlDocument           XML string in which to add attribute
     * @param xmlDocumentSource The source type of the xml document.
     *                          Valid values: xmlString, xmlPath
     *                          Default value: xmlString
     * @param xPathElementQuery     XPATH query that results in an element or element list, not an attribute
     * @param attributeName         name of attribute to add or replace
     * @param value                 value of attribute to add or replace with
     * @param secureProcessing      optional - whether to use secure processing
     * @return map of results containing success or failure text, a result message, and the modified XML
     */
    @Action(name = "Add Attribute",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(RESULT_XML),
                    @Output(Constants.Outputs.ERROR_MESSAGE)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = XML_DOCUMENT_SOURCE) String xmlDocumentSource,
            @Param(value = XPATH_ELEMENT_QUERY, required = true) String xPathElementQuery,
            @Param(value = ATTRIBUTE_NAME, required = true) String attributeName,
            @Param(value = VALUE, required = true) String value,
            @Param(value = SECURE_PROCESSING) String secureProcessing) {

        final CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withXpathQuery(xPathElementQuery)
                .withSecureProcessing(secureProcessing)
                .build();

        final CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withAttributeName(attributeName)
                .withValue(value)
                .build();

        return new AddAttributeService().execute(inputs, customInputs);
    }
}
