package io.cloudslang.content.xml.utils;

import com.hp.oo.sdk.content.annotations.Param;
import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.lang3.StringUtils;
import io.cloudslang.content.xml.utils.Constants.InputNames;
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

    private static boolean isBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static void validateBoolean(@Param(value = InputNames.INCLUDE_ROOT) String includeRootStr) throws Exception {
        if (!InputUtils.isBoolean(includeRootStr)) {
            throw new Exception(includeRootStr + " is not a valid value for Boolean");
        }
    }
}
