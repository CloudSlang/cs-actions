package io.cloudslang.content.xml.utils;

/**
 * Created by markowis on 18/02/2016.
 */
public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String NAMESPACE_DELIMITER = ":";
    public static final String JSON_ATTRIBUTE_PREFIX = "@";
    public static final String NO_MATCH_FOUND = "No match found";
    public static final String EMPTY_XML_FILE = "XML file error: Not found";
    public static final String YES = "yes";
    public static final String XML_PATH = "xmlPath";
    public static final String XML_URL = "xmlUrl";
    public static final String INVALID_XML_DOCUMENT_SOURCE = " is an invalid input value. Valid values are: xmlString, xmlPath and xmlUrl";
    public static final String INVALID_XSD_DOCUMENT_SOURCE = " is an invalid input value. Valid values are: xsdString and xsdPath";
    public static final String XSD_PATH = "xsdPath";
    public static final String DIFFERENT_LIST_SIZE = "The two lists are of different size";
    public static final String ROOT_TAG_NAME_IS_MISSING = "The root tag name is missing";
    public static final String ONLY_ONE_ROOT_ELEMENT = "There must be only one root element";
    public static final String NEW_LINE = "\n";
    public static final String INDENT = "  ";
    public static final String UTF_8_ENCODING = "UTF-8";

    public static final class Outputs {
        public static final String RETURN_RESULT = "returnResult";
        public static final String RESULT_TEXT = "result";
        public static final String SELECTED_VALUE = "selectedValue";
        public static final String RESULT_XML = "resultXML";
        public static final String RETURN_CODE = "returnCode";
        public static final String EXCEPTION = "exception";
        public static final String ERROR_MESSAGE = "errorMessage";
        public static final String NAMESPACES_URIS = "namespacesUris";
        public static final String NAMESPACES_PREFIXES = "namespacesPrefixes";
    }

    public static final class Inputs {
        //ConvertJsonToXml
        public static final String JSON = "json";
        public static final String PRETTY_PRINT = "prettyPrint";
        public static final String SHOW_XML_DECLARATION = "showXmlDeclaration";
        public static final String ROOT_TAG_NAME = "rootTagName";
        public static final String DEFAULT_JSON_ARRAY_ITEM_NAME = "defaultJsonArrayItemName";
        public static final String NAMESPACES_URIS = "namespacesUris";
        public static final String NAMESPACES_PREFIXES = "namespacesPrefixes";
        public static final String JSON_ARRAYS_NAMES = "jsonArraysNames";
        public static final String JSON_ARRAYS_ITEM_NAMES = "jsonArraysItemNames";

        //ConvertXmlToJson
        public static final String TEXT_ELEMENTS_NAME = "textElementsName";
        public static final String INCLUDE_ROOT = "includeRootElement";
        public static final String INCLUDE_ATTRIBUTES = "includeAttributes";
        public static final String PARSING_FEATURES = "parsingFeatures";

        public static final String XML_DOCUMENT = "xmlDocument";
        public static final String XML_DOCUMENT_SOURCE = "xmlDocumentSource";
        public static final String XSD_DOCUMENT = "xsdDocument";
        public static final String XSD_DOCUMENT_SOURCE = "xsdDocumentSource";
        public static final String XPATH_QUERY = "xPathQuery";
        public static final String XPATH_ELEMENT_QUERY = "xPathElementQuery";
        public static final String QUERY_TYPE = "queryType";
        public static final String DELIMITER = "delimiter";
        public static final String ATTRIBUTE_NAME = "attributeName";
        public static final String XML_ELEMENT = "xmlElement";

        public static final String SECURE_PROCESSING = "secureProcessing";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String X_509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";

        public static final String HTTP_PREFIX_STRING = "http://";
        public static final String HTTPS_PREFIX_STRING = "https://";
        public static final String FILE_PATH = "filePath";
        public static final String XML = "xml";
        public static final String ACTION = "action";
        public static final String XPATH1 = "xpath1";
        public static final String XPATH2 = "xpath2";
        public static final String VALUE = "value";
        public static final String TYPE = "type";
        public static final String TYPE_NAME = "name";
        public static final String FEATURES = "parsingFeatures";
        public static final String DELETE_ACTION = "delete";
        public static final String INSERT_ACTION = "insert";
        public static final String APPEND_ACTION = "append";
        public static final String SUBNODE_ACTION = "subnode";
        public static final String MOVE_ACTION = "move";
        public static final String RENAME_ACTION = "rename";
        public static final String UPDATE_ACTION = "update";
        public static final String TYPE_ELEM = "elem";
        public static final String TYPE_ATTR = "attr";
        public static final String TYPE_TEXT = "text";
        public static final String EMPTY_STRING = "";
    }

    public static final class ResponseNames {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class QueryTypes {
        public static final String NODE = "node";
        public static final String NODE_LIST = "nodelist";
        public static final String VALUE = "value";
    }

    public static final class ErrorMessages {
        public static final String GENERAL_ERROR = "Error: ";
        public static final String PARSING_ERROR = "Parsing error: ";
        public static final String TRANSFORMER_ERROR = "Transformer error: ";
        public static final String XPATH_PARSING_ERROR = "XPath parsing error: ";
        public static final String NEED_ELEMENT_TYPE = "XPath must return element types.";
        public static final String ELEMENT_NOT_FOUND = "Element not found.";
        public static final String ADD_ATTRIBUTE_FAILURE = "Addition of attribute failed: ";
        public static final String APPEND_CHILD_FAILURE = "Append failed: ";
        public static final String INSERT_BEFORE_FAILURE = "Insert failed: ";
        public static final String REMOVE_FAILURE = "Removal failed: ";
        public static final String SET_VALUE_FAILURE = "Setting value failed: ";
        public static final String VALIDATION_FAILURE =  "Validation failed: ";
    }

    public static final class SuccessMessages {
        public static final String ADD_ATTRIBUTE_SUCCESS = "Attribute set successfully.";
        public static final String APPEND_CHILD_SUCCESS = "Child appended successfully.";
        public static final String INSERT_BEFORE_SUCCESS = "Inserted before successfully.";
        public static final String REMOVE_SUCCESS = "Removed successfully.";
        public static final String SELECT_SUCCESS = "XPath queried successfully.";
        public static final String SET_VALUE_SUCCESS = "Value set successfully.";
        public static final String PARSING_SUCCESS = "Parsing successful.";
        public static final String VALIDATION_SUCCESS = "XML is valid.";
    }

    public static final class Defaults {
        public static final String DELIMITER = ",";
        public static final String PREFIX_DELIMITER = ":";
        public static final String XML_DOCUMENT_SOURCE = "xmlString";
        public static final String XSD_DOCUMENT_SOURCE = "xsdString";
        public static final String DEFAULT_TEXT_ELEMENTS_NAME = "_text";
        public static final String JSON_ARRAY_ITEM_NAME = "item";

    }

    public static final class ReturnCodes {
        public static final String SUCCESS = "0";
        public static final String FAILURE = "-1";
    }

    public static final class BooleanNames {
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

}
