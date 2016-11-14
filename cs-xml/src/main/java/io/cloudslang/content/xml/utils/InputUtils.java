package io.cloudslang.content.xml.utils;

import io.cloudslang.content.utils.BooleanUtilities;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.xml.utils.Constants.*;

/**
 * Created by moldovas on 6/24/2016.
 */
public class InputUtils {
    private InputUtils() {
    }

    public static String validateXmlDocumentSource(String xmlDocumentSource) {
        if (StringUtils.isBlank(xmlDocumentSource)) {
            return Constants.Defaults.XML_DOCUMENT_SOURCE;
        } else if (Constants.Defaults.XML_DOCUMENT_SOURCE.equalsIgnoreCase(xmlDocumentSource) || Constants.XML_PATH.equalsIgnoreCase(xmlDocumentSource) || Constants.XML_URL.equalsIgnoreCase(xmlDocumentSource)) {
            return xmlDocumentSource;
        }
        throw new RuntimeException(xmlDocumentSource + Constants.INVALID_XML_DOCUMENT_SOURCE);
    }

    public static String validateXsdDocumentSource(String xsdDocumentSource) {
        if (StringUtils.isBlank(xsdDocumentSource)) {
            return Constants.Defaults.XSD_DOCUMENT_SOURCE;
        } else if (Constants.Defaults.XSD_DOCUMENT_SOURCE.equalsIgnoreCase(xsdDocumentSource) || Constants.XSD_PATH.equalsIgnoreCase(xsdDocumentSource)) {
            return xsdDocumentSource;
        }
        throw new RuntimeException(xsdDocumentSource + Constants.INVALID_XSD_DOCUMENT_SOURCE);
    }

    public static void validateBoolean(String includeRootStr) throws Exception {
        if (!BooleanUtilities.isValid(includeRootStr)) {
            throw new Exception(includeRootStr + " is not a valid value for Boolean");
        }
    }

    public static Map<String, String> generateMap(String namesList, String valuesList, String delimiter) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isEmpty(namesList)) {
            return result;
        }
        String[] names = StringUtils.splitByWholeSeparatorPreserveAllTokens(namesList, delimiter);
        String[] values = StringUtils.splitByWholeSeparatorPreserveAllTokens(valuesList, delimiter);

        if (names.length != values.length) {
            throw new IllegalArgumentException(DIFFERENT_LIST_SIZE);
        }

        for (int i = 0; i < names.length; i++) {
            result.put(names[i], values[i]);
        }

        return result;
    }
}
