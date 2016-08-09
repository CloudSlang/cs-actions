package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.xml.services.ConvertJsonToXmlService;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.*;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXml {

    /**
     * Converts a JSON array or a JSON object to a XML document.
     *
     * @param json - The JSON array or object (in the form of a String).
     * @param prettyPrint - The flag for formatting the resulted XML. If it is true the result will contain tabs and newline chars.
     *                      Default value: true
     *                      Valid values: true, false
     * @param showXmlDeclaration - The flag for showing the xml declaration (<?xml version="1.0" encoding="UTF-8" standalone="yes"?>).
     *                           If this is true then rootTagName can't be empty.
     *                           Default value: false
     *                           Valid values: true, false
     * @param rootTagName - The XML tag name. If this input is empty you will get a list of XML elements.
     * @param defaultJsonArrayItemName - Default XML tag name for items in a JSON array if there isn't a pair (array name, array item name) defined in jsonArraysNames and jsonArraysItemNames.
     *                                   Default value: 'item'
     * @param jsonArraysNames - The list of array names separated by delimiter.
     * @param jsonArraysItemNames - The coresponding list of array item names separated by delimiter.
     * @param namespacesPrefixes - The list of tag prefixes separated by delimiter.
     * @param namespacesUris - The coresponding list of namespaces uris separated by delimiter.
     * @param delimiter - The list separator
     *                    Default value: ','
     *
     * @return The converted JSON array or object as an XML document
     */
    @Action(name = "Convert JSON to XML",
            outputs = {
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.RETURN_CODE)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = Outputs.RETURN_CODE, value = ReturnCodes.SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = Outputs.RETURN_CODE, value = ReturnCodes.FAILURE)
            })
    public Map<String, String> execute(
            @Param(value = Inputs.JSON, required = true) String json,
            @Param(value = Inputs.PRETTY_PRINT) String prettyPrint,
            @Param(value = Inputs.SHOW_XML_DECLARATION) String showXmlDeclaration,
            @Param(value = Inputs.ROOT_TAG_NAME) String rootTagName,
            @Param(value = Inputs.DEFAULT_JSON_ARRAY_ITEM_NAME) String defaultJsonArrayItemName,
            @Param(value = Inputs.NAMESPACES_PREFIXES) String namespacesPrefixes,
            @Param(value = Inputs.NAMESPACES_URIS) String namespacesUris,
            @Param(value = Inputs.JSON_ARRAYS_NAMES) String jsonArraysNames,
            @Param(value = Inputs.JSON_ARRAYS_ITEM_NAMES) String jsonArraysItemNames,
            @Param(value = Inputs.DELIMITER) String delimiter) {

        Map<String, String> result = new HashMap<>();
        try {
            delimiter = StringUtils.defaultIfEmpty(delimiter, Defaults.DELIMITER);
            prettyPrint = StringUtils.defaultIfEmpty(prettyPrint, BooleanNames.TRUE);
            showXmlDeclaration = StringUtils.defaultIfEmpty(showXmlDeclaration, BooleanNames.FALSE);
            rootTagName = StringUtils.defaultIfEmpty(rootTagName, EMPTY_STRING);
            defaultJsonArrayItemName = StringUtils.defaultIfEmpty(defaultJsonArrayItemName, Defaults.JSON_ARRAY_ITEM_NAME);
            namespacesUris = StringUtils.defaultString(namespacesUris, EMPTY_STRING);
            namespacesPrefixes = StringUtils.defaultIfEmpty(namespacesPrefixes, EMPTY_STRING);
            jsonArraysNames = StringUtils.defaultString(jsonArraysNames, EMPTY_STRING);
            jsonArraysItemNames = StringUtils.defaultIfEmpty(jsonArraysItemNames, EMPTY_STRING);

            InputUtils.validateBoolean(prettyPrint);
            InputUtils.validateBoolean(showXmlDeclaration);

            Boolean prettyPrintBoolean = Boolean.parseBoolean(prettyPrint);
            Boolean showXmlDeclarationBoolean = Boolean.parseBoolean(showXmlDeclaration);

            // get list values
            Map<String, String> namespaces = InputUtils.generateMap(namespacesUris, namespacesPrefixes, delimiter);
            Map<String, String> arraysItemNames = InputUtils.generateMap(jsonArraysNames, jsonArraysItemNames, delimiter);

            // run the converter
            ConvertJsonToXmlService converter = new ConvertJsonToXmlService();
            converter.setNamespaces(namespaces);
            converter.setJsonArrayItemNames(arraysItemNames);
            converter.setJsonArrayItemName(defaultJsonArrayItemName);
            String xml = converter.convertToXmlString(json, prettyPrintBoolean, showXmlDeclarationBoolean, rootTagName);

            result.put(Outputs.RETURN_RESULT, xml);
            result.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);

        } catch (Exception e) {
            result.put(Outputs.RETURN_RESULT, e.getMessage());
            result.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
        }
        return result;
    }

}
