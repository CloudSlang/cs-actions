/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.content.json.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.main.JsonSchema;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import io.cloudslang.content.constants.OtherValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.json.exceptions.JsonSchemaValidationException;
import io.cloudslang.content.json.exceptions.RemoveEmptyElementException;
import io.cloudslang.content.json.utils.JsonUtils;
import io.cloudslang.content.json.utils.StringUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static io.cloudslang.content.json.utils.Constants.InputNames.HTTP_GET;
import static io.cloudslang.content.json.utils.Constants.ValidationMessages.*;
import static io.cloudslang.content.json.utils.ValidationUtils.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * Created by Folea Ilie Cristian on 2/3/2016.
 */
public class JsonService {

    public String removeEmptyElementsJson(String json) throws RemoveEmptyElementException {
        String normalizedJson = json.trim();

        char wrappingQuote = retrieveWrappingQuoteTypeOfJsonMemberNames(normalizedJson);

        Map<String, Object> jsonMap;

        try {
            parseJsonForInconsistencies(normalizedJson);
            jsonMap = JsonPath.read(normalizedJson, "$");
        } catch (com.jayway.jsonpath.InvalidJsonException | com.google.gson.JsonSyntaxException ije) {
            throw new RemoveEmptyElementException(ije);
        }

        removeEmptyElementsFromMap(jsonMap);
        return generateResultingJsonString(wrappingQuote, jsonMap);
    }

    private String generateResultingJsonString(char wrappingQuote, Map<String, Object> jsonMap) {
        JSONObject jsonObject = new JSONObject(jsonMap);
        String newJson = jsonObject.toJSONString(JSONStyle.LT_COMPRESS);

        if ((!jsonObject.isEmpty()) && (newJson.charAt(1) != wrappingQuote)) {
            return replaceUnescapedOccurrencesOfCharacterInText(newJson, newJson.charAt(1), wrappingQuote);
        }

        return newJson;
    }


    private void parseJsonForInconsistencies(String normalizedJson) {
        JsonProvider provider = new GsonJsonProvider();
        Configuration configuration = Configuration.builder().jsonProvider(provider).build();
        JsonPath.parse(normalizedJson, configuration);       //throws an exception at runtime if the json is malformed
    }

    /**
     * Returns the quote character used for specifying json member names and String values of json members
     *
     * @param jsonString the source json from which to extract the wrapping quote
     * @return either one of the characters ' (single quote)or " (double quote)
     */
    private char retrieveWrappingQuoteTypeOfJsonMemberNames(String jsonString) {
        char quote = '\"';   //  the default quote character used to specify json member names and string value according to the json specification
        for (char c : jsonString.toCharArray()) {
            if (c == '\'' || c == '\"') {
                quote = c;
                break;
            }
        }
        return quote;
    }

