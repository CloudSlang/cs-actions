package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import static  io.cloudslang.content.xml.utils.Constants.*;

import io.cloudslang.content.xml.services.JdomXmlConverter;
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

            InputUtils.validateBoolean(prettyPrintStr);
            InputUtils.validateBoolean(showXmlDeclarationStr);

            Boolean prettyPrint = Boolean.parseBoolean(prettyPrintStr);
            Boolean showXmlDeclaration = Boolean.parseBoolean(showXmlDeclarationStr);

            // get list values
            Map<String, String> namespaces = generateMap(namespacesUris, namespacesPrefixes, delimiter);
            Map<String, String> arraysItemNames = generateMap(jsonArraysNames, jsonArraysItemNames, delimiter);

            // run the converter
            JdomXmlConverter converter = new JdomXmlConverter();
            converter.setPrettyPrint(prettyPrint);
            converter.setShowXmlDeclaration(showXmlDeclaration);
            converter.setRootTagName(rootTagName);

            if (!StringUtils.isBlank(defaultJsonArrayItemName)) {
                converter.setJsonArrayItemName(defaultJsonArrayItemName);
            }
            converter.setJsonArrayItemNames(arraysItemNames);
            // value = namespacePrefix, key = namespaceUri
            for (Map.Entry<String, String> entry : namespaces.entrySet()) {
                converter.addNamespace(entry.getValue(), entry.getKey());
            }
            String xml = StringUtils.isEmpty(json)? EMPTY_STRING : converter.convertToXmlString(json);

            result.put(Outputs.RETURN_RESULT, xml);
            result.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);

        } catch (Exception e) {
            result.put(Outputs.RETURN_RESULT, ExceptionUtils.getStackTrace(e));
            result.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
        }
        return result;
    }
     /* Utility functions */

    private Map<String, String> generateMap(String namesList, String valuesList, String delimiter) {
        Map<String, String> result = new HashMap<>();
        if (!StringUtils.isEmpty(namesList)) {
            String[] names = StringUtils.splitByWholeSeparatorPreserveAllTokens(namesList, delimiter);
            if (valuesList == null) {
                valuesList = "";
            }
            String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(valuesList, delimiter);
            if (names.length != values.length) {
                throw new IllegalArgumentException(String.format(PropsLoader.EXCEPTIONS.getProperty(DIFFERENT_LIST_SIZE), Outputs.NAMESPACES_URIS, Outputs.NAMESPACES_PREFIXES));
            }
            for (int i = 0; i < names.length; i++) {
                result.put(names[i], values[i]);
            }
        }
        return result;
    }
}
