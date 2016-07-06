package io.cloudslang.content.xml.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by moldovas on 7/6/2016.
 */
public class ValidateUtils {

    public static void validateIsNotEmpty(String action, String message) throws Exception {
        if (action == null || StringUtils.isEmpty(action)) {
            throw new Exception(message);
        }
    }

    public static void validateXmlAndFilePathInputs(String xml, String filePath) throws Exception {
        if ((StringUtils.isBlank(filePath)) && (StringUtils.isBlank(xml))) {
            throw new Exception("Supplied parameters: either file path or xml is missing when one is required");
        }
        if ((!StringUtils.isEmpty(filePath)) && (!StringUtils.isEmpty(xml))) {
            throw new Exception("Supplied parameters: file path and xml when only one is required");
        }
    }
}
