/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.json.utils;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class Constants {

    public static final String EMPTY_STRING = "";

    public static final class BooleanNames {

        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

        public static final class OutputNames {

        public static final String RETURN_RESULT = "returnResult";
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
        public static final String ERROR_MESSAGE = "errorMessage";
        public static final String NAMESPACES_PREFIXES = "differentListSize";
        public static final String NAMESPACES_URIS = "namespacesUris";
    }

    public static final class ReturnCodes {

        public static final String RETURN_CODE_FAILURE = "-1";
        public static final String RETURN_CODE_SUCCESS = "0";
    }

    public static final class ResponseNames {

        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }

    public static final class InputNames {
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
        public static final String DELIMITER = "delimiter";

        //ConvertXmlToJson
        public static final String XML = "xml";
        public static final String TEXT_ELEMENTS_NAME = "textElementsName";
        public static final String INCLUDE_ROOT = "includeRootElement";
        public static final String INCLUDE_ATTRIBUTES = "includeAttributes";
        public static final String PARSING_FEATURES = "parsingFeatures";

        public static final String JSON_OBJECT = "jsonObject";
        public static final String NEW_PROPERTY_NAME = "newPropertyName";
        public static final String NEW_PROPERTY_VALUE = "newPropertyValue";
        public static final String OBJECT = "object";
        public static final String KEY = "key";
        public static final String ARRAY = "array";
        public static final String ACTION = "action";
        public static final String JSON_PATH = "jsonPath";
        public static final String NAME = "name";
        public static final String VALUE = "value";
        public static final String VALIDATE_VALUE = "validateValue";
    }

    public static final class Defaults {

        public static final String DEFAULT_DELIMITER = ",";
        public static final String DEFAULT_TEXT_ELEMENTS_NAME = "_text";
    }

    public static final class EditJsonOperations {

        public static final String GET_ACTION = "get";
        public static final String INSERT_ACTION = "insert";
        public static final String UPDATE_ACTION = "update";
        public static final String DELETE_ACTION = "delete";
        public static final String ADD_ACTION = "add";
    }
}
