package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.ValidateService;
import io.cloudslang.content.xml.utils.Constants;

import java.util.Map;


/**
 * Created by markowis on 18/02/2016.
 */
public class Validate {

    @Action(name = "Validate",
            outputs = {
                @Output(Constants.OutputNames.RESULT_TEXT),
                @Output(Constants.OutputNames.RETURN_RESULT)},
            responses = {
                @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.InputNames.XSD_DOCUMENT, required = false) String xsdDocument) {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withXsdDocument(xsdDocument)
                .build();

        return new ValidateService().execute(inputs, customInputs);
    }
}
