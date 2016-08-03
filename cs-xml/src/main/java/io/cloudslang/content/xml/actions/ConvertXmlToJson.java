package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.xml.entities.Constants;
import io.cloudslang.content.xml.entities.Constants.*;
import io.cloudslang.content.xml.utils.InputUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ursan on 8/2/2016.
 */
public class ConvertXmlToJson {

    @Action(name = "Convert XML to Json",
            outputs = {
                    @Output(OutputNames.NAMESPACES_PREFIXES),
                    @Output(OutputNames.NAMESPACES_URIS),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE)
            })
    public Map<String, String> convertXmlToJson(
            @Param(value = Inputs.XML, required = true) String xml,
            @Param(value = Inputs.TEXT_ELEMENTS_NAME) String textElementsName,
            @Param(value = Inputs.INCLUDE_ROOT) String includeRootStr,
            @Param(value = Inputs.INCLUDE_ATTRIBUTES) String includeAttributesStr,
            @Param(value = Inputs.PRETTY_PRINT) String prettyPrintStr,
            @Param(value = Inputs.PARSING_FEATURES) String parsingFeatures) {

        Map<String, String> result = new HashMap<>();
        try {
            textElementsName = StringUtils.defaultIfEmpty(textElementsName, Defaults.DEFAULT_TEXT_ELEMENTS_NAME);
            includeRootStr = StringUtils.defaultIfEmpty(includeRootStr, BooleanNames.TRUE);
            includeAttributesStr = StringUtils.defaultIfEmpty(includeAttributesStr, BooleanNames.TRUE);
            prettyPrintStr = StringUtils.defaultIfEmpty(prettyPrintStr, BooleanNames.TRUE);

            InputUtils.validateBoolean(includeRootStr);
            InputUtils.validateBoolean(includeAttributesStr);
            InputUtils.validateBoolean(prettyPrintStr);

            Boolean includeRoot = Boolean.parseBoolean(includeRootStr);
            Boolean includeAttributes = Boolean.parseBoolean(includeAttributesStr);
            Boolean prettyPrint = Boolean.parseBoolean(prettyPrintStr);



        } catch (Exception e) {
            result.put(OutputNames.NAMESPACES_PREFIXES, Constants.EMPTY_STRING);
            result.put(OutputNames.NAMESPACES_URIS, Constants.EMPTY_STRING);
            result.put(OutputNames.RETURN_RESULT, ExceptionUtils.getStackTrace(e));
            result.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        }
        return result;
    }


}