    private void removeEmptyElementsFromMap(Map<String, Object> json) {
        Set<Map.Entry<String, Object>> jsonElements = json.entrySet();
        Iterator<Map.Entry<String, Object>> jsonElementsIterator = jsonElements.iterator();
        while (jsonElementsIterator.hasNext()) {
            Map.Entry<String, Object> jsonElement = jsonElementsIterator.next();
            Object jsonElementValue = jsonElement.getValue();
            if (StringUtils.isEmpty(jsonElementValue)) {
                jsonElementsIterator.remove();
            } else if (jsonElementValue instanceof JSONArray) {
                if (((JSONArray) jsonElementValue).isEmpty()) {
                    jsonElementsIterator.remove();
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) jsonElementValue);
                }
            } else if (jsonElementValue instanceof LinkedHashMap) {
                if (((LinkedHashMap) jsonElementValue).isEmpty()) {
                    jsonElementsIterator.remove();
                } else {
                    removeEmptyElementsFromMap((Map<String, Object>) jsonElementValue);
                }
            }
        }
    }

    private void removeEmptyElementFromJsonArray(JSONArray jsonArray) {
        Iterator jsonArrayIterator = jsonArray.iterator();
        while (jsonArrayIterator.hasNext()) {
            Object jsonArrayElement = jsonArrayIterator.next();
            if (StringUtils.isEmpty(jsonArrayElement)) {
                jsonArrayIterator.remove();
            } else if (jsonArrayElement instanceof JSONArray) {
                if (((JSONArray) jsonArrayElement).isEmpty()) {
                    jsonArrayIterator.remove();
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) jsonArrayElement);
                }
            } else if (jsonArrayElement instanceof LinkedHashMap) {
                if (((LinkedHashMap) jsonArrayElement).isEmpty()) {
                    jsonArrayIterator.remove();
                } else {
                    removeEmptyElementsFromMap((LinkedHashMap) jsonArrayElement);
                }
            }
        }

    }

    @NotNull
    private String replaceUnescapedOccurrencesOfCharacterInText(String text, char toReplace, char newChar) {
        char[] charArrayText = text.toCharArray();
        for (int i = 0; i < charArrayText.length; i++) {
            if (shouldCharacterBeReplaced(charArrayText, toReplace, i)) {
                charArrayText[i] = newChar;
            }
        }

        return String.valueOf(charArrayText);
    }

    @Contract(pure = true)
    private boolean shouldCharacterBeReplaced(char[] characters, char characterToReplace, int characterPosition) {
        return characters[characterPosition] == characterToReplace &&
                (characterPosition == 0 || characters[characterPosition - 1] != '\\');
    }

    @NotNull
    public static JsonNode evaluateJsonPathQuery(@Nullable final String jsonObject, @Nullable final String jsonPath) {
        final JsonContext jsonContext = JsonUtils.getValidJsonContext(jsonObject);
        final JsonPath path = JsonUtils.getValidJsonPath(jsonPath);
        return jsonContext.read(path);
    }

    public static String replaceSingleQuotesWithDoubleQuotes(String jsonObject) {
        return jsonObject.replace("\'", "\"");
    }

    public static String getJsonFromURL(String Url, String proxyHost, String proxyPort, String proxyUsername, String proxyPassword) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(Url);
        httpClientInputs.setMethod(HTTP_GET);

        if (proxyHost != null && !proxyHost.equals(OtherValues.EMPTY_STRING)) {
            httpClientInputs.setProxyHost(proxyHost);
            httpClientInputs.setProxyPort(proxyPort);
            httpClientInputs.setProxyUsername(proxyUsername);
            httpClientInputs.setProxyPassword(proxyPassword);
        }

        HttpClientService httpClientService = new HttpClientService();
        Map<String, String> resultMap = httpClientService.execute(httpClientInputs);
        return resultMap.get(OutputNames.RETURN_RESULT);
    }

    public static Map<String, String> validateJsonResultMap(String jsonObject) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

        try {
            mapper.readTree(jsonObject);
            getJsonNode(jsonObject);

            return getSuccessResultsMap(VALID_JSON);
        } catch (IOException exception) {
            return getFailureResultsMap(exception.getMessage());
        }
    }

    public static Map<String, String> validateJsonAgainstSchemaResultMap(String jsonObject, String jsonSchemaObject) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);

        JsonNode jsonNode;
        JsonSchema jsonSchemaNode;

        try {
            mapper.readTree(jsonSchemaObject);
            jsonSchemaNode = getSchemaNode(jsonSchemaObject);
            jsonNode = getJsonNode(jsonObject);

            if (isJsonValid(jsonSchemaNode, jsonNode)) {
                return getSuccessResultsMap(VALID_JSON_AGAINST_SCHEMA);
            } else
                return getFailureResultsMap(INVALID_JSON_AGAINST_SCHEMA);
        } catch (Exception exception) {
            return getFailureResultsMap(exception.getMessage());
        }
    }

    /**
     * Returns a valid JSON schema retrieved from a GET request or from a file. If the given parameter is a valid JSON
     * schema, it is returned as it is.
     *
     * @param jsonSchema    the JSON schema to retrieve; can be an URL, a file or a string representation of a JSON schema
     * @param proxyHost     the proxy host for the GET request
     * @param proxyPort     the proxy port for the GET request
     * @param proxyUsername the username for connecting via proxy
     * @param proxyPassword the password for connecting via proxy
     * @return a valid JSON schema represented as a string
     * @throws JsonSchemaValidationException the exception thrown if the schema validation failed
     */
    public static String getJsonSchemaFromResource(
            String jsonSchema,
            String proxyHost,
            String proxyPort,
            String proxyUsername,
            String proxyPassword)
            throws JsonSchemaValidationException {
        String jsonSchemaObject;
        final File jsonSchemaFile = new File(jsonSchema);

        if (jsonSchemaFile.exists() && !jsonSchemaFile.isDirectory()) {
            try {
                jsonSchemaObject = FileUtils.readFileToString(jsonSchemaFile, "UTF-8");
            } catch (IOException exception) {
                throw new JsonSchemaValidationException(exception);
            }
        } else if (isValidUrl(jsonSchema)) {
            jsonSchemaObject = getJsonFromURL(jsonSchema, proxyHost, proxyPort, proxyUsername, proxyPassword);

            if (isJsonSchemaEmpty(jsonSchemaObject)) {
                throw new JsonSchemaValidationException(EMPTY_SCHEMA_URL);
            }
        } else {
            jsonSchemaObject = jsonSchema;
        }

        return jsonSchemaObject;
    }

}
