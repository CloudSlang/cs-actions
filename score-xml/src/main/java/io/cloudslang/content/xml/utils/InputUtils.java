package io.cloudslang.content.xml.utils;

import io.cloudslang.content.xml.entities.Constants;
import org.apache.commons.lang3.StringUtils;

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
}
