/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
***********************************************************************************************************************/
package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.inputs.ConvertXmlToJsonInputs;
import io.cloudslang.content.xml.services.ConvertXmlToJsonService;
import io.cloudslang.content.xml.utils.ValidateUtils;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.xml.utils.Constants.Inputs.INCLUDE_ATTRIBUTES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.INCLUDE_ROOT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PARSING_FEATURES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PRETTY_PRINT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.TEXT_ELEMENTS_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.XML;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_PREFIXES;
import static io.cloudslang.content.xml.utils.Constants.Outputs.NAMESPACES_URIS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertXmlToJson {

    /**
     * Converts a XML document to a JSON array or a JSON object.
     *
     * @param xml                - The XML document (in the form of a String)
     * @param textElementsName   - specify custom property name for text elements. This will be used for elements that have attributes and text content.
     *                           Default value: '_text'
     * @param includeRootElement - The flag for including the xml root in the resulted JSON.
     *                           Default value: true
     *                           Valid values: true, false
     * @param includeAttributes  - The flag for including XML attributes in the resulted JSON
     *                           Default value: true
     *                           Valid values: true, false
     * @param prettyPrint        - The flag for formatting the resulted XML. The newline character is '\n'
     *                           Default value: true
     *                           Valid values: true, false
     * @param parsingFeatures    - The list of XML parsing features separated by new line (CRLF). The feature name - value must be separated by empty space. Setting specific features this field could be used to avoid XML security issues like "XML Entity Expansion injection" and "XML External Entity injection". To avoid aforementioned security issues we strongly recommend to set this input to the following values:
     *                           http://apache.org/xml/features/disallow-doctype-decl true
     *                           http://xml.org/sax/features/external-general-entities false
     *                           http://xml.org/sax/features/external-parameter-entities false
     *                           When the "http://apache.org/xml/features/disallow-doctype-decl" feature is set to "true" the parser will throw a FATAL ERROR if the incoming document contains a DOCTYPE declaration.
     *                           When the "http://xml.org/sax/features/external-general-entities" feature is set to "false" the parser will not include external general entities.
     *                           When the "http://xml.org/sax/features/external-parameter-entities" feature is set to "false" the parser will not include external parameter entities or the external DTD subset.
     *                           If any of the validations fails, the operation will fail with an error message describing the problem.
     *                           Default value:
     *                           http://apache.org/xml/features/disallow-doctype-decl true
     *                           http://xml.org/sax/features/external-general-entities false
     *                           http://xml.org/sax/features/external-parameter-entities false
     * @return The converted XML document as a JSON array or object
     */

    @Action(name = "Convert XML to Json",
            outputs = {
                    @Output(NAMESPACES_PREFIXES),
                    @Output(NAMESPACES_URIS),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE)
            })
    public Map<String, String> execute(
            @Param(value = XML, required = true) String xml,
            @Param(value = TEXT_ELEMENTS_NAME) String textElementsName,
            @Param(value = INCLUDE_ROOT) String includeRootElement,
            @Param(value = INCLUDE_ATTRIBUTES) String includeAttributes,
            @Param(value = PRETTY_PRINT) String prettyPrint,
            @Param(value = PARSING_FEATURES) String parsingFeatures) {

        try {
            includeRootElement = defaultIfEmpty(includeRootElement, TRUE);
            includeAttributes = defaultIfEmpty(includeAttributes, TRUE);
            prettyPrint = defaultIfEmpty(prettyPrint, TRUE);
            ValidateUtils.validateInputs(includeRootElement, includeAttributes, prettyPrint);

            final ConvertXmlToJsonInputs inputs = new ConvertXmlToJsonInputs.ConvertXmlToJsonInputsBuilder()
                    .withXml(xml)
                    .withTextElementsName(textElementsName)
                    .withIncludeRootElement(Boolean.parseBoolean(includeRootElement))
                    .withIncludeAttributes(Boolean.parseBoolean(includeAttributes))
                    .withPrettyPrint(Boolean.parseBoolean(prettyPrint))
                    .withParsingFeatures(parsingFeatures)
                    .build();

            final ConvertXmlToJsonService converter = new ConvertXmlToJsonService();
            final String json = converter.convertToJsonString(inputs);

            final Map<String, String> result = getSuccessResultsMap(json);
            result.put(NAMESPACES_PREFIXES, converter.getNamespacesPrefixes());
            result.put(NAMESPACES_URIS, converter.getNamespacesUris());
            return result;
        } catch (Exception e) {
            final Map<String, String> result = getFailureResultsMap(e);
            result.put(NAMESPACES_PREFIXES, EMPTY);
            result.put(NAMESPACES_URIS, EMPTY);
            return result;
        }
    }
}
