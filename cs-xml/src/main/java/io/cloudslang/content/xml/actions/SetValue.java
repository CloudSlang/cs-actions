/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.SetValueService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.xml.utils.Constants.Inputs.ATTRIBUTE_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.SECURE_PROCESSING;
import static io.cloudslang.content.xml.utils.Constants.Inputs.VALUE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML_DOCUMENT_SOURCE;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XPATH_ELEMENT_QUERY;
import static io.cloudslang.content.xml.utils.Constants.Outputs.ERROR_MESSAGE;
import static io.cloudslang.content.xml.utils.Constants.Outputs.RESULT_XML;

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
     * @return map of results containing success or failure text, a result message, and the modified XML
     */
    @Action(name = "Set Value",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(RESULT_XML),
                    @Output(ERROR_MESSAGE)},
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = COMPARE_EQUAL),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE, matchType = COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = XML_DOCUMENT_SOURCE, required = true) String xmlDocumentSource,
            @Param(value = XPATH_ELEMENT_QUERY, required = true) String xPathQuery,
            @Param(value = ATTRIBUTE_NAME) String attributeName,
            @Param(value = VALUE, required = true) String value,
            @Param(value = SECURE_PROCESSING) String secureProcessing) {

        final CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXmlDocumentSource(xmlDocumentSource)
                .withXpathQuery(xPathQuery)
                .withSecureProcessing(secureProcessing)
                .build();

        final CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withAttributeName(attributeName)
                .withValue(value)
                .build();

        return new SetValueService().execute(inputs, customInputs);
    }

}
