package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.constants.Outputs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class OutputsUtil {
    public static Map<String, String> getResultsMap(String returnResult) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
        results.put(Outputs.RETURN_RESULT, returnResult);

        return results;
    }
}