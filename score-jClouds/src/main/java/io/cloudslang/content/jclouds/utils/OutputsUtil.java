package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.constants.Outputs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class OutputsUtil {
    private static final String EMPTY = "";

    private OutputsUtil() {
    }

    public static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }

    public static String getElementsString(Set<String> elements, String delimiter) {
        if (elements.size() > 0) {
            int index = 0;
            StringBuilder sb = new StringBuilder();
            for (String element : elements) {
                sb.append(element);
                if (index < elements.size() - 1) {
                    sb.append(delimiter);
                }
                index++;
            }
            return sb.toString();
        }
        return EMPTY;
    }
}