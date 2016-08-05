package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;

import static io.cloudslang.content.xml.utils.Constants.*;

import io.cloudslang.content.xml.services.ConvertJsonToXmlService;
import io.cloudslang.content.xml.services.PropsLoader;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertJsonToXml {

    @Action(name = "Convert JSON to XML",
            outputs = {
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.RETURN_CODE)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = Outputs.RETURN_CODE, value = ReturnCodes.SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = Outputs.RETURN_CODE, value = ReturnCodes.FAILURE)
            })
    public Map<String, String> convertJsonToXml(
            @Param(value = Inputs.JSON, required = true) String json,
            @Param(value = Inputs.PRETTY_PRINT) String prettyPrintStr,
            @Param(value = Inputs.SHOW_XML_DECLARATION) String showXmlDeclarationStr,
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
            prettyPrintStr = StringUtils.defaultIfEmpty(prettyPrintStr, BooleanNames.TRUE);
            showXmlDeclarationStr = StringUtils.defaultIfEmpty(showXmlDeclarationStr, BooleanNames.FALSE);
            rootTagName = StringUtils.defaultIfEmpty(rootTagName, EMPTY_STRING);
            defaultJsonArrayItemName = StringUtils.defaultIfEmpty(defaultJsonArrayItemName, Defaults.JSON_ARRAY_ITEM_NAME);
            namespacesUris = StringUtils.defaultString(namespacesUris, EMPTY_STRING);
            namespacesPrefixes = StringUtils.defaultIfEmpty(namespacesPrefixes, EMPTY_STRING);
            jsonArraysNames = StringUtils.defaultString(jsonArraysNames, EMPTY_STRING);
            jsonArraysItemNames = StringUtils.defaultIfEmpty(jsonArraysItemNames, EMPTY_STRING);

            InputUtils.validateBoolean(prettyPrintStr);
            InputUtils.validateBoolean(showXmlDeclarationStr);

            Boolean prettyPrint = Boolean.parseBoolean(prettyPrintStr);
            Boolean showXmlDeclaration = Boolean.parseBoolean(showXmlDeclarationStr);

            // get list values
            Map<String, String> namespaces = InputUtils.generateMap(namespacesUris, namespacesPrefixes, delimiter);
            Map<String, String> arraysItemNames = InputUtils.generateMap(jsonArraysNames, jsonArraysItemNames, delimiter);

            // run the converter
            ConvertJsonToXmlService converter = new ConvertJsonToXmlService();
            converter.setNamespaces(namespaces);
            converter.setJsonArrayItemNames(arraysItemNames);
            String xml = converter.convertToXmlString(json, prettyPrint, showXmlDeclaration, rootTagName, defaultJsonArrayItemName);

            result.put(Outputs.RETURN_RESULT, xml);
            result.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);

        } catch (Exception e) {
            result.put(Outputs.RETURN_RESULT, ExceptionUtils.getStackTrace(e));
            result.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
        }
        return result;
    }

}
