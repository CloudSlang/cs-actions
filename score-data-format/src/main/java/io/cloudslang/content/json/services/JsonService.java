package io.cloudslang.content.json.services;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import io.cloudslang.content.json.exceptions.RemoveEmptyElementException;
import io.cloudslang.content.json.utils.StringUtils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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

        if (newJson.charAt(1) != wrappingQuote) {
            return replaceUnescapedOccurrencesOfCharacterInText(newJson, newJson.charAt(1), wrappingQuote);
        }

        return newJson;
    }


    private void parseJsonForInconsistencies(String normalizedJson) {
        JsonProvider provider = new GsonJsonProvider();
        Configuration configuration =  Configuration.builder().jsonProvider(provider).build();
        JsonPath.parse(normalizedJson, configuration);       //throws an exception at runtime if the json is malformed
    }

    /**
     * Returns the quote character used for specifying json member names and String values of json members
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
        while(jsonElementsIterator.hasNext()) {
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
        while(jsonArrayIterator.hasNext()){
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

    private String replaceUnescapedOccurrencesOfCharacterInText(String text, char toReplace, char newChar) {
        char[] charArrayText = text.toCharArray();
        for (int i = 0; i < charArrayText.length; i++) {
            if(shouldCharacterBeReplaced(charArrayText, toReplace, i))  {
                charArrayText[i] = newChar;
            }
        }

        return String.valueOf(charArrayText);
    }

    private boolean shouldCharacterBeReplaced(char[] characters, char characterToReplace, int characterPosition) {
        if(characters[characterPosition] == characterToReplace){
            if( characterPosition == 0){
                return true;
            } else if(characters[characterPosition - 1] != '\\'){
                return true;
            }
        }
        return false;
    }
}
