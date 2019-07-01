

package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.xml.entities.inputs.ConvertJsonToXmlInputs;
import io.cloudslang.content.xml.services.ConvertJsonToXmlService;
import io.cloudslang.content.xml.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.xml.utils.Constants.Inputs.DEFAULT_JSON_ARRAY_ITEM_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.DELIMITER;
import static io.cloudslang.content.xml.utils.Constants.Inputs.JSON;
import static io.cloudslang.content.xml.utils.Constants.Inputs.JSON_ARRAYS_ITEM_NAMES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.JSON_ARRAYS_NAMES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.NAMESPACES_PREFIXES;
import static io.cloudslang.content.xml.utils.Constants.Inputs.NAMESPACES_URIS;
import static io.cloudslang.content.xml.utils.Constants.Inputs.PRETTY_PRINT;
import static io.cloudslang.content.xml.utils.Constants.Inputs.ROOT_TAG_NAME;
import static io.cloudslang.content.xml.utils.Constants.Inputs.SHOW_XML_DECLARATION;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXml {

    /**
     * Converts a JSON array or a JSON object to a XML document.
     *
     * @param json                     - The JSON array or object (in the form of a String).
     * @param prettyPrint              - The flag for formatting the resulted XML. If it is true the result will contain tabs and newline ('\n') chars.
     *                                 Default value: true
     *                                 Valid values: true, false
     * @param showXmlDeclaration       - The flag for showing the xml declaration (<?xml version="1.0" encoding="UTF-8" standalone="yes"?>).
     *                                 If this is true then rootTagName can't be empty.
     *                                 Default value: false
     *                                 Valid values: true, false
     * @param rootTagName              - The XML tag name. If this input is empty you will get a list of XML elements.
     * @param defaultJsonArrayItemName - Default XML tag name for items in a JSON array if there isn't a pair (array name, array item name) defined in jsonArraysNames and jsonArraysItemNames.
     *                                 Default value: 'item'
     * @param jsonArraysNames          - The list of array names separated by delimiter.
     * @param jsonArraysItemNames      - The coresponding list of array item names separated by delimiter.
     * @param namespacesPrefixes       - The list of tag prefixes separated by delimiter.
     * @param namespacesUris           - The coresponding list of namespaces uris separated by delimiter.
     * @param delimiter                - The list separator
     *                                 Default value: ','
     * @return The converted JSON array or object as an XML document
     */
    @Action(name = "Convert JSON to XML",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = FAILURE)
            })
    public Map<String, String> execute(
            @Param(value = JSON, required = true) String json,
            @Param(value = PRETTY_PRINT) String prettyPrint,
            @Param(value = SHOW_XML_DECLARATION) String showXmlDeclaration,
            @Param(value = ROOT_TAG_NAME) String rootTagName,
            @Param(value = DEFAULT_JSON_ARRAY_ITEM_NAME) String defaultJsonArrayItemName,
            @Param(value = NAMESPACES_PREFIXES) String namespacesPrefixes,
            @Param(value = NAMESPACES_URIS) String namespacesUris,
            @Param(value = JSON_ARRAYS_NAMES) String jsonArraysNames,
            @Param(value = JSON_ARRAYS_ITEM_NAMES) String jsonArraysItemNames,
            @Param(value = DELIMITER) String delimiter) {

        try {
            showXmlDeclaration = StringUtils.defaultIfEmpty(showXmlDeclaration, TRUE);
            prettyPrint = StringUtils.defaultIfEmpty(prettyPrint, TRUE);
            ValidateUtils.validateInputs(prettyPrint, showXmlDeclaration);

            final ConvertJsonToXmlInputs inputs = new ConvertJsonToXmlInputs.ConvertJsonToXmlInputsBuilder()
                    .withJson(json)
                    .withPrettyPrint(Boolean.parseBoolean(prettyPrint))
                    .withShowXmlDeclaration(Boolean.parseBoolean(showXmlDeclaration))
                    .withRootTagName(rootTagName)
                    .withDefaultJsonArrayItemName(defaultJsonArrayItemName)
                    .withNamespaces(namespacesUris, namespacesPrefixes, delimiter)
                    .withJsonArraysNames(jsonArraysNames, jsonArraysItemNames, delimiter)
                    .build();

            final ConvertJsonToXmlService converter = new ConvertJsonToXmlService();
            converter.setNamespaces(inputs.getNamespaces());
            converter.setJsonArrayItemNames(inputs.getArraysItemNames());
            converter.setJsonArrayItemName(inputs.getDefaultJsonArrayItemName());
            final String xml = converter.convertToXmlString(inputs);

            return getSuccessResultsMap(xml);
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }

}
