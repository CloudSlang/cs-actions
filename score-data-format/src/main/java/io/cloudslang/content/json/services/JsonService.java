package io.cloudslang.content.json.services;

import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.json.exceptions.RemoveEmptyElementException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by Folea Ilie Cristian on 2/3/2016.
 */
public class JsonService {

    public String removeEmptyElementsJson(String json) throws RemoveEmptyElementException {
        String normalizedJson = removeBlanks(json);

        char quote = '\"';
        for (char c : normalizedJson.toCharArray()) {
            if (c == '\'' || c == '\"') {
                quote = c;
                break;
            }
        }

        LinkedHashMap<String, Object> jsonMap;

        try {
            jsonMap = JsonPath.read(normalizedJson, "$");
        } catch (com.jayway.jsonpath.InvalidJsonException ije) {
            throw new RemoveEmptyElementException(ije);
        }

        removeEmptyElementsFromMap(jsonMap);
        JSONObject jsonObject = new JSONObject(jsonMap);
        String newJson = jsonObject.toJSONString();

        if (newJson.charAt(1) != quote) {
            return searchAndReplace(newJson, newJson.charAt(1), quote);
        }

        return newJson;
    }

    private void removeEmptyElementsFromMap(LinkedHashMap<String, Object> json) {
        LinkedHashMap<String, Object> copyJson = new LinkedHashMap<String, Object>(json);
        for (Object element : copyJson.values()) {
            if (element == null) {
                json.values().remove(element);
            } else if (element instanceof String) {
                if (element.equals("")) {
                    json.values().remove(element);
                }
            } else if (element instanceof JSONArray) {
                if (((JSONArray) element).isEmpty()) {
                    json.values().remove(element);
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) element);
                }
            } else if (element instanceof LinkedHashMap) {
                if (((LinkedHashMap) element).isEmpty()) {
                    json.values().remove(element);
                } else {
                    removeEmptyElementsFromMap((LinkedHashMap) element);
                }
            }
        }
    }

    private void removeEmptyElementFromJsonArray(JSONArray jsonArray) {
        JSONArray copyJsonArray = (JSONArray) jsonArray.clone();
        for (Object element : copyJsonArray) {
            if (element == null) {
                jsonArray.remove(element);
            } else if (element instanceof String) {
                if (element.equals("")) {
                    jsonArray.remove(element);
                }
            } else if (element instanceof JSONArray) {
                if (((JSONArray) element).isEmpty()) {
                    jsonArray.remove(element);
                } else {
                    removeEmptyElementFromJsonArray((JSONArray) element);
                }
            } else if (element instanceof LinkedHashMap) {
                if (((LinkedHashMap) element).isEmpty()) {
                    jsonArray.remove(element);
                } else {
                    removeEmptyElementsFromMap((LinkedHashMap) element);
                }
            }
        }
    }

    private String searchAndReplace(String text, char toReplace, char newChar) {
        char[] charArrayText = text.toCharArray();
        for (int i = 0; i < charArrayText.length; i++) {
            if (charArrayText[i] == toReplace && charArrayText[i - 1] != '\\') {
                charArrayText[i] = newChar;
            }
        }

        return String.valueOf(charArrayText);
    }

    private String removeBlanks(String json) {
        return json.substring(json.indexOf('{'), json.length());
    }
}
