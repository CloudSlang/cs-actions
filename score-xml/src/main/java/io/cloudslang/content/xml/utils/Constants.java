package io.cloudslang.content.xml.utils;

import javax.xml.xpath.XPathConstants;

/**
 * Created by markowis on 18/02/2016.
 */
public class Constants {
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final class OutputNames {
        public static final String RETURN_RESULT = "returnResult";
        public static final String RESULT_TEXT = "result";
        public static final String SELECTED_VALUE = "selectedValue";
        public static final String RESULT_XML = "resultXML";
    }

    public static final class InputNames {
        public static final String XML_DOCUMENT = "xmlDocument";
        public static final String XSD_DOCUMENT = "xsdDocument";
        public static final String XPATH_QUERY = "xPathQuery";
        public static final String XPATH_ELEMENT_QUERY = "xPathElementQuery";
        public static final String QUERY_TYPE = "queryType";
        public static final String DELIMITER = "delimiter";
        public static final String ATTRIBUTE_NAME = "attributeName";
        public static final String VALUE = "value";
        public static final String XML_ELEMENT = "xmlElement";
        public static final String SECURE_PROCESSING = "secureProcessing";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class QueryTypes {
        public static final String NODE = "node";
        public static final String NODE_LIST = "nodeList";
        public static final String VALUE = "value";
    }
}
