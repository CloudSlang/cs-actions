package io.cloudslang.content.json.actions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.gson.JsonElement;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import io.cloudslang.content.json.services.JsonService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

/**
 * Created by victor on 06.09.2016.
 */
public class JsonPathQuery {

    @Action(name = "JSON Path Query",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.JSON_OBJECT, required = true) String jsonObject,
            @Param(value = Constants.InputNames.JSON_PATH, required = true) String jsonPath) {
        String someString = "";
        try {
            JsonNode jsonNode = JsonService.evaluateJsonPathQuery(jsonObject, jsonPath);
            if (!jsonNode.isNull()) {
                someString = jsonNode.toString();
            }

        } catch (Exception exception) {
            final String exceptionValue = "Invalid jsonObject provided! " + exception.getMessage();
//            return populateResult(returnResult, exceptionValue, exception);
        }

        return new HashMap<>();
    }

    public static void main(String[] args) {
        new JsonPathQuery().execute("[{'a':'b', 'a1':'b1','a2':'b1'}, {'a':'b2', 'a1':'b3'}]", "$[0].a");
    }
}
