package io.cloudslang.content.xml.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.xml.entities.inputs.CommonInputs;
import io.cloudslang.content.xml.entities.inputs.CustomInputs;
import io.cloudslang.content.xml.services.SelectService;
import io.cloudslang.content.xml.utils.Constants;

import java.util.Map;

/**
 * Created by markowis on 22/02/2016.
 */
public class Select {
    /**
     * Selects from an XML document using an XPATH query.
     *
     * @param xmlDocument       XML string in which query
     * @param xPathQuery        XPATH query
     * @param queryType         type of selection result from query attribute value
     * @param delimiter         optional - string to use as delimiter in case query_type is nodelist
     * @param secureProcessing  optional - whether to use secure processing
     * @return  map of results containing success or failure text, a result message, and the value selected
     */
    @Action(name = "Select",
            outputs = {
                    @Output(Constants.OutputNames.RESULT_TEXT),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.SELECTED_VALUE)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.XML_DOCUMENT, required = true) String xmlDocument,
            @Param(value = Constants.InputNames.XPATH_QUERY, required = true) String xPathQuery,
            @Param(value = Constants.InputNames.QUERY_TYPE, required = true) String queryType,
            @Param(Constants.InputNames.DELIMITER) String delimiter,
            @Param(Constants.InputNames.SECURE_PROCESSING) String secureProcessing) {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withXmlDocument(xmlDocument)
                .withXpathQuery(xPathQuery)
                .withSecureProcessing(secureProcessing)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withQueryType(queryType)
                .withDelimiter(delimiter)
                .build();

        return new SelectService().execute(inputs, customInputs);
    }
}
