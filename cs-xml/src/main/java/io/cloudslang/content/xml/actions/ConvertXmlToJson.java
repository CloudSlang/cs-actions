package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.xml.services.ConvertXmlToJsonService;
import io.cloudslang.content.xml.utils.Constants;
import io.cloudslang.content.xml.utils.Constants.*;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertXmlToJson {

    /**
     * Converts a XML document to a JSON array or a JSON object.
     *
     * @param xml - The XML document (in the form of a String)
     * @param textElementsName - specify custom property name for text elements. This will be used for elements that have attributes and text content.
     *                           Default value: _text
     * @param includeRoot - The flag for including the xml root in the resulted JSON.
     *                      Default value: true
     *                      Valid values: true, false
     * @param includeAttributes - The flag for including XML attributes in the resulted JSON
     *                            Default value: true
     *                            Valid values: true, false
     * @param prettyPrint - The flag for formatting the resulted XML.
     *                      Default value: true
     *                      Valid values: true, false
     * @param  parsingFeatures - The list of XML parsing features separated by new line (CRLF). The feature name - value must be separated by empty space. Setting specific features this field could be used to avoid XML security issues like "XML Entity Expansion injection" and "XML External Entity injection". To avoid aforementioned security issues we strongly recommend to set this input to the following values:
     *                           http://apache.org/xml/features/disallow-doctype-decl true
     *                           http://xml.org/sax/features/external-general-entities false
     *                           http://xml.org/sax/features/external-parameter-entities false
     *                           When the "http://apache.org/xml/features/disallow-doctype-decl" feature is set to "true" the parser will throw a FATAL ERROR if the incoming document contains a DOCTYPE declaration.
     *                           When the "http://xml.org/sax/features/external-general-entities" feature is set to "false" the parser will not include external general entities.
     *                           When the "http://xml.org/sax/features/external-parameter-entities" feature is set to "false" the parser will not include external parameter entities or the external DTD subset.
     *                           If any of the validations fails, the operation will fail with an error message describing the problem.
     *                           Default value:
     *                              http://apache.org/xml/features/disallow-doctype-decl true
     *                              http://xml.org/sax/features/external-general-entities false
     *                              http://xml.org/sax/features/external-parameter-entities false
     * @return The converted XML document as a JSON array or object
     */

    @Action(name = "Convert XML to Json",
            outputs = {
                    @Output(Outputs.NAMESPACES_PREFIXES),
                    @Output(Outputs.NAMESPACES_URIS),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = Outputs.RETURN_CODE, value = ReturnCodes.SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = Outputs.RETURN_CODE, value = ReturnCodes.FAILURE)
            })
    public Map<String, String> convertXmlToJson(
            @Param(value = Inputs.XML, required = true) String xml,
            @Param(value = Inputs.TEXT_ELEMENTS_NAME) String textElementsName,
            @Param(value = Inputs.INCLUDE_ROOT) String includeRoot,
            @Param(value = Inputs.INCLUDE_ATTRIBUTES) String includeAttributes,
            @Param(value = Inputs.PRETTY_PRINT) String prettyPrint,
            @Param(value = Inputs.PARSING_FEATURES) String parsingFeatures) {

        Map<String, String> result = new HashMap<>();
        try {
            textElementsName = StringUtils.defaultIfEmpty(textElementsName, Defaults.DEFAULT_TEXT_ELEMENTS_NAME);
            includeRoot = StringUtils.defaultIfEmpty(includeRoot, BooleanNames.TRUE);
            includeAttributes = StringUtils.defaultIfEmpty(includeAttributes, BooleanNames.TRUE);
            prettyPrint = StringUtils.defaultIfEmpty(prettyPrint, BooleanNames.TRUE);

            InputUtils.validateBoolean(includeRoot);
            InputUtils.validateBoolean(includeAttributes);
            InputUtils.validateBoolean(prettyPrint);

            Boolean includeRootBoolean = Boolean.parseBoolean(includeRoot);
            Boolean includeAttributesBoolean = Boolean.parseBoolean(includeAttributes);
            Boolean prettyPrintBoolean = Boolean.parseBoolean(prettyPrint);

            ConvertXmlToJsonService converter = new ConvertXmlToJsonService();
            String json = converter.convertToJsonString(xml, includeAttributesBoolean, prettyPrintBoolean, includeRootBoolean, parsingFeatures, textElementsName);

            result.put(Outputs.NAMESPACES_PREFIXES, converter.getNamespacesPrefixes());
            result.put(Outputs.NAMESPACES_URIS, converter.getNamespacesUris());
            result.put(Outputs.RETURN_RESULT, json);
            result.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS);

        } catch (Exception e) {
            result.put(Outputs.NAMESPACES_PREFIXES, Constants.EMPTY_STRING);
            result.put(Outputs.NAMESPACES_URIS, Constants.EMPTY_STRING);
            result.put(Outputs.RETURN_RESULT, e.getMessage());
            result.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE);
        }
        return result;
    }

}
